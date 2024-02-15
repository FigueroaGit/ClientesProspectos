package com.concredito.clientes.util

object Constants {
    const val BASE_URL = "http://10.0.2.2:8080/api/"
    const val EMPTY_STRING = ""
    const val SPLIT_DELIMITER = ", "
    const val PHONE_URI = "tel:"
    const val MESSAGE_URI = "sms:"
    const val ONE_LINE = 1
    const val TWO_LINES = 2
    const val MULTILINE = 5
    const val FIRST = 0
    const val SECOND = 1
    const val THIRD = 2
    const val MAX_CHARACTERS_BY_NAME = 20
    const val MAX_CHARACTERS_BY_ADDRESS = 30
    const val MAX_CHARACTERS_BY_NUMBER_ADDRESS = 5
    const val MAX_CHARACTERS_BY_ZIP_CODE = 5
    const val MAX_CHARACTERS_BY_PHONE_NUMBER = 10
    const val MAX_CHARACTERS_BY_RFC = 13
    const val MAX_CHARACTERS_BY_OBSERVATIONS = 150
    const val DELAY_TIME_TWO_SECONDS = 2000L
    const val LETTER_TILE_FONT_SIZE = 20
    const val LETTER_TILE_FONT_SIZE_2X = 24
    const val LETTER_TILE_FONT_SIZE_3X = 36
    const val LETTER_TILE_FONT_SIZE_4X = 80
    val LIST_OF_CHARACTERS_WITH_ACCENT =
        listOf('á', 'é', 'í', 'ó', 'ú', 'ü', 'ñ', 'Á', 'É', 'Í', 'Ó', 'Ú', 'Ü', 'Ñ')
    val LIST_OF_SPECIAL_CHARACTERS = listOf('/', '?', '.', ',', '&', '-')
}
