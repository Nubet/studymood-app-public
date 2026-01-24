package pl.nubet.studymood.core.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

interface MainScopeProvider {
    val scope: CoroutineScope
}

class DefaultMainScopeProvider : MainScopeProvider {
    override val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
}
