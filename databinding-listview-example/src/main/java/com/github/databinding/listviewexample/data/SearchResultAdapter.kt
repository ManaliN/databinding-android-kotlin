package com.github.databinding.listviewexample.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.github.databinding.listviewexample.databinding.SingleSearchElementBinding

/**
 * Search Result Adapter binds each search result elements to the list view
 * @author: manali
 */

class SearchResultAdapter(
        private val context: Context,
        private val searchItemListViewModel: List<SearchResultViewModel>
) : BaseAdapter() {

    override fun getCount(): Int = searchItemListViewModel.size

    override fun getItem(position: Int): Any = searchItemListViewModel[position]

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val searchBinding: SingleSearchElementBinding
        if (convertView == null) {
            searchBinding = SingleSearchElementBinding.inflate(LayoutInflater.from(context), parent, false)
            searchBinding.root.tag = searchBinding
        } else {
            searchBinding = convertView.tag as SingleSearchElementBinding
        }
        searchBinding. let {it.searchResultViewModel = getItem(position) as SearchResultViewModel }

        return searchBinding.root
    }
}