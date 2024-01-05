package com.example.shopmatemobile.model

class OrderProduct(val id: String,
                   val title: String,
                   val thumbnail: String,
                   val grade: Double,
                   val price: Double,
                   var count: Int){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderProduct

        if (id != other.id) return false
        if (title != other.title) return false
        if (thumbnail != other.thumbnail) return false
        if (grade != other.grade) return false
        if (price != other.price) return false
        return count == other.count
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + thumbnail.hashCode()
        result = 31 * result + grade.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + count
        return result
    }
}