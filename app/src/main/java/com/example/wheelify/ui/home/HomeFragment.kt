package com.example.wheelify.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wheelify.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var listNewsAdapter: ListNewsAdapter
    private lateinit var popularNewsAdapter: PopularNewsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listNewsAdapter = ListNewsAdapter()
        binding.rvBanner.adapter = listNewsAdapter

        popularNewsAdapter = PopularNewsAdapter()
        binding.rvListnews.adapter = popularNewsAdapter

        binding.rvBanner.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvListnews.layoutManager = LinearLayoutManager(requireContext())

        homeViewModel.getNews("country", "category")

        homeViewModel.news.observe(viewLifecycleOwner) { news ->
            listNewsAdapter.submitList(news)
            popularNewsAdapter.submitList(news)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        homeViewModel.news.observe(viewLifecycleOwner) { news ->
            if (news != null) {
                Toast.makeText(requireContext(), "News fetched successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Failed to fetch news", Toast.LENGTH_SHORT).show()
            }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}