package com.example.mykart.database


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mykart.classes.Address
import com.example.mykart.classes.Name
import com.google.gson.annotations.SerializedName


@Entity(tableName = "user_details")
data class UserDetails(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val email: String,
    val username: String,
    val password: String,
    @Embedded val name: Name,
    @Embedded val address: Address,
    val phone: String
)

@Entity(tableName = "product_list")
data class ProductList(
    @PrimaryKey(autoGenerate = false) @SerializedName("id") val id: Int,

    @SerializedName("title") val title: String,

    @SerializedName("price") val price: Float,

    @SerializedName("description") val description: String,

    @SerializedName("category") val category: String,

    @SerializedName("image") val image: String,

    @SerializedName("rating") @Embedded val rating: Rating
)


data class Rating(
    @SerializedName("rate") val rate: Float,

    @SerializedName("count") val count: Int
)

@Entity(tableName = "cart")
data class Cart(
    val name: String,
    @PrimaryKey(autoGenerate = false)
    val productId: Int,
    var qty :Int
)
@Entity(tableName = "address")
data class MultiAddress(
    val name : String,
    val recieverName : String,
    @PrimaryKey(autoGenerate = false)
    val address : String,
    var isSelected : Boolean = false
)

@Entity(tableName = "orders")
data class Orders(
    val name : String,
    @PrimaryKey(autoGenerate = false)
    val orderId : Int,
    val itemTotal : Float,
    val item : String,
    val dateTime : String
)








