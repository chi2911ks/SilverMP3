package com.cbtool.silvermp3.ui.custom

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cbtool.silvermp3.data.model.Playlist
import com.cbtool.silvermp3.databinding.SheetPlaylistDetailBinding
import com.cbtool.silvermp3.ui.library.LibraryViewModel
import com.cbtool.silvermp3.ui.library.UserPlaylistViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.getValue

class DetailPlaylistSheet : BottomSheetDialogFragment() {
    private var _binding: SheetPlaylistDetailBinding? = null
    private val binding get() = _binding!!
    private var playlist: Playlist? = null
    private val playlistViewModel: UserPlaylistViewModel by activityViewModel()
    private val libraryViewModel: LibraryViewModel by activityViewModel()
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
        _binding = SheetPlaylistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlist?.let {
            binding.titleTv.setText(it.title)
            binding.descTv.setText(it.description)
        }

        binding.discardBtn.setOnClickListener {
            dismiss()
        }
        binding.saveBtn.setOnClickListener {
            if (binding.titleTv.text.toString().isEmpty()) return@setOnClickListener
            playlist?.apply {
                playlistViewModel.updatePlaylist(
                    id,
                    binding.titleTv.text.toString(),
                    binding.descTv.text.toString()
                )
                playlistViewModel.getPlaylist(id)
                libraryViewModel.getPlaylists()
            }

            dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "DetailPlaylistDialog"
        private const val ARG_PLAYLIST = "PLAYLIST"
        fun newInstance(playlist: Playlist) = DetailPlaylistSheet().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_PLAYLIST, playlist)
            }
        }

    }
}