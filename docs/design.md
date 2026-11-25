# Reusing the existing design

The shared module is [`:design`](../shared/design/build.gradle.kts), mapped in
[`settings.gradle.kts`](../settings.gradle.kts). Its public API is
[`app.suite.design.SuiteColors`](../shared/design/src/main/java/app/suite/design/SuiteColors.kt).
It contains the existing Calendar colors, with unchanged values:

| Token | Usage in Calendar |
| --- | --- |
| `Ink` | Primary text and toolbar icons |
| `Muted` | Secondary labels and adjacent-month dates |
| `Accent` | Today, selection and current-time indicator |
| `Line` | Grid dividers |
| `Soft` | Selected navigation background |

An Android Kotlin/Compose app in this repository consumes the definitions rather
than copying them. Add to that app module's `dependencies`:

```kotlin
implementation(project(":design"))
```

Then use the public API with existing Compose UI/Foundation components:

```kotlin
import app.suite.design.SuiteColors
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.text.TextStyle

// Inside a @Composable function:
BasicText("Title", style = TextStyle(color = SuiteColors.Ink))
```

Calendar (`:calendar`) already consumes this module in
[`apps/calendar-app/build.gradle.kts`](../apps/calendar-app/build.gradle.kts).
[`CalendarApp.kt`](../apps/calendar-app/src/main/java/app/local/calendar/CalendarApp.kt) and
[`TimedCalendar.kt`](../apps/calendar-app/src/main/java/app/local/calendar/TimedCalendar.kt)
import `SuiteColors` under the local alias `CalendarColors`; the alias is not a
second palette. Change the versioned shared definition, not a per-app copy.
The module uses the same pinned Compose UI dependency as Calendar and adds no
Material library, theme, permissions or runtime service.

## Deliberately not shared

Calendar's typography and dimensions remain at their existing call sites: they
are adapted to date cells, toolbar and hour-axis constraints, not a general
component catalogue. White surfaces, Compose defaults and platform system-bar
configuration are unchanged. No new palette or visual redesign was introduced.

Date calculations, period navigation, gestures, semantics/test tags, local
clock/locale inputs, hour-label measurement and all calendar rendering behavior
stay in Calendar. `:design` does not depend on `:calendar`, Android activities, event
storage or application navigation. Future business logic must not be added to
this palette module merely because multiple apps could use it.

Only Calendar exists. Sharing has been implemented and built for this consumer;
it has not been demonstrated in a second app. Extract further genuinely shared
presentation only when required and approved, rather than adding speculative
components.

## Check changes

Follow the Calendar checks in the [app guide](../apps/calendar-app/README.md#verify), including
all 49 generated UI acceptance cases on a dedicated offline test emulator,
`./gradlew :design:lintDebug`, debug/release builds and release-artifact checks.
Check all real consuming apps when others exist. Preserve full single-line hour
labels at fractional densities and large fonts; changing a palette must not
change date/navigation behavior. Rebuild and install Calendar debug in place on
the owner's verified `android-dev` emulator after approved implementation changes.
