package com.naji.pokemon.adapter

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.naji.pokemon.R
import com.naji.pokemon.api.Pokemon
import com.squareup.picasso.Picasso

class RepositoryListAdapter : PagedListAdapter<Pokemon, RepositoryListAdapter.PokemonVH>(
        object : DiffUtil.ItemCallback<Pokemon>() {

            override fun areItemsTheSame(oldItem: Pokemon?, newItem: Pokemon?): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Pokemon?, newItem: Pokemon?): Boolean {
                return oldItem == newItem
            }
        }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonVH {
        return PokemonVH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_pikacu,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PokemonVH, position: Int) {
        val pokemon = getItem(position)
        pokemon?.let {
            holder.name.text = pokemon.name
            holder.id.text = pokemon.id.toString()
            Picasso.get()
                    .load(pokemon.url)
                    .placeholder(R.drawable.ic_logo_pokemon_black)
                    .error(R.drawable.ic_logo_pokemon_black)
                    .into(holder.sprite)
        }
    }

    class PokemonVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: AppCompatTextView = itemView.findViewById(R.id.pokemon_name)
        val id: AppCompatTextView = itemView.findViewById(R.id.pokemon_id)
        val sprite: AppCompatImageView = itemView.findViewById(R.id.pokemon_image)
    }
}