package com.factory.data.util

import kotlinx.coroutines.flow.StateFlow

interface NetworkInterface {
    val networkState: StateFlow<Boolean>
}