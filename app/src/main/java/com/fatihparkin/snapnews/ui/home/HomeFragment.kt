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
import com.fatihparkin.snapnews.ui.adapter.NewsAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel

    private val searchHandler = Handler(Looper.getMainLooper()) // 🔥 Debounce için Handler
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

        viewModel.newsLiveData.observe(viewLifecycleOwner) { newsResponse ->
            if (newsResponse.articles.isNotEmpty()) {
                binding.recyclerView.adapter = NewsAdapter(newsResponse.articles)
            }
        }

        viewModel.fetchNews() //  Uygulama açılınca haberleri getir

        setupSearchView() // Gerçek zamanlı arama başlat
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false // Enter’a basınca manuel arama yapmayı engelliyoruz
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchRunnable?.let { searchHandler.removeCallbacks(it) } // Önceki aramayı iptal et

                searchRunnable = Runnable {
                    viewModel.fetchNews(newText) //  Kullanıcı yazmayı bitirince API çağrısı yap
                }

                searchHandler.postDelayed(searchRunnable!!, 500) //  500ms bekleyip çalıştır
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
