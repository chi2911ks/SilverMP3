package com.cbtool.silvermp3.ui.library

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbtool.silvermp3.MainActivity
import com.cbtool.silvermp3.adapter.SongAdapter
import com.cbtool.silvermp3.databinding.FragmentFavouriteBinding
import com.cbtool.silvermp3.ui.custom.SongOptionsSheet
import com.cbtool.silvermp3.ui.player.PlayerFragment
import com.cbtool.silvermp3.ui.player.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FavouriteFragment : Fragment() {
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!
    private val favouriteViewModel: FavouriteViewModel by activityViewModel()
    private val playerViewModel: PlayerViewModel by activityViewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    fun init(){
        (activity as MainActivity).setSelectedItemId()
        Log.d("FavouriteFragment", "init: getSongs")
        favouriteViewModel.getSongs()
        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()

        }

        binding.rvSongs.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val adapter = SongAdapter(
            onItemClick = { song ->
                playerViewModel.setSongs(song)
                (activity as MainActivity).navigateTo(PlayerFragment.newInstance())
            },
            moreClick = { song ->
            val bottomSheet = SongOptionsSheet.newInstance(song, "favourites")
            bottomSheet.show(requireActivity().supportFragmentManager, "BottomSheetSong")
        })
        binding.rvSongs.adapter = adapter
        favouriteViewModel.song.observe(viewLifecycleOwner){
            binding.tvCount.text = "${it.size} bài hát"
            adapter.submitList(it)
        }


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        @JvmStatic
        fun newInstance() = FavouriteFragment()
    }
}