package com.anita_coding_challenge

data class SearchItemModel(
    val incomplete_results: Boolean?,
    val items: ArrayList<Item>?,
    val total_count: Int?
)