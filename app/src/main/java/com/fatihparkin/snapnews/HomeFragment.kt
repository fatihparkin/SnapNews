package com.fatihparkin.snapnews

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatihparkin.snapnews.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView ayarları
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Haberleri API'den çek
        fetchNews()
    }

    private fun fetchNews() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.getNews()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
