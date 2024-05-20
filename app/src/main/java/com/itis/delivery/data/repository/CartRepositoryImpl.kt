package com.itis.delivery.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.itis.delivery.base.Keys
import com.itis.delivery.base.Keys.COUNT
import com.itis.delivery.base.Keys.PRODUCT_ID
import com.itis.delivery.base.Keys.USER_ID
import com.itis.delivery.domain.model.CartModel
import com.itis.delivery.domain.repository.CartRepository
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : CartRepository {

    override suspend fun getInCartCount(userId: String, productId: Long): Long {
        val documents = db.collection(Keys.CARTS_COLLECTION_KEY)
            .whereEqualTo(USER_ID, userId)
            .whereEqualTo(PRODUCT_ID, productId)
            .get()
            .await()
            .documents
        return if (documents.isNotEmpty()) documents[0].getLong(COUNT)!! else 0

    }

    override suspend fun addToCart(userId: String, productId: Long): Boolean {
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

    override suspend fun removeFromCart(userId: String, productId: Long): Boolean {
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

    override suspend fun isInCart(userId: String,productId: Long): Boolean {
        val documents = db.collection(Keys.CARTS_COLLECTION_KEY)
            .whereEqualTo(USER_ID, userId)
            .whereEqualTo(PRODUCT_ID, productId)
            .get()
            .await()
            .documents
        return documents.isNotEmpty()
    }
}