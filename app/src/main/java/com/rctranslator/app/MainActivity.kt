package com.rctranslator.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.rctranslator.app.databinding.ActivityMainBinding
import com.rctranslator.app.model.Language
import com.rctranslator.app.service.TranslationService
import com.rctranslator.app.utils.SpeechRecognitionHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var speechRecognitionHelper: SpeechRecognitionHelper
    private lateinit var translationService: TranslationService
    
    private var sourceLanguage: Language = Language.ENGLISH
    private var targetLanguage: Language = Language.SPANISH
    private var isListening = false

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startSpeechRecognition()
        } else {
            Toast.makeText(
                this,
                getString(R.string.permission_required),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        initializeServices()
    }

    private fun setupUI() {
        // Set initial language display
        binding.sourceLanguageText.setText(sourceLanguage.displayName)
        binding.targetLanguageText.setText(targetLanguage.displayName)

        // Language selection
        binding.sourceLanguageText.setOnClickListener {
            showLanguageDialog(true)
        }

        binding.targetLanguageText.setOnClickListener {
            showLanguageDialog(false)
        }

        // Swap languages
        binding.swapButton.setOnClickListener {
            swapLanguages()
        }

        // Voice input
        binding.voiceButton.setOnClickListener {
            if (isListening) {
                stopSpeechRecognition()
            } else {
                checkPermissionAndStartSpeechRecognition()
            }
        }

        // Clear input
        binding.clearButton.setOnClickListener {
            binding.inputText.setText("")
            binding.translatedText.setText("")
        }

        // Translate button
        binding.translateButton.setOnClickListener {
            translateText()
        }
    }

    private fun initializeServices() {
        speechRecognitionHelper = SpeechRecognitionHelper(this) { text ->
            binding.inputText.setText(text)
            stopSpeechRecognition()
            // Auto-translate after speech input
            translateText()
        }

        translationService = TranslationService()
    }

    private fun showLanguageDialog(isSource: Boolean) {
        val languages = Language.values()
        val languageNames = languages.map { it.displayName }.toTypedArray()
        val currentIndex = if (isSource) {
            languages.indexOf(sourceLanguage)
        } else {
            languages.indexOf(targetLanguage)
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(if (isSource) R.string.select_source_language else R.string.select_target_language)
            .setSingleChoiceItems(languageNames, currentIndex) { dialog, which ->
                val selectedLanguage = languages[which]
                if (isSource) {
                    sourceLanguage = selectedLanguage
                    binding.sourceLanguageText.setText(selectedLanguage.displayName)
                } else {
                    targetLanguage = selectedLanguage
                    binding.targetLanguageText.setText(selectedLanguage.displayName)
                }
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun swapLanguages() {
        val temp = sourceLanguage
        sourceLanguage = targetLanguage
        targetLanguage = temp

        binding.sourceLanguageText.setText(sourceLanguage.displayName)
        binding.targetLanguageText.setText(targetLanguage.displayName)

        // Swap text content
        val inputText = binding.inputText.text.toString()
        val translatedText = binding.translatedText.text.toString()
        binding.inputText.setText(translatedText)
        binding.translatedText.setText(inputText)
    }

    private fun checkPermissionAndStartSpeechRecognition() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                startSpeechRecognition()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    private fun startSpeechRecognition() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            isListening = true
            binding.voiceButton.text = getString(R.string.listening)
            binding.voiceButton.setIconResource(R.drawable.ic_pause)
            speechRecognitionHelper.startListening(sourceLanguage)
        } else {
            Toast.makeText(
                this,
                getString(R.string.error_speech),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun stopSpeechRecognition() {
        isListening = false
        binding.voiceButton.text = getString(R.string.tap_to_speak)
        binding.voiceButton.setIconResource(R.drawable.ic_mic)
        speechRecognitionHelper.stopListening()
    }

    private fun translateText() {
        val inputText = binding.inputText.text.toString().trim()
        
        if (inputText.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_no_text), Toast.LENGTH_SHORT).show()
            return
        }

        if (sourceLanguage == targetLanguage) {
            binding.translatedText.setText(inputText)
            return
        }

        binding.progressBar.visibility = android.view.View.VISIBLE
        binding.translateButton.isEnabled = false

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val translatedText = withContext(Dispatchers.IO) {
                    translationService.translate(
                        text = inputText,
                        sourceLang = sourceLanguage.code,
                        targetLang = targetLanguage.code
                    )
                }
                
                binding.translatedText.setText(translatedText)
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.error_translation),
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                binding.progressBar.visibility = android.view.View.GONE
                binding.translateButton.isEnabled = true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognitionHelper.destroy()
    }
}
