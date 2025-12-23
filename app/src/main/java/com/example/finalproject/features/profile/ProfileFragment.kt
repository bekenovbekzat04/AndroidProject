package com.example.finalproject.features.profile


import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.finalproject.R
import com.example.finalproject.core.common.ThemePreferences
import com.example.finalproject.databinding.FragmentProfileBinding
import com.example.finalproject.features.detail.AnimeDetailFragment
import com.example.finalproject.features.search.SearchAdapter
import com.example.finalproject.features.search.SearchItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()
    private val adapter = SearchAdapter { anime ->
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container_view, AnimeDetailFragment.newInstance(anime.id))
            addToBackStack(null)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        binding.savedRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.savedRecycler.adapter = adapter

//        setupThemeToggle()
        observeState()
        viewModel.loadSaved()
    }

//    private fun setupThemeToggle() {
//        val isDarkMode = ThemePreferences.isDarkModeEnabled(requireContext())
//        binding.themeSwitch.isChecked = isDarkMode
//        updateThemeStatusText(isDarkMode)
//
//        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
//            ThemePreferences.updateDarkMode(requireContext(), isChecked)
//            updateThemeStatusText(isChecked)
//        }
//    }
//
//    private fun updateThemeStatusText(isDarkModeEnabled: Boolean) {
//        binding.themeStatus.text = getString(
//            if (isDarkModeEnabled) R.string.profile_theme_dark else R.string.profile_theme_light
//        )
//    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is ProfileState.Loading -> showLoading()
                        is ProfileState.Success -> showContent(state.items.map { it.toItem() })
                        is ProfileState.Error -> showError(state.message)
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.profileProgress.isVisible = true
        binding.errorText.isVisible = false
        binding.emptyText.isVisible = false
        binding.savedRecycler.isVisible = false
    }

    private fun showContent(items: List<SearchItem>) {
        binding.profileProgress.isVisible = false
        binding.errorText.isVisible = false

        val hasItems = items.isNotEmpty()
        binding.emptyText.isVisible = !hasItems
        binding.savedRecycler.isVisible = hasItems

        if (hasItems) {
            adapter.submitList(items)
        } else {
            adapter.submitList(emptyList())
        }
    }

    private fun showError(message: String) {
        binding.profileProgress.isVisible = false
        binding.savedRecycler.isVisible = false
        binding.errorText.isVisible = true
        binding.errorText.text = message
        binding.emptyText.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
