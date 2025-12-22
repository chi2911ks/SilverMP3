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
import com.cbtool.silvermp3.databinding.SongBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class BottomSheetSong : BottomSheetDialogFragment() {
    private var _binding: SongBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: BottomSheetSongViewModel by viewModel()
    private var song: Song? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        song = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("song", Song::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("song")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SongBottomSheetBinding.inflate(inflater, container, false)
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
        viewmodel.isFavourite.observe(viewLifecycleOwner){
            binding.favouriteBtn.isSelected = it
            binding.favouriteBtn.apply {
                text = if (isSelected) "Đã yêu thích" else "Yêu thích"
            }
        }
        viewmodel.checkFavourite(song!!)
        song!!.apply {
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
        binding.favouriteBtn.setOnClickListener {
            viewmodel.toggleFavourite(song!!)
        }
        binding.addPlaylistBtn.setOnClickListener {
            dismiss()
            BottomSheetPlayLists.newInstance(song!!).show(requireActivity().supportFragmentManager, BottomSheetPlayLists.TAG)
        }
        binding.artistBtn.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_SONG = "song"

        @JvmStatic
        fun newInstance(song: Song) =
            BottomSheetSong().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_SONG, song)

                }
            }


    }
}