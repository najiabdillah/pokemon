package com.naji.pokemon.room

import com.naji.pokemon.api.PokemonRaw


data class Repository(
        val count: Int,
        val previous: String?,
        val next: String?,
        val results: List<PokemonRaw>
)