package com.itis.delivery.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.itis.delivery.base.Keys
import com.itis.delivery.base.Keys.LAST_CHANGE
import com.itis.delivery.base.Keys.ORDER_NUMBER
import com.itis.delivery.base.Keys.STATE
import com.itis.delivery.base.Keys.USER_ID
import com.itis.delivery.data.exceptions.UserNotAuthorizedException
import com.itis.delivery.data.mapper.OrderDomainModelMapper
import com.itis.delivery.domain.model.OrderData
import com.itis.delivery.domain.model.OrderDomainModel
import com.itis.delivery.domain.model.OrderDomainModel.Companion.CANCELLED
import com.itis.delivery.domain.repository.OrderRepository
import com.itis.delivery.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val userRepository: UserRepository,
    private val mapper: OrderDomainModelMapper
) : OrderRepository {
    override suspend fun getOrders(): List<OrderDomainModel> {
        val userId = userRepository.getCurrentUserId()
            ?: throw UserNotAuthorizedException("User not authorized")

        val dataDocuments = db.collection(Keys.ORDERS_COLLECTION_KEY)
            .whereEqualTo(USER_ID, userId)
            .get()
            .await()
            .documents

        val list = mutableListOf<OrderDomainModel>()

        dataDocuments.forEach { dataDocument ->
            val productDocuments = dataDocument
                .reference
                .collection(Keys.PRODUCTS)
                .get()
                .await()
                .documents

            list.add(mapper.mapDocumentsToDomainModel(dataDocument, productDocuments))
        }

        Log.d("OrderRepository", "orderList.size: ${list.size}")
        return list
    }

    override suspend fun addOrder(
        products: LongArray,
        counts: LongArray,
        prices: LongArray,
        address: String
    ): Boolean {
        val userId = userRepository.getCurrentUserId()
        if (userId.isNullOrEmpty()) throw UserNotAuthorizedException("User not authorized")

        if (products.size != counts.size || products.size != prices.size) return false

        val orderId = UUID.randomUUID()
        val orderNumber = orderId.mostSignificantBits and Long.MAX_VALUE

        val orderDomainModel = OrderDomainModel(
            orderData = OrderData(
                id = orderId.toString(),
                userId = userId,
                orderNumber = orderNumber,
                address = address,
                createdTime = System.currentTimeMillis(),
                lastChange = System.currentTimeMillis(),
            )
        ).apply {
            addOrders(products, counts, prices)
        }

        db.collection(Keys.ORDERS_COLLECTION_KEY)
            .document(orderId.toString())
            .set(orderDomainModel.orderData)
            .await()

        orderDomainModel.orderProducts.forEach {
            db.collection(Keys.ORDERS_COLLECTION_KEY)
                .document(orderId.toString())
                .collection(Keys.PRODUCTS)
                .document(it.productId.toString())
                .set(it)
                .await()
        }
        return true
    }

    override suspend fun getOrder(orderNumber: Long): OrderDomainModel {
        val userId = userRepository.getCurrentUserId()
        if (userId.isNullOrEmpty()) throw UserNotAuthorizedException("User not authorized")

        val dataDocuments = db.collection(Keys.ORDERS_COLLECTION_KEY)
            .whereEqualTo(USER_ID, userId)
            .whereEqualTo(ORDER_NUMBER, orderNumber)
            .get()
            .await()
            .documents

        val productDocuments = dataDocuments[0]
            .reference
            .collection(Keys.PRODUCTS)
            .get()
            .await()
            .documents

        return mapper.mapDocumentsToDomainModel(dataDocuments[0], productDocuments)
    }

    override suspend fun cancelOrder(orderNumber: Long): Boolean {
        val userId = userRepository.getCurrentUserId()
        if (userId.isNullOrEmpty()) throw UserNotAuthorizedException("User not authorized")

        val documents = db.collection(Keys.ORDERS_COLLECTION_KEY)
            .whereEqualTo(USER_ID, userId)
            .whereEqualTo(ORDER_NUMBER, orderNumber)
            .get()
            .await()
            .documents

        if (documents.isNotEmpty()) {
            documents[0].reference
                .update(STATE, CANCELLED)
                .await()
            documents[0].reference
                .update(LAST_CHANGE, System.currentTimeMillis())
                .await()

            return true
        }
        return false
    }
}