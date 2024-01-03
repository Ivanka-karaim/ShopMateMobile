package com.example.shopmatemobile.service

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.shopmatemobile.addResources.RetrofitClient
import com.example.shopmatemobile.addResources.SharedPreferencesFactory
import com.example.shopmatemobile.api.CouponsApi
import com.example.shopmatemobile.model.CouponStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CouponsService {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getCouponsByStatus(context: Context, status:String):List<CouponStatus>?{
        val couponsApi = RetrofitClient.getInstance().create(CouponsApi::class.java)
        return withContext(Dispatchers.IO) {
            val dictStatus = mapOf(
                "Активні" to 0, "Використані" to 1, "Прострочені" to 2,
            )
            val dictStatusName = mapOf(
                "Активні" to "Активний", "Використані" to "Використаний", "Прострочені" to "Прострочений",
            )

            if ( dictStatus[status] == null){
                return@withContext null
            }
            val statusId = dictStatus[status]
            val coupons = statusId?.let { couponsApi.getCouponsByStatus("Bearer "+ SharedPreferencesFactory(context).getToken()!!, it ) }
            if (coupons!=null) {
                val couponsStatus = mutableListOf<CouponStatus>()
                for (coupon in coupons){
                    coupon.dateExpiration = DateService.parseDate(coupon.dateExpiration).toString()
                    if (dictStatusName[status] != null) {
                        couponsStatus.add(CouponStatus(coupon,dictStatusName[status].toString()))
                    }else{
                        couponsStatus.add(CouponStatus(coupon,""))
                    }
                }
                return@withContext couponsStatus
            }else{
                return@withContext null
            }

        }


    }

}