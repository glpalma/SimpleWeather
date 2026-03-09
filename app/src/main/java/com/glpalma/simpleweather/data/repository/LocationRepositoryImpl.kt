package com.glpalma.simpleweather.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.glpalma.simpleweather.domain.model.CityInfo
import com.glpalma.simpleweather.domain.repository.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.CancellationException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationRepositoryImpl @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val context: Context
) : LocationRepository {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Result<CityInfo> = runCatching {
        withContext(Dispatchers.IO) {
            val location = withTimeout(LOCATION_TIMEOUT_MS) { fetchLocation() }
            reverseGeocode(location.latitude, location.longitude)
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun fetchLocation(): android.location.Location =
        suspendCancellableCoroutine { cont ->
            val cts = CancellationTokenSource()
            cont.invokeOnCancellation { cts.cancel() }

            locationClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                cts.token
            ).addOnSuccessListener { location ->
                if (location != null) {
                    cont.resume(location)
                } else {
                    cont.resumeWithException(
                        IllegalStateException("Location returned null")
                    )
                }
            }.addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }.addOnCanceledListener {
                cont.resumeWithException(CancellationException("Location request cancelled"))
            }
        }

    private fun reverseGeocode(latitude: Double, longitude: Double): CityInfo {
        val geocoder = Geocoder(context, Locale.getDefault())
        @Suppress("DEPRECATION")
        val addresses = try {
            geocoder.getFromLocation(latitude, longitude, 1)
        } catch (_: Exception) {
            null
        }
        val address = addresses?.firstOrNull()

        return CityInfo(
            name = address?.locality ?: address?.subAdminArea ?: "Current location",
            id = CURRENT_LOCATION_ID,
            latitude = latitude,
            longitude = longitude,
            timezone = TimeZone.getDefault().id,
            country = address?.countryName ?: "",
            stateOrProvince = address?.adminArea ?: ""
        )
    }

    companion object {
        const val CURRENT_LOCATION_ID = "current_location"
        private const val LOCATION_TIMEOUT_MS = 15_000L
    }
}
