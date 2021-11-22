package ar.yendoc.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class Profesional (
    @Json(name = "profesional_id") val profesional_id: Int,
    @Json(name = "nombre") val nombre: String,
    @Json(name = "email") val email: String,
    @Json(name = "legajo") val legajo: Int
)

@JsonClass(generateAdapter = true)
data class Paciente (
    @Json(name = "paciente_id") val paciente_id: Int,
    @Json(name = "nombre") val nombre: String,
    @Json(name = "email") val email: String? = null,
    @Json(name = "telefono") val legajo: Int? = null,
    @Json(name = "direccion_calle") val direccion_calle: String,
    @Json(name = "direccion_numero") val direccion_numero: Int,
    @Json(name = "direccion_obs") val direccion_obs: String? = null,
    @Json(name = "direccion_longitud") val direccion_longitud: Float? = null,
    @Json(name = "direccion_latitud") val direccion_latitud: Float? = null
)

@JsonClass(generateAdapter = true)
data class Diagnostico (
    @Json(name = "diagnostico_id") val diagnostico_id: Int,
    @Json(name = "fecha") val fecha: Date? = null,
    @Json(name = "profesional_id") val profesional_id: Int,
    @Json(name = "paciente_id") val paciente_id: Int,
    @Json(name = "diagnostico") val diagnostico: String
)

@JsonClass(generateAdapter = true)
data class Visita (
    @Json(name = "visita_id") val visita_id: Int,
    @Json(name = "fecha") val fecha: Date? = null,
    @Json(name = "profesional_id") val profesional_id: Int,
    @Json(name = "paciente_id") val paciente_id: Int,
    @Json(name = "diagnostico_id") val diagnostico_id: Int? = null,
    @Json(name = "realizada") val realizada: Int? = 0
)