package app.local.calendar

import android.os.Bundle
import android.text.format.DateFormat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.time.Clock
import java.util.Locale

/** Device inputs are injectable into the same UI for deterministic development tests. */
data class CalendarEnvironment(val clock: Clock, val locale: Locale, val is24Hour: Boolean)

class MainActivity : ComponentActivity() {
    private var environment by mutableStateOf(CalendarEnvironment(Clock.systemDefaultZone(), Locale.getDefault(), true))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        refreshEnvironment()
        setContent { CalendarApp(environment) }
    }

    override fun onResume() {
        super.onResume()
        refreshEnvironment()
    }

    private fun refreshEnvironment() {
        environment = CalendarEnvironment(Clock.systemDefaultZone(), resources.configuration.locales[0], DateFormat.is24HourFormat(this))
    }
}
