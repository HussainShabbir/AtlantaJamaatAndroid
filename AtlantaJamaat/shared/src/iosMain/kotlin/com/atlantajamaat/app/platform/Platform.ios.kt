package com.atlantajamaat.app.platform
import platform.UIKit.UIDevice

actual fun platform(): String {
    return "iOS ${UIDevice.currentDevice.model}"
}