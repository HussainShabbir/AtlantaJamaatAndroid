package com.atlantajamaat.app.platform

import android.os.Build

actual fun platform(): String {
    return "Android ${Build.VERSION.RELEASE}"
}