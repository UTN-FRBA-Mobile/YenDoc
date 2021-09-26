package ar.yendoc.core

class VisitaAPI {
    fun getVisitas(): MutableList<Visita> {
        val dataset = mutableListOf<Visita>()
        for (i in 0..30) {
            if (i % 5 == 0) {
                dataset.add(Visita(i, "Paciente $i", "Dirección paciente: $i", 0))
                continue
            }

            if (i % 2 == 0)
            //even
                dataset.add(Visita(i, "Paciente $i", "Dirección paciente: $i", 1))
            else
            //odd
                dataset.add(Visita(i, "Paciente $i", "Dirección paciente: $i", 2))
        }
        val idTemporal = dataset.size + 1
        dataset.add(Visita(idTemporal, "Paciente $idTemporal", "Dirección paciente: $idTemporal", 3))
        return dataset
    }
}