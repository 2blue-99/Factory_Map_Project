package com.factory.data.remote.datasource

import com.factory.data.remote.model.FactoryResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject


class FireStoreDataSourceImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
): FireStoreDataSource {

    override suspend fun login(id: String, passWord: String): String {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(id, passWord)
                .await()
            Timber.d("result : ${result.user?.uid}")
            result.user?.uid ?: ""
        }catch (e: Exception){
            Timber.e("login err : $e")
            ""
        }
    }

    override suspend fun getAllData(userCode: String): List<Pair<String,FactoryResponse>> {
        Timber.d("userCode : $userCode")
        val result = fireStore.collection("factory")
            .whereNotEqualTo("userCode", userCode)
            .get()
            .await()
        return result.map { Pair(it.id, it.toObject(FactoryResponse::class.java)) }
    }

    override suspend fun insertData(dataList: List<FactoryResponse>): Boolean {
        Timber.d("insertData")
        return try {
            val batch = fireStore.batch()

            dataList.forEach { data ->
                Timber.d("data : $data")
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