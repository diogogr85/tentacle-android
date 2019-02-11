package br.com.concrete.tentacle.features.registerGame.searchGame

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.concrete.tentacle.base.BaseViewModel
import br.com.concrete.tentacle.data.models.Game
import br.com.concrete.tentacle.data.models.GameRequest
import br.com.concrete.tentacle.data.models.ViewStateModel
import br.com.concrete.tentacle.data.repositories.GameRepository
import br.com.concrete.tentacle.utils.Event

class SearchGameViewModel(
    private val gameRepository: GameRepository
) : BaseViewModel(),
    LifecycleObserver {

    private val viewSearchGame = MutableLiveData<ViewStateModel<ArrayList<Game?>>>()
    private val viewGame = MutableLiveData<Event<ViewStateModel<Game>>>()

    val game: LiveData<Event<ViewStateModel<Game>>>
        get() = viewGame

    fun searchGame(title: String) {
        viewSearchGame.postValue(ViewStateModel(ViewStateModel.Status.LOADING))
        disposables.add(obsSearchGames(title))
    }

    private fun obsSearchGames(title: String) =
        gameRepository.getSearchGames(title).subscribe({ base ->
            viewSearchGame.postValue(
                ViewStateModel(
                    status = ViewStateModel.Status.SUCCESS,
                    model = ArrayList(base.data.list)
                )
            )
        }, {
            viewSearchGame.postValue(
                ViewStateModel(
                    status = ViewStateModel.Status.ERROR,
                    errors = notKnownError(it)
                )
            )
        })

    fun registerNewGame(title: String) {
        viewGame.postValue(Event(ViewStateModel(ViewStateModel.Status.LOADING)))
        disposables.add(obsRegisterGame(title))
    }

    private fun obsRegisterGame(title: String) =
        gameRepository.registerNewGame(GameRequest(title)).subscribe({ base ->
            viewGame.postValue(
                Event(
                    ViewStateModel(
                        status = ViewStateModel.Status.SUCCESS,
                        model = base.data
                    )
                )
            )
        }, {
            viewGame.postValue(
                Event(
                    ViewStateModel(
                        status = ViewStateModel.Status.ERROR,
                        errors = notKnownError(it)
                    )
                )
            )
        })

    fun getSearchGame(): LiveData<ViewStateModel<ArrayList<Game?>>> = viewSearchGame
    fun getRegisteredGame(): LiveData<Event<ViewStateModel<Game>>> = viewGame

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}