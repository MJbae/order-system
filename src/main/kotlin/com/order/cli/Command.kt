package com.order.cli

import com.order.cli.printer.ByePrinter
import com.order.cli.printer.ProductPrinter
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.commands.Quit

@ShellComponent
class Command(
    private val productPrinter: ProductPrinter,
    private val byePrinter: ByePrinter
) : Quit.Command {
    @ShellMethod(key = ["order", "o"], value = "order")
    fun order() {
        productPrinter.show()
    }

    @ShellMethod(key = ["quit", "q"], value = "quit")
    fun quit() {
        byePrinter.show()
    }
}
