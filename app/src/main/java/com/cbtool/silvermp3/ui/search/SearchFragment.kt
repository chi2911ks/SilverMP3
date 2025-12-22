package com.cbtool.silvermp3.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import androidx.recyclerview.widget.GridLayoutManager
import com.cbtool.silvermp3.MainActivity
import com.cbtool.silvermp3.adapter.GenreAdapter
import com.cbtool.silvermp3.databinding.FragmentSearchBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    fun init(){
        (activity as MainActivity).setSelectedItemId()
        searchViewModel.genres.observe(viewLifecycleOwner){
            genres ->
            binding.genresRc.adapter = GenreAdapter(genres)
        }
        binding.genresRc.layoutManager = GridLayoutManager(requireContext(), 2)
        searchViewModel.getGenres()

    }
    companion object {
        @JvmStatic
        val instance: SearchFragment by lazy { SearchFragment() }

        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}