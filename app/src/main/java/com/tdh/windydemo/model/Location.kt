package com.tdh.windydemo.model

data class Location(
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val state: String,
    var isSelected: Boolean = false
)