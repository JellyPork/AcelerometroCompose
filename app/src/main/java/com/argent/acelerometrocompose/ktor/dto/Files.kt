package com.argent.acelerometrocompose.ktor.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.nio.Buffer


@Serializable
data class FileUploadRequest(
    val id: String,
    val file: String
)
@Serializable
data class GenericFile(
    val type: String,
    val data: List<Int>
)
@Serializable
data class FileUploadResponse(
    val id: String,
    val file: GenericFile,
    val v: Int
)


