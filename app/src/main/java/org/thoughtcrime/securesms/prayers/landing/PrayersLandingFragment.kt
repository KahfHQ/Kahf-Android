package org.thoughtcrime.securesms.prayers.landing

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import io.nlopez.smartlocation.SmartLocation
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.databinding.PrayersLandingFragmentBinding
import org.thoughtcrime.securesms.prayers.constants.PrayersConstants
import org.thoughtcrime.securesms.prayers.views.PrayerModel
import org.thoughtcrime.securesms.prayers.views.PrayerModelView
import java.util.Calendar
import java.util.Date


class PrayersLandingFragment : Fragment() {

    private lateinit var binding: PrayersLandingFragmentBinding
    private var prayerModels: List<PrayerModel>? = null
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
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
        }
    }

    private fun prepareData() {
        val prayers = mutableListOf<PrayerModel>()

        getDataFromApi(Date())

        val calendar = Calendar.getInstance()
        calendar.time = prayerTimes.fajr
        prayers.add(PrayerModel("Fajr", getTimeString(calendar), PrayersConstants.NotificationTypes.MUTED))
        calendar.time = prayerTimes.sunrise
        prayers.add(PrayerModel("Sunrise", getTimeString(calendar), PrayersConstants.NotificationTypes.VOICE_ENABLED))
        calendar.time = prayerTimes.dhuhr
        prayers.add(PrayerModel("Dhuhr", getTimeString(calendar), PrayersConstants.NotificationTypes.UNMUTED))
        calendar.time = prayerTimes.asr
        prayers.add(PrayerModel("Asr", getTimeString(calendar), PrayersConstants.NotificationTypes.MUTED))
        calendar.time = prayerTimes.maghrib
        prayers.add(PrayerModel("Magrib", getTimeString(calendar), PrayersConstants.NotificationTypes.UNMUTED))
        calendar.time = prayerTimes.isha
        prayers.add(PrayerModel("Isha", getTimeString(calendar), PrayersConstants.NotificationTypes.UNMUTED))

        prayerModels = prayers.toList()
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
            prayerModels?.forEach {
                val prayerModelView = PrayerModelView(requireContext(), it, this@PrayersLandingFragment)
                prayersLinearLayout.addView(prayerModelView)
            }

            goYesterdayBtn.setOnClickListener {
                if (dateIndex > 0) {
                    dateIndex--
                    when (dateIndex) {
                        1 -> {
                            prayerDayLabel.text = "Today"
                            getDataFromApi(Date())
                        }
                        else -> {
                            prayerDayLabel.text = "Yesterday"
                            getDataFromApi(getNDaysBeforeOrAfter(-1))
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
                            getDataFromApi(Date())
                        }
                        else -> {
                            prayerDayLabel.text = "Tomorrow"
                            getDataFromApi(getNDaysBeforeOrAfter(1))
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

    private fun getUsersLocation() {
//        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
//        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//            if (location != null) {
//                coordinates = Coordinates(location.latitude, location.longitude)
//                prepareData()
//            }
//        }
        SmartLocation.with(context).location()
                .oneFix()
                .start{location ->
                    location?.let {
//                        coordinates = Coordinates(38.470982, 27.087806)
                        coordinates = Coordinates(it.latitude, it.longitude)
                        prepareData()
                    }
                }
    }

    private fun getTimeString(calendar: Calendar): String {
        var hour = "0"
        var minute = "0"
        var text = "AM"
        val calendarHour = calendar.get(Calendar.HOUR_OF_DAY)
        val calendarMinute = calendar.get(Calendar.MINUTE)


        if (calendarHour < 12) {
            text = "AM"
            if (calendarHour < 10) {
                hour = "0${calendarHour}"
            } else {
                hour = "$calendarHour"
            }
        } else {
            text = "PM"
            hour = "$calendarHour"
        }

        minute = if (calendarMinute < 10) {
            "0${calendarMinute}"
        } else {
            "$calendarMinute"
        }

        return "$hour:$minute $text"
    }

    /**
     * @param n send negative to go back, send positive to go ahead **/
    private fun getNDaysBeforeOrAfter(n: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, n)
        return calendar.time
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with location retrieval
                getUsersLocation()
            } else {
                // Permission denied, handle accordingly (e.g., show a message)
            }
        }
    }
}