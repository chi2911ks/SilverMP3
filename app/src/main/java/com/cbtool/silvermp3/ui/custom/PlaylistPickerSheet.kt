package com.cbtool.silvermp3.ui.custom

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbtool.silvermp3.R
import com.cbtool.silvermp3.adapter.AddPlaylistAdapter
import com.cbtool.silvermp3.data.model.LibraryItem
import com.cbtool.silvermp3.data.model.Playlist
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.databinding.SheetPlaylistPickerBinding
import com.cbtool.silvermp3.interfaces.OnClickAddPlaylist
import com.cbtool.silvermp3.ui.library.LibraryViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class PlaylistPickerSheet : BottomSheetDialogFragment() {
    private var _binding: SheetPlaylistPickerBinding? = null
    private val binding get() = _binding!!
    private lateinit var song: Song
    private val viewModel: LibraryViewModel by activityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        song = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_SONG, Song::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(ARG_SONG)
        })!!
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheet =
                (dialog as? com.google.android.material.bottomsheet.BottomSheetDialog)
                    ?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundResource(R.drawable.bg_bottom_sheet)
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SheetPlaylistPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerPlaylists.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getPlaylists()
        viewModel.libItems.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                setAdapter(it, viewModel.containsPlaylist(song.id))
            }


        }

    }
    fun setAdapter(items: List<LibraryItem>, songInPlaylists: List<String>){
        binding.recyclerPlaylists.adapter =
            AddPlaylistAdapter(items, songInPlaylists, object : OnClickAddPlaylist{
                override fun playlist(playlist: Playlist, isAdd: Boolean) {
                    if (isAdd)
                        viewModel.addSongToPlaylist(playlist.id, song)
                    else
                        viewModel.removeSongFromPlaylist(playlist.id, song.id)
                }
                override fun favourite(isAdd: Boolean) {
                    if (isAdd)
                        viewModel.addSongToFavourite(song)
                    else
                        viewModel.removeSongFromFavourite(song.id)

                }

            })
    }
    companion object {
        const val TAG = "ModalPlayList"
        const val ARG_SONG = "SONG"

        @JvmStatic
        fun newInstance(song: Song) = PlaylistPickerSheet().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_SONG, song)
            }
        }

    }
}