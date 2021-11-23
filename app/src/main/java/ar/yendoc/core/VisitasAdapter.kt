package ar.yendoc.core

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ar.yendoc.R
import ar.yendoc.network.VisitaAdapt

class VisitasAdapter (private val myDataset: MutableList<VisitaAdapt>, private val clickListener: (VisitaAdapt, Int) -> Unit) :
    RecyclerView.Adapter<VisitasAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): VisitasAdapter.MyViewHolder {
        val view : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_list_visits, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var item : VisitaAdapt = myDataset[position]

        // Calling the clickListener sent by the constructor
        holder?.view?.setOnClickListener { clickListener(item, position) }

        holder.view.findViewById<TextView>(R.id.paciente).text = myDataset[position].paciente
        holder.view.findViewById<TextView>(R.id.paciente_direccion).text = myDataset[position].direccionPaciente
        when (myDataset[position].estado) {
            0 -> holder.view.findViewById<ImageView>(R.id.estado_visita).setImageResource(R.drawable.pendiente)
            1 -> holder.view.findViewById<ImageView>(R.id.estado_visita).setImageResource(R.drawable.cumplida)
            2 -> holder.view.findViewById<ImageView>(R.id.estado_visita).setImageResource(R.drawable.no_cumplida)
            3 -> holder.view.findViewById<ImageView>(R.id.estado_visita).setImageResource(R.drawable.en_curso)
        }
    }

    override fun getItemCount() = myDataset.size
}