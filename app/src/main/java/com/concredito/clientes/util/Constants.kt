package com.concredito.clientes.util

object Constants {
    val BASE_URL = "http://10.0.2.2:8080/api/"
    val MAX_CHARACTERS_BY_NAME = 20
    val MAX_CHARACTERS_BY_ADDRESS = 30
    val MAX_CHARACTERS_BY_NUMBER_ADDRESS = 5
    val MAX_CHARACTERS_BY_ZIP_CODE = 5
    val MAX_CHARACTERS_BY_PHONE_NUMBER = 10
    val MAX_CHARACTERS_BY_RFC = 13
    val LIST_OF_CHARACTERS_WITH_ACCENT =
        listOf('á', 'é', 'í', 'ó', 'ú', 'ü', 'ñ', 'Á', 'É', 'Í', 'Ó', 'Ú', 'Ü', 'Ñ')
    val LIST_OF_SPECIAL_CHARACTERS = listOf('/', '?', '.', ',', '&', '-')
}
