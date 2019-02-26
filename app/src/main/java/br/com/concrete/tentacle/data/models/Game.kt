package br.com.concrete.tentacle.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Game(
    val _id: String = String(),
    @SerializedName("name", alternate = ["title"])
    val name: String = String(),
    val createdBy: User,
    var createdAt: String,
    var updateAt: String

) : Serializable {

    companion object {

        const val ID_EMPTY_GAME = "-1"

        fun getEmptyGame(): Game = Game(
            ID_EMPTY_GAME,
            "",
            User(
                city = "",
                name = "",
                password = "",
                state = State(
                    "",
                    "",
                    ""
                )
            ),
            "",
            ""
        )
    }
}
