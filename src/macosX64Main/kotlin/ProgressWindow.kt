import kotlinx.cinterop.*
import platform.AppKit.*
import platform.Foundation.*
import platform.darwin.*

actual class ProgressWindow actual constructor(title: String) {

    private val delegate = AppDelegate(title)

    private fun runOnMain(block: () -> Unit) {
        dispatch_async(dispatch_get_main_queue(), block)
    }

    actual fun setTitle(title: String) {
        runOnMain { delegate.window.setTitle(title) }
    }

    actual fun setProgress(progress: Int) {
        runOnMain { delegate.progressIndicator.setDoubleValue(progress.toDouble()) }
    }

    actual fun setProgressTotal(total: Int) {
        runOnMain { delegate.progressIndicator.setMaxValue(total.toDouble()) }
    }

    actual fun stepProgress() {
        runOnMain { delegate.progressIndicator.incrementBy(1.0) }
    }

    actual fun showAndWait() {
        autoreleasepool {
            val app = NSApplication.sharedApplication()
            app.delegate = delegate
            app.setActivationPolicy(NSApplicationActivationPolicy.NSApplicationActivationPolicyRegular)
            app.activateIgnoringOtherApps(true)
            app.run()
        }
    }
}

private class AppDelegate(title: String) : NSObject(), NSApplicationDelegateProtocol {

    val progressIndicator = NSProgressIndicator().apply {
        indeterminate = false
    }
    val window = NSWindow(
        contentRect = NSMakeRect(100.0, 100.0, 500.0, 75.0),
        styleMask = NSWindowStyleMaskTitled or NSWindowStyleMaskClosable,
        backing = NSBackingStoreBuffered,
        defer = false
    ).apply {
        center()
        setTitle(title)
        preferredBackingLocation = NSWindowBackingLocationVideoMemory
        hidesOnDeactivate = false
        releasedWhenClosed = false
        delegate = object : NSObject(), NSWindowDelegateProtocol {
            override fun windowShouldClose(sender: NSWindow): Boolean {
                NSApplication.sharedApplication().stop(this)
                return true
            }
        }
        contentView = progressIndicator
    }

    override fun applicationWillFinishLaunching(notification: NSNotification) {
        window.makeKeyAndOrderFront(this)
    }
}
