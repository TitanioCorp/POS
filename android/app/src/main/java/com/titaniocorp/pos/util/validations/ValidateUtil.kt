package com.titaniocorp.pos.util.validations

import android.widget.EditText
import com.titaniocorp.pos.R
import com.titaniocorp.pos.util.AppCode.Companion.VALIDATE_EMPTY_FIELD
import com.titaniocorp.pos.util.AppCode.Companion.VALIDATE_ERROR_DEFAULT
import com.titaniocorp.pos.util.AppCode.Companion.VALIDATE_LONG_NUMBER_LENGTH
import com.titaniocorp.pos.util.AppCode.Companion.VALIDATE_MONEY_ZERO
import com.titaniocorp.pos.util.AppCode.Companion.VALIDATE_NUMBER_LENGTH
import com.titaniocorp.pos.util.AppCode.Companion.VALIDATE_NUMBER_ZERO
import com.titaniocorp.pos.util.AppCode.Companion.VALIDATE_SUCCESS
import com.titaniocorp.pos.util.moneyFilter
import timber.log.Timber
import java.lang.Exception

object ValidateUtil {
    fun inputs(vararg data: ValidateInput): Boolean{
        var isSuccess = true

        for (item in data){
            if(!item.input.validate(item.type)){
                isSuccess = false
            }
        }

        return isSuccess
    }
}

/* Extensions */
fun EditText.toValidate(
    type: ValidateType = ValidateType.SINGLE
): ValidateInput {
    return ValidateInput(this, type)
}

fun EditText.validate(
    type: ValidateType,
    callBack: (() -> Unit)?= null
): Boolean {
    var isSuccess = false
    when(text.toString().validate(type)){
        VALIDATE_ERROR_DEFAULT -> { error = context.getString(R.string.validate_text_default)}
        VALIDATE_EMPTY_FIELD -> { error = context.getString(R.string.validate_text_empty_field)}

        VALIDATE_NUMBER_LENGTH -> { error = context.getString(R.string.validate_text_number_length)}
        VALIDATE_LONG_NUMBER_LENGTH -> { error = context.getString(R.string.validate_text_long_number_length)}

        VALIDATE_MONEY_ZERO -> { error = context.getString(R.string.validate_text_money_zero)}
        VALIDATE_SUCCESS -> {
            isSuccess = true
            callBack?.invoke()
        }
    }
    return isSuccess
}

/* String Validations */
fun String.validate(type: ValidateType = ValidateType.SINGLE): Int{
    return try{
        when{
            isEmpty() -> VALIDATE_EMPTY_FIELD

            //NUMBER
            type == ValidateType.NUMBER -> {
                when{
                    length > 9 -> VALIDATE_NUMBER_LENGTH
                    toInt() <= 0 -> VALIDATE_NUMBER_ZERO
                    else -> VALIDATE_SUCCESS
                }
            }

            //LONG NUMBER
            type == ValidateType.LONG_NUMBER -> {
                when{
                    length > 15 -> VALIDATE_LONG_NUMBER_LENGTH
                    toLong() <= 0 -> VALIDATE_NUMBER_ZERO
                    else -> VALIDATE_SUCCESS
                }
            }

            //MONEY
            type == ValidateType.MONEY -> {
                when{
                    moneyFilter() <= 0 -> VALIDATE_MONEY_ZERO
                    else -> VALIDATE_SUCCESS
                }
            }

            //PERCENT
            type == ValidateType.PERCENT -> {
                when{
                    length > 6 -> VALIDATE_LONG_NUMBER_LENGTH
                    toDouble() <= 0 -> VALIDATE_NUMBER_ZERO
                    else -> VALIDATE_SUCCESS
                }
            }

            else -> VALIDATE_SUCCESS
        }
    }catch(exception: Exception){
        Timber.e(exception.localizedMessage)
        VALIDATE_ERROR_DEFAULT
    }
}