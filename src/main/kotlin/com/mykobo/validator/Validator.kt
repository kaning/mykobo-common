package com.mykobo.validator

import java.util.*
import java.util.regex.Pattern

data class ValidationResult(val valid: Boolean, val message: String? = null)

interface Validator {}

object NonEmptyValidator : Validator {
    fun validate(field: String, item: String?): ValidationResult {
        val isValid = item?.isNotBlank() ?: false
        return ValidationResult(
            isValid,
            if (!isValid) "[$field] cannot be empty" else null
        )
    }
}

object CharLengthValidator: Validator {
    fun validate(field: String, item: String?, maxLength: Int): ValidationResult{
        val isValid = item?.length?.let {
            it >= maxLength
        }  ?: false

        return ValidationResult(
            isValid,
            if (!isValid) "[$field] is longer than $maxLength characters" else null
        )
    }

}

object EmailValidator : Validator {
    private val emailRegex: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    fun validate(field: String, email: String?): ValidationResult {
        val isValid = email?.let {
            emailRegex.matcher(it).matches()
        } ?: false
        return ValidationResult(
            isValid,
            if (!isValid) "$field is not a valid email address" else null
        )
    }
}

object LengthValidator : Validator {
    fun validate(field: String, minLength: Int? = null, maxLength: Int, item: String?): ValidationResult {
        val isValid =
            !item.isNullOrBlank() && (if (minLength != null) item.length >= minLength else true) && item.length <= maxLength
        return ValidationResult(
            isValid,
            if (!isValid) "$field is not a valid length" else null
        )
    }
}

object NonZeroValidator : Validator {
    fun validate(field: String, item: Double): ValidationResult {
        val isValid = !item.isNaN() && item > 0
        return ValidationResult(
            isValid,
            if (!isValid) "$field should be greater than zero" else null
        )
    }
}

object NonEmptyCollectionValidator: Validator {
    fun <E> validate(field: String, item: Collection<E>): ValidationResult  =
        ValidationResult(
            item.isNotEmpty(),
            if (item.isEmpty()) "This collection [$field] cannot be empty" else null
        )
}

object NumberFormatValidator: Validator {
    fun validate(field: String, item: String): ValidationResult {
        val valid = item.toDoubleOrNull() != null
        return ValidationResult(
            valid,
            if(!valid) "$field is not an acceptable number" else null
        )
    }

}

object UUIDFormatValidator: Validator {
    fun validate(field: String, item: String): ValidationResult {
        val valid = try {
            UUID.fromString(item)
            true
        } catch (e: Exception) {
            false
        }
        return ValidationResult(
            valid,
            if(!valid) "$field is not an acceptable user id" else null
        )
    }
}
