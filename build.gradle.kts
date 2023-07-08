plugins {
    kotlin("multiplatform") version "1.9.0"
    id("com.louiscad.complete-kotlin") version "1.1.0"
}

repositories {
    mavenCentral()
}

kotlin {
    val targets = listOf(
        macosX64(),
        macosArm64(),
        mingwX64(),
    )

    targets.forEach { target ->
        target.binaries {
            executable {
                entryPoint = "main"
                if (target == mingwX64()) {
                    linkerOpts.add("-Wl,-subsystem,windows")
                }
            }
        }
    }
    sourceSets {
        all {
            languageSettings {
                optIn("kotlinx.cinterop.BetaInteropApi")
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
            }
        }
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
            }
        }
        val macosX64Main by getting
        val macosArm64Main by getting {
            dependsOn(macosX64Main)
        }
    }
}
