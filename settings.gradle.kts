pluginManagement {
    repositories { google(); mavenCentral(); gradlePluginPortal() }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories { google(); mavenCentral() }
}
rootProject.name = "app-suite"
include(":calendar", ":design")
project(":calendar").projectDir = file("apps/calendar-app")
project(":design").projectDir = file("shared/design")
