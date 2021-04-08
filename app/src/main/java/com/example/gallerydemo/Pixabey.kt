package com.example.gallerydemo

import com.google.gson.annotations.SerializedName

data class Pixabey (
    val totalHits: Int,
    val hits: Array<PhotoItem>,
    val total: Int
    ){
    override fun equals(other: Any?): Boolean {
        if(this === other)return true
        if (javaClass != other?.javaClass)return false
        other as Pixabey

        if (totalHits != other.totalHits)return false
        if (!hits.contentEquals(other.hits))return false
        if (total != other.total)return false

        return true
    }

    override fun hashCode(): Int {
        var result: Int = totalHits
        result = 31 * result + hits.contentHashCode()
        result = 31 * result + total
        return result
    }

    override fun toString(): String {
        return totalHits.toString() + hits.toString() + total.toString()
    }
}

data class PhotoItem(
    @SerializedName("id")
    val photoId: Int,
    @SerializedName("webformatURL")
    val previewUrl: String,
    @SerializedName("largeImageURL")
    val fullUrl: String
)