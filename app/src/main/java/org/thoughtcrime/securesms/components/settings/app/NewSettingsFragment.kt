package org.thoughtcrime.securesms.components.settings.app

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EdgeEffect
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.thoughtcrime.securesms.R
import org.thoughtcrime.securesms.badges.BadgeImageView
import org.thoughtcrime.securesms.components.AvatarImageView
import org.thoughtcrime.securesms.components.settings.*
import org.thoughtcrime.securesms.databinding.FragmentNewSettingsBinding
import org.thoughtcrime.securesms.keyvalue.SignalStore
import org.thoughtcrime.securesms.phonenumbers.PhoneNumberFormatter
import org.thoughtcrime.securesms.recipients.Recipient
import org.thoughtcrime.securesms.util.FeatureFlags
import org.thoughtcrime.securesms.util.Util
import org.thoughtcrime.securesms.util.adapter.mapping.LayoutFactory
import org.thoughtcrime.securesms.util.adapter.mapping.MappingAdapter
import org.thoughtcrime.securesms.util.adapter.mapping.MappingViewHolder
import org.thoughtcrime.securesms.util.navigation.safeNavigate
import java.lang.UnsupportedOperationException

class NewSettingsFragment : Fragment() {

    private var layoutManagerProducer: (Context) -> RecyclerView.LayoutManager = { context -> LinearLayoutManager(context) }
    private lateinit var binding: FragmentNewSettingsBinding
    private val viewModel: AppSettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val config = ConcatAdapter.Config.Builder().setIsolateViewTypes(false).build()
        val settingsAdapters = createAdapters()
        val settingsAdapter: RecyclerView.Adapter<out RecyclerView.ViewHolder> = when {
            settingsAdapters.size > 1 -> ConcatAdapter(config, *settingsAdapters)
            settingsAdapters.size == 1 -> settingsAdapters.first()
            else -> error("Require one or more settings adapters.")
        }
        //findNavController().navigate(R.id.action_newSettingsFragment_to_deviceActivity2)
        binding.settingsRecyclerView.apply {
            edgeEffectFactory = EdgeEffectFactory()
            layoutManager = layoutManagerProducer(requireContext())
            adapter = settingsAdapter
        }
        when (settingsAdapter) {
            is ConcatAdapter -> bindAdapters(settingsAdapter)
            is MappingAdapter -> bindAdapter(settingsAdapter)
            else -> error("Illegal adapter subtype: ${settingsAdapter.javaClass.simpleName}")
        }

    }

    private fun getConfiguration(state: AppSettingsState): DSLConfiguration {
        return configure {
            customPref(
                BioPreference(state.self) {
                    findNavController().safeNavigate(R.id.action_appSettingsFragment_to_manageProfileActivity)
                }
            )

            clickPref(
                title = DSLSettingsText.from(R.string.AccountSettingsFragment__account),
                icon = DSLSettingsIcon.from(R.drawable.ic_profile_circle_24),
                onClick = {
                    findNavController().safeNavigate(R.id.action_appSettingsFragment_to_accountSettingsFragment)
                }
            )

            clickPref(
                title = DSLSettingsText.from(R.string.preferences__linked_devices),
                icon = DSLSettingsIcon.from(R.drawable.ic_linked_devices_24),
                onClick = {
                    findNavController().safeNavigate(R.id.action_appSettingsFragment_to_deviceActivity)
                }
            )

//            if (FeatureFlags.donorBadges() && PlayServicesUtil.getPlayServicesStatus(requireContext()) == PlayServicesUtil.PlayServicesStatus.SUCCESS) {
//
//                clickPref(
//                    title = DSLSettingsText.from(R.string.preferences__donate_to_signal),
//                    icon = DSLSettingsIcon.from(R.drawable.ic_heart_24),
//                    iconEnd = if (state.hasExpiredGiftBadge) DSLSettingsIcon.from(R.drawable.ic_info_solid_24, R.color.signal_accent_primary) else null,
//                    onClick = {
//                        findNavController().safeNavigate(AppSettingsFragmentDirections.actionAppSettingsFragmentToManageDonationsFragment())
//                    },
//                    onLongClick = this@NewSettingsFragment::copySubscriberIdToClipboard
//                )
//            } else {
//                externalLinkPref(
//                    title = DSLSettingsText.from(R.string.preferences__donate_to_signal),
//                    icon = DSLSettingsIcon.from(R.drawable.ic_heart_24),
//                    linkId = R.string.donate_url
//                )
//            }

            dividerPref()

            clickPref(
                title = DSLSettingsText.from(R.string.preferences__appearance),
                icon = DSLSettingsIcon.from(R.drawable.ic_appearance_24),
                onClick = {
                    findNavController().safeNavigate(R.id.action_appSettingsFragment_to_appearanceSettingsFragment)
                }
            )

            clickPref(
                title = DSLSettingsText.from(R.string.preferences_chats__chats),
                icon = DSLSettingsIcon.from(R.drawable.ic_chat_message_24),
                onClick = {
                    findNavController().safeNavigate(R.id.action_appSettingsFragment_to_chatsSettingsFragment)
                }
            )

            clickPref(
                title = DSLSettingsText.from(R.string.preferences__notifications),
                icon = DSLSettingsIcon.from(R.drawable.ic_bell_24),
                onClick = {
                    findNavController().safeNavigate(R.id.action_appSettingsFragment_to_notificationsSettingsFragment)
                }
            )

            clickPref(
                title = DSLSettingsText.from(R.string.preferences__privacy),
                icon = DSLSettingsIcon.from(R.drawable.ic_lock_24),
                onClick = {
                    findNavController().safeNavigate(R.id.action_appSettingsFragment_to_privacySettingsFragment)
                }
            )

            clickPref(
                title = DSLSettingsText.from(R.string.preferences__data_and_storage),
                icon = DSLSettingsIcon.from(R.drawable.ic_archive_24dp),
                onClick = {
                    findNavController().safeNavigate(R.id.action_appSettingsFragment_to_dataAndStorageSettingsFragment)
                }
            )

            dividerPref()

//            if (SignalStore.paymentsValues().paymentsAvailability.showPaymentsMenu()) {
//                customPref(
//                    PaymentsPreference(
//                        unreadCount = state.unreadPaymentsCount
//                    ) {
//                        findNavController().safeNavigate(R.id.action_appSettingsFragment_to_paymentsActivity)
//                    }
//                )
//
//                dividerPref()
//            }

            clickPref(
                title = DSLSettingsText.from(R.string.preferences__help),
                icon = DSLSettingsIcon.from(R.drawable.ic_help_24),
                onClick = {
                    findNavController().safeNavigate(R.id.action_appSettingsFragment_to_helpSettingsFragment)
                }
            )

            clickPref(
                title = DSLSettingsText.from(R.string.AppSettingsFragment__invite_your_friends),
                icon = DSLSettingsIcon.from(R.drawable.ic_invite_24),
                onClick = {
                    findNavController().safeNavigate(R.id.action_appSettingsFragment_to_inviteActivity)
                }
            )

            if (FeatureFlags.internalUser()) {
                dividerPref()

                clickPref(
                    title = DSLSettingsText.from(R.string.preferences__internal_preferences),
                    onClick = {
                        findNavController().safeNavigate(R.id.action_appSettingsFragment_to_internalSettingsFragment)
                    }
                )
            }
        }
    }

    private fun bindAdapters(adapter: ConcatAdapter) {
        throw UnsupportedOperationException("This method is not implemented.")
    }

    private fun bindAdapter(adapter: MappingAdapter) {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            adapter.submitList(getConfiguration(state).toMappingModelList())
        }
        adapter.registerFactory(
            BioPreference::class.java, LayoutFactory(
                NewSettingsFragment::BioPreferenceViewHolder, R.layout.bio_preference_item)
        )
        adapter.registerFactory(
            PaymentsPreference::class.java, LayoutFactory(
                NewSettingsFragment::PaymentsPreferenceViewHolder, R.layout.dsl_payments_preference)
        )
        adapter.registerFactory(
            SubscriptionPreference::class.java, LayoutFactory(
                NewSettingsFragment::SubscriptionPreferenceViewHolder, R.layout.dsl_preference_item)
        )
    }

    private fun createAdapters(): Array<MappingAdapter> {
        return arrayOf(DSLSettingsAdapter())
    }

    private class EdgeEffectFactory : RecyclerView.EdgeEffectFactory() {
        override fun createEdgeEffect(view: RecyclerView, direction: Int): EdgeEffect {
            return super.createEdgeEffect(view, direction).apply {
                if (Build.VERSION.SDK_INT > 21) {
                    color =
                        requireNotNull(ContextCompat.getColor(view.context, R.color.settings_ripple_color))
                }
            }
        }
    }

    private fun copySubscriberIdToClipboard(): Boolean {
        val subscriber = SignalStore.donationsValues().getSubscriber()
        return if (subscriber == null) {
            false
        } else {
            Toast.makeText(requireContext(), R.string.AppSettingsFragment__copied_subscriber_id_to_clipboard, Toast.LENGTH_LONG).show()
            Util.copyToClipboard(requireContext(), subscriber.subscriberId.serialize())
            true
        }
    }

    private class SubscriptionPreference(
        override val title: DSLSettingsText,
        override val summary: DSLSettingsText? = null,
        override val icon: DSLSettingsIcon? = null,
        override val isEnabled: Boolean = true,
        val isActive: Boolean = false,
        val onClick: (Boolean) -> Unit,
        val onLongClick: () -> Boolean
    ) : PreferenceModel<SubscriptionPreference>() {
        override fun areItemsTheSame(newItem: SubscriptionPreference): Boolean {
            return true
        }

        override fun areContentsTheSame(newItem: SubscriptionPreference): Boolean {
            return super.areContentsTheSame(newItem) && isActive == newItem.isActive
        }
    }

    private class SubscriptionPreferenceViewHolder(itemView: View) : PreferenceViewHolder<SubscriptionPreference>(itemView) {
        override fun bind(model: SubscriptionPreference) {
            super.bind(model)
            itemView.setOnClickListener { model.onClick(model.isActive) }
            itemView.setOnLongClickListener { model.onLongClick() }
        }
    }

    private class BioPreference(val recipient: Recipient, val onClick: () -> Unit) : PreferenceModel<BioPreference>() {
        override fun areContentsTheSame(newItem: BioPreference): Boolean {
            return super.areContentsTheSame(newItem) && recipient.hasSameContent(newItem.recipient)
        }

        override fun areItemsTheSame(newItem: BioPreference): Boolean {
            return recipient == newItem.recipient
        }
    }

    private class BioPreferenceViewHolder(itemView: View) : PreferenceViewHolder<BioPreference>(itemView) {

        private val avatarView: AvatarImageView = itemView.findViewById(R.id.icon)
        private val aboutView: TextView = itemView.findViewById(R.id.about)
        private val badgeView: BadgeImageView = itemView.findViewById(R.id.badge)

        override fun bind(model: BioPreference) {
            super.bind(model)

            itemView.setOnClickListener { model.onClick() }

            titleView.text = model.recipient.profileName.toString()
            summaryView.text = PhoneNumberFormatter.prettyPrint(model.recipient.requireE164())
            avatarView.setRecipient(Recipient.self())
            badgeView.setBadgeFromRecipient(Recipient.self())

            titleView.visibility = View.VISIBLE
            summaryView.visibility = View.VISIBLE
            avatarView.visibility = View.VISIBLE

            if (model.recipient.combinedAboutAndEmoji != null) {
                aboutView.text = model.recipient.combinedAboutAndEmoji
                aboutView.visibility = View.VISIBLE
            } else {
                aboutView.visibility = View.GONE
            }
        }
    }

    private class PaymentsPreference(val unreadCount: Int, val onClick: () -> Unit) : PreferenceModel<PaymentsPreference>() {
        override fun areContentsTheSame(newItem: PaymentsPreference): Boolean {
            return super.areContentsTheSame(newItem) && unreadCount == newItem.unreadCount
        }

        override fun areItemsTheSame(newItem: PaymentsPreference): Boolean {
            return true
        }
    }

    private class PaymentsPreferenceViewHolder(itemView: View) : MappingViewHolder<PaymentsPreference>(itemView) {

        private val unreadCountView: TextView = itemView.findViewById(R.id.unread_indicator)

        override fun bind(model: PaymentsPreference) {
            unreadCountView.text = model.unreadCount.toString()
            unreadCountView.visibility = if (model.unreadCount > 0) View.VISIBLE else View.GONE

            itemView.setOnClickListener {
                model.onClick()
            }
        }
    }

}

