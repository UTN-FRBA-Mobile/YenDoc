package ar.yendoc.core

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ar.yendoc.ui.MapFragment
import ar.yendoc.ui.PhotosFragment
import ar.yendoc.ui.VisitFragment

class TabsAdapter(context: Context, fm: FragmentManager?, totalTabs: Int) :
    FragmentPagerAdapter(fm!!) {
    private val myContext: Context
    var totalTabs: Int

    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> {
                MapFragment()
            }
            2 -> {
                PhotosFragment()
            }
            else ->
                VisitFragment()
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }

    init {
        myContext = context
        this.totalTabs = totalTabs
    }
}