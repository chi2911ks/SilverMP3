package com.cbtool.silvermp3.ui.library

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbtool.silvermp3.MainActivity
import com.cbtool.silvermp3.adapter.LibraryAdapter
import com.cbtool.silvermp3.interfaces.OnClickPlaylist
import com.cbtool.silvermp3.data.model.Playlist
import com.cbtool.silvermp3.databinding.FragmentLibraryBinding
import com.cbtool.silvermp3.ui.custom.CreatePlaylistDialog
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class LibraryFragment : Fragment() {
    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!
    private val libraryViewModel: LibraryViewModel by activityViewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("LibraryFragment", "LibraryViewModel: $libraryViewModel")
        init()
    }
    fun init(){
        (activity as MainActivity).setSelectedItemId()
        binding.playListsRc.layoutManager = LinearLayoutManager(requireContext())
        val adapter = LibraryAdapter(object: OnClickPlaylist{
            override fun onClickPlaylist(playlist: Playlist) {
                (activity as MainActivity).navigateTo(PlaylistFragment.newInstance(playlist))
            }
            override fun onClickFavourite() {
                (activity as MainActivity).navigateTo(FavouriteFragment.newInstance())
            }

        })
        binding.playListsRc.adapter = adapter
        libraryViewModel.libItems.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
        if (libraryViewModel.size == 0) {
            libraryViewModel.getPlaylists()
        }
        binding.addPlayListBtn.setOnClickListener {

            CreatePlaylistDialog.newInstance(libraryViewModel.size).show(requireActivity().supportFragmentManager, "CreatePlayListDialog")
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        @JvmStatic
        val instance: LibraryFragment by lazy { LibraryFragment() }

        @JvmStatic
        fun newInstance() = LibraryFragment()
    }
}