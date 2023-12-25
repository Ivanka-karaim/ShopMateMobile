package com.example.shopmatemobile.model

class BasketCheckbox(val isChecked: Boolean,
                     val id: String,
                     val title: String,
                     val thumbnail: String,
                     val grade: Double,
                     val price: Double,
                     val count: Int,
    ){
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = isChecked.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + thumbnail.hashCode()
        result = 31 * result + grade.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + count
        return result
    }

}
