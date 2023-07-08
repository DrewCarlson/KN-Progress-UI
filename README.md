# K/N Progress UI

A simple and dependency free progress UI example for Kotlin Native.


```Kotlin
expect class ProgressWindow(title: String) {

    fun setTitle(title: String)

    fun setProgress(progress: Int)

    fun setProgressTotal(total: Int)

    fun stepProgress()

    fun showAndWait()
}
```

- Windows [ProgressWindow.kt](src/mingwX64Main/kotlin/ProgressWindow.kt)
- macOS [ProgressWindow.kt](src/macosX64Main/kotlin/ProgressWindow.kt)
- [Example Program](src/commonMain/kotlin/main.kt)

### Changes

Updated to Kotlin 1.9.0

- OptIn to `kotlinx.cinterop.ExperimentalForeignApi`
- OptIn to `kotlinx.cinterop.BetaInteropApi`
- A number of Windows constants/inputs must be converted to `uint`s
- Remove explicit memory model configuration from `gradle.properties`
