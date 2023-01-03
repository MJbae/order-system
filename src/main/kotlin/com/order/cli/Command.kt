package com.order.cli

import com.order.application.OrderServiceImp
import com.order.cli.dto.OrderData
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
    private val orderServiceImp: OrderServiceImp,
    private val orderData: ArrayList<OrderData>
) : Quit.Command {
    @ShellMethod(key = ["order", "o"], value = "order")
    fun order() {
        productPrinter.show()

        while (true) {
            itemIdPrompt.display()
            val itemInput = readLine()

            if (itemInput.equals(" ")) {
                val order = orderServiceImp.order(orderData[0])
                orderProductPrinter.showBy(order)
                orderData.clear()
                break
            }

            orderCountPrompt.display()
            val countInput = readLine()

            orderData.add(OrderData(itemInput!!.toLong(), countInput!!.toInt(), null))
        }
    }

    @ShellMethod(key = ["quit", "q"], value = "quit")
    fun quit() {
        byePrinter.show()
        exitProcess(1)
    }
}
