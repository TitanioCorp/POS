package com.titaniocorp.pos.app.ui.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.titaniocorp.pos.app.model.Category

class CategorySpinnerAdapter(
    context: Context,
    @LayoutRes private val layoutResource: Int,
    @LayoutRes private val layoutDropDownResource: Int,
    private val list: List<Category>):
    ArrayAdapter<Category>(context, layoutResource, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return (LayoutInflater.from(context).inflate(layoutDropDownResource, parent, false) as TextView).also {
            it.text = list[position].name
        }
    }

    private fun createViewFromResource(position: Int, parent: ViewGroup?): View{
        return (LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView).also {
            it.text = list[position].name
        }
    }
}