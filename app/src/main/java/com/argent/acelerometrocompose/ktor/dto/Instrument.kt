package com.argent.acelerometrocompose.ktor.dto

import kotlinx.serialization.Serializable


@Serializable
data class Instrument(
    val _id: String,
    val nombre: String,
    val acronimo: String,
    val categoria: String,
    val items: Array<Item>,
    val createdAt: String,
    val updatedAt: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Instrument

        if (_id != other._id) return false
        if (nombre != other.nombre) return false
        if (acronimo != other.acronimo) return false
        if (categoria != other.categoria) return false
        if (!items.contentEquals(other.items)) return false
        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _id.hashCode()
        result = 31 * result + nombre.hashCode()
        result = 31 * result + acronimo.hashCode()
        result = 31 * result + categoria.hashCode()
        result = 31 * result + items.contentHashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + updatedAt.hashCode()
        return result
    }
}

