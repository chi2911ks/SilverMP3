package com.cbtool.silvermp3.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cbtool.silvermp3.data.model.Country
import com.cbtool.silvermp3.databinding.ItemCountryBinding

class CountryAdapter (
    private val countryList: List<Country>,
    private val onItemClick: (Country) -> Unit,

) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {
    private var filteredCountries: List<Country> = countryList
    inner class CountryViewHolder(private val binding: ItemCountryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(country: Country) {
            binding.tvName.text = country.name
            binding.tvCode.text = country.code
            itemView.setOnClickListener { onItemClick(country) }
        }

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CountryViewHolder {
        val binding = ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding)
    }
    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = filteredCountries[position]
        holder.bindData(country)
    }

    override fun getItemCount(): Int = filteredCountries.size
    @SuppressLint("NotifyDataSetChanged")
    fun filterList(query: String) {
        filteredCountries = if (query.isEmpty()) {
            countryList
        } else {
            countryList.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }

}