package com.example.factory_map_project.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import com.example.domain.model.FactoryInfo
import com.example.domain.model.FilterData
import com.example.factory_map_project.BuildConfig
import com.example.factory_map_project.R
import com.example.factory_map_project.util.map.FactoryCluster
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

const val ARG_CONTENT = "argument.content"
const val ARG_SECOND_CONTENT = "argument.second.content"

const val MARKER_UNKNOWN = 0
const val MARKER_HOTEL = 1
const val MARKER_RESORT = 2

const val CHECK_ALPHA = 0.95f
const val NONE_CHECK_ALPHA = 0.6f

const val SELECT_NONE = 0
const val SELECT_SERVER = 1
const val SELECT_LOCAL = 2


object CommonUtil {

    fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    /**
     * viewLifecycleOwner 를 안붙이면,
     * 다른 곳으로 이동했다가 복귀 시 옵저버가 중첩됨
     */
    fun Fragment.repeatOnFragmentStarted(block: suspend CoroutineScope.() -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    fun GoogleMap.setMarker(latitude: Double, longitude: Double, name: String = ""): Marker? {
        val location = LatLng(latitude, longitude)
        return this.addMarker(MarkerOptions().position(location).title(name))
    }

    fun FactoryInfo.toCluster(): FactoryCluster =
        FactoryCluster(
            id = id,
            companyName = companyName,
            companyScale = companyScale,
            lotArea = lotArea,
            contact = contact,
            employeeCount = employeeCount,
            businessType = businessType,
            product = product,
            loadAddress = loadAddress,
            latitude = latitude,
            longitude = longitude,
            isCheck = isCheck,
            memo = memo,
            category = category,
            isDeleted = isDeleted,
            lastTime = lastTime
        )

    fun moveCall(context: Context, number: String){
        val uri = Uri.parse("tel://${number}")
        context.startActivity(Intent(Intent.ACTION_VIEW,uri))
    }

    fun moveTMap(context: Context, data: FactoryCluster) {
        val packageName = "com.skt.tmap.ku"
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tmap://route?goalx=${data.longitude}&goaly=${data.latitude}&goalname=${data.companyName}"))
            context.startActivity(intent)
        }catch (e: Exception){
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${packageName}")
                )
            )
        }
    }

    /**
     * Fragment Arguments 추출
     */
    inline fun <reified T : Serializable> Bundle.getData(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.getSerializable(key, T::class.java)
        } else {
            val result = this.getSerializable(key)
            if (result is T) result else null
        }
    }

    /**
     * 앱 기본 애니메이션
     *
     * 오른쪽 슬라이드 인, 오른쪽 슬라이드 아웃
     * 딥링크 접근 시, 화면 중복 방지를 위한 SingleTop 옵션 설정
     */
    fun slideRightBaseNavOptions(): NavOptions {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_from_right)
            .setExitAnim(R.anim.hold)
            .setPopExitAnim(R.anim.slide_out_to_right)
            .build()
    }

    fun moveSettingIntent(): Intent {
        return Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + BuildConfig.APPLICATION_ID)
        )
    }

    fun isListEmpty(list: List<*>?):Boolean {
        return list.isNullOrEmpty()
    }

    fun List<FilterData>.isContain(target: String, keyword: String): Boolean{
        return this.any { it.target == target && it.keyword == keyword }
    }

    /**
     * Geocode를 통해 나온 도로명 주소의 "대한민국" 제거
     */
    fun String.toNoCountry(): String {
        return this.removePrefix("대한민국 ")
    }

    fun currentTime(): String{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            current.format(formatter)
        }else{
            val currentTimeMillis = System.currentTimeMillis()
            val calendar = Calendar.getInstance().apply {
                timeInMillis = currentTimeMillis
            }
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1 // 월은 0부터 시작하므로 +1
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val hour = calendar.get(Calendar.HOUR_OF_DAY) // 24시간제
            val minute = calendar.get(Calendar.MINUTE)
            val second = calendar.get(Calendar.SECOND)
            "$year-$month-$day $hour:$minute:$second"
        }
    }

    /**
     * 2 : Gone
     */
    fun isEmpty(text: String?): Boolean{
        Timber.d("text : ${text.isNullOrBlank()}")
        return text.isNullOrBlank()
    }

    fun Float.toDoubleRange(): Double{
        return when {
            this >= 15f -> 0.01
            this >= 14f -> 0.02
            this >= 13f -> 0.03
            this >= 12f -> 0.04
            this >= 11f -> 0.1
            this >= 10f -> 0.2
            this >= 9f -> 0.4
            this >= 8f -> 0.5
            else -> 0.6
        }
    }
}