package com.boostcamp.planj

fun String.getDate()
    = this.split("T").first()

fun String.getTime()
    = this.split("T").last()