package com.cbtool.silvermp3.ui.custom

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.cbtool.silvermp3.R
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.databinding.SheetSongOptionsBinding
import com.cbtool.silvermp3.ui.library.FavouriteViewModel
import com.cbtool.silvermp3.ui.library.LibraryViewModel
import com.cbtool.silvermp3.ui.library.PlaylistViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SongOptionsSheet: BottomSheetDialogFragment() {
    private var _binding: SheetSongOptionsBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: SongOptionsViewModel by viewModel()
    private val libraryViewModel: LibraryViewModel by activityViewModel()
    private val favouriteViewModel: FavouriteViewModel by activityViewModel()
    private val playListViewModel: PlaylistViewModel by activityViewModel()
    private var _song: Song? = null
    private val song get() = _song!!
    private var playList_Id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            _song = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getParcelable(ARG_SONG, Song::class.java)
            } else {
                @Suppress("DEPRECATION")
                getParcelable(ARG_SONG)
            }
            playList_Id = getString(ARG_PLAYLIST_ID)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SheetSongOptionsBinding.inflate(inflater, container, false)
        return binding.root
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding.deleteInPlaylist.visibility = if (!playList_Id.isNullOrEmpty()) View.VISIBLE else View.GONE
        binding.addPlaylistBtn.text = if (!playList_Id.isNullOrEmpty()) "Thêm vào danh sách phát khác" else "Thêm vào danh sách phát"
        viewmodel.isFavourite.observe(viewLifecycleOwner){
            binding.favouriteBtn.isSelected = it
            binding.favouriteBtn.apply {
                text = if (isSelected) "Đã yêu thích" else "Yêu thích"
            }
        }
        viewmodel.checkFavourite(song)
        song.apply {
            binding.tvTitle.text = title
            binding.tvArtist.text = artistName
            Glide
                .with(requireContext())
                .load(coverUrl)
                .transform(CenterCrop(), RoundedCorners(10))
                .into(binding.imageCover)

        }
        binding.shareBtn.setOnClickListener {

        }
        binding.deleteInPlaylist.setOnClickListener {
            playList_Id?.let {
                if (it == "favourites"){
                    libraryViewModel.removeSongFromFavourite(song.id)
                    libraryViewModel.refreshFavouriteCount()
                    favouriteViewModel.getSongs()
                }
                else{
                    libraryViewModel.removeSongFromPlaylist(it,song.id)
                    playListViewModel.getSongs(it)
                }
                dismiss()


            }
        }
        binding.favouriteBtn.setOnClickListener {
            viewmodel.toggleFavourite(song)
            libraryViewModel.refreshFavouriteCount()
            if (playList_Id == "favourites") favouriteViewModel.getSongs()
        }
        binding.addPlaylistBtn.setOnClickListener {
            dismiss()
            PlaylistPickerSheet.newInstance(song).show(requireActivity().supportFragmentManager, PlaylistPickerSheet.TAG)
        }
        binding.artistBtn.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "BottomSheetSong"
        private const val ARG_SONG = "SONG"
        private const val ARG_PLAYLIST_ID = "PLAYLIST_ID"

        @JvmStatic
        fun newInstance(song: Song, playList_Id: String? = null) =
            SongOptionsSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_SONG, song)
                    putString(ARG_PLAYLIST_ID, playList_Id)
                }
            }


    }
}