package com.order.cli.interfaces

interface ResultPrinter<T> {
    fun showBy(result: T)
}
