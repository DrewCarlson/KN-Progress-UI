@file:OptIn(DelicateCoroutinesApi::class)

import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.system.exitProcess

fun title(progress: Int) = "Downloading ${progress}%..."

fun main(): Unit = runBlocking {
    val window = ProgressWindow(title(0))
    window.setProgressTotal(100)

    // Some fake progress tracking logic
    GlobalScope.launch(Dispatchers.Default) {
        repeat(100) { i ->
            val next = i + 1
            window.setProgress(next) // Update current progress
            window.setTitle(title(next))

            delay(Random.nextLong(200, 1000))
        }
        delay(500)
        exitProcess(0) // Terminate program when complete
    }

    // Display window and wait for program termination
    window.showAndWait()
    exitProcess(0) // Necessary when `main` body is wrapped in `runBlocking`
}
