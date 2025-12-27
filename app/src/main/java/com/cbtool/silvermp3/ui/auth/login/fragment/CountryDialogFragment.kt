package com.cbtool.silvermp3.ui.auth.login.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbtool.silvermp3.R
import com.cbtool.silvermp3.adapter.CountryAdapter
import com.cbtool.silvermp3.data.model.Country
import com.cbtool.silvermp3.databinding.FragmentCountryBinding
import org.xmlpull.v1.XmlPullParser

class CountryDialogFragment: DialogFragment() {
    private var _binding: FragmentCountryBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val countryList: List<Country> = loadCountriesFromXml()
        val adapter = CountryAdapter(countryList) { country ->
            parentFragmentManager.setFragmentResult(
                "countryRequestKey",
                bundleOf(
                    "name" to country.name,
                    "code" to country.code
                )
            )
            dismiss()
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.closeBtn.setOnClickListener { dismiss() }
        binding.searchInput.addTextChangedListener { text ->
            adapter.filterList(text.toString())
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun loadCountriesFromXml(): List<Country> {
        val countries = mutableListOf<Country>()
        val parser = resources.getXml(R.xml.country_code)

        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.name == "country") {
                val name = parser.getAttributeValue(null, "name")
                val code = parser.getAttributeValue(null, "code")
                countries.add(Country(name, code))
            }
            eventType = parser.next()
        }
        return countries
    }
}