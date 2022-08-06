package com.infbyte.aeon.ui.adapters

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.infbyte.aeon.R

class PagerAdapter(private val fragments: List<Fragment>, owner: FragmentActivity): FragmentStateAdapter(owner) {

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount() = fragments.size

    companion object{

        @StringRes
         val TAB_TITLES = arrayOf(
            R.string.all_songs,
            R.string.playlists,
            R.string.artists,
            R.string.albums,
            R.string.folders
        )
    }
}