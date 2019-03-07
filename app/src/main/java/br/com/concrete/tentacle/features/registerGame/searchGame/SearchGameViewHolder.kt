package br.com.concrete.tentacle.features.registerGame.searchGame

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.extensions.getPartOfDate
import br.com.concrete.tentacle.extensions.loadImageUrl
import br.com.concrete.tentacle.extensions.progress
import br.com.concrete.tentacle.extensions.toDate
import br.com.concrete.tentacle.utils.IMAGE_SIZE_TYPE_COVER_SMALL
import br.com.concrete.tentacle.utils.Utils
import kotlinx.android.synthetic.main.game_view_header_layout.view.*
import kotlinx.android.synthetic.main.item_game_search.view.horizontalLine
import java.util.Calendar

class SearchGameViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {

    companion object {
        fun callBack(holder: RecyclerView.ViewHolder, game: Game, listener: (Game) -> Unit) {

            if (holder is SearchGameViewHolder) {
                if (game._id == Game.ID_EMPTY_GAME) {
                    visibleViews(holder, false)
                } else {
                    with(holder.item) {
                        game.cover?.imageId?.let { imageId ->
                            ivGameCover.loadImageUrl(
                                Utils.assembleGameImageUrl(
                                    IMAGE_SIZE_TYPE_COVER_SMALL,
                                    imageId
                                )
                            )
                        }

                        tvGameName.text = game.name
                        game.rating?.let {
                            rbGame.progress(it)
                        }
                        game.releaseDate?.let {
                            tvGameReleaseYear.text = it.toDate().getPartOfDate(Calendar.YEAR)
                        }

                        setOnClickListener {
                            listener(game)
                        }
                        groupStatus.visibility = View.GONE
                        visibleViews(holder, true)
                    }
                }
            }
        }

        private fun visibleViews(holder: SearchGameViewHolder, isVisible: Boolean) {
            val state = if (isVisible) View.VISIBLE else View.INVISIBLE

            with(holder.item) {
                ivGameCover.visibility = state
                tvGameName.visibility = state
                rbGame.visibility = state
                tvGameReleaseYear.visibility = state
                horizontalLine.visibility = state
            }
        }
    }
}