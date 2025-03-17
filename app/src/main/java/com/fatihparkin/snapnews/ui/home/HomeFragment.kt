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

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel

    private val searchHandler = Handler(Looper.getMainLooper())
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

        val repository = NewsRepository(RetrofitClient.api)
        val factory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Manşet adapter
        val headlineAdapter = HeadlineAdapter(emptyList()) { selectedArticle ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(selectedArticle)
            findNavController().navigate(action)
        }
        binding.viewPager.adapter = headlineAdapter
        binding.dotsIndicator.setViewPager2(binding.viewPager)

        // HABERLER GELİNCE MANŞET & LİSTE AYIRIMI BURADA
        viewModel.newsLiveData.observe(viewLifecycleOwner) { newsResponse ->
            if (newsResponse.articles.isNotEmpty()) {
                // İlk 5 haber manşet
                val headlines = newsResponse.articles.take(5)
                headlineAdapter.updateHeadlines(headlines)

                // Manşet dışındakiler listeye
                val remainingNews = newsResponse.articles.drop(5)
                binding.recyclerView.adapter = NewsAdapter(remainingNews) { selectedArticle ->
                    val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(selectedArticle)
                    findNavController().navigate(action)
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
