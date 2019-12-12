@file:Suppress("UNCHECKED_CAST")

package com.naji.pokemon.presenter

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.naji.pokemon.splash.SplashActivityPresenter
import com.naji.pokemon.model.PikacuDao
import com.naji.pokemon.api.PikacuServer


class SplashPresenterFactory(
    private val server: PikacuServer,
    private val dao: PikacuDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashActivityPresenter::class.java)) {
            return SplashActivityPresenter(server, dao) as T
        }
        throw IllegalArgumentException("Unknown SplashActivityPresenter class")
    }
}

class PokemonPresenterFactory(
        private val dao: PikacuDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivityPresenter::class.java)) {
            return ActivityPresenter(dao) as T
        }
        throw IllegalArgumentException("Unknown ActivityPresenter class")
    }
}
