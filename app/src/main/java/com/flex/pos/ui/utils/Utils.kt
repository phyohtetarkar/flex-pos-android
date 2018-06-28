package com.flex.pos.ui.utils

import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.support.v4.app.FragmentActivity
import android.support.v7.preference.PreferenceManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import com.flex.pos.R

class Utils {

    companion object {
        @JvmStatic
        fun getSelectedLanguage(context: Context?): Array<String> {
            val pref = PreferenceManager.getDefaultSharedPreferences(context)

            return when (pref.getString("p_app_language", "english")) {
                "myanmar(unicode)" -> arrayOf("my", "MM")
                "myanmar(zawgyi)" -> arrayOf("my", "ZG")
                else -> arrayOf("en", "US")
            }
        }

        fun initSearchView(activity: FragmentActivity?, menu: Menu?): SearchView? {
            val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as? SearchManager
            val searchView = menu?.findItem(R.id.action_search)?.actionView as? SearchView
            searchView?.setSearchableInfo(searchManager?.getSearchableInfo(activity.componentName))
            val v = searchView?.findViewById<View>(android.support.v7.appcompat.R.id.search_plate)
            v?.setBackgroundColor(Color.TRANSPARENT)

            return searchView
        }
    }

}