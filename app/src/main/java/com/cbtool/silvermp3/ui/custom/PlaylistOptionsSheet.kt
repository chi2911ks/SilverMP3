package com.cbtool.silvermp3.ui.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cbtool.silvermp3.databinding.SheetPlaylistOptionsBinding

import com.cbtool.silvermp3.ui.library.PlaylistViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.getValue

class PlaylistOptionsSheet: BottomSheetDialogFragment() {
    private var _binding: SheetPlaylistOptionsBinding? = null
    private val binding get() = _binding!!
    private val playlistViewModel: PlaylistViewModel by activityViewModel()

    private var playlist_id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            playlist_id = getString(ARG_PLAYLIST_ID)
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
    private fun init(){
        binding.deleteBtn.setOnClickListener {
            playlistViewModel.deletePlaylist(playlist_id!!)
            requireActivity().onBackPressedDispatcher.onBackPressed()
            dismiss()
        }
    }
    companion object {
        const val TAG = "BottomSheetPlaylist"

        private const val ARG_PLAYLIST_ID = "PLAYLIST_ID"

        fun newInstance(playlist_id: String) = PlaylistOptionsSheet().apply {
            arguments = Bundle().apply {
                putString(ARG_PLAYLIST_ID, playlist_id)
            }
        }

    }
}