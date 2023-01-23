package com.bikeshare.vhome.util

import android.view.View

const val BASE_URL = "https://api.innoway.vn"
const val APP_KEY = "nlaDOC8uS6Xn7L0JIcPD"
const val APP_SECRET = "yKeMoImiHp9DUXxoGpERza31xSyCWunW"
const val QUERY_PAGE_SIZE = 10

fun View.enable(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) 1f else 0.5f
}
