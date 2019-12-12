package com.naji.pokemon.model

import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import android.util.Log
import com.naji.pokemon.api.Pokemon
import com.naji.pokemon.api.PokemonRaw
import com.naji.pokemon.room.Repository
import com.naji.pokemon.api.PikacuServer
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class PikacuRepository(private val server: PikacuServer, private val dao: PikacuDao) {

    companion object {
        private const val TAG = "PikacuRepository"
        private const val PAGE_SIZE = 20
        private const val ENABLE_PLACEHOLDERS = true
    }

    fun fetchProducts(): Flowable<Repository> {
        return server.getAllPokemon(1000)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { callWrapper ->
                    Log.d(TAG, "fetchProduct on ${Thread.currentThread().name}")
                    insertAll(callWrapper.results)
                }
    }

    private fun insertAll(list: List<PokemonRaw>?) {
        if (list != null && list.isNotEmpty()) {
            Observable.fromIterable(list.asIterable())
                    .subscribeOn(Schedulers.computation())
                    .map { pRaw ->
                        Pokemon.makePokemon(pRaw)
                    }
                    .filter { pokemon -> pokemon.id < 1000 }
                    .toList()
                    .observeOn(Schedulers.io())
                    .doOnSuccess {
                        Log.d(TAG, "Pokemon register into DB ${it.size}")
                        dao.insertAll(it)
                    }
                    .subscribe()
        }

    }

    fun getPhotosPerCategoryPaging(name: String): Flowable<PagedList<Pokemon>> {
        return RxPagedListBuilder(
                dao.getAllNamesByName(name),
                pagedListConfig
        ).buildFlowable(BackpressureStrategy.LATEST)

    }

    private val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(ENABLE_PLACEHOLDERS)
            .build()

}