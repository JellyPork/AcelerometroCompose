package com.argent.acelerometrocompose.ktor.dto

import kotlinx.serialization.Serializable

@Serializable
data class Root(
    val data: List<Instrument>,
)