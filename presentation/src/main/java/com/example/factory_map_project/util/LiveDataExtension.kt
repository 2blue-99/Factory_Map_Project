package com.example.factory_map_project.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

open class VerifiedLiveData<T: Any>(value: T): LiveData<T>(value) {
    override fun getValue(): T {
        return super.getValue() as T
    }

    inline fun observe(owner: LifecycleOwner, crossinline observer: (t: T) -> Unit) {
        this.observe(owner, Observer {
            it?.let(observer)
        })
    }
}

class VerifiedMutableLiveData<T: Any>(value: T): VerifiedLiveData<T>(value) {
    public override fun setValue(value: T) {
        super.setValue(value)
    }

    public override fun postValue(value: T) {
        super.postValue(value)
    }
}