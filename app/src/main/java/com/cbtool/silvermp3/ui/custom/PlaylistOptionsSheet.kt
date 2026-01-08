package com.cbtool.silvermp3.ui.custom

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cbtool.silvermp3.data.model.Playlist
import com.cbtool.silvermp3.databinding.SheetPlaylistOptionsBinding
import com.cbtool.silvermp3.ui.library.LibraryViewModel

import com.cbtool.silvermp3.ui.library.UserPlaylistViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.getValue

class PlaylistOptionsSheet : BottomSheetDialogFragment() {
    private var _binding: SheetPlaylistOptionsBinding? = null
    private val binding get() = _binding!!
    private val playlistViewModel: UserPlaylistViewModel by activityViewModel()
    private val libraryViewModel: LibraryViewModel by activityViewModel()
    private var playlist: Playlist? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.apply {
            playlist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getParcelable(ARG_PLAYLIST, Playlist::class.java)
            } else {
                @Suppress("DEPRECATION")
                getParcelable(ARG_PLAYLIST)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SheetPlaylistOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding.tvTitle.text = playlist!!.title
        binding.deleteBtn.setOnClickListener {
            playlistViewModel.deletePlaylist(playlist!!.id)
            libraryViewModel.getPlaylists()
            requireActivity().onBackPressedDispatcher.onBackPressed()
            dismiss()
        }
        binding.detailBtn.setOnClickListener {
            DetailPlaylistSheet.newInstance(playlist!!)
                .show(requireActivity().supportFragmentManager, DetailPlaylistSheet.TAG)
            dismiss()
        }
        binding.downloadBtn.setOnClickListener {
            dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "BottomSheetPlaylist"

        private const val ARG_PLAYLIST = "PLAYLIST"

        fun newInstance(playlist: Playlist) = PlaylistOptionsSheet().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_PLAYLIST, playlist)
            }
        }

    }
}