package org.thoughtcrime.securesms.home.views

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import org.json.JSONObject
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.databinding.NearbyMosqueViewBinding
import org.thoughtcrime.securesms.home.api.NearbyMosqueApi
import org.thoughtcrime.securesms.home.landing.HomeLandingFragment
import org.thoughtcrime.securesms.home.listeners.MosqueResponseListener
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class NearbyMosqueView(context: Context,
                       private val model: NearbyMosqueModel,
                       private val myLocation: Location
) : ConstraintLayout(context) {

    private val binding: NearbyMosqueViewBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.nearby_mosque_view, this)
        binding = NearbyMosqueViewBinding.bind(this)
        setData()
    }

    private fun setData() {
        binding.apply {
            name.text = model.name
            val distanceValue = calculateDistanceInKm(myLocation.latitude, myLocation.longitude, model.latitude ?: 0.0, model.longitude ?: 0.0)
            val distanceText = "${String.format("%.2f", distanceValue)} KM from your location"
            distance.text = distanceText
            location.setOnClickListener {
                val uri = "http://maps.google.com/maps?daddr=${model.latitude},${model.longitude}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                intent.setPackage("com.google.android.apps.maps")
                context.startActivity(intent)
            }
        }
    }

    private fun calculateDistanceInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371 // Earth radius in kilometers

        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a = sin(latDistance / 2) * sin(latDistance / 2) +
                (cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                        sin(lonDistance / 2) * sin(lonDistance / 2))
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return R * c // Distance in kilometers
    }
}

data class NearbyMosqueModel(
    val name: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)