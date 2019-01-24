package br.com.concrete.tentacle.features.loadmygames

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.concrete.tentacle.R
import br.com.concrete.tentacle.base.BaseAdapter
import br.com.concrete.tentacle.base.BaseFragment
import br.com.concrete.tentacle.data.models.ViewStateModel
import kotlinx.android.synthetic.main.fragment_game_list.list
import kotlinx.android.synthetic.main.list_custom.recyclerListView
import org.koin.android.viewmodel.ext.android.viewModel
import androidx.lifecycle.Observer
import br.com.concrete.tentacle.features.registerGame.RegisterGameHostActivity
import kotlinx.android.synthetic.main.list_custom.view.buttonAction
import kotlinx.android.synthetic.main.list_custom.view.recyclerListError
import kotlinx.android.synthetic.main.list_error_custom.view.buttonNameError

private const val REQUEST_CODE = 1

class LoadMyGamesFragment : BaseFragment() {

    private val viewModelLoadMyGames: LoadMyGamesViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_game_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initObserver() {
        viewModelLoadMyGames.getMyGames().observe(this, Observer { stateModel ->
            val medias = stateModel.model
            when (stateModel.status) {
                ViewStateModel.Status.SUCCESS -> {
                    medias?.let {
                        recyclerListView.setHasFixedSize(true)
                        val layoutManager = LinearLayoutManager(context)
                        recyclerListView.layoutManager = layoutManager

                        val recyclerViewAdapter = BaseAdapter(
                            medias,
                            R.layout.item_game,
                            { view ->
                                LoadMyGamesViewHolder(view)
                            }, { holder, element ->
                                LoadMyGamesViewHolder.callBack(holder = holder, element = element)
                            })

                        recyclerListView.adapter = recyclerViewAdapter
                    }
                    list.updateUi(medias)
                    list.setLoading(false)
                }

                ViewStateModel.Status.ERROR -> {
                    stateModel.errors?.let {
                        showError(it)
                    }
                    list.updateUi(medias)
                    list.setLoading(false)
                }
            }
        })
        lifecycle.addObserver(viewModelLoadMyGames)
    }

    private fun init() {
        initObserver()
        list.recyclerListError.buttonNameError.setOnClickListener {
            showRegisterGame()
        }

        list.buttonAction.setOnClickListener {
            showRegisterGame()
        }
    }

    private fun showRegisterGame(){
        startActivityForResult(Intent(context, RegisterGameHostActivity::class.java), REQUEST_CODE)
    }

    override fun getToolbarTitle(): Int {
        return R.string.toolbar_title_my_games
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        else super.onActivityResult(requestCode, resultCode, data)
    }
}
