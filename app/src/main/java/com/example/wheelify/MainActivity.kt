package com.example.wheelify

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.wheelify.databinding.ActivityMainBinding
import com.example.wheelify.preferences.UserPreference
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dataStoreManager: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStoreManager = UserPreference(this)
        checkAndApplySavedTheme()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        navView.setupWithNavController(navController)
    }

    private fun checkAndApplySavedTheme() {
        GlobalScope.launch(Dispatchers.Main) {
            dataStoreManager.themeFlow.collect { isDarkThemeEnabled ->
                val currentMode = if (isDarkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                if (AppCompatDelegate.getDefaultNightMode() != currentMode) {
                    AppCompatDelegate.setDefaultNightMode(currentMode)
                }
            }
        }
    }
}
