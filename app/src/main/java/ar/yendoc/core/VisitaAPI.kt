package ar.yendoc.core

class VisitaAPI {
    fun getVisitas(): MutableList<Visita> {
        val dataset = mutableListOf<Visita>()
        var j = 0
        for (i in 0..3) {
            dataset.add(Visita(j, "Paciente $j", "Dirección paciente: $j", 1))
            j += 1
        }
        for (i in 0..2) {
            dataset.add(Visita(j, "Paciente $j", "Dirección paciente: $j", 2))
            j += 1
        }
        for (i in 0..2) {
            dataset.add(Visita(j, "Paciente $j", "Dirección paciente: $j", 1))
            j += 1
        }
        dataset.add(Visita(j, "Paciente $j", "Dirección paciente: $j", 3))
        j += 1
        for (i in 0..10) {
            dataset.add(Visita(j, "Paciente $j", "Dirección paciente: $j", 0))
            j += 1
        }
        return dataset
    }
}