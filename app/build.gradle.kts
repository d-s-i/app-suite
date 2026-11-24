plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

val taskRoot = rootProject.layout.projectDirectory.dir(".ai/tasks/CAL-001")
val generatedAcceptance = taskRoot.dir("acceptance/generated")
val acceptanceFeature = providers.gradleProperty("acceptanceFeature")

android {
    namespace = "app.local.calendar"
    compileSdk = 35
    defaultConfig {
        applicationId = "app.local.calendar"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        acceptanceFeature.orNull?.let {
            require(it.matches(Regex("CAL-001-F0[123]"))) { "Unknown acceptanceFeature: $it" }
            testInstrumentationRunnerArguments["class"] = "app.local.calendar.acceptance.${it.replace("-", "")}AcceptanceTest"
        }
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
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.compose.ui:ui:1.7.8")
    implementation("androidx.compose.foundation:foundation:1.7.8")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.8")
}

val generateAcceptance by tasks.registering(Exec::class) {
    group = "verification"
    description = "Parse approved Gherkin with parse-gherkin and generate Android acceptance entrypoints."
    workingDir(rootProject.projectDir)
    commandLine("node", "tools/generate-acceptance.mjs")
    inputs.dir(taskRoot.dir("features"))
    inputs.file(rootProject.file("tools/generate-acceptance.mjs"))
    outputs.dir(generatedAcceptance)
    outputs.upToDateWhen { false }
}
tasks.matching { it.name == "preDebugAndroidTestBuild" }.configureEach { dependsOn(generateAcceptance) }
tasks.register("acceptanceTest") {
    group = "verification"
    description = "Run generated Gherkin acceptance tests on a connected test device."
    dependsOn("connectedDebugAndroidTest")
}
tasks.register<Exec>("testAcceptanceGenerator") {
    group = "verification"
    workingDir(rootProject.projectDir)
    commandLine("node", "--test", "tools/generate-acceptance.test.mjs")
}
