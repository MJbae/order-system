package com.order.cli

import com.order.application.OrderCommand
import com.order.application.OrderService
import com.order.cli.printer.ByePrinter
import com.order.cli.printer.ItemPrinter
import com.order.cli.printer.OrderResultPrinter
import com.order.cli.prompt.ItemIdPrompt
import com.order.cli.prompt.OrderQuantityPrompt
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.commands.Quit
import kotlin.system.exitProcess

@ShellComponent
class Command(
    private val itemPrinter: ItemPrinter,
    private val byePrinter: ByePrinter,
    private val orderResultPrinter: OrderResultPrinter,
    private val itemIdPrompt: ItemIdPrompt,
    private val orderQuantityPrompt: OrderQuantityPrompt,
    private val orderService: OrderService,
    private val orderCommands: ArrayList<OrderCommand>
) : Quit.Command {
    @ShellMethod(key = ["order", "o"], value = "order")
    fun order() {
        itemPrinter.show()

        while (true) {
            itemIdPrompt.display()
            val itemInput = readLine()

            if (itemInput.equals(" ")) {
                val orderResult = orderService.order(orderCommands[0])
                orderResultPrinter.showBy(orderResult)
                orderCommands.clear()
                break
            }

            orderQuantityPrompt.display()
            val quantityInput = readLine()

            orderCommands.add(OrderCommand(itemInput!!.toLong(), quantityInput!!.toInt()))
        }
    }

    @ShellMethod(key = ["quit", "q"], value = "quit")
    fun quit() {
        byePrinter.show()
        exitProcess(1)
    }
}
