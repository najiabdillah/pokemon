package com.naji.pokemon.splash

import com.naji.pokemon.model.PikacuRepository
import com.naji.pokemon.model.PikacuDao
import com.naji.pokemon.api.PikacuServer
import com.naji.pokemon.presenter.BasePresenter

/**
 * Presenter for Splash Activity
 *
 * @author Damien
 */
class SplashActivityPresenter(server: PikacuServer, dao: PikacuDao) : BasePresenter() {

    /**
     *  Repository to handler persistent and online data for Pokemon
     */
    private val mPokemonRepo = PikacuRepository(server, dao)

    /**
     * Fetch the data in order to initialized the application.
     */
    fun initializedData() {
        proceedToFetchData(
                mPokemonRepo.fetchProducts()
        )
    }
}
