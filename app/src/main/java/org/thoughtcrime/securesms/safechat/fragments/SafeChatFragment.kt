package org.thoughtcrime.securesms.safechat.fragments

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.conversation.colors.ChatColors
import org.thoughtcrime.securesms.databinding.SafeChatFragmentBinding

class SafeChatFragment: Fragment() {

    private lateinit var binding: SafeChatFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.safe_chat_fragment, container, false)
        binding = SafeChatFragmentBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        initViews()
    }

    private fun initViews() {
        binding.apply {
            tabLayout.setupWithViewPager(viewPager)
        }
    }

    private fun setupViewPager() {
        val adapter = CustomViewPagerAdapter(childFragmentManager)
        adapter.addFragment(SafeChatWhatsappFragment(), "WhatsApp")
        adapter.addFragment(SafeChatTelegramFragment(), "Telegram")
        setupTabIcons(adapter)
        binding.viewPager.adapter = adapter
    }

    private fun setupTabIcons(adapter: CustomViewPagerAdapter) {
        for (i in 0 until adapter.count) {
            val tab = binding.tabLayout.getTabAt(i)
            tab?.customView = adapter.getTabView(i)
        }
    }

    inner class CustomViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val fragmentList = mutableListOf<Fragment>()
        private val tabTitles = mutableListOf<String>()
        private val tabIcons = mutableListOf<Int>()

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return tabTitles[position]
        }

        fun addFragment(fragment: Fragment, title: String/*, @DrawableRes icon: Int*/) {
            fragmentList.add(fragment)
            tabTitles.add(title)
//            tabIcons.add(icon)
        }

        fun getTabView(position: Int): View {
            val view = LayoutInflater.from(context).inflate(R.layout.safe_chat_custom_tab_item_view, null)
//            val tabIcon = view.findViewById<ImageView>(R.id.tabIcon)
            val tabText = view.findViewById<TextView>(R.id.tab_title)

//            tabIcon.setImageResource(tabIcons[position])
            tabText.text = tabTitles[position]

            return view
        }
    }
}

class NonSwipeableViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    private var isPagingEnabled = false

    init {
        isPagingEnabled = false // By default, disable swipe
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return isPagingEnabled && super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return isPagingEnabled && super.onInterceptTouchEvent(event)
    }

    fun setPagingEnabled(enabled: Boolean) {
        isPagingEnabled = enabled
    }
}