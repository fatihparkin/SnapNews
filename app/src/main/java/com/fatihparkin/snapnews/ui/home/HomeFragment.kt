package com.fatihparkin.snapnews.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatihparkin.snapnews.data.remote.RetrofitClient
import com.fatihparkin.snapnews.data.repository.NewsRepository
import com.fatihparkin.snapnews.databinding.FragmentHomeBinding
import com.fatihparkin.snapnews.ui.adapter.HeadlineAdapter
import com.fatihparkin.snapnews.ui.adapter.NewsAdapter
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private val searchHandler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    private val autoScrollHandler = Handler(Looper.getMainLooper())
    private lateinit var autoScrollRunnable: Runnable
    private var currentPage = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAnalytics = Firebase.analytics
        Firebase.crashlytics.log("HomeFragment açıldı")

        val repository = NewsRepository(RetrofitClient.api)
        val factory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val headlineAdapter = HeadlineAdapter(emptyList()) { selectedArticle ->
            try {
                firebaseAnalytics.logEvent("manset_haber_tiklandi", Bundle().apply {
                    putString("haber_basligi", selectedArticle.title)
                })
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(selectedArticle)
                findNavController().navigate(action)
            } catch (e: Exception) {
                Firebase.crashlytics.recordException(e)
            }
        }
        binding.viewPager.adapter = headlineAdapter
        binding.dotsIndicator.setViewPager2(binding.viewPager)

        viewModel.newsLiveData.observe(viewLifecycleOwner) { newsResponse ->
            if (newsResponse.articles.isNotEmpty()) {
                val headlines = newsResponse.articles.take(5)
                headlineAdapter.updateHeadlines(headlines)
                startAutoScroll(headlines.size)

                val remainingNews = newsResponse.articles.drop(5)
                binding.recyclerView.adapter = NewsAdapter(remainingNews) { selectedArticle ->
                    try {
                        firebaseAnalytics.logEvent("haber_tiklandi", Bundle().apply {
                            putString("haber_basligi", selectedArticle.title)
                        })
                        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(selectedArticle)
                        findNavController().navigate(action)
                    } catch (e: Exception) {
                        Firebase.crashlytics.recordException(e)
                    }
                }
            }
        }

        viewModel.fetchNews()
        setupSearchView()
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                searchRunnable?.let { searchHandler.removeCallbacks(it) }
                searchRunnable = Runnable {
                    try {
                        firebaseAnalytics.logEvent("arama_yapildi", Bundle().apply {
                            putString("arama_kelimesi", newText)
                        })
                        viewModel.fetchNews(newText)
                    } catch (e: Exception) {
                        Firebase.crashlytics.recordException(e)
                    }
                }
                searchHandler.postDelayed(searchRunnable!!, 500)
                return true
            }
        })
    }

    private fun startAutoScroll(pagerSize: Int) {
        autoScrollRunnable = object : Runnable {
            override fun run() {
                if (pagerSize == 0) return
                currentPage = (currentPage + 1) % pagerSize
                binding.viewPager.setCurrentItem(currentPage, true)
                autoScrollHandler.postDelayed(this, 4000)
            }
        }
        autoScrollHandler.postDelayed(autoScrollRunnable, 4000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        autoScrollHandler.removeCallbacks(autoScrollRunnable)
        _binding = null
    }
}
