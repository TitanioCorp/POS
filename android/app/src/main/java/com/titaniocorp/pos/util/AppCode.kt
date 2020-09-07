package com.titaniocorp.pos.util

/**
 * Codigos de la aplicación
 * @version 1.0
 * @author Juan Ortiz
 * @date 26/11/2019
 */
class AppCode {
    companion object{
        /* System */
        const val DEFAULT = 1000
        const val NO_INTERNET = 1001
        const val DATA_EMPTY = 1002
        const val SUCCESS_FORM_VALIDATE = 1003
        const val ERROR_FORM_VALIDATE = 1004
        const val ERROR_FORM_CATEGORY = 1005

        /* ERROR */

        const val ERROR_PURCHASE_EMPTY_PRICES = 1006
        const val ERROR_PURCHASE_EMPTY_PAYMENTS = 1007
        const val ERROR_PURCHASE_NO_CUSTOMER = 1008
        const val ERROR_QUANTITY_PRICE_PURCHASE = 1009
        const val ERROR_ZERO_QUANTITY_PRICE = 1010
        const val ERROR_CANNOT_REFUND = 1011
        const val ERROR_CANNOT_ADD_PAYMENT = 1012
        const val ERROR_EMPTY_PROFIT_LIST = 1013

        /* Database */
        const val QUERY_DATABASE = 1500
        const val INSERT_DATABASE = 1501
        const val UPDATE_DATABASE = 1502
        const val DELETE_DATABASE= 1503

        const val SUCCESS_QUERY_DATABASE= 1504
        const val ERROR_QUERY_DATABASE= 1505
        const val IMPORT_DATABASE= 1506

        /* Services */
        const val RESPONSE_EMTPY = 1600

        /* Validations */
        const val VALIDATE_SUCCESS = 3000
        const val VALIDATE_ERROR_DEFAULT = 3001
        const val VALIDATE_EMPTY_FIELD = 3002
        const val VALIDATE_MONEY_ZERO = 3003
        const val VALIDATE_NUMBER_LENGTH = 3004
        const val VALIDATE_NUMBER_ZERO = 3005
        const val VALIDATE_LONG_NUMBER_LENGTH = 3005
    }
}