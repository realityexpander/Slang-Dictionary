package com.realityexpander.dictionary.core.util

fun String.lettersOnly(): String = this.filter { it.isLetter() }