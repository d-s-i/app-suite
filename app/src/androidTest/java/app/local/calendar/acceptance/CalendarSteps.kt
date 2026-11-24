package app.local.calendar.acceptance

import android.content.pm.PackageManager
import androidx.activity.compose.setContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.text.TextLayoutResult
import app.local.calendar.CalendarApp
import app.local.calendar.CalendarEnvironment
import app.local.calendar.MainActivity
import java.time.Clock
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.abs
import org.junit.Assert.*
import org.junit.Rule

/** Development acceptance adapter: fixed device inputs, real Compose UI actions and assertions. */
open class CalendarSteps {
    @get:Rule val ui = createAndroidComposeRule<MainActivity>()
    private var launched = false
    private lateinit var environment: CalendarEnvironment
    private val accent = Color(0xFF6D4AFF)
    private var outline: OutlinePixels? = null
    private data class OutlinePixels(val width: Float, val height: Float, val pixels: androidx.compose.ui.graphics.PixelMap, val left: Int, val top: Int)
    private data class Definition(val pattern: Regex, val action: (MatchResult) -> Unit)
    private val definitions = mutableListOf<Definition>()
    private fun step(pattern: String, action: (MatchResult) -> Unit) { definitions += Definition(Regex(pattern), action) }
    private fun node(tag: String) = ui.onNodeWithTag(tag, useUnmergedTree = true)
    private fun bounds(tag: String): Rect = node(tag).fetchSemanticsNode().boundsInRoot
    private val density get() = ui.density
    private fun dp(value: Int): Float = with(density) { value.toFloat() * this.density }
    private fun textLayout(tag: String): TextLayoutResult {
        val results = mutableListOf<TextLayoutResult>()
        node(tag).performSemanticsAction(SemanticsActions.GetTextLayoutResult) { it(results) }
        return results.single()
    }
    private fun dateNodes() = ui.onAllNodes(SemanticsMatcher("month date") {
        it.config.getOrElse(SemanticsProperties.TestTag) { "" }.startsWith("date-")
    }, useUnmergedTree = true).fetchSemanticsNodes()

    fun runScenario(id: String, steps: List<Pair<String, String>>) {
        val givens = steps.takeWhile { it.first != "When" }.map { it.second }
        val dateMatch = givens.mapNotNull { Regex("the device date is (.+) in time zone (.+)").matchEntire(it) }.last()
        val zone = ZoneId.of(dateMatch.groupValues[2])
        val date = LocalDate.parse(dateMatch.groupValues[1])
        val localeName = givens.lastOrNull { it.startsWith("the device locale is ") }?.removePrefix("the device locale is ") ?: "English United States"
        environment = CalendarEnvironment(Clock.fixed(date.atTime(12, 30).atZone(zone).toInstant(), zone), when (localeName) { "English United States" -> Locale.US; "French France" -> Locale.FRANCE; else -> error("Unknown locale $localeName") }, givens.lastOrNull { it.startsWith("the device uses ") } != "the device uses 12-hour time")
        registerMonthSteps()
        registerTimedSteps()
        registerNavigationSteps()
        var phase = "Given"
        steps.forEach { (keyword, text) ->
            if (keyword != "And") phase = keyword
            val qualified = "$phase $text"
            val matches = definitions.mapNotNull { definition -> definition.pattern.matchEntire(qualified)?.let { definition to it } }
            check(matches.size == 1) { "$id: expected exactly one handler for '$qualified', found ${matches.size}" }
            try { matches.single().let { (definition, match) -> definition.action(match) } }
            catch (failure: Throwable) { throw AssertionError("$id: $qualified", failure) }
        }
    }

