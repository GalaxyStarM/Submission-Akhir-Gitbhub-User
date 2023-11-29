package id.ac.unri.androidfundamental.data.retrofit

import id.ac.unri.androidfundamental.data.response.DetailUserResponse
import id.ac.unri.androidfundamental.data.response.GithubResponse
import id.ac.unri.androidfundamental.data.response.ItemsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    fun getUser(): Call<List<ItemsItem>>

    @GET("search/users")
    fun searchForUser(@Query("q") q: String) : Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<ItemsItem>>

}