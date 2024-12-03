package com.example.wheelify.ui.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.wheelify.databinding.FragmentSettingsBinding
import com.example.wheelify.preferences.UserPreference
import com.example.wheelify.ui.about.AboutActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var dataStoreManager: UserPreference
    private var isSwitchBeingSetByObserver = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        dataStoreManager = UserPreference(requireContext())

        val root: View = binding.root

        val aboutUsCard: View = binding.aboutUsCard
        aboutUsCard.setOnClickListener {
            val intent : Intent = Intent(requireContext(), AboutActivity::class.java)
            startActivity(intent)
        }

        val contactUsCard: View = binding.contactUsCard
        contactUsCard.setOnClickListener {
            // Aksi untuk item Contact Us
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeThemePreference()

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (!isSwitchBeingSetByObserver) {
                val currentMode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                if (AppCompatDelegate.getDefaultNightMode() != currentMode) {
                    AppCompatDelegate.setDefaultNightMode(currentMode)
                }

                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    dataStoreManager.setTheme(isChecked)
                    Log.d("SettingsFragment", "Theme set to: $isChecked") // Log untuk memastikan nilai disimpan
                }
            }
        }
    }

    private fun observeThemePreference() {
        viewLifecycleOwner.lifecycleScope.launch {
            dataStoreManager.themeFlow.collect { isDarkThemeEnabled ->
                Log.d("SettingsFragment", "Received theme: $isDarkThemeEnabled") // Debug log
                isSwitchBeingSetByObserver = true
                if (binding.themeSwitch.isChecked != isDarkThemeEnabled) {
                    binding.themeSwitch.isChecked = isDarkThemeEnabled
                }
                isSwitchBeingSetByObserver = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
