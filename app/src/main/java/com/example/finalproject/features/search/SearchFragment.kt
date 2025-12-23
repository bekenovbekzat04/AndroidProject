package com.example.finalproject.features.search


import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.databinding.FragmentDiscoverBinding
import com.example.finalproject.features.detail.AnimeDetailFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiscoverFragment : Fragment() {

    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DiscoverViewModel by viewModels()
    private val resultsAdapter = SearchAdapter { anime ->
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container_view, AnimeDetailFragment.newInstance(anime.id))
            addToBackStack(null)
        }
    }
    private val skeletonAdapter = SearchSkeletonAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchResults.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.searchResults.adapter = resultsAdapter
        val spacing = (12 * resources.displayMetrics.density).toInt()
        binding.searchResults.addItemDecoration(GridSpacingItemDecoration(spacing))

        binding.searchButton.setOnClickListener { submitSearch() }
        binding.searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                submitSearch()
                true
            } else false
        }
        binding.searchInput.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                submitSearch(); true
            } else false
        }

        observeState()
    }

    private fun submitSearch() {
        val query = binding.searchInput.text.toString().trim()
        if (query.isNotEmpty()) {
            viewModel.search(query)
            hideKeyboard()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is DiscoverState.Idle -> showIdle()
                        is DiscoverState.Loading -> showLoading()
                        is DiscoverState.Success -> showResults(state.items)
                        is DiscoverState.Error -> showError(state.message)
                    }
                }
            }
        }
    }

    private fun showIdle() {
        binding.searchError.isVisible = false
        binding.searchResults.adapter = resultsAdapter
        resultsAdapter.submitList(emptyList())
    }

    private fun showLoading() {
        binding.searchError.isVisible = false
        binding.searchResults.adapter = skeletonAdapter
    }

    private fun showResults(items: List<SearchItem>) {
        binding.searchError.isVisible = false
        binding.searchResults.adapter = resultsAdapter
        resultsAdapter.submitList(items)
    }

    private fun showError(message: String) {
        binding.searchError.isVisible = true
        binding.searchError.text = message
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
