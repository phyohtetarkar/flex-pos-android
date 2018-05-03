package com.jsoft.es.func

interface KConsumer2<in T, in U> {
    fun accept(var1: T, var2: U)
}