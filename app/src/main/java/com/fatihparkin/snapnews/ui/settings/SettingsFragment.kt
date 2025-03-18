package com.fatihparkin.snapnews.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.fatihparkin.snapnews.R
import com.fatihparkin.snapnews.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private var isFaqOpen = false
    private var isContactOpen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SSS ve Bize Ulaşın başlangıçta kapalı
        binding.faqContent.visibility = View.GONE
        binding.contactContent.visibility = View.GONE
        binding.answer1.visibility = View.GONE
        binding.answer2.visibility = View.GONE

        // Karanlık mod switch
        binding.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        // Bildirim switch
        binding.notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            val msg = if (isChecked) "Bildirimler Açıldı" else "Bildirimler Kapatıldı"
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }

        // SSS komple aç/kapat
        binding.faqHeader.setOnClickListener {
            isFaqOpen = !isFaqOpen
            binding.faqContent.visibility = if (isFaqOpen) View.VISIBLE else View.GONE
            binding.faqHeader.setCompoundDrawablesWithIntrinsicBounds(
                0, 0,
                if (isFaqOpen) R.drawable.ic_arrow_drop_up else R.drawable.ic_arrow_drop_down,
                0
            )
        }

        // Soru 1 toggle
        binding.question1Layout.setOnClickListener {
            toggleAnswer(binding.answer1)
        }

        // Soru 2 toggle
        binding.question2Layout.setOnClickListener {
            toggleAnswer(binding.answer2)
        }

        // Bize Ulaşın toggle
        binding.contactHeader.setOnClickListener {
            isContactOpen = !isContactOpen
            binding.contactContent.visibility = if (isContactOpen) View.VISIBLE else View.GONE
            binding.contactHeader.setCompoundDrawablesWithIntrinsicBounds(
                0, 0,
                if (isContactOpen) R.drawable.ic_arrow_drop_up else R.drawable.ic_arrow_drop_down,
                0
            )
        }

        // GitHub link
        binding.githubLink.setOnClickListener {
            openUrl("https://github.com/fatihparkin")
        }

        // Instagram link
        binding.instagramLink.setOnClickListener {
            openUrl("https://instagram.com/fatihparkin")
        }
    }

    private fun toggleAnswer(answerView: View) {
        answerView.visibility = if (answerView.visibility == View.GONE) View.VISIBLE else View.GONE
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
