package com.naji.pokemon.model

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.naji.pokemon.api.Pokemon


@Dao
interface PikacuDao {

    @Query("SELECT * FROM pokemon WHERE name LIKE '%' || :sub || '%'")
    fun getAllNamesByName(sub: String): DataSource.Factory<Int, Pokemon>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(names: List<Pokemon>)
}