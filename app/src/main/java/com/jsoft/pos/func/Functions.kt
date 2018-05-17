package com.jsoft.pos.func

interface KConsumer2<in T, in U> {
    fun accept(var1: T, var2: U)
}