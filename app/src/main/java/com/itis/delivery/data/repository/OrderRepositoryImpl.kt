package com.itis.delivery.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.itis.delivery.base.Keys
import com.itis.delivery.base.Keys.LAST_CHANGE
import com.itis.delivery.base.Keys.ORDER_NUMBER
import com.itis.delivery.base.Keys.STATE
import com.itis.delivery.base.Keys.USER_ID
import com.itis.delivery.data.exceptions.UserNotAuthorizedException
import com.itis.delivery.domain.model.OrderModel
import com.itis.delivery.domain.model.OrderModel.Companion.CANCELLED
import com.itis.delivery.domain.repository.OrderRepository
import com.itis.delivery.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val userRepository: UserRepository
) : OrderRepository {
    override suspend fun getOrders(): List<OrderModel> {
        val userId = userRepository.getCurrentUserId()
        if (userId.isNullOrEmpty()) throw UserNotAuthorizedException("User not authorized")

        val documents = db.collection(Keys.ORDERS_COLLECTION_KEY)
            .whereEqualTo(USER_ID, userId)
            .get()
            .await()
            .documents

        val result = mutableListOf<OrderModel>()
        documents.forEach {
            it.toObject(OrderModel::class.java)?.let { it1 -> result.add(it1) }
        }
        return result
    }

    override suspend fun addOrder(products: Map<Long, Long>, address: String): Boolean {
        val userId = userRepository.getCurrentUserId()
        if (userId.isNullOrEmpty()) throw UserNotAuthorizedException("User not authorized")

        val id = UUID.randomUUID()
        val orderNumber = id.mostSignificantBits and Long.MAX_VALUE
        db.collection(Keys.ORDERS_COLLECTION_KEY)
            .document(id.toString())
            .set(
                OrderModel(
                    id = id.toString(),
                    userId = userId,
                    products = products.map { it.key.toString() to it.value.toString() }.toMap(),
                    orderNumber = orderNumber,
                    address = address,
                    lastChange = System.currentTimeMillis(),
            ))
            .await()
        return true
    }

    override suspend fun getOrder(orderNumber: Long): OrderModel {
        val userId = userRepository.getCurrentUserId()
        if (userId.isNullOrEmpty()) throw UserNotAuthorizedException("User not authorized")

        val documents = db.collection(Keys.ORDERS_COLLECTION_KEY)
            .whereEqualTo(USER_ID, userId)
            .whereEqualTo(ORDER_NUMBER, orderNumber)
            .get()
            .await()
            .documents

        documents[0].toObject(OrderModel::class.java)?.let { it1 -> return it1 }
        return OrderModel()
    }

    override suspend fun cancelOrder(orderId: String): Boolean {
        val userId = userRepository.getCurrentUserId()
        if (userId.isNullOrEmpty()) throw UserNotAuthorizedException("User not authorized")

        val documents = db.collection(Keys.ORDERS_COLLECTION_KEY)
            .whereEqualTo(USER_ID, userId)
            .whereEqualTo(ORDER_NUMBER, orderId)
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