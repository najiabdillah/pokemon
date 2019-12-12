package com.naji.pokemon.presenter

import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import com.naji.pokemon.model.PikacuRepository
import com.naji.pokemon.model.PikacuDao
import com.naji.pokemon.api.Pokemon
import com.naji.pokemon.api.PikacuServer
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers

class ActivityPresenter(dao: PikacuDao) : ViewModel() {

    private val mRepo = PikacuRepository(PikacuServer.get(), dao)

    fun queryPagingPokemon(name: String): Flowable<PagedList<Pokemon>> {
        return mRepo.getPhotosPerCategoryPaging(name)
                .observeOn(AndroidSchedulers.mainThread())
    }

}