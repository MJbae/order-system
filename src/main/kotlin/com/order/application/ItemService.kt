package com.order.application

import com.order.domain.Item
import com.order.infra.ItemRepository
import org.springframework.stereotype.Service

@Service
class ItemService(
    private val itemRepository: ItemRepository
) {
    fun loadAll(): List<Item> {
        return itemRepository.findAll()
    }
}