    private fun launch() {
        ui.activityRule.scenario.onActivity { it.setContent { CalendarApp(environment) } }
        launched = true
        ui.waitForIdle()
    }
    private fun ensureLaunched() { if (!launched) launch() }
    private fun choose(view: String) {
        ensureLaunched()
        node("open-drawer").performClick()
        node("choose-$view").performClick()
    }
    private fun navigateMonth(target: YearMonth) {
        ensureLaunched()
        if (ui.onAllNodesWithTag("view-Month", useUnmergedTree = true).fetchSemanticsNodes().isEmpty()) choose("Month")
        val date = dateNodes().first().config[SemanticsProperties.TestTag].removePrefix("date-")
        // The 15th visible cell always belongs to the displayed month in a Monday-first 42-cell grid.
        val current = YearMonth.from(LocalDate.parse(date).plusDays(14))
        val delta = ChronoUnit.MONTHS.between(current, target).toInt()
        repeat(abs(delta)) { node("month-grid").performTouchInput { if (delta > 0) swipeLeft() else swipeRight() } }
    }
    private fun parseMonth(value: String): YearMonth = if (value.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) YearMonth.from(LocalDate.parse(value)) else {
        val parts = value.split(" ")
        YearMonth.of(parts[1].toInt(), java.time.Month.entries.single { it.getDisplayName(java.time.format.TextStyle.FULL, Locale.US) == parts[0] }.value)
    }
    private fun assertDateRange(first: String, last: String) {
        val dates = dateNodes().map { LocalDate.parse(it.config[SemanticsProperties.TestTag].removePrefix("date-")) }
        assertEquals(42, dates.size)
        assertEquals(LocalDate.parse(first), dates.first())
        assertEquals(LocalDate.parse(last), dates.last())
        dates.zipWithNext().forEach { (a, b) -> assertEquals(a.plusDays(1), b) }
    }

    private fun dateHeaders() = ui.onAllNodes(SemanticsMatcher("date header") {
        it.config.getOrElse(SemanticsProperties.TestTag) { "" }.startsWith("day-header-")
    }, useUnmergedTree = true).fetchSemanticsNodes()

