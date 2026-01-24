package pl.nubet.studymood.core.util

import kotlin.random.Random

interface RandomProvider {
    fun nextInt(bound: Int): Int

    fun nextBoolean(): Boolean
}

class DefaultRandomProvider : RandomProvider {
    override fun nextInt(bound: Int): Int = Random.nextInt(bound)

    override fun nextBoolean(): Boolean = Random.nextBoolean()
}
