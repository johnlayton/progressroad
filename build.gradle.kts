plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.logging)
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.9.1")
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("org.progressroad.App")
}