    private fun openDate(date: LocalDate, view: String) {
        navigateMonth(YearMonth.from(date))
        node("date-$date").performClick()
        if (view != "Day") choose(view)
    }
    private fun assertTimedRange(view: String, first: String, last: String) {
        node("view-$view").assertExists()
        val dates = dateHeaders().map { LocalDate.parse(it.config[SemanticsProperties.TestTag].removePrefix("day-header-")) }
        assertEquals(if (view == "Week") 7 else 1, dates.size)
        assertEquals(LocalDate.parse(first), dates.first())
        assertEquals(LocalDate.parse(last), dates.last())
        dates.zipWithNext().forEach { (a,b) -> assertEquals(a.plusDays(1), b) }
    }
    private fun registerNavigationSteps() {
        step("Given the navigation drawer is open") { ensureLaunched(); node("open-drawer").performClick(); node("view-drawer").assertIsDisplayed() }
        step("When the owner presses system Back") { ui.waitForIdle(); shellOutput("input keyevent KEYCODE_BACK"); ui.waitForIdle() }
        step("When the owner swipes the time grid (left|right) by one complete period") {
            node("time-viewport").performTouchInput { if (it.groupValues[1] == "left") swipeLeft() else swipeRight() }
        }
        step("Given (Month|Week|Day) displays a period in December 2027") {
            if (it.groupValues[1] == "Month") navigateMonth(YearMonth.of(2027, 12))
            else openDate(LocalDate.of(2027, 12, 15), it.groupValues[1])
        }
        step("When the owner activates the Today control") { node("today").performClick() }
        step("Then its displayed date range is (.+) through (.+)") {
            if (ui.onAllNodesWithTag("view-Month", useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()) assertDateRange(it.groupValues[1], it.groupValues[2])
            else assertTimedRange(if (dateHeaders().size == 7) "Week" else "Day", it.groupValues[1], it.groupValues[2])
        }
        step("Then the Today control displays the number (\\d+)") {
            ui.onNode(hasText(it.groupValues[1]) and hasAnyAncestor(hasTestTag("today")), useUnmergedTree = true).assertIsDisplayed()
        }
        step("Given (Week|Day) displays a period containing (.+)") { openDate(LocalDate.parse(it.groupValues[2]), it.groupValues[1]) }
        step("When the owner scrolls the time grid to the current device time") {
            val hour = ZonedDateTime.now(environment.clock).hour.coerceAtLeast(1)
            node("hour-$hour").performScrollTo()
        }
        step("Then a current-time line and dot appear in the February 15 column at the device's current local time") {
            val now = ZonedDateTime.now(environment.clock)
            node("now-indicator").assertIsDisplayed()
            val indicator = bounds("now-indicator")
            val day = isDay()
            val header = bounds("day-header-${now.toLocalDate()}")
            val gridLeft = if (day) bounds("time-axis").right else header.left
            val gridWidth = if (day) bounds("time-viewport").right - gridLeft else header.width
            assertEquals(gridLeft, indicator.left, 1f)
            assertEquals(gridWidth, indicator.width, 1.1f)
            val hourLine = if (day) bounds("hour-${now.hour}").center.y else bounds("hour-${now.hour}").top + dp(8)
            assertEquals(hourLine + now.minute / 60f * dp(60), indicator.top, 1.1f)
            val pixels = node("time-viewport").captureToImage().toPixelMap()
            val viewport = bounds("time-viewport")
            val x = (indicator.left - viewport.left).toInt()
            val y = (indicator.top - viewport.top).toInt()
            val line = pixels[(x + indicator.width / 2).toInt(),y]
            val dot = pixels[(x - dp(2)).toInt(),y]
            assertTrue("Current-time line must be painted", abs(line.blue - accent.blue) < 0.1f && line.green < 0.5f)
            assertTrue("Current-time dot must extend left of the line", abs(dot.blue - accent.blue) < 0.1f && dot.green < 0.5f)
        }
    }

    private fun isDay() = ui.onAllNodesWithTag("view-Day", useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()

    private fun hourSeparatorY(hour: Int): Float {
        val viewport = bounds("time-viewport")
        val pixels = node("time-viewport").captureToImage().toPixelMap()
        val x = ((if (isDay()) (bounds("time-axis").right + viewport.right) / 2 else dateHeaders().first().boundsInRoot.center.x) - viewport.left).toInt()
        val expectedY = ((if (isDay()) bounds("hour-$hour").center.y else bounds("hour-$hour").top + dp(8)) - viewport.top).toInt()
        val color = Color(0xFFE5E4EA)
        val painted = ((expectedY - 2)..(expectedY + 2)).filter { y ->
            if (y !in 0 until pixels.height) false else {
                val pixel = pixels[x,y]
                abs(pixel.red-color.red) < 0.07f && abs(pixel.green-color.green) < 0.07f && abs(pixel.blue-color.blue) < 0.07f
            }
        }
        assertTrue("Expected a rendered hour separator near hour $hour", painted.isNotEmpty())
        return viewport.top + painted.minBy { abs(it-expectedY) }
    }

    private fun registerTimedSteps() {
        step("Then the time-column separator continues through the full date header") {
            val header = bounds("date-headers")
            val viewport = bounds("time-viewport")
            val axis = bounds("time-axis")
            val headerPixels = node("date-headers").captureToImage().toPixelMap()
            val bodyPixels = node("time-viewport").captureToImage().toPixelMap()
            val line = Color(0xFFE5E4EA)
            fun painted(pixel: Color) = pixel.alpha > 0.9f && abs(pixel.red-line.red)<0.07f && abs(pixel.green-line.green)<0.07f && abs(pixel.blue-line.blue)<0.07f
            val columns = ((axis.right - dp(2)).toInt()..axis.right.toInt()).filter { x ->
                val hx = (x-header.left).toInt()
                val bx = (x-viewport.left).toInt()
                hx in 0 until headerPixels.width && bx in 0 until bodyPixels.width &&
                    (2 until headerPixels.height-2).all { y -> painted(headerPixels[hx,y]) } &&
                    painted(bodyPixels[bx,dp(4).toInt()])
            }
            assertTrue("A painted vertical separator must align across header and grid", columns.isNotEmpty())
        }
        step("Then the date-header bottom divider spans the full calendar width") {
            val pixels = node("date-headers").captureToImage().toPixelMap()
            val line = Color(0xFFE5E4EA)
            for (x in 0 until pixels.width) {
                val painted = (pixels.height-dp(2).toInt() until pixels.height).any { y ->
                    val pixel = pixels[x,y]
                    pixel.alpha > 0.9f && abs(pixel.red-line.red)<0.07f && abs(pixel.green-line.green)<0.07f && abs(pixel.blue-line.blue)<0.07f
                }
                assertTrue("Header divider missing at x=$x, including the time column", painted)
            }
        }
        step("Then the Day weekday and date are centered over the left time column") {
            val axis = bounds("time-axis")
            val header = bounds("day-header-2028-02-15")
            assertEquals(axis.left, header.left, 1f)
            assertEquals(axis.width, header.width, 1f)
            assertEquals(axis.center.x, bounds("day-weekday").center.x, 1f)
            assertEquals(axis.center.x, bounds("day-number").center.x, 1f)
        }
        step("Then the Day weekday uses 12sp text and its date uses 16sp text") {
            assertEquals(12f, textLayout("day-weekday").layoutInput.style.fontSize.value, 0f)
            assertEquals(16f, textLayout("day-number").layoutInput.style.fontSize.value, 0f)
        }
        step("Then no weekday strip is displayed above the Day header") { node("weekday-headers").assertDoesNotExist(); node("month-grid").assertDoesNotExist() }
        step("Then Day hour labels use 12sp text centered vertically on hour lines") {
            node("hour-13").performScrollTo()
            assertEquals(12f, textLayout("hour-13").layoutInput.style.fontSize.value, 0f)
            assertEquals(bounds("hour-13").center.y, hourSeparatorY(13), 1.1f)
        }
        step("Then the Day time column fits the widest hour label with 10dp padding on each side") {
            val widest = (1..23).maxOf { hour -> textLayout("hour-$hour").let { layout -> layout.getLineRight(0) - layout.getLineLeft(0) } }
            assertEquals(widest + dp(20), bounds("time-axis").width, 1.1f)
            val label = bounds("hour-13")
            assertEquals(bounds("time-axis").left + dp(10), label.left, 1.1f)
            assertEquals(bounds("time-axis").right - dp(10), label.right, 1.1f)
        }
        step("Then the Day time grid starts one hour before the current local time within its scroll limits") {
            val range = node("time-grid").fetchSemanticsNode().config[SemanticsProperties.VerticalScrollAxisRange]
            val time = ZonedDateTime.now(environment.clock).toLocalTime()
            val wantedHours = if (time.hour == 0) 0f else time.hour + time.minute / 60f - 1f
            assertEquals((wantedHours * dp(60)).coerceAtMost(range.maxValue()), range.value(), 1.1f)
        }
        step("Given Month displays ([A-Za-z]+ \\d{4}) with today selected") {
            navigateMonth(parseMonth(it.groupValues[1])); node("today").performClick()
        }
        step("When the owner opens the navigation drawer and chooses (Month|Week|Day)") { choose(it.groupValues[1]) }
        step("Then the drawer closes") { node("view-drawer").assertDoesNotExist() }
        step("Then the time grid has (\\d+) date columns from (.+) through (.+)") {
            val count = it.groupValues[1].toInt()
            assertEquals(count, dateHeaders().size)
            assertTimedRange(if (count == 7) "Week" else "Day", it.groupValues[2], it.groupValues[3])
        }
        step("Then hour labels appear to the left of horizontal hour separators") {
            node("hour-12").performScrollTo().assertIsDisplayed()
            assertTrue(bounds("hour-12").right <= bounds("time-axis").right + 1)
            hourSeparatorY(12) // Verify a painted separator, not just the presence of an empty container.
        }
        step("Then the default distance between successive hour separators is (\\d+) density-independent pixels") {
            node("hour-12").performScrollTo()
            assertEquals(dp(it.groupValues[1].toInt()), hourSeparatorY(13) - hourSeparatorY(12), 1f)
        }
        step("Then no month expansion control or dropdown picker is displayed") {
            listOf("toggle-mini", "mini-expanded", "mini-collapsed", "month-grid").forEach { tag -> node(tag).assertDoesNotExist() }
            ui.onAllNodesWithContentDescription("Expand date picker", useUnmergedTree = true).assertCountEquals(0)
            ui.onAllNodesWithContentDescription("Collapse date picker", useUnmergedTree = true).assertCountEquals(0)
        }
        step("Then the time grid remains visible") { node("time-viewport").assertIsDisplayed(); node("date-headers").assertIsDisplayed() }
        step("Given (Week|Day) displays (?:the date range )?(\\d{4}-\\d{2}-\\d{2}) through (\\d{4}-\\d{2}-\\d{2})") {
            openDate(LocalDate.parse(it.groupValues[2]), it.groupValues[1]); assertTimedRange(it.groupValues[1], it.groupValues[2], it.groupValues[3])
        }
        step("Given the device uses (24-hour|12-hour) time") { assertEquals(it.groupValues[1] == "24-hour", environment.is24Hour) }
        step("When the owner scrolls vertically to the (start|end) of the day") {
            node("time-grid").performSemanticsAction(SemanticsActions.ScrollBy) { scroll -> scroll(0f, if (it.groupValues[1] == "start") -10000f else 10000f) }
        }
        step("Then the hour label (.+) is visible") { ui.onNodeWithText(it.groupValues[1], useUnmergedTree = true).assertIsDisplayed() }
        step("Then (Week|Day) displays the date range (.+) through (.+)") { assertTimedRange(it.groupValues[1], it.groupValues[2], it.groupValues[3]) }
        step("When the owner taps the date header for (.+)") { node("day-header-${it.groupValues[1]}").performClick() }
        step("Then (Month|Week|Day) remains the active view") { node("view-${it.groupValues[1]}").assertExists() }
        step("Given Day displays (\\d{4}-\\d{2}-\\d{2})") { openDate(LocalDate.parse(it.groupValues[1]), "Day") }
        step("When the owner scrolls to the hour thirteen hours after midnight") { node("hour-13").performScrollTo() }
        step("Then that hour label is (.+)") { node("hour-13").assertTextEquals(it.groupValues[1]).assertIsDisplayed() }
    }

    private fun shellOutput(command: String): String {
        val descriptor = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand(command)
        return android.os.ParcelFileDescriptor.AutoCloseInputStream(descriptor).bufferedReader().use { it.readText().trim() }
    }

    private fun registerMonthSteps() {
        step("Given the calendar contains no events") { /* Empty app has no event data source; verify visible result after launch. */ }
        step("Given the device date is (.+) in time zone (.+)") { assertEquals(ZoneId.of(it.groupValues[2]), environment.clock.zone) }
        step("Given the device locale is (English United States|French France)") { /* Input resolved before composition; later localized assertions exercise it. */ }
        step("Given the app has not been launched before") { assertFalse(launched) }
        step("Given the device has no network connection and no Google Play services") {
            val pm = ui.activity.packageManager
            @Suppress("DEPRECATION") val app = pm.getPackageInfo(ui.activity.packageName, PackageManager.GET_PERMISSIONS)
            assertFalse(app.requestedPermissions.orEmpty().contains("android.permission.INTERNET"))
            // Shell package visibility avoids a false negative from Android's app package-query filtering.
            assertTrue("Acceptance emulator must not contain Google Play services", shellOutput("pm path com.google.android.gms").isEmpty())
            assertEquals("Acceptance emulator must be offline", "1", shellOutput("settings get global airplane_mode_on"))
            assertEquals("Wi-Fi must be off even in airplane mode", "0", shellOutput("settings get global wifi_on"))
        }
        step("When the owner launches the app") { launch() }
        step("(Given|When) (?:the owner opens Month for|Month displays) (\\d{4}-\\d{2}-\\d{2}|[A-Za-z]+ \\d{4})") { navigateMonth(parseMonth(it.groupValues[2])) }
        step("When the owner swipes the month grid (left|right) once") {
            node("month-grid").performTouchInput { if (it.groupValues[1] == "left") swipeLeft() else swipeRight() }
        }
        step("When the owner taps the cell for (.+)") { node("date-${it.groupValues[1]}").performClick() }
        step("Then (Month|Week|Day) is the active view") { node("view-${it.groupValues[1]}").assertExists() }
        step("Then the toolbar title is (.+)") { node("month-title").assertTextEquals(it.groupValues[1]) }
        step("Then Month displays (.+)") {
            val month = parseMonth(it.groupValues[1])
            assertTrue(dateNodes().count { n -> LocalDate.parse(n.config[SemanticsProperties.TestTag].removePrefix("date-")).let { d -> d.year == month.year && d.month == month.month } } == month.lengthOfMonth())
        }
        step("Then the calendar has (\\d+) equal-width columns and (\\d+) equal-height rows") {
            val columns = it.groupValues[1].toInt(); val rows = it.groupValues[2].toInt()
            val grid = bounds("month-grid")
            val cells = dateNodes()
            assertEquals(columns * rows, cells.size)
            cells.forEach { cell -> assertEquals(grid.width / columns, cell.boundsInRoot.width, 1.1f); assertEquals(grid.height / rows, cell.boundsInRoot.height, 1.1f) }
            assertEquals(rows, cells.map { n -> n.boundsInRoot.top }.distinct().size)
            assertEquals(columns, cells.map { n -> n.boundsInRoot.left }.distinct().size)
        }
        step("Then weekday headers run from Monday through Sunday") {
            val headers = (1..7).map { day -> node("weekday-$day").fetchSemanticsNode() }
            headers.zipWithNext().forEach { (a,b) -> assertTrue(a.boundsInRoot.left < b.boundsInRoot.left) }
            java.time.DayOfWeek.entries.forEachIndexed { index, day -> assertEquals(day.getDisplayName(java.time.format.TextStyle.FULL, environment.locale), headers[index].config[SemanticsProperties.ContentDescription].single()) }
        }
        step("Then (?:the|its) 42 date cells run consecutively from (.+) through (.+)") { assertDateRange(it.groupValues[1], it.groupValues[2]) }
        step("Then no event blocks appear") { ui.onAllNodesWithTag("event-block", useUnmergedTree = true).assertCountEquals(0) }
        step("Then no sign-in or network requirement prevents calendar use") { node("month-grid").assertIsDisplayed(); node("open-drawer").assertHasClickAction() }
        step("Then (.+) appears exactly once") { ui.onAllNodesWithTag("date-${it.groupValues[1]}", useUnmergedTree = true).assertCountEquals(1) }
        step("Then its single date column is (.+)") { node("day-header-${it.groupValues[1]}").assertIsDisplayed() }
        step("Then weekday headers occupy (\\d+) density-independent pixels in height") { assertEquals(dp(it.groupValues[1].toInt()), bounds("weekday-headers").height, 1f) }
        step("Then month grid dividing lines are (\\d+) density-independent pixel wide") {
            val image = node("month-grid").captureToImage().toPixelMap()
            val grid = bounds("month-grid")
            val x = (grid.width / 7).toInt()
            val y = (grid.height / 12).toInt()
            val line = Color(0xFFE5E4EA)
            val width = ((x - 4)..(x + 4)).count { px -> image[px,y].red < 0.99f && abs(image[px,y].red - line.red) < 0.08f }
            assertEquals(dp(it.groupValues[1].toInt()), width.toFloat(), 1.1f)
        }
        step("Then day numbers use (\\d+) scale-independent pixel text centered horizontally in each cell") {
            dateNodes().forEach { cell ->
                val date = cell.config[SemanticsProperties.TestTag].removePrefix("date-")
                val layout = textLayout("number-$date")
                assertEquals(it.groupValues[1].toFloat(), layout.layoutInput.style.fontSize.value, 0f)
                assertEquals(cell.boundsInRoot.center.x, bounds("number-$date").center.x, 1f)
            }
        }
        step("Then day-number baselines are (\\d+) density-independent pixels below the top of each row") {
            dateNodes().forEach { cell ->
                val date = cell.config[SemanticsProperties.TestTag].removePrefix("date-")
                val baseline = bounds("number-$date").top + textLayout("number-$date").firstBaseline
                assertEquals(dp(it.groupValues[1].toInt()), baseline - cell.boundsInRoot.top, 1f)
            }
        }
        step("Then adjacent-month date numbers are muted relative to February date numbers") {
            val muted = textLayout("number-2028-01-31").layoutInput.style.color
            val normal = textLayout("number-2028-02-01").layoutInput.style.color
            assertTrue(muted.red > normal.red && muted.green > normal.green && muted.blue > normal.blue)
        }
        step("Then February 15 has an accent-colored rounded outline (\\d+) density-independent pixels wide and (\\d+) high") {
            val pixels = node("date-2028-02-15").captureToImage().toPixelMap()
            val points = buildList { for (y in 0 until dp(26).toInt()) for (x in 0 until pixels.width) if (abs(pixels[x,y].red - accent.red) < 0.06f && abs(pixels[x,y].blue - accent.blue) < 0.06f && pixels[x,y].green < 0.4f) add(x to y) }
            assertTrue("Expected accent pixels", points.isNotEmpty())
            val left = points.minOf { p -> p.first }; val top = points.minOf { p -> p.second }
            val width = points.maxOf { p -> p.first } - left + 1f
            val height = points.maxOf { p -> p.second } - top + 1f
            assertEquals(dp(it.groupValues[1].toInt()), width, 2f)
            assertEquals(dp(it.groupValues[2].toInt()), height, 2f)
            outline = OutlinePixels(width, height, pixels, left, top)
        }
        step("Then the outline has a radius of (\\d+) density-independent pixels and a stroke of (\\d+)") {
            val value = requireNotNull(outline)
            val radius = dp(it.groupValues[1].toInt())
            // Use the geometric rectangle corner, not the inset bounding box of fully saturated pixels.
            val left = ((value.pixels.width - dp(21)) / 2).toInt()
            val top = dp(4).toInt()
            val corner = value.pixels[left,top]
            assertTrue("Rounded corner should be unpainted", corner.alpha < 0.1f || corner.green > 0.7f)
            val middle = value.pixels.width / 2
            val thickness = (top - 1 until top + dp(3).toInt()).count { py ->
                val pixel = value.pixels[middle,py]
                pixel.alpha > 0.5f && pixel.green < 0.7f
            }
            assertEquals(dp(it.groupValues[2].toInt()), thickness.toFloat(), 1f)
            val edge = value.pixels[left + radius.toInt(),top]
            assertTrue("Top edge must begin beyond the rounded corner", edge.alpha > 0.2f && edge.green < 0.9f)
        }
        step("Then no other date cell has the today outline") {
            dateNodes().filter { it.config[SemanticsProperties.TestTag] != "date-2028-02-15" }.forEach { cell ->
                val pixels = node(cell.config[SemanticsProperties.TestTag]).captureToImage().toPixelMap()
                val hasAccent = (0 until dp(26).toInt()).any { y -> (0 until pixels.width).any { x -> pixels[x,y].green < 0.4f && abs(pixels[x,y].blue-accent.blue)<0.06f && abs(pixels[x,y].red-accent.red)<0.06f } }
                assertFalse("Unexpected today outline", hasAccent)
            }
        }
    }
}
