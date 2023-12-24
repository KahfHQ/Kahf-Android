package org.thoughtcrime.securesms.prayers.landing

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Rect
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telecom.Call
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.CalculationParameters
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
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.components.AvatarImageView
import org.thoughtcrime.securesms.conversationlist.ConversationListFragment.Callback
import org.thoughtcrime.securesms.databinding.PrayersLandingFragmentBinding
import org.thoughtcrime.securesms.prayers.adapters.CalculationMethodRecyclerAdapter
import org.thoughtcrime.securesms.prayers.adapters.MadhabAsrTimeRecyclerAdapter
import org.thoughtcrime.securesms.prayers.constants.PrayersConstants
import org.thoughtcrime.securesms.prayers.listeners.OnCalculationMethodClickedListener
import org.thoughtcrime.securesms.prayers.listeners.OnMadhabMethodClickedListener
import org.thoughtcrime.securesms.prayers.utils.NotificationUtils
import org.thoughtcrime.securesms.prayers.views.PrayerModel
import org.thoughtcrime.securesms.prayers.views.PrayerModelView
import org.thoughtcrime.securesms.recipients.Recipient
import org.thoughtcrime.securesms.util.visible
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.ceil


class PrayersLandingFragment : Fragment() {

    private lateinit var binding: PrayersLandingFragmentBinding
    private var prayerModelViews: List<PrayerModelView>? = null
        set(value) {
            value?.let {
                field = value
                preparePrayerTimesView()
            }
        }

    var dateIndex = 1
    private lateinit var todayPrayerTimes: PrayerTimes
    private lateinit var tomorrowPrayerTimes: PrayerTimes
    private lateinit var yesterdayPrayerTimes: PrayerTimes
    private lateinit var params: CalculationParameters
    private lateinit var coordinates: Coordinates
    private var monthNames = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    private var calculationTitles = mutableMapOf<CalculationMethod, String>()
    private var madhabTitles = mutableMapOf<Madhab, String>()
    private var fullAddress: String? = null
    private var cityName: String? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null

    private var settingsActivityResult: ActivityResultLauncher<Intent>? = null
    private var requestPermissionLauncher: ActivityResultLauncher<String>? = null
    private var isLocationUpdatesRunning: Boolean = false

    private lateinit var locationManager: LocationManager
    private lateinit var job: Job

    private var sharedPreferences: SharedPreferences? = null
    private val sharedPrefName = "PrayersPreferences"
    private val defaultCalculationMethod = "MUSLIM_WORLD_LEAGUE"
    private val defaultMadhabMethod = "HANAFI"
    private var currentDay = -1
    private var toolbar: Toolbar? = null
    private var settingsIcon: AppCompatImageView? = null

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

