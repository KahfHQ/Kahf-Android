package org.thoughtcrime.securesms.profiles.manage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import org.thoughtcrime.securesms.databinding.FragmentGenderEditBinding

class GenderEditFragment : Fragment() {

    private lateinit var binding: FragmentGenderEditBinding
    private lateinit var arrayAdapter: ArrayAdapter<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGenderEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = listOf("Male", "Female")
        arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_single_choice, list)
        binding.genderListView.apply {
            choiceMode = ListView.CHOICE_MODE_SINGLE
            adapter = arrayAdapter
        }
    }

}