@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "unused")

package com.verygoodsecurity.sdk.analytics.utils

import platform.Foundation.NSLog

actual object Logger {

    actual fun e(tag: String, message: String, throwable: Throwable?) {
        if (throwable != null) {
            NSLog("ERROR: [$tag] $message. Throwable: $throwable CAUSE ${throwable.cause}")
        } else {
            NSLog("ERROR: [$tag] $message")
        }
    }

    actual fun d(tag: String, message: String) {
        NSLog("DEBUG: [$tag] $message")
    }

    actual fun i(tag: String, message: String) {
        NSLog("INFO: [$tag] $message")
    }

    actual fun w(tag: String, message: String) {
        NSLog("WARN: [$tag] $message")
    }
}