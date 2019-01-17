package br.com.concrete.tentacle.data.network

import br.com.concrete.tentacle.data.models.BaseModel
import br.com.concrete.tentacle.data.models.MediaRequest
import br.com.concrete.tentacle.data.models.MediaResponse
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("/media")
    fun getRegisteredGames(@Query("mineOnly") mineOnly: Boolean): Observable<BaseModel<MediaResponse>>

    @POST("/media")
    fun registerMedia(@Body media: MediaRequest): Completable
}