package ru.qwerty.ui.domain

import javax.inject.Inject

/**
 * @author y.yangel
 */
internal data class ValidationResult(
    val errorType: ErrorType? = null,
    val isValid: Boolean? = null,
)

enum class ErrorType {
    PASSWORD,
    PHONE,
}

internal class ValidatePhoneInputUseCase @Inject constructor() {

    operator fun invoke(phoneInput: String): ValidationResult {
        val sanitizedInput = phoneInput.replace(Regex("[^\\d+]"), "")
        return when {
            sanitizedInput.length != 11 -> {
                ValidationResult(ErrorType.PHONE, isValid = false)
            }

            !sanitizedInput.startsWith("79") -> {
                ValidationResult(ErrorType.PHONE, isValid = false)
            }

            else -> {
                ValidationResult(isValid = true)
            }
        }
    }
}


internal class ValidatePasswordInputUseCase @Inject constructor() {

    operator fun invoke(passwordInput: String): ValidationResult {
        if (passwordInput.length < 6) {
            return ValidationResult(ErrorType.PASSWORD, isValid = false)
        }
        val hasUpperCase = passwordInput.any { it.isUpperCase() }
        val hasDigit = passwordInput.any { it.isDigit() }
        return if (hasUpperCase && hasDigit) {
            ValidationResult(isValid = true)
        } else {
            ValidationResult(ErrorType.PASSWORD, isValid = false)
        }
    }
}