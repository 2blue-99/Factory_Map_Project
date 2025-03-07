package com.example.data.remote.datasource

import com.example.data.remote.model.FactoryInfoResponse
import com.example.data.remote.model.FactoryResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject


class FireStoreDataSourceImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
): FireStoreDataSource {
    override suspend fun getAllData(): List<Pair<String,FactoryResponse>> {
        val result = fireStore.collection("factory")
            .whereNotEqualTo("user_code", "1234")
            .get()
            .await()
        return result.map { Pair(it.id, it.toObject(FactoryResponse::class.java)) }
    }

    override suspend fun insertData(dataList: List<FactoryResponse>): Boolean {
        Timber.d("insertData")
        return try {
            val batch = fireStore.batch()

            dataList.forEach { data ->
                val docRef = fireStore.collection("factory").document()
                batch.set(docRef, data)
            }

            batch.commit().await()
            true
        }catch (e: Exception){
            false
        }
    }
}