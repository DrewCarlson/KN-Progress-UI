import kotlinx.cinterop.*
import platform.windows.*

actual class ProgressWindow actual constructor(title: String) {

    private val windowWidth = 500
    private val windowHeight = 100
    private val className = "progressWindow"

    private val hInstance = GetModuleHandleA(null)
    private var hwnd: HWND? = null
    private var hProgressCtrl: HWND? = null

    init {
        memScoped {
            val icc = alloc<INITCOMMONCONTROLSEX> {
                dwSize = sizeOf<INITCOMMONCONTROLSEX>().toUInt()
                dwICC = ICC_WIN95_CLASSES.toUInt()
            }
            val wndCls = alloc<WNDCLASSEXA> {
                lpfnWndProc = windowProcCallback
                hInstance = hInstance
                lpszClassName = className.cstr.ptr
                cbSize = sizeOf<WNDCLASSEXA>().toUInt()
            }
            InitCommonControlsEx(icc.ptr)
            if (RegisterClassExA(wndCls.ptr) == 0u.toUShort()) {
                error("Failed to register window class.")
            }
            hwnd = CreateWindowExA(
                WS_EX_CLIENTEDGE.toUInt(),
                className,
                title,
                (WS_OVERLAPPEDWINDOW xor WS_THICKFRAME xor WS_MAXIMIZEBOX).toUInt(),
                CW_USEDEFAULT,
                CW_USEDEFAULT,
                windowWidth,
                windowHeight,
                null,
                null,
                hInstance,
                null,
            )
            val clientRect = alloc<RECT>()
            check(GetClientRect(hwnd, clientRect.ptr) == TRUE) { "Failed to get window client area." }
            hProgressCtrl = CreateWindowExA(
                0u,
                PROGRESS_CLASSA,
                "",
                (WS_CHILD or WS_VISIBLE or PBS_SMOOTH).toUInt(),
                0,
                0,
                clientRect.right,
                clientRect.bottom,
                hwnd,
                alloc<UInt>(401u).ptr.reinterpret(),
                hInstance,
                null,
            )
            SendMessageA(hProgressCtrl, PBM_SETSTEP.toUInt(), 1u, 0)
        }
    }

    actual fun setTitle(title: String) {
        SetWindowTextA(hwnd, title)
    }

    actual fun setProgress(progress: Int) {
        SendMessageA(hProgressCtrl, PBM_SETPOS.toUInt(), progress.toULong(), 0)
    }

    actual fun setProgressTotal(total: Int) {
        SendMessageA(hProgressCtrl, PBM_SETRANGE32.toUInt(), 0u, total.toLong())
    }

    actual fun stepProgress() {
        SendMessageA(hProgressCtrl, PBM_STEPIT.toUInt(), 0u, 0)
    }

    actual fun showAndWait(): Unit = memScoped {
        val windowRect = alloc<RECT>()
        check(GetWindowRect(hwnd, windowRect.ptr) == TRUE) { "Failed to get window area." }

        val centerX = (GetSystemMetrics(SM_CXSCREEN) - windowRect.right) / 2
        val centerY = (GetSystemMetrics(SM_CYSCREEN) - windowRect.bottom) / 2
        SetWindowPos(hwnd, null, centerX, centerY, 0, 0, (SWP_NOZORDER or SWP_NOSIZE).toUInt())

        ShowWindow(hwnd, SW_SHOWDEFAULT)
        UpdateWindow(hwnd)

        val msg = alloc<MSG>()
        while (GetMessageA(msg.ptr, null, 0u, 0u) > 0) {
            TranslateMessage(msg.ptr)
            DispatchMessageA(msg.ptr)
        }

        UnregisterClassA(className, hInstance)
    }

}

private val windowProcCallback = staticCFunction { hwnd: HWND?, uMsg: UInt, wParam: WPARAM, lParam: LPARAM ->
    when (uMsg.toInt()) {
        WM_CLOSE -> DestroyWindow(hwnd)
        WM_DESTROY -> PostQuitMessage(0)
        else -> return@staticCFunction DefWindowProcA(hwnd, uMsg, wParam, lParam)
    }
    0
}
