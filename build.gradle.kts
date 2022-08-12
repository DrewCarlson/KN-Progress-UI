plugins {
    kotlin("multiplatform") version "1.7.10"
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
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
            }
        }
        val macosX64Main by getting
        val macosArm64Main by getting {
            dependsOn(macosX64Main)
        }
    }
}
