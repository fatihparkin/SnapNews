package com.fatihparkin.snapnews

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatihparkin.snapnews.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true) // Menü için gerekli
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView ayarları
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // İlk yüklemede haberleri getir
        fetchNews("android")

        // SearchView işlemleri
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    fetchNews(it.trim())
                    hideKeyboard()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel()
                searchJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(500) // Kullanıcı yazmayı bitirdiğinde API çağrısı yap
                    newText?.let {
                        if (it.isNotEmpty()) {
                            fetchNews(it.trim())
                        } else {
                            fetchNews("android") // Arama temizlenirse varsayılan haberleri yükle
                        }
                    }
                }
                return true
            }
        })

        // Aramayı temizleme işlemi (X butonu)
        binding.searchView.setOnCloseListener {
            fetchNews("android") // Arama kapatılınca varsayılan haberler gösterilsin
            hideKeyboard()
            false
        }
    }

    private fun fetchNews(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.getNews(query)
                if (response.isSuccessful) {
                    val newsList = response.body()?.articles ?: emptyList()
                    activity?.runOnUiThread {
                        binding.recyclerView.adapter = NewsAdapter(newsList)
                    }
                } else {
                    Log.e("HomeFragment", "API başarısız: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "Hata: ${e.message}")
            }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = activity?.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
