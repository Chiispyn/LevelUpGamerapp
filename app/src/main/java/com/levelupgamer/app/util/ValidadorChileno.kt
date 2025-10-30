package com.levelupgamer.app.util

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object ValidadorChileno {

    fun validarRut(rut: String): Boolean {
        val cleanRut = rut.replace("[.-]".toRegex(), "").uppercase()
        if (!cleanRut.matches("^\\d{7,8}[0-9K]$".toRegex())) return false
        val body = cleanRut.substring(0, cleanRut.length - 1)
        val dv = cleanRut.last()
        return try {
            calcularDv(body) == dv
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun calcularDv(rut: String): Char {
        var suma = 0
        var multiplo = 2
        for (i in rut.length - 1 downTo 0) {
            suma += (rut[i].toString().toInt() * multiplo)
            multiplo = if (multiplo == 7) 2 else multiplo + 1
        }
        val dv = 11 - (suma % 11)
        return when (dv) {
            11 -> '0'
            10 -> 'K'
            else -> dv.toString().first()
        }
    }

    fun validarNombre(nombre: String): Boolean {
        // Permite letras (incluyendo acentos y ñ) y espacios
        return nombre.matches("^[\\p{L} ]+$".toRegex())
    }

    fun validarEdad(fechaNac: String): Boolean {
        return try {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val fecha = LocalDate.parse(fechaNac, formatter)
            val edad = Period.between(fecha, LocalDate.now()).years
            edad in 18..99 // Rango de edad entre 18 y 99 años
        } catch (e: DateTimeParseException) {
            false
        }
    }
}