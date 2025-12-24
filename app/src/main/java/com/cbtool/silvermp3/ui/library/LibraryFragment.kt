package com.cbtool.silvermp3.ui.library

import android.os.Bundle
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
import com.cbtool.silvermp3.ui.custom.CreatePlayListDialog
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
        init()
    }
    fun init(){
        (activity as MainActivity).setSelectedItemId()
        binding.playListsRc.layoutManager = LinearLayoutManager(requireContext())
        libraryViewModel.libItems.observe(viewLifecycleOwner){
            binding.playListsRc.adapter = LibraryAdapter(it, object: OnClickPlaylist{
                override fun onClickPlaylist(playlist: Playlist) {
                    (activity as MainActivity).navigateTo(PlaylistFragment.newInstance(playlist))
                }
                override fun onClickFavourite() {
                    (activity as MainActivity).navigateTo(FavouriteFragment.newInstance())
                }

            })
        }
        libraryViewModel.getPlaylists()
        binding.addPlayListBtn.setOnClickListener {
            CreatePlayListDialog().show(requireActivity().supportFragmentManager, "AddPlayListDialog")
        }
    }
    companion object {
        @JvmStatic
        val instance: LibraryFragment by lazy { LibraryFragment() }

        @JvmStatic
        fun newInstance() = LibraryFragment()
    }
}