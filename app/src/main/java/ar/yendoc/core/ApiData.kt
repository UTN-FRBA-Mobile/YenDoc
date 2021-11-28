package ar.yendoc.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class Profesional (
    @Json(name = "profesional_id") val profesional_id: Int,
    @Json(name = "usuario") val usuario: String,
    @Json(name = "nombre") val nombre: String,
    @Json(name = "email") val email: String,
    @Json(name = "legajo") val legajo: Int
)

@JsonClass(generateAdapter = true)
data class Paciente (
    @Json(name = "paciente_id") val paciente_id: Int,
    @Json(name = "nombre") val nombre: String,
    @Json(name = "edad") val edad: Int? = null,
    @Json(name = "email") val email: String? = null,
    @Json(name = "telefono") val legajo: Int? = null,
    @Json(name = "direccion_calle") val direccion_calle: String,
    @Json(name = "direccion_numero") val direccion_numero: Int,
    @Json(name = "direccion_obs") val direccion_obs: String? = null,
    @Json(name = "direccion_longitud") val direccion_longitud: Float? = null,
    @Json(name = "direccion_latitud") val direccion_latitud: Float? = null
)

@JsonClass(generateAdapter = true)
data class Visita (
    @Json(name = "visita_id") val visita_id: Int,
    @Json(name = "fecha") val fecha: Long? = null,
    @Json(name = "profesional_id") val profesional_id: Int,
    @Json(name = "paciente_id") val paciente_id: Int,
    @Json(name = "diagnostico") val diagnostico: String? = null,
    @Json(name = "sintomas") val sintomas: String? = null,
    @Json(name = "estado") val realizada: Int? = 0
)

@JsonClass(generateAdapter = true)
data class VisitaAdapt (
    @Json(name = "visita_id") val visita_id: Int,
    @Json(name = "fecha") val fecha: Long? = null,
    @Json(name = "paciente") val paciente: String,
    @Json(name = "direccionPaciente") val direccionPaciente : String,
    @Json(name = "estado_visita") val estado: Int? = 0
)