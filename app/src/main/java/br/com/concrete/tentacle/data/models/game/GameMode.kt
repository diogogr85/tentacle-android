package br.com.concrete.tentacle.data.models.game

import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.utils.GAME_MODE_CO_OPERATIVE
import br.com.concrete.tentacle.utils.GAME_MODE_MMO
import br.com.concrete.tentacle.utils.GAME_MODE_MULTI_PLAYER
import br.com.concrete.tentacle.utils.GAME_MODE_SINGLE_PLAYER
import br.com.concrete.tentacle.utils.GAME_MODE_SPLIT_SCREEN
import com.google.gson.annotations.SerializedName

data class GameMode(
    @SerializedName("slug")
    val slug: String?,
    @SerializedName("name")
    val name: String?
) {

    fun getIconResource(): Int? {
        var icon: Int? = null
        slug?.let {
            icon = when (it) {
                    GAME_MODE_SINGLE_PLAYER -> R.drawable.ic_game_mode_single_player
                    GAME_MODE_MULTI_PLAYER -> R.drawable.ic_game_mode_multiplayer
                    GAME_MODE_CO_OPERATIVE -> R.drawable.ic_game_mode_coperative
                    GAME_MODE_MMO -> R.drawable.ic_game_mode_mmo
                    GAME_MODE_SPLIT_SCREEN -> R.drawable.ic_game_mode_split_screen
                    else -> null
            }
        }
        return icon
    }
}