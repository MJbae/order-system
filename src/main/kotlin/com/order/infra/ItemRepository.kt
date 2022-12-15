package com.order.infra

import com.order.domain.Item
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.repository.query.Param
import javax.persistence.LockModeType

interface ItemRepository : JpaRepository<Item, Long> {
    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select i from Item i where i.id = :id")
    fun findByIdInLock(@Param("id") id: Long) = this.findByIdOrNull(id) ?: throw NotFoundException()
}
