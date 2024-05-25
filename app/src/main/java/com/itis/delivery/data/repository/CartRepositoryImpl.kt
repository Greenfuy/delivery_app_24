package com.itis.delivery.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.itis.delivery.base.Keys
import com.itis.delivery.base.Keys.COUNT
import com.itis.delivery.base.Keys.PRODUCT_ID
import com.itis.delivery.base.Keys.USER_ID
import com.itis.delivery.data.exceptions.UserNotAuthorizedException
import com.itis.delivery.domain.model.CartModel
import com.itis.delivery.domain.repository.CartRepository
import com.itis.delivery.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val userRepository: UserRepository
) : CartRepository {

    override suspend fun getInCartCount(productId: Long): Long {
        val userId = userRepository.getCurrentUserId()
        if (userId.isNullOrEmpty()) throw UserNotAuthorizedException("User not authorized")

        val documents = db.collection(Keys.CARTS_COLLECTION_KEY)
            .whereEqualTo(USER_ID, userId)
            .whereEqualTo(PRODUCT_ID, productId)
            .get()
            .await()
            .documents
        return if (documents.isNotEmpty()) documents[0].getLong(COUNT)!! else 0

    }

    override suspend fun addToCart(productId: Long): Boolean {
        val userId = userRepository.getCurrentUserId()
        if (userId.isNullOrEmpty()) throw UserNotAuthorizedException("User not authorized")

        val documents = db.collection(Keys.CARTS_COLLECTION_KEY)
            .whereEqualTo(USER_ID, userId)
            .whereEqualTo(PRODUCT_ID, productId)
            .get()
            .await()
            .documents

        if (documents.isNotEmpty()) {
            documents.forEach { it.reference.update(COUNT, it.getLong(COUNT)!!.inc()).await() }
        } else {
            val id = UUID.randomUUID().toString()
            db.collection(Keys.CARTS_COLLECTION_KEY)
                .document(id)
                .set(CartModel(id, productId, userId, 1))
                .await()
        }
        return true
    }

    override suspend fun removeFromCart(productId: Long): Boolean {
        val userId = userRepository.getCurrentUserId()
        if (userId.isNullOrEmpty()) throw UserNotAuthorizedException("User not authorized")

        val documents = db.collection(Keys.CARTS_COLLECTION_KEY)
            .whereEqualTo(USER_ID, userId)
            .whereEqualTo(PRODUCT_ID, productId)
            .get()
            .await()
            .documents
        if (documents.isNotEmpty()) {
            if (documents[0].getLong(COUNT)!! > 1) {
                documents.forEach { it.reference.update(COUNT, it.getLong(COUNT)!!.dec()).await() }
            } else {
                documents[0].reference.delete().await()
            }
        }
        return false
    }

    override suspend fun isInCart(productId: Long): Boolean {
        val userId = userRepository.getCurrentUserId()
        if (userId.isNullOrEmpty()) throw UserNotAuthorizedException("User not authorized")

        val documents = db.collection(Keys.CARTS_COLLECTION_KEY)
            .whereEqualTo(USER_ID, userId)
            .whereEqualTo(PRODUCT_ID, productId)
            .get()
            .await()
            .documents
        return documents.isNotEmpty()
    }

    override suspend fun getCartList(): List<CartModel> {
        val userId = userRepository.getCurrentUserId()
        if (userId.isNullOrEmpty()) throw UserNotAuthorizedException("User not authorized")

        val documents = db.collection(Keys.CARTS_COLLECTION_KEY)
            .whereEqualTo(USER_ID, userId)
            .get()
            .await()
            .documents
        val result = mutableListOf<CartModel>()
        documents.forEach {
            it.toObject(CartModel::class.java)?.let { it1 -> result.add(it1) }
        }
        return result
    }

    override suspend fun getCartListByProductIndices(vararg productIds: Long): List<CartModel> {
        val userId = userRepository.getCurrentUserId()
        if (userId.isNullOrEmpty()) throw UserNotAuthorizedException("User not authorized")

        val documents = db.collection(Keys.CARTS_COLLECTION_KEY)
            .whereEqualTo(USER_ID, userId)
            .whereIn(PRODUCT_ID, productIds.toMutableList())
            .get()
            .await()
            .documents
        val result = mutableListOf<CartModel>()
        documents.forEach {
            it.toObject(CartModel::class.java)?.let { it1 -> result.add(it1) }
        }
        return result
    }

    override suspend fun removeAll(vararg productIds: Long): Boolean {
        val userId = userRepository.getCurrentUserId()
        if (userId.isNullOrEmpty()) throw UserNotAuthorizedException("User not authorized")

        if (userId.isEmpty()) return false
        for (id in productIds) {
            val documents = db.collection(Keys.CARTS_COLLECTION_KEY)
                .whereEqualTo(PRODUCT_ID, id)
                .whereEqualTo(USER_ID, userId)
                .get()
                .await()
                .documents

            if (documents.isNotEmpty()) documents.forEach { it.reference.delete().await() }
        }
        return true
    }
}