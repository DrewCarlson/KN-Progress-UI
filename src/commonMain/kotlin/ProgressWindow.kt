expect class ProgressWindow(title: String) {

    fun setTitle(title: String)

    fun setProgress(progress: Int)

    fun setProgressTotal(total: Int)

    fun stepProgress()

    fun showAndWait()
}