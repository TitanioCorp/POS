package com.titaniocorp.pos.app.ui

import timber.log.Timber

object KotlinExample {
    data class User(var name: String, var lastName: String)

    fun execute(){
        let()
        //run()
        //also()
        //apply()
        //with()

        val inPlus: Int.(Int) -> Int = Int::plus

        log(2.inPlus(3).toString())
    }

    private fun log(string: String){
        Timber.tag("KotlinExample").i(string)
    }

    /*  Let
        * 1. Permite renombrar dentro de la funcion scope la variable.
        * 2. Se usa para checkear nulls
        * 3. Devuelve el ultimo valor del lambda.
        * 4. Se puede concatenar let.
        *  */
    private fun let(){
        log("let")
        log("------")

        //---
        val string = "NovoPayment"
        string.let { log("1. Hola $it") }
        string.let {novoMensaje -> log("2. Hola $novoMensaje") }

        //---
        val stringNull: String? = "NovoPayment"
        stringNull?.let { log("3. Hola $it") }

        //---
        val unitValue = stringNull?.let {novoMensaje -> log("4. Hola $novoMensaje") }
        val value = stringNull?.let { novoMensaje ->
            log("5. Hola $novoMensaje")
            novoMensaje.length
        }

        //---
        val valueConcat = stringNull?.let { "6. Hola $it" }?.let{it.length}
        log(valueConcat.toString())
    }

    /*  run
        * 1. Devuelve el ultimo valor del lambda.
        * 3. Funciona de la mano con let
        *  */
    private fun run(){
        log("run")
        log("------")

        //---
        var stringRun = "NovoPayment"
        log(stringRun)

        stringRun = run {
            val stringRun = "NovoPayment updated!"
            stringRun
        }
        log(stringRun)

        //---
        val letRunString: String?= null
        letRunString?.let { log(it) } ?: run { log("letRunString is null") }
    }

    /*  also
        * 1. Devuelve el mismo objeto con el que entró (a menos que se modifique una propiedad).
        * 2. Se puede concatenar.
        *  */
    private fun also(){
        log("also")
        log("------")

        User("Novo", "Example").also {
            log(it.toString())
            it.lastName = "Payment"
            User("Bogota", "Colombia")
        }.also {
            log(it.toString())
        }
    }

    /*  Apply
        * 1. Se pueden user los atributos del objeto como si estuviera en una clase.
        *  */
    private fun apply(){
        log("apply")
        log("------")

        val user = User("Novo", "Example").apply {
            lastName = "Payment"
            log(this.toString())
            "$name $lastName"
        }
    }

    /*  With
        * 1. Se pueden user los atributos del objeto como si estuviera en una clase.
        * 2. Devuelve un objeto.
        *  */
    private fun with(){
        log("with")
        log("------")

        val user = User("Novo", "Example")
        val value = with(user){
            lastName = "Payment"
            log(this.toString())
            "$name $lastName"
        }
    }
}