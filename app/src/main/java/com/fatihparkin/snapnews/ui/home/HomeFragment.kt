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
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatihparkin.snapnews.data.remote.RetrofitClient
import com.fatihparkin.snapnews.data.repository.NewsRepository
import com.fatihparkin.snapnews.databinding.FragmentHomeBinding
import com.fatihparkin.snapnews.ui.adapter.HeadlineAdapter
import com.fatihparkin.snapnews.ui.adapter.NewsAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel

    private val searchHandler = Handler(Looper.getMainLooper()) // Debounce için Handler
    private var searchRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel ve Repository
        val repository = NewsRepository(RetrofitClient.api)
        val factory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        // RecyclerView ve Adapter'ı bağlama
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ViewPager2 için Adapter başlatma
        val headlineAdapter = HeadlineAdapter(emptyList())
        binding.viewPager.adapter = headlineAdapter

        // DotsIndicator'u ViewPager2 ile bağlama
        binding.dotsIndicator.setViewPager2(binding.viewPager)

        // Haberleri getirme işlemi
        viewModel.newsLiveData.observe(viewLifecycleOwner) { newsResponse ->
            if (newsResponse.articles.isNotEmpty()) {
                binding.recyclerView.adapter = NewsAdapter(newsResponse.articles)
                headlineAdapter.updateHeadlines(newsResponse.articles.take(5)) // İlk 5 manşet
            }
        }

        // İlk başta haberleri çek
        viewModel.fetchNews()

        // Arama işlemi için SearchView ayarı
        setupSearchView()
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchRunnable?.let { searchHandler.removeCallbacks(it) }
                searchRunnable = Runnable {
                    viewModel.fetchNews(newText)
                }
                searchHandler.postDelayed(searchRunnable!!, 500)
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
