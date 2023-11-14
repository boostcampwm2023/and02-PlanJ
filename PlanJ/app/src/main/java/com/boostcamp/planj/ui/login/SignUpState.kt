package com.boostcamp.planj.ui.login

enum class EmailState {
    AVAILABLE, ERROR_FORMAT, ERROR_EXIST
}

enum class PwdState {
    AVAILABLE, ERROR_LENGTH, ERROR_ENGLISH, ERROR_NUM, ERROR_SPECIAL
}

enum class PwdConfirmState {
    AVAILABLE, ERROR
}

enum class NicknameState {
    AVAILABLE, ERROR_LENGTH
}