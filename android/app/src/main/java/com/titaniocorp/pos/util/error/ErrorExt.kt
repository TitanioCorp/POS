package com.titaniocorp.pos.util.error

import com.titaniocorp.pos.R
import com.titaniocorp.pos.app.model.ErrorResource
import com.titaniocorp.pos.util.AppCode
import com.titaniocorp.pos.util.Constants
import timber.log.Timber
import java.net.HttpURLConnection

fun ErrorResource.getStringsId(): Pair<Int, Int?>{
    Timber.tag(Constants.TAG_APP_DEBUG).e("Code: $code - Message: $message")
    return when(this.code){
        /* HTTP ERROR */
        HttpURLConnection.HTTP_INTERNAL_ERROR,
        HttpURLConnection.HTTP_UNAVAILABLE,
        HttpURLConnection.HTTP_VERSION -> {
            Pair(R.string.error_text_http_internal_error, null)
        }

        HttpURLConnection.HTTP_NOT_FOUND -> {
            Pair( R.string.error_text_http_not_found, null)
        }

        /* SYSTEM ERROR */
        AppCode.NO_INTERNET -> { Pair( R.string.error_text_no_internet, null) }
        AppCode.QUERY_DATABASE -> { Pair( R.string.error_text_query_database, null) }

        AppCode.ERROR_FORM_VALIDATE ->
            Pair(R.string.error_text_form_validate, R.string.error_title_information)

        AppCode.ERROR_FORM_CATEGORY ->
            Pair(R.string.error_text_form_category, R.string.error_title_information)

        /* DATA ERROR */
        AppCode.DATA_EMPTY -> Pair(R.string.error_text_data_empty, null)

        /* PURCHASE VALIDATION */
        AppCode.ERROR_PURCHASE_EMPTY_PRICES -> {
            Pair(R.string.validate_text_purchase_empty_prices, R.string.error_title_information)
        }

        AppCode.ERROR_PURCHASE_NO_CUSTOMER -> {
            Pair(
                R.string.validate_text_purchase_no_customer,
                R.string.error_title_validation_customer
            )
        }

        AppCode.ERROR_QUANTITY_PRICE_PURCHASE -> {
            Pair(
                R.string.validate_text_quantity_price_purchase,
                R.string.error_title_quantity_price_purchase
            )
        }

        AppCode.ERROR_ZERO_QUANTITY_PRICE -> {
            Pair(
                R.string.validate_text_zero_quantity_price,
                R.string.error_title_zero_quantity_price
            )
        }

        AppCode.ERROR_CANNOT_REFUND -> {
            Pair(
                R.string.validate_text_cannot_refund,
                R.string.error_title_alert
            )
        }

        AppCode.ERROR_CANNOT_ADD_PAYMENT -> {
            Pair(
                R.string.validate_text_cannot_add_payment,
                R.string.error_title_alert
            )
        }

        AppCode.ERROR_EMPTY_PROFIT_LIST -> {
            Pair(
                R.string.validate_text_empty_profit_list,
                R.string.error_title_empty_profit_list
            )
        }

        /* Default Message */
        else -> { Pair(R.string.error_text_default, null) }
    }
}