package com.naji.pokemon.room

import android.app.Application
import com.squareup.picasso.LruCache
import com.squareup.picasso.Picasso

@Suppress("unused")
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Picasso.setSingletonInstance(
                Picasso.Builder(this)
                        .memoryCache(LruCache(this))
                        .build()
        )
    }

}