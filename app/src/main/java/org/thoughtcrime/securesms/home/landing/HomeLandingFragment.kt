package org.thoughtcrime.securesms.home.landing

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
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
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.Madhab
import com.batoulapps.adhan.Prayer
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.components.settings.DSLSettingsFragment
import org.thoughtcrime.securesms.databinding.FragmentNewSettingsBinding
import org.thoughtcrime.securesms.databinding.HomeLandingFragmentBinding
import org.thoughtcrime.securesms.home.api.NearbyMosqueApi
import org.thoughtcrime.securesms.home.listeners.MosqueResponseListener
import org.thoughtcrime.securesms.home.listeners.OnAllPrayerTimeButtonClicked
import org.thoughtcrime.securesms.home.views.NearbyMosqueModel
import org.thoughtcrime.securesms.home.views.NearbyMosqueView
import org.thoughtcrime.securesms.home.views.NextPrayerTimeModel
import org.thoughtcrime.securesms.home.views.NextPrayerTimeView
import org.thoughtcrime.securesms.home.views.PrayerModel
import org.thoughtcrime.securesms.home.views.PrayerView
import org.thoughtcrime.securesms.home.views.ReadingArticleModel
import org.thoughtcrime.securesms.home.views.ReadingArticleView
import org.thoughtcrime.securesms.home.views.RecentStoryView
import org.thoughtcrime.securesms.home.views.StoryModel
import org.thoughtcrime.securesms.home.views.UpcomingEventModel
import org.thoughtcrime.securesms.home.views.UpcomingEventsView
import org.thoughtcrime.securesms.prayers.constants.PrayersConstants
import org.thoughtcrime.securesms.prayers.landing.SharedPrefKey
import org.thoughtcrime.securesms.recipients.Recipient
import org.thoughtcrime.securesms.stories.tabs.ConversationListTabsViewModel
import org.thoughtcrime.securesms.util.navigation.safeNavigate
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class HomeLandingFragment : Fragment() {

    private lateinit var binding: HomeLandingFragmentBinding
    private lateinit var job: Job
    private var sharedPreferences: SharedPreferences? = null
    private var nextPrayerTimeView: NextPrayerTimeView? = null
    private val prayerSharedPrefName = "PrayersPreferences"
    private var settingsActivityResult: ActivityResultLauncher<Intent>? = null
    private var requestPermissionLauncher: ActivityResultLauncher<String>? = null
    private var isLocationUpdatesRunning: Boolean = false
    private lateinit var locationManager: LocationManager
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var monthNames = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    private lateinit var prayerTimes: PrayerTimes
    private val defaultCalculationMethod = "MUSLIM_WORLD_LEAGUE"
    private val defaultMadhabMethod = "HANAFI"

    private val viewModel: ConversationListTabsViewModel by viewModels(ownerProducer = { requireActivity() })

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            prepareNextPrayerTimeData(location)
            prepareNearbyMosqueData(location)
            isLocationUpdatesRunning = false
            locationManager.removeUpdates(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_landing_fragment, container, false)
        binding = HomeLandingFragmentBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = context?.getSharedPreferences(prayerSharedPrefName, Context.MODE_PRIVATE)

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

        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        job = GlobalScope.launch(Dispatchers.Main) {
            while (isActive) {
                if (::prayerTimes.isInitialized) {
                    val nextPrayer = prayerTimes.nextPrayer()
                    nextPrayerTimeView?.changeTexts(prayerTimes.timeForPrayer(nextPrayer), nextPrayer)
                }

                updateDateAndTime()

                delay(1000)
            }
        }

        requestLocationPermission()
        initViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
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
            getLastKnownLocation()
        } else {
            showLocationServicesDisabledPopUp()
        }
    }

    private fun getLastKnownLocation() {
        GlobalScope.launch(Dispatchers.Main) {
            val location = getLastLocation()
            location?.let {
                // Handle the obtained location
                prepareNextPrayerTimeData(it)
                prepareNearbyMosqueData(it)
            }
        }
    }

    private suspend fun getLastLocation(): Location? {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val lastKnownLocationGPS =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                val lastKnownLocationNetwork =
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                // Choose the best available location
                if (lastKnownLocationGPS != null && lastKnownLocationNetwork != null) {
                    if (lastKnownLocationGPS.time > lastKnownLocationNetwork.time) {
                        lastKnownLocationGPS
                    } else {
                        lastKnownLocationNetwork
                    }
                } else {
                    lastKnownLocationGPS ?: lastKnownLocationNetwork
                }
            } catch (e: SecurityException) {
                null
            }
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

    private fun prepareNextPrayerTimeData(location: Location) {
        val coordinates = Coordinates(location.latitude, location.longitude)
        val savedCalculationMethod = sharedPreferences?.getString(SharedPrefKey.CALCULATION_METHOD_KEY.value, defaultCalculationMethod) ?: defaultCalculationMethod
        val calculationMethod = CalculationMethod.valueOf(savedCalculationMethod)
        val savedMadhabMethod = sharedPreferences?.getString(SharedPrefKey.MADHAB_METHOD_KEY.value, defaultMadhabMethod) ?: defaultMadhabMethod
        val madhabMethod = Madhab.valueOf(savedMadhabMethod)
        val params = calculationMethod.parameters
        params.madhab = madhabMethod

        val now = Date()
        prayerTimes = PrayerTimes(coordinates, DateComponents.from(now), params)
        if (prayerTimes.nextPrayer() == Prayer.NONE) {
            val calendar = Calendar.getInstance()
            calendar.time = now
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            prayerTimes = PrayerTimes(coordinates, DateComponents.from(calendar.time), params)
        }

        val nextPrayer = prayerTimes.nextPrayer()
        val timeForNextPrayer = prayerTimes.timeForPrayer(nextPrayer)
        val notificationType = sharedPreferences?.getString("${SharedPrefKey.ALARM_SET_AT.value}${timeToNotificationId(timeForNextPrayer.time)}", PrayersConstants.NotificationTypes.MUTED) ?: PrayersConstants.NotificationTypes.MUTED
        nextPrayerTimeView = NextPrayerTimeView(requireContext(), NextPrayerTimeModel(notificationType, timeForNextPrayer, nextPrayer), object : OnAllPrayerTimeButtonClicked {
            override fun onButtonClick() {
                viewModel.onPrayersSelected()
            }
        })
        binding.containerLayout.addView(nextPrayerTimeView)

        binding.progressBar.visibility = View.GONE
        binding.homePageContainer.visibility = View.VISIBLE
    }

    private fun prepareNearbyMosqueData(location: Location) {
        NearbyMosqueApi.initialize()
        NearbyMosqueApi.getNearbyMosque(location.latitude.toFloat(), location.longitude.toFloat(), object : MosqueResponseListener {
            override fun onSuccess(responseData: String?) {
                responseData?.let { response ->
                    val jsonResponse = JSONObject(response)
                    val mosquesArray = jsonResponse.getJSONArray("mosques")
                    val mosqueObject = mosquesArray.getJSONObject(0)
                    val addressString = mosqueObject.getString("address")

                    val geocoder = Geocoder(requireContext())
                    val addresses: MutableList<Address>? = geocoder.getFromLocationName(addressString, 1)

                    addresses?.let {
                        if (it.isNotEmpty()) {
                            val address = it[0]
                            val mosqueLatitude = address.latitude
                            val mosqueLongitude = address.longitude

                            val model = NearbyMosqueModel(
                                mosqueObject.getString("name"),
                                mosqueLatitude,
                                mosqueLongitude
                            )

                            requireActivity().runOnUiThread {
                                val nearbyMosqueView = NearbyMosqueView(requireContext(), model, location)
                                binding.containerLayout.addView(nearbyMosqueView)
                            }
                        }
                    }


                }
            }

            override fun onFailure(errorMessage: String?) {}
        })

    }

    private fun initViews() {
        updateDateAndTime()

        binding.apply {

        }
    }

    private fun timeToNotificationId(value: Long) : Int {
        return ((value.div(1000L)).toInt() % Integer.MAX_VALUE)
    }

    private fun updateDateAndTime() {
        binding.apply {
            homeLandingFragmentDateInfoLayoutDate.text = getDateText()
            homeLandingFragmentDateInfoLayoutTime.text = getTimeText()
        }
    }

    private fun getDateText(): String{
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        return "${calendar.get(Calendar.DAY_OF_MONTH)} ${monthNames[calendar.get(Calendar.MONTH)]} ${calendar.get(Calendar.YEAR)}; ${getTimeText()}"
    }

    private fun getTimeText(): String {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val minute = calendar.get(Calendar.MINUTE)
        return when (minute > 9) {
            true -> "${calendar.get(Calendar.HOUR_OF_DAY)}:$minute"
            else -> "${calendar.get(Calendar.HOUR_OF_DAY)}:0$minute"
        }
    }
}