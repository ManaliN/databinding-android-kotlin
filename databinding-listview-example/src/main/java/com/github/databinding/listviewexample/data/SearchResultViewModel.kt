package com.github.databinding.listviewexample.data

/**
 * Model for SearchResult
 * @author: manali
 */
data class SearchResultViewModel(
        val ownerAvatar: String,
        val repositoryName: String,
        val description: String,
        val numberOfStars: String
)