        sharedPreferences = context?.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE)
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val calculationMethods = CalculationMethod.values()
        val madhabAsrMethods = Madhab.values()
        if (calculationTitles.isEmpty()) {
            for (method in calculationMethods) {
                calculationTitles[method] = convertToTitleText(method.name)
            }
        }
        if (madhabTitles.isEmpty()) {
            for (method in madhabAsrMethods) {
                madhabTitles[method] = convertToTitleText(method.name)
            }
        }

        val calendar = Calendar.getInstance()
        currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        toolbar = (parentFragment?.parentFragment as? Callback)?.toolbar
        settingsIcon = toolbar?.findViewById(R.id.prayer_settings_action)

        requestLocationPermission()
        NotificationUtils.createNotificationChannel(requireContext())

        initViews()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        locationManager.removeUpdates(locationListener)
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

    private fun prepareLocationInformation(location: Location) {
        try {
            binding.progressBar.visibility = View.GONE
            coordinates = Coordinates(location.latitude, location.longitude)

            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses: MutableList<Address>? = geocoder.getFromLocation(location.latitude, location.longitude, 1)

            addresses?.let {
                if (it.isNotEmpty()) {
                    val address: Address = it[0]

                    cityName = address.adminArea
                    val sb = StringBuilder()
                    sb.apply {
                        address.subLocality?.let { str ->
                            append(str)
                            append(", ")
                        }
                        address.thoroughfare?.let { str ->
                            append(str)
                            append(", ")
                        }
                        address.postalCode?.let { str ->
                            append(str)
                        }
                    }
                    this@PrayersLandingFragment.fullAddress = sb.toString()
                }
            }

            prepareData(initialCall = true)
        } catch (error: Exception) {
            prepareData(initialCall = true)
            println("geocoder error " + error.message)
        }
    }

    private fun prepareData(initialCall: Boolean = false) {
        val prayers = mutableListOf<PrayerModelView>()

        getDataFromApi()

        val prayerTimes = when(dateIndex) {
            0 -> yesterdayPrayerTimes
            1 -> todayPrayerTimes
            2 -> tomorrowPrayerTimes
            else -> todayPrayerTimes
        }

        val calendar = Calendar.getInstance()
        calendar.time = prayerTimes.fajr
        prayers.add(PrayerModelView(requireContext(), PrayerModel(PrayersConstants.PrayerTime.FAJR, "Fajr", getTimeString(calendar), getNotificationPreference(prayerTimes.fajr), prayerTimes.fajr), this@PrayersLandingFragment))
        calendar.time = prayerTimes.sunrise
        prayers.add(PrayerModelView(requireContext(), PrayerModel(PrayersConstants.PrayerTime.SUNRISE, "Sunrise", getTimeString(calendar), getNotificationPreference(prayerTimes.sunrise), prayerTimes.sunrise), this@PrayersLandingFragment))
        calendar.time = prayerTimes.dhuhr
        prayers.add(PrayerModelView(requireContext(), PrayerModel(PrayersConstants.PrayerTime.DHUHR, "Dhuhr", getTimeString(calendar), getNotificationPreference(prayerTimes.dhuhr), prayerTimes.dhuhr), this@PrayersLandingFragment))
        calendar.time = prayerTimes.asr
        prayers.add(PrayerModelView(requireContext(), PrayerModel(PrayersConstants.PrayerTime.ASR, "Asr", getTimeString(calendar), getNotificationPreference(prayerTimes.asr), prayerTimes.asr), this@PrayersLandingFragment))
        calendar.time = prayerTimes.maghrib
        prayers.add(PrayerModelView(requireContext(), PrayerModel(PrayersConstants.PrayerTime.MAGHRIB, "Maghrib", getTimeString(calendar), getNotificationPreference(prayerTimes.maghrib), prayerTimes.maghrib), this@PrayersLandingFragment))
        calendar.time = prayerTimes.isha
        prayers.add(PrayerModelView(requireContext(), PrayerModel(PrayersConstants.PrayerTime.ISHA, "Isha", getTimeString(calendar), getNotificationPreference(prayerTimes.isha), prayerTimes.isha), this@PrayersLandingFragment))

        prayerModelViews = prayers.toList()

        if (initialCall) {
            job = GlobalScope.launch(Dispatchers.Main) {
                while (isActive) {
                    val cal = Calendar.getInstance()
                    if (cal.get(Calendar.DAY_OF_MONTH) != currentDay) {
                        prepareData()
                        delay(5000)
                    } else {
                        if (dateIndex == 1) {
                            binding.bannerPrayerTime.text = convertToTitleText(prayerTimes.currentPrayer().name)
                            binding.bannerRemainingTimeText.text = getNextPrayerTimeText(Date())
                        }
                        delay(1000)
                    }
                }
            }
        }
    }

    private fun getDataFromApi() {
        val savedCalculationMethod = sharedPreferences?.getString(SharedPrefKey.CALCULATION_METHOD_KEY.value, defaultCalculationMethod) ?: defaultCalculationMethod
        val calculationMethod = CalculationMethod.valueOf(savedCalculationMethod)
        val savedMadhabMethod = sharedPreferences?.getString(SharedPrefKey.MADHAB_METHOD_KEY.value, defaultMadhabMethod) ?: defaultMadhabMethod
        val madhabMethod = Madhab.valueOf(savedMadhabMethod)
        params = calculationMethod.parameters
        params.madhab = madhabMethod

        yesterdayPrayerTimes = PrayerTimes(coordinates, DateComponents.from(getNDaysBeforeOrAfter(-1)), params)
        todayPrayerTimes = PrayerTimes(coordinates, DateComponents.from(Date()), params)
        tomorrowPrayerTimes = PrayerTimes(coordinates, DateComponents.from(getNDaysBeforeOrAfter(1)), params)
    }

    private fun initViews() {
        binding.apply {
            goYesterdayBtn.setOnClickListener {
                if (dateIndex > 0) {
                    dateIndex--
                    prepareData()
                }
            }

            goTomorrowBtn.setOnClickListener {
                if (dateIndex < 2) {
                    dateIndex++
                    prepareData()
                }
            }

            calculationTimeRecyclerView.adapter = CalculationMethodRecyclerAdapter(calculationTitles, context, object : OnCalculationMethodClickedListener {
                override fun onCalculationMethodClicked(calculationMethod: CalculationMethod) {
                    val sharedPreferences = context?.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE)
                    val editor = sharedPreferences?.edit()
                    editor?.putString(SharedPrefKey.CALCULATION_METHOD_KEY.value, calculationMethod.name)
                    editor?.apply()

                    removeAllAlarms()
                    prepareData()
                    prayersPageContainer.visibility = View.VISIBLE
                    calculationTimeRecyclerView.visibility = View.GONE
                }
            })
            calculationTimeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            calculationTimeRecyclerView.addItemDecoration(ItemOffsetDecoration(requireContext()))

            madhabMethodRecyclerView.adapter = MadhabAsrTimeRecyclerAdapter(madhabTitles, context, object : OnMadhabMethodClickedListener {
                override fun onMadhabMethodClicked(madhab: Madhab) {
                    val sharedPreferences = context?.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE)
                    val editor = sharedPreferences?.edit()
                    editor?.putString(SharedPrefKey.MADHAB_METHOD_KEY.value, madhab.name)
                    editor?.apply()

                    removeAllAlarms()
                    prepareData()
                    prayersPageContainer.visibility = View.VISIBLE
                    madhabMethodRecyclerView.visibility = View.GONE
                }
            })
            madhabMethodRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            madhabMethodRecyclerView.addItemDecoration(ItemOffsetDecoration(requireContext()))
        }

        settingsIcon?.setOnClickListener {
            val popUpMenu = PopupMenu(requireContext(), it)
            popUpMenu.menuInflater.inflate(R.menu.prayers_page_popup_menu, popUpMenu.menu)
            popUpMenu.setOnMenuItemClickListener {item: MenuItem ->
                when (item.itemId) {
                    R.id.prayers_page_popup_menu_option_1 -> {
                        binding.apply {
                            prayersPageContainer.visibility = View.GONE
                            calculationTimeRecyclerView.visibility = View.GONE
                            madhabMethodRecyclerView.visibility = View.VISIBLE
                        }
                        true
                    }
                    R.id.prayers_page_popup_menu_option_2 -> {
                        binding.apply {
                            prayersPageContainer.visibility = View.GONE
                            madhabMethodRecyclerView.visibility = View.GONE
                            calculationTimeRecyclerView.visibility = View.VISIBLE
                        }
                        true
                    } else -> false
                }
            }
            popUpMenu.show()
        }
    }

    private fun removeAllAlarms() {
        removeAlarmIfSet(todayPrayerTimes.fajr)
        removeAlarmIfSet(todayPrayerTimes.sunrise)
        removeAlarmIfSet(todayPrayerTimes.dhuhr)
        removeAlarmIfSet(todayPrayerTimes.asr)
        removeAlarmIfSet(todayPrayerTimes.maghrib)
        removeAlarmIfSet(todayPrayerTimes.isha)
        removeAlarmIfSet(tomorrowPrayerTimes.fajr)
        removeAlarmIfSet(tomorrowPrayerTimes.sunrise)
        removeAlarmIfSet(tomorrowPrayerTimes.dhuhr)
        removeAlarmIfSet(tomorrowPrayerTimes.asr)
        removeAlarmIfSet(tomorrowPrayerTimes.maghrib)
        removeAlarmIfSet(tomorrowPrayerTimes.isha)
    }

    private fun removeAlarmIfSet(prayer: Date) {
        if (getNotificationPreference(prayer) != PrayersConstants.NotificationTypes.MUTED) {
            val notificationId = timeToNotificationId(prayer.time)
            removeFromSharedPref(notificationId)
            NotificationUtils.cancelNotification(requireContext(), notificationId)
        }
    }

    fun saveToSharedPref(notificationId: Int, notificationType: String) {
        val sharedPreferences = requireContext().getSharedPreferences("PrayersPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        val key = "${SharedPrefKey.ALARM_SET_AT.value}$notificationId"
        editor?.putString(key, notificationType)
        editor?.apply()
    }

    fun removeFromSharedPref(notificationId: Int) {
        val editor = requireContext().getSharedPreferences("PrayersPreferences", Context.MODE_PRIVATE).edit()
        editor.remove("${SharedPrefKey.ALARM_SET_AT.value}$notificationId")
        editor.apply()
    }

    private fun changePrayerDayLabelText() {
        binding.prayerDayLabel.text = when (dateIndex) {
            0 -> "Yesterday"
            1 -> "Today"
            2 -> "Tomorrow"
            else -> ""
        }

    }

    private fun preparePrayerTimesView() {
        binding.apply {
            changePrayerDayLabelText()
            arrangeDateInformation()
            addressInfo.text = fullAddress
            dateInfoContainer.visibility = View.VISIBLE

            prayersLinearLayout.removeAllViews()
            prayerModelViews?.forEach {
                prayersLinearLayout.addView(it)
            }
        }
    }

    private fun getNextPrayerTimeText(now: Date): CharSequence {
        var nextPrayer = todayPrayerTimes.nextPrayer()
        val remainingSecs = when (nextPrayer) {
            Prayer.NONE -> {
                val calendar = Calendar.getInstance()
                calendar.time = getNDaysBeforeOrAfter(1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                nextPrayer = tomorrowPrayerTimes.nextPrayer(calendar.time)
                tomorrowPrayerTimes.timeForPrayer(nextPrayer).time.minus(now.time).div(60000L)
            }
            else -> todayPrayerTimes.timeForPrayer(nextPrayer).time.minus(now.time).div(60000L)
        }
        val remainingHours = remainingSecs.div(60L)
        val remainingMinutes = ceil(remainingSecs.mod(60L).toDouble()).toInt()
        val convertedName = convertToTitleText(nextPrayer.name)
        return when(remainingHours > 0) {
            true -> {
                when (remainingMinutes > 0) {
                    true -> "$remainingHours hours $remainingMinutes minutes until $convertedName"
                    else -> "$remainingHours hours until $convertedName"
                }
            }
            else -> {
                when (remainingMinutes > 0) {
                    true -> "$remainingMinutes minutes until $convertedName"
                    else -> "$remainingSecs secs until $convertedName"
                }
            }
        }
    }

    private fun arrangeDateInformation() {
        binding.apply {
            val calendar = Calendar.getInstance()
            calendar.time = getDate()
            val prayerDateText = "$cityName | ${calendar.get(Calendar.DAY_OF_MONTH)} ${monthNames[calendar.get(Calendar.MONTH)]} ${calendar.get(Calendar.YEAR)}"
            prayerDate.text = prayerDateText
        }
    }

    private fun getDate(): Date {
        return when (dateIndex) {
            0 -> getNDaysBeforeOrAfter(-1)
            1 -> Date()
            2 -> getNDaysBeforeOrAfter(1)
            else -> Date()
        }
    }

    fun timeToNotificationId(value: Long) : Int {
        return ((value.div(1000L)).toInt() % Integer.MAX_VALUE)
    }

    private fun getNotificationPreference(prayerTime: Date): String {
        return sharedPreferences?.getString("${SharedPrefKey.ALARM_SET_AT.value}${timeToNotificationId(prayerTime.time)}", PrayersConstants.NotificationTypes.MUTED) ?: PrayersConstants.NotificationTypes.MUTED
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

    private fun convertToTitleText(input: String): String {
        val words = input.split("_").map {
            it.lowercase().replaceFirstChar { lowercased ->
                if (lowercased.isLowerCase()) lowercased.titlecase()
                else it
            }
        }
        return words.joinToString(" ")
    }

    class ItemOffsetDecoration(private val context: Context) : RecyclerView.ItemDecoration() {

        private val spacing: Int = dpToPx(10) // Convert dp to pixels

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            val itemPosition = parent.getChildAdapterPosition(view)

            // Add top spacing for the first item
            if (itemPosition == 0) {
                outRect.top = spacing * 2
                outRect.bottom = spacing / 2
            }

            // Add bottom spacing for the last item
            if (itemPosition == parent.adapter?.itemCount?.minus(1)) {
                outRect.top = spacing / 2
                outRect.bottom = spacing * 2
            }

            // Add spacing between items
            if (itemPosition > 0 && itemPosition < (parent.adapter?.itemCount?.minus(1) ?: 0)) {
                outRect.top = spacing / 2
                outRect.bottom = spacing / 2
            }
        }

        private fun dpToPx(dp: Int): Int {
            val density = context.resources.displayMetrics.density
            return (dp * density).toInt()
        }
    }
}

enum class SharedPrefKey(val value: String) {
    ALARM_SET_AT("ALARM_SET_AT_"),
    CALCULATION_METHOD_KEY("CALCULATION_METHOD_KEY"),
    MADHAB_METHOD_KEY("MADHAB_METHOD_KEY")
}