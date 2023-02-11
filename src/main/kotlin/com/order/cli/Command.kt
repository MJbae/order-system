package com.order.cli

import com.order.application.OrderCommand
import com.order.application.OrderService
import com.order.cli.printer.ByePrinter
import com.order.cli.printer.OrderProductPrinter
import com.order.cli.printer.ProductPrinter
import com.order.cli.prompt.ItemIdPrompt
import com.order.cli.prompt.OrderCountPrompt
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.commands.Quit
import kotlin.system.exitProcess

@ShellComponent
class Command(
    private val productPrinter: ProductPrinter,
    private val byePrinter: ByePrinter,
    private val orderProductPrinter: OrderProductPrinter,
    private val itemIdPrompt: ItemIdPrompt,
    private val orderCountPrompt: OrderCountPrompt,
    private val orderService: OrderService,
    private val orderData: ArrayList<OrderCommand>
) : Quit.Command {
    @ShellMethod(key = ["order", "o"], value = "order")
    fun order() {
        productPrinter.show()

        while (true) {
            itemIdPrompt.display()
            val itemInput = readLine()

            if (itemInput.equals(" ")) {
                val orderResult = orderService.order(orderData[0])
                orderProductPrinter.showBy(orderResult)
                orderData.clear()
                break
            }

            orderCountPrompt.display()
            val countInput = readLine()

            orderData.add(OrderCommand(itemInput!!.toLong(), countInput!!.toInt()))
        }
    }

    @ShellMethod(key = ["quit", "q"], value = "quit")
    fun quit() {
        byePrinter.show()
        exitProcess(1)
    }
}
