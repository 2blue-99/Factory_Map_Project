package com.example.factory_map_project.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import com.example.domain.model.FactoryInfo
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

object Util {

    fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
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
            lotArea = lotArea,
            employeeCount = employeeCount,
            scaleDivisionName = scaleDivisionName,
            registrationDate = registrationDate,
            productInfo = productInfo,
            contact = contact,
            loadAddress = loadAddress,
            latitude = latitude,
            longitude = longitude,
            isClick = isClick,
            memo = memo,
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
}