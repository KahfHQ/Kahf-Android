package org.thoughtcrime.securesms.prayers.landing

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.CalculationParameters
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.Madhab
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.databinding.PrayersLandingFragmentBinding
import org.thoughtcrime.securesms.prayers.constants.PrayersConstants
import org.thoughtcrime.securesms.prayers.views.PrayerModel
import org.thoughtcrime.securesms.prayers.views.PrayerModelView
import java.util.Calendar
import java.util.Date
import java.util.Locale


class PrayersLandingFragment : Fragment() {

    private lateinit var binding: PrayersLandingFragmentBinding
    private var prayerModelViews: List<PrayerModelView>? = null
        set(value) {
            value?.let {
                field = value
                initViews()
            }
        }

    private var dateIndex = 1
    private lateinit var prayerTimes: PrayerTimes
    private lateinit var params: CalculationParameters
    private lateinit var coordinates: Coordinates
    private var monthNames = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    private var fullAddress: String? = null
    private var cityName: String? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null

    private var settingsActivityResult: ActivityResultLauncher<Intent>? = null
    private var requestPermissionLauncher: ActivityResultLauncher<String>? = null

    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    private var isLocationUpdatesRunning: Boolean = false

    private lateinit var locationManager: LocationManager

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            prepareLocationInformation(location)
            isLocationUpdatesRunning = false
            locationManager.removeUpdates(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.prayers_landing_fragment, container, false)
        binding = PrayersLandingFragmentBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingsActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            requestLocationPermission()
        }

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            when (it) {
                true -> {
                    checkIfLocationServicesEnabled()
                }
                else -> {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        showAppLocationPermissionNotGranted()
                    } else {
                        requestLocationPermission()
                    }
                }
            }
        }

        locationManager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        requestLocationPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(locationListener)
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            checkIfLocationServicesEnabled()
        } else {
            requestPermissionLauncher?.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun checkIfLocationServicesEnabled() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
                prepareLocationInformation(it)
            } ?: run {
                binding.progressBar.visibility = View.VISIBLE
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
            }
        } else {
            showLocationServicesDisabledPopUp()
        }
    }

    private fun showLocationServicesDisabledPopUp() {
        MaterialAlertDialogBuilder(requireContext()).setTitle("Alert")
            .setMessage(getString(R.string.PrayersFragment__location_could_not_get))
            .setPositiveButton(
                R.string.ok
            ) { d: DialogInterface, w: Int ->
                val packageManager = context?.packageManager
                if (packageManager != null) {
                    val locationSettingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    if (locationSettingsIntent.resolveActivity(packageManager) != null) {
                        settingsActivityResult?.launch(locationSettingsIntent)
                    }
                }
            }
            .show()
    }

    private fun showAppLocationPermissionNotGranted() {
        MaterialAlertDialogBuilder(requireContext()).setTitle("Alert")
            .setMessage(getString(R.string.PrayersFragment__location_could_not_get_app_level))
            .setPositiveButton(
                R.string.ok
            ) { d: DialogInterface, w: Int ->
                val packageManager = context?.packageManager
                if (packageManager != null) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:${requireContext().packageName}")
                    if (intent.resolveActivity(packageManager) != null) {
                        settingsActivityResult?.launch(intent)
                    }
                }
            }
            .show()
    }

    private fun prepareData() {
        val prayers = mutableListOf<PrayerModelView>()

        getDataFromApi(Date())

        val calendar = Calendar.getInstance()
        calendar.time = prayerTimes.fajr
        prayers.add(PrayerModelView(requireContext(), PrayerModel(PrayersConstants.PrayerTime.FAJR, "Fajr", getTimeString(calendar), PrayersConstants.NotificationTypes.MUTED), this@PrayersLandingFragment))
        calendar.time = prayerTimes.sunrise
        prayers.add(PrayerModelView(requireContext(), PrayerModel(PrayersConstants.PrayerTime.SUNRISE, "Sunrise", getTimeString(calendar), PrayersConstants.NotificationTypes.VOICE_ENABLED), this@PrayersLandingFragment))
        calendar.time = prayerTimes.dhuhr
        prayers.add(PrayerModelView(requireContext(), PrayerModel(PrayersConstants.PrayerTime.DHUHR, "Dhuhr", getTimeString(calendar), PrayersConstants.NotificationTypes.UNMUTED), this@PrayersLandingFragment))
        calendar.time = prayerTimes.asr
        prayers.add(PrayerModelView(requireContext(), PrayerModel(PrayersConstants.PrayerTime.ASR, "Asr", getTimeString(calendar), PrayersConstants.NotificationTypes.MUTED), this@PrayersLandingFragment))
        calendar.time = prayerTimes.maghrib
        prayers.add(PrayerModelView(requireContext(), PrayerModel(PrayersConstants.PrayerTime.MAGRIB, "Magrib", getTimeString(calendar), PrayersConstants.NotificationTypes.UNMUTED), this@PrayersLandingFragment))
        calendar.time = prayerTimes.isha
        prayers.add(PrayerModelView(requireContext(), PrayerModel(PrayersConstants.PrayerTime.ISHA, "Isha", getTimeString(calendar), PrayersConstants.NotificationTypes.UNMUTED), this@PrayersLandingFragment))

        prayerModelViews = prayers.toList()
    }

    private fun getDataFromApi(date: Date) {
        params = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters
        params.madhab = Madhab.HANAFI
        params.adjustments.fajr = 2

        val dateComponents = DateComponents.from(date)
        prayerTimes = PrayerTimes(coordinates, dateComponents, params)
    }

    private fun initViews() {
        binding.apply {
            prayerDayLabel.text = "Today"
            arrangeDateInformation(Date())
            addressInfo.text = fullAddress
            dateInfoContainer.visibility = View.VISIBLE

            prayerModelViews?.forEach {
                prayersLinearLayout.addView(it)
            }

            goYesterdayBtn.setOnClickListener {
                if (dateIndex > 0) {
                    dateIndex--
                    when (dateIndex) {
                        1 -> {
                            prayerDayLabel.text = "Today"
                            arrangeDateInformation(Date())
                            getDataFromApi(Date())
                            changeModelTexts()
                        }
                        else -> {
                            prayerDayLabel.text = "Yesterday"
                            arrangeDateInformation(getNDaysBeforeOrAfter(-1))
                            getDataFromApi(getNDaysBeforeOrAfter(-1))
                            changeModelTexts()
                        }
                    }

                    prayersLinearLayout.children.forEach {
                        val view = it as PrayerModelView?
                        if (dateIndex == 0)
                            view?.changeToYesterdayModel()
                    }
                }
            }

            goTomorrowBtn.setOnClickListener {
                if (dateIndex < 2) {
                    dateIndex++
                    when (dateIndex) {
                        1 -> {
                            prayerDayLabel.text = "Today"
                            arrangeDateInformation(Date())
                            getDataFromApi(Date())
                            changeModelTexts()
                        }
                        else -> {
                            prayerDayLabel.text = "Tomorrow"
                            arrangeDateInformation(getNDaysBeforeOrAfter(1))
                            getDataFromApi(getNDaysBeforeOrAfter(1))
                            changeModelTexts()
                        }
                    }
                    prayersLinearLayout.children.forEach {
                        val view = it as PrayerModelView?
                        if (dateIndex > 0)
                            view?.changeToNormalModel()
                    }
                }
            }
        }
    }

    private fun arrangeDateInformation(date: Date) {
        binding.apply {
            val calendar = Calendar.getInstance()
            calendar.time = date
            val prayerDateText = "$cityName | ${calendar.get(Calendar.DAY_OF_MONTH)} ${monthNames[calendar.get(Calendar.MONTH)]} ${calendar.get(Calendar.YEAR)}"
            prayerDate.text = prayerDateText
        }
    }

    private fun changeModelTexts() {
        val calendar = Calendar.getInstance()
        prayerModelViews?.forEach {
            when (it.prayerModel.type) {
                PrayersConstants.PrayerTime.FAJR -> calendar.time = prayerTimes.fajr
                PrayersConstants.PrayerTime.SUNRISE -> calendar.time = prayerTimes.sunrise
                PrayersConstants.PrayerTime.DHUHR -> calendar.time = prayerTimes.dhuhr
                PrayersConstants.PrayerTime.ASR -> calendar.time = prayerTimes.asr
                PrayersConstants.PrayerTime.MAGRIB -> calendar.time = prayerTimes.maghrib
                PrayersConstants.PrayerTime.ISHA -> calendar.time = prayerTimes.isha
            }

            it.changeTimeString(getTimeString(calendar))
        }
    }

    private fun prepareLocationInformation(location: Location) {
        try {
            binding.progressBar.visibility = View.GONE
            coordinates = Coordinates(location.latitude, location.longitude)

            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)

            if (addresses.isNotEmpty()) {
                val address: Address = addresses[0]

                cityName = address.adminArea
                val sb = StringBuilder()
                sb.apply {
                    address.subLocality?.let {
                        append(it)
                        append(", ")
                    }
                    address.thoroughfare?.let {
                        append(it)
                        append(", ")
                    }
                    address.postalCode?.let {
                        append(it)
                    }
                }
                this@PrayersLandingFragment.fullAddress = sb.toString()
            }

            prepareData()
        } catch (error: Exception) {
            prepareData()
            println("geocoder error " + error.message)
        }
    }

    private fun getTimeString(calendar: Calendar): String {
        val calendarHour = calendar.get(Calendar.HOUR_OF_DAY)
        val calendarMinute = calendar.get(Calendar.MINUTE)
        val ampm = calendar.get(Calendar.AM_PM)

        val hour = when(calendarHour < 10) {
            true -> "0${calendarHour}"
            else -> "$calendarHour"
        }

        val minute = when (calendarMinute < 10) {
            true -> "0${calendarMinute}"
            else -> "$calendarMinute"
        }

        val text = when (ampm) {
            0 -> "AM"
            else -> "PM"
        }

        return "$hour:$minute $text"
    }

    /**
     * @param n send negative to go back, send positive to go ahead
     * */
    private fun getNDaysBeforeOrAfter(n: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, n)
        return calendar.time
    }
}