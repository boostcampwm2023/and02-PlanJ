package com.boostcamp.planj.ui.login

enum class EmailState {
    NONE, AVAILABLE, ERROR_FORMAT, ERROR_EXIST
}

enum class PwdState {
    NONE, AVAILABLE, ERROR_LENGTH, ERROR_CONTENTS
}

enum class PwdConfirmState {
    NONE, AVAILABLE, ERROR
}

enum class NicknameState {
    NONE, AVAILABLE, ERROR_LENGTH
}