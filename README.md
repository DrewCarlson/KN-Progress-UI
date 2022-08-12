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
