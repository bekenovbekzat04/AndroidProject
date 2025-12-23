package com.example.finalproject.app

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.finalproject.R
import com.example.finalproject.core.common.ThemePreferences
import com.example.finalproject.databinding.ActivityMainBinding
import com.example.finalproject.features.search.DiscoverFragment
import com.example.finalproject.features.home.HomeFragment
import com.example.finalproject.features.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @IdRes
    private var selectedItemId: Int = R.id.navigation_home

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemePreferences.applySavedTheme(this)
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedItemId =
            savedInstanceState?.getInt(SELECTED_ITEM_KEY, R.id.navigation_home)
                ?: R.id.navigation_home

        setupBottomNavigation()
        showFragment(selectedItemId)
        binding.bottomNavigation.selectedItemId = selectedItemId
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            if (item.itemId != selectedItemId) {
                showFragment(item.itemId)
            }
            true
        }
    }

    private fun showFragment(@IdRes itemId: Int) {
        val fragmentManager = supportFragmentManager
        val tag = fragmentTagFor(itemId)
        var fragment = fragmentManager.findFragmentByTag(tag)

        fragmentManager.commit {
            setReorderingAllowed(true)

            if (fragment == null) {
                fragment = createFragment(itemId)
                add(R.id.fragment_container_view, fragment!!, tag)
            }

            fragmentManager.fragments.forEach { existing ->
                if (existing == fragment) {
                    show(existing)
                } else {
                    hide(existing)
                }
            }
        }

        selectedItemId = itemId
    }

    private fun createFragment(@IdRes itemId: Int): Fragment {
        return when (itemId) {
            R.id.navigation_home -> HomeFragment()
            R.id.navigation_discover -> DiscoverFragment()
            R.id.navigation_profile -> ProfileFragment()
            else -> throw IllegalArgumentException("Unknown navigation item id: $itemId")
        }
    }

    private fun fragmentTagFor(@IdRes itemId: Int): String = "bottom_nav_fragment_$itemId"

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTED_ITEM_KEY, selectedItemId)
    }

    fun setBottomNavigationVisible(visible: Boolean) {
        binding.bottomNavigation.visibility = if (visible) View.VISIBLE else View.GONE
    }

    companion object {
        private const val SELECTED_ITEM_KEY = "selected_bottom_nav_item"
    }
}
