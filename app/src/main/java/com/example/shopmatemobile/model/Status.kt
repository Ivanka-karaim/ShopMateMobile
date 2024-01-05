package com.example.shopmatemobile.model

class Status(val id:Int, val title:String, val image:Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Status

        if (id != other.id) return false
        if (title != other.title) return false
        return image == other.image
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + image.hashCode()
        return result
    }
}