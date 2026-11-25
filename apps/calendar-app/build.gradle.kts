plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

val taskRoot = rootProject.layout.projectDirectory.dir(".ai/tasks/CAL-001")
val generatedAcceptance = taskRoot.dir("acceptance/generated")
val acceptanceFeature = providers.gradleProperty("acceptanceFeature")
// Credential state stays outside Git and the Nix store. Never fall back to debug signing.
val releaseSigningDir = providers.environmentVariable("CALENDAR_SIGNING_DIR")
    .getOrElse("${System.getProperty("user.home")}/.local/share/calendar-signing")
val releaseStore = file("$releaseSigningDir/calendar-release.p12")
val releasePassword = file("$releaseSigningDir/calendar-release.password")

android {
    namespace = "app.local.calendar"
    compileSdk = 35
    defaultConfig {
        applicationId = "app.local.calendar"
        minSdk = 26
        targetSdk = 35
        versionCode = 2
        versionName = "0.1.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        acceptanceFeature.orNull?.let {
            require(it.matches(Regex("CAL-001-F0[123]"))) { "Unknown acceptanceFeature: $it" }
            testInstrumentationRunnerArguments["class"] = "app.local.calendar.acceptance.${it.replace("-", "")}AcceptanceTest"
        }
    }
    signingConfigs {
        create("calendarRelease") {
            storeFile = releaseStore
            storeType = "PKCS12"
            keyAlias = "calendar-release"
            storePassword = releasePassword.takeIf { it.isFile }?.readText()?.trim()
            keyPassword = storePassword
        }
    }
    buildTypes.getByName("release") {
        signingConfig = signingConfigs.getByName("calendarRelease")
    }
    buildFeatures { compose = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    sourceSets.getByName("androidTest").java.srcDir(generatedAcceptance)
    testOptions { animationsDisabled = true }
    // Keep correctness errors fatal; report SDK/library update advisories as warnings.
    lint { abortOnError = true }
}
kotlin { jvmToolchain(17) }

dependencies {
    implementation(project(":design"))
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.compose.ui:ui:1.7.8")
    implementation("androidx.compose.foundation:foundation:1.7.8")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.8")
}

val validateReleaseCredentials by tasks.registering {
    group = "verification"
    description = "Refuse unsigned release packaging when private signing credentials are missing."
    doLast {
        check(releaseStore.isFile && releasePassword.isFile && releasePassword.readText().isNotBlank()) {
            "Release signing credentials missing in $releaseSigningDir; restore the owner's key/password or set CALENDAR_SIGNING_DIR. Unsigned release packaging is disabled."
        }
    }
}
tasks.matching { it.name == "packageRelease" || it.name == "bundleRelease" }.configureEach {
    dependsOn(validateReleaseCredentials)
}

val generateAcceptance by tasks.registering(Exec::class) {
    group = "verification"
    description = "Parse approved Gherkin with parse-gherkin and generate Android acceptance entrypoints."
    workingDir(project.projectDir)
    commandLine("node", "tools/generate-acceptance.mjs")
    inputs.dir(taskRoot.dir("features"))
    inputs.file(project.file("tools/generate-acceptance.mjs"))
    outputs.dir(generatedAcceptance)
    outputs.upToDateWhen { false }
}
tasks.matching { it.name == "preDebugAndroidTestBuild" }.configureEach { dependsOn(generateAcceptance) }
tasks.register("acceptanceTest") {
    group = "verification"
    description = "Run generated Gherkin acceptance tests on a connected test device."
    dependsOn("connectedDebugAndroidTest")
}
tasks.register<Exec>("testReleaseArtifact") {
    group = "verification"
    description = "Verify release identity, signature pin and separation from debug signing."
    dependsOn("assembleRelease", "assembleDebug")
    workingDir(project.projectDir)
    commandLine("node", "--test", "tools/release-artifact.test.mjs")
}
tasks.register<Exec>("testAcceptanceGenerator") {
    group = "verification"
    workingDir(project.projectDir)
    commandLine("node", "--test", "tools/generate-acceptance.test.mjs")
}
