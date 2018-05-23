package com.github.databinding.listviewexample.customview

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.squareup.picasso.Picasso

/**
 * Custom Image View to load image from URL
 */
class CustomImageView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {

    fun setImageURL(url: String) {
        Picasso.with(context).load(url).into(this)
    }
}