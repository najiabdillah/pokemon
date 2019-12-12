package com.naji.pokemon.activity

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.widget.Toast
import com.naji.pokemon.R
import com.naji.pokemon.room.PikacuDatabase
import com.naji.pokemon.presenter.ActivityPresenter
import com.naji.pokemon.adapter.RepositoryListAdapter
import com.naji.pokemon.presenter.PokemonPresenterFactory
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_pikacu.*


class PikacuActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "PikacuActivity"
        private const val SAVE_LAYOUT_STATE = "save_layout_state"
    }

    private lateinit var mPokemonPresenterFactory: PokemonPresenterFactory
    private lateinit var mPokemonActivityPresenter: ActivityPresenter

    private val disposables = CompositeDisposable()

    private val mAdapter = RepositoryListAdapter()
    private val mLayoutManager by lazy { LinearLayoutManager(this) }
    private val mItemDecoration by lazy { DividerItemDecoration(this, DividerItemDecoration.VERTICAL) }
    private var restoredListState: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pikacu)

        mPokemonPresenterFactory = PokemonPresenterFactory(PikacuDatabase.getInstance(this).pokemonDao())
        mPokemonActivityPresenter = ViewModelProviders.of(this, mPokemonPresenterFactory).get(ActivityPresenter::class.java)

        recycler.adapter = mAdapter
        recycler.layoutManager = mLayoutManager

        loadList(searchView.query.toString())
    }

    override fun onStart() {
        super.onStart()

        recycler.addItemDecoration(mItemDecoration)
        recycler.setOnTouchListener { _, _ ->
            searchView.clearFocus()
            false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val text = newText ?: ""
                Log.d(TAG, "onQueryTextChange [$text]")
                loadList(text)
                return true
            }
        })

    }

    fun loadList(text: String) {
        disposables.clear()
        disposables.addAll(
                mPokemonActivityPresenter.queryPagingPokemon(text)
                        .subscribe(
                                {
                                    mAdapter.submitList(it)
                                    if (restoredListState != null) {
                                        Log.d(TAG, "Restore layout state")
                                        mLayoutManager.onRestoreInstanceState(restoredListState)
                                        restoredListState = null
                                    }
                                },
                                {
                                    Log.e(TAG, "Photo error $it")
                                    Toast.makeText(
                                            this@PikacuActivity,
                                            R.string.launcher_network_error,
                                            Toast.LENGTH_LONG
                                    ).show()
                                }
                        )
        )
    }

    override fun onStop() {
        disposables.clear()

        recycler.removeItemDecoration(mItemDecoration)
        super.onStop()
    }

    override fun onDestroy() {
        recycler.adapter = null
        recycler.layoutManager = null

        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(SAVE_LAYOUT_STATE, mLayoutManager.onSaveInstanceState())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            restoredListState = savedInstanceState.getParcelable(SAVE_LAYOUT_STATE)
        }
        super.onRestoreInstanceState(savedInstanceState)
    }


}
