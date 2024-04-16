package com.itis.delivery.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat

class ResManager(private val ctx: Context) {

    fun getString(@StringRes res: Int): String = ctx.resources.getString(res)

    fun getString(@StringRes res: Int, vararg args: Any?): String {
        return ctx.resources.getString(res, *args)
    }

    fun getDrawable(@DrawableRes res: Int): Drawable? {
        return ResourcesCompat.getDrawable(ctx.resources, res, ctx.theme)
    }
}