package com.example.mykart.database

import androidx.room.Insert
import androidx.room.Dao
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import retrofit2.http.DELETE

@Dao
interface Dao {
    @Insert(onConflict = IGNORE)
    fun addUserToDataBase(addUser: UserDetails)

    @Insert(onConflict = REPLACE)
    fun addProductToCart(cart: Cart)

    @Insert(onConflict = REPLACE)
    fun addAddress(address: MultiAddress)

    @Insert(onConflict = REPLACE)
    fun addAddressAll(address: List<MultiAddress>)

    @Insert(onConflict = REPLACE)
    fun addOrder(order: Orders)

    @Query("SELECT * FROM orders WHERE name =:name")
    fun allOrders(name: String) : List<Orders>

    @Query("DELETE FROM cart WHERE productId =:id")
    fun deleteProductFromCart(id :Int)

    @Query("DELETE FROM cart WHERE name =:name")
    fun deleteUserCart(name: String)

    @Query("SELECT * FROM cart WHERE name =:name")
    fun getCart(name: String) : List<Cart>

    @Query("SELECT * FROM cart")
    fun getAllCart() : List<Cart>

    @Query("SELECT * FROM address WHERE name =:name")
    fun getAllAddress(name :String) : List<MultiAddress>

    @Update
    fun updateAddress(name :MultiAddress)

    @Query("SELECT productId FROM cart")
    fun getAllCartId() : List<Int>

    @Insert(onConflict = REPLACE)
    fun insertAllUsers(users: List<UserDetails>)

    @Query("SELECT * FROM user_details")
    fun getAllUsers() : List<UserDetails>

    @Query("SELECT * FROM user_details WHERE username =:name")
    fun getUserDetails(name : String) : UserDetails

    @Query("SELECT * FROM product_list WHERE id =:id")
    fun getProduct(id : Int) : ProductList

    @Insert(onConflict = REPLACE)
    fun addAllProduct(products : List<ProductList>)

    @Query("SELECT * FROM product_list")
    fun getAllProducts() : List<ProductList>


}