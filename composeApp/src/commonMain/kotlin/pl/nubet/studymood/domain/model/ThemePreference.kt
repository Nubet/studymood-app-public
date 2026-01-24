package pl.nubet.studymood.domain.model

enum class ThemePreference {
    LIGHT,
    DARK,
    SYSTEM;

    companion object {
        fun fromString(value: String?): ThemePreference {
            return when (value?.uppercase()) {
                "DARK" -> DARK
                "SYSTEM" -> SYSTEM
                else -> LIGHT
            }
        }
    }
}
