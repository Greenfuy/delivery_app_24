package com.itis.delivery.data.mapper

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.itis.delivery.domain.model.OrderData
import com.itis.delivery.domain.model.OrderDomainModel
import com.itis.delivery.domain.model.OrderProduct
import javax.inject.Inject

class OrderDomainModelMapper @Inject constructor() {

    fun mapDocumentsToDomainModel(
        dataDocument: DocumentSnapshot,
        productsDocuments: List<DocumentSnapshot>
    ): OrderDomainModel {
        val orderData = dataDocument.toObject(OrderData::class.java)
        Log.d("OrderDomainMapper", "orderData: $orderData")
        val orderProducts = productsDocuments
            .map {
                it.toObject(OrderProduct::class.java)
                    ?: OrderProduct()
            }
        Log.d("OrderDomainMapper", "orderProducts.size: ${orderProducts.size}")

        return OrderDomainModel(
            orderData = orderData ?: OrderData(),
            orderProducts = orderProducts
        )
    }
}