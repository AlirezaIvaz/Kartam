package ir.alirezaivaz.kartam.utils

object ClipboardMemory {
    private var lastText: String? = null

    fun shouldHandle(text: String): Boolean {
        if (text == lastText) {
            return false
        }
        lastText = text
        return true
    }
}
