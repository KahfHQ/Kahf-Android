package org.thoughtcrime.securesms.prayers.landing

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.CalculationParameters
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.Madhab
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import com.google.android.gms.location.LocationServices
import io.nlopez.smartlocation.SmartLocation
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.databinding.PrayersLandingFragmentBinding
import org.thoughtcrime.securesms.prayers.constants.PrayersConstants
import org.thoughtcrime.securesms.prayers.views.PrayerModel
import org.thoughtcrime.securesms.prayers.views.PrayerModelView
import java.io.IOException
import java.lang.Exception
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
    private val REQUEST_CODE = 1
    private lateinit var prayerTimes: PrayerTimes
    private lateinit var params: CalculationParameters
    private lateinit var coordinates: Coordinates
    private var monthNames = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    private lateinit var fullAddress: String
    private lateinit var cityName: String

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
        requestLocationPermission()
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted
            // Proceed with location retrieval
            getUsersLocation()
        } else {
            // Request permission
            val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                when (it) {
                    true -> getUsersLocation()
                    else -> {}
                }
            }
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
        }
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

    private fun getUsersLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                try {
                    coordinates = Coordinates(it.latitude, it.longitude)

                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val addresses: List<Address> = geocoder.getFromLocation(it.latitude, it.longitude, 1)

                    if (addresses.isNotEmpty()) {
                        val address: Address = addresses[0]

                        cityName = address.adminArea
                        this@PrayersLandingFragment.fullAddress = "${address.subLocality}, ${address.thoroughfare}, ${address.postalCode}"
                    }

                    prepareData()
                } catch (error: Exception) {
                    println("geocoder error " + error.message)
                }
            }
        }
//        SmartLocation.with(context).location()
//                .oneFix()
//                .start{location ->
//                    location?.let {
////                        coordinates = Coordinates(38.470982, 27.087806)
//                        coordinates = Coordinates(it.latitude, it.longitude)
//                        prepareData()
//                    }
//                }
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