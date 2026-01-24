package pl.nubet.studymood.presentation.common

class StepNavigator(private val totalSteps: Int, startIndex: Int = 0) {
    var currentIndex: Int = startIndex
        private set

    var isCompleted: Boolean = false
        private set

    fun next() {
        val nextIndex = currentIndex + 1
        if (nextIndex < totalSteps) {
            currentIndex = nextIndex
        }
    }

    fun prev() {
        val prevIndex = currentIndex - 1
        if (prevIndex >= 0 && !isCompleted) {
            currentIndex = prevIndex
        }
    }

    fun complete() {
        isCompleted = true
    }
}
