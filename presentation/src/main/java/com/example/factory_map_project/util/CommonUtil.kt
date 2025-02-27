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
import java.io.Serializable

const val ARG_CONTENT = "argument.content"

const val MARKER_UNKNOWN = 0
const val MARKER_HOTEL = 1
const val MARKER_RESORT = 2

const val CHECK_ALPHA = 0.95f
const val NONE_CHECK_ALPHA = 0.5f


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
            isDelete = isDelete
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
}