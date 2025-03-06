package com.example.data.remote.datasource

import com.example.data.remote.model.FactoryResponse
import com.example.domain.model.FactoryInfo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject


class FirebaseDataSourceImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
): FirebaseDataSource {
    override fun getAllData(): Flow<List<FactoryResponse>> {
        return flow {
            Timber.d("getAllData")
            fireStore.collection("factory")
//                .whereNotEqualTo("user_id", "123")
                .get()
                .addOnSuccessListener { result ->
                    Timber.d("result : $result")
                    val gap = result.map {
                        it.toObject(FactoryResponse::class.java)
                    }
                    Timber.d("gap : $gap")
                }
                .await()
                .mapNotNull  { result ->
                    Timber.d("result : $result")
                    emit(emptyList())
                }
        }
    }

    override fun insertData(): Boolean {
        Timber.d("insertData")
        fireStore.collection("factory")
            .add(FactoryResponse(1,"앙", FactoryInfo.empty() ,"오늘인데요"))
            .addOnSuccessListener {
                Timber.d("success")
            }
            .addOnFailureListener {
                Timber.d("fail")
            }

        return true
    }
}