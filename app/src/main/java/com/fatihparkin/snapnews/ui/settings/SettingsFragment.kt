package com.fatihparkin.snapnews.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fatihparkin.snapnews.R
import com.fatihparkin.snapnews.databinding.FragmentSettingsBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAnalytics: FirebaseAnalytics
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

        firebaseAnalytics = Firebase.analytics
        Firebase.crashlytics.log("SettingsFragment açıldı")

        // Bildirim switch
        binding.notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            val msg = if (isChecked) "Bildirimler Açıldı" else "Bildirimler Kapatıldı"
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            firebaseAnalytics.logEvent("bildirim_degisti", Bundle().apply {
                putString("durum", if (isChecked) "acildi" else "kapandi")
            })
        }

        // --- SSS bölüm ---
        binding.faqContent.visibility = View.GONE
        binding.answer1.visibility = View.GONE
        binding.answer2.visibility = View.GONE

        binding.faqHeader.setOnClickListener {
            isFaqOpen = !isFaqOpen
            binding.faqContent.visibility = if (isFaqOpen) View.VISIBLE else View.GONE
            binding.faqHeader.setCompoundDrawablesWithIntrinsicBounds(
                0, 0,
                if (isFaqOpen) R.drawable.ic_arrow_drop_up else R.drawable.ic_arrow_drop_down,
                0
            )
            firebaseAnalytics.logEvent("sss_acildi_kapatildi", Bundle().apply {
                putString("durum", if (isFaqOpen) "acildi" else "kapandi")
            })
        }

        binding.question1Layout.setOnClickListener {
            try {
                toggleAnswer(binding.answer1)
                firebaseAnalytics.logEvent("sss_soru_tiklandi", Bundle().apply {
                    putString("soru", "Haber kaynaklarını nereden alıyorsunuz?")
                })
            } catch (e: Exception) {
                Firebase.crashlytics.recordException(e)
            }
        }

        binding.question2Layout.setOnClickListener {
            try {
                toggleAnswer(binding.answer2)
                firebaseAnalytics.logEvent("sss_soru_tiklandi", Bundle().apply {
                    putString("soru", "SnapNews ücretli mi?")
                })
            } catch (e: Exception) {
                Firebase.crashlytics.recordException(e)
            }
        }

        // --- Bize Ulaşın bölüm ---
        binding.contactContent.visibility = View.GONE

        binding.contactHeader.setOnClickListener {
            isContactOpen = !isContactOpen
            binding.contactContent.visibility = if (isContactOpen) View.VISIBLE else View.GONE
            binding.contactHeader.setCompoundDrawablesWithIntrinsicBounds(
                0, 0,
                if (isContactOpen) R.drawable.ic_arrow_drop_up else R.drawable.ic_arrow_drop_down,
                0
            )
            firebaseAnalytics.logEvent("bize_ulasin_acildi_kapatildi", Bundle().apply {
                putString("durum", if (isContactOpen) "acildi" else "kapandi")
            })
        }

        // GitHub
        binding.githubLink.setOnClickListener {
            try {
                openUrl("https://github.com/fatihparkin")
                firebaseAnalytics.logEvent("github_tiklandi", null)
            } catch (e: Exception) {
                Firebase.crashlytics.recordException(e)
            }
        }

        // Instagram
        binding.instagramLink.setOnClickListener {
            try {
                openUrl("https://instagram.com/fatihparkin")
                firebaseAnalytics.logEvent("instagram_tiklandi", null)
            } catch (e: Exception) {
                Firebase.crashlytics.recordException(e)
            }
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
