package com.koniukhov.cinecircle.feature.media.details.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import coil3.load
import coil3.request.crossfade
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.IMAGE_URL_TEMPLATE
import com.koniukhov.cinecircle.feature.movie_details.R
import com.koniukhov.cinecircle.feature.movie_details.databinding.DialogFullscreenImageBinding

class FullscreenImageDialog : DialogFragment() {

    private var _binding: DialogFullscreenImageBinding? = null
    private val binding get() = _binding!!

    private var imagePath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDialogStyle()
        initImagePath()
    }

    private fun setupDialogStyle() {
        setStyle(STYLE_NO_TITLE, R.style.FullscreenDialogStyle)
    }

    private fun initImagePath() {
        imagePath = arguments?.getString(ARG_IMAGE_PATH) ?: ""
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.let { window ->
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val controller = WindowInsetsControllerCompat(window, window.decorView)
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFullscreenImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNavigationListener()
        loadImage()
    }

    private fun setupNavigationListener() {
        binding.toolbar.setNavigationOnClickListener {
            dismiss()
        }
    }

    private fun loadImage() {
        binding.progressBar.visibility = View.VISIBLE

        binding.fullscreenImage.load(IMAGE_URL_TEMPLATE.format(imagePath)) {
            crossfade(CROSS_FADE_DURATION)
            listener(
                onStart = {
                    binding.progressBar.visibility = View.VISIBLE
                },
                onSuccess = { _, _ ->
                    binding.progressBar.visibility = View.GONE
                },
                onError = { _, _ ->
                    binding.progressBar.visibility = View.GONE
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CROSS_FADE_DURATION = 300
        private const val ARG_IMAGE_PATH = "image_path"

        fun newInstance(imagePath: String): FullscreenImageDialog {
            return FullscreenImageDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE_PATH, imagePath)
                }
            }
        }
    }
}
