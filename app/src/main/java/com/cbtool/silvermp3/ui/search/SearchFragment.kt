package com.cbtool.silvermp3.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.cbtool.silvermp3.MainActivity
import com.cbtool.silvermp3.adapter.GenreAdapter
import com.cbtool.silvermp3.databinding.FragmentSearchBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by activityViewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("SearchFragment", "SearchViewModel: $searchViewModel")
        init()
    }

    fun init() {
        val adapter = GenreAdapter {
            (activity as MainActivity).navigateTo(GenreFragment.newInstance(it.name))
        }


        binding.genresRc.adapter = adapter
        (activity as MainActivity).setSelectedItemId()
        if (searchViewModel.genres.value.isNullOrEmpty()) {
            searchViewModel.getGenres()
        }
        searchViewModel.genres.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        binding.genresRc.layoutManager = GridLayoutManager(requireContext(), 2)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        val instance: SearchFragment by lazy { SearchFragment() }

        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}