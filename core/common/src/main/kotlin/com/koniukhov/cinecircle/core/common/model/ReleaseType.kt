package com.koniukhov.cinecircle.core.common.model

enum class ReleaseType(val value: Int) {
    PREMIERE(1),
    THEATRICAL_LIMITED(2),
    THEATRICAL(3),
    DIGITAL(4),
    PHYSICAL(5),
    TV(6);

    companion object {
        fun fromValue(value: Int): ReleaseType? {
            return entries.find { it.value == value }
        }
    }
}
