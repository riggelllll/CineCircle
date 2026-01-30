package com.koniukhov.cinecirclex.feature.media.details.dialog

import android.app.Dialog
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import com.koniukhov.cinecirclex.feature.movie_details.R
import com.koniukhov.cinecirclex.feature.movie_details.databinding.DialogFullscreenVideoBinding

class FullscreenVideoDialog : DialogFragment() {

    private var _binding: DialogFullscreenVideoBinding? = null
    private val binding get() = _binding!!

    private var fullscreenView: View? = null
    private var exitFullscreenFunction: (() -> Unit)? = null
    private var originalOrientation: Int = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDialogStyle()
        saveAndSetOrientation()
    }

    private fun setupDialogStyle() {
        setStyle(STYLE_NO_TITLE, R.style.FullscreenDialogStyle)
    }

    private fun saveAndSetOrientation() {
        originalOrientation = requireActivity().requestedOrientation
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        setupDialogWindow(dialog)
        return dialog
    }

    private fun setupDialogWindow(dialog: Dialog) {
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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFullscreenVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addFullscreenVideoView()
    }

    private fun addFullscreenVideoView() {
        fullscreenView?.let { videoView ->
            binding.fullscreenVideoContainer.addView(videoView)
        }
    }

    override fun onDestroyView() {
        exitFullscreenFunction?.invoke()
        restoreOrientation()
        fullscreenView = null
        exitFullscreenFunction = null
        _binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        fullscreenView = null
        exitFullscreenFunction = null
        super.onDestroy()
    }

    private fun restoreOrientation() {
        requireActivity().requestedOrientation = originalOrientation
    }

    fun setFullscreenContent(view: View, exitFunction: () -> Unit) {
        this.fullscreenView = view
        this.exitFullscreenFunction = exitFunction
    }

    companion object {
        fun newInstance(fullscreenView: View, exitFunction: () -> Unit): FullscreenVideoDialog {
            val dialog = FullscreenVideoDialog()
            dialog.setFullscreenContent(fullscreenView, exitFunction)
            return dialog
        }
    }
}