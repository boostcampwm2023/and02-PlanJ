// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.plugin.hilt.android) apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.5.3" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
}