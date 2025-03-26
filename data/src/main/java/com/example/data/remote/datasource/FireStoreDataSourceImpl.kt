package com.example.data.remote.datasource

import com.example.data.remote.model.FactoryResponse
import com.example.data.remote.model.UserResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject


class FireStoreDataSourceImpl @Inject constructor(
    private val fireStore: FirebaseFirestore
): FireStoreDataSource {

    override suspend fun login(id: String, passWord: String): String {
        Timber.d("id : $id / passWord : $passWord")
        val result = fireStore.collection("user")
            .whereEqualTo("id", id)
            .whereEqualTo("pass_word", passWord)
            .get()
            .await()

        if(!result.isEmpty){
            val accessCode = result.map { it.toObject(UserResponse::class.java) }.first()
            Timber.d("accessCode : $accessCode")
            return accessCode.user_code
        }else{
            Timber.d("no id")
            return ""
        }
    }

    override suspend fun getAllData(userCode: String): List<Pair<String,FactoryResponse>> {
        Timber.d("userCode : $userCode")
        val result = fireStore.collection("factory")
            .whereNotEqualTo("userCode", userCode)
            .get()
            .await()
        Timber.d("result : $result")
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