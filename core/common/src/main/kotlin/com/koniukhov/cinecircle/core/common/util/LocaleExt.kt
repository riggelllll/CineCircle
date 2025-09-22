package com.koniukhov.cinecircle.core.common.util

import java.util.Locale

fun Locale.getLocalizedLanguageMap(): Map<String, String> {
    val userLocale = this
    val allLocales = Locale.getAvailableLocales()
    val languageCodes = allLocales.map { it.language }.toSet()
    val languageMap = mutableMapOf<String, String>()

    languageCodes.forEach { languageCode ->
        if (languageCode.isNotBlank()) {
            val languageLocale = Locale(languageCode)
            val localizedName = languageLocale.getDisplayLanguage(userLocale)
            languageMap[localizedName.replaceFirstChar { it.uppercase() }] = languageCode
        }
    }

    return languageMap.toSortedMap()
}

fun Locale.getLocalizedCountryMap(): Map<String, String> {
    val userLocale = this
    val countryCodes = Locale.getISOCountries()
    val countryMap = mutableMapOf<String, String>()

    countryCodes.forEach { countryCode ->
        if (countryCode.isNotBlank()) {
            val countryLocale = Locale("", countryCode)
            val localizedName = countryLocale.getDisplayCountry(userLocale)
            countryMap[localizedName.replaceFirstChar { it.uppercase() }] = countryCode
        }
    }

    return countryMap.toSortedMap()
}