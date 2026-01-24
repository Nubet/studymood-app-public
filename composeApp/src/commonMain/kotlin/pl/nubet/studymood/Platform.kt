package pl.nubet.studymood

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
