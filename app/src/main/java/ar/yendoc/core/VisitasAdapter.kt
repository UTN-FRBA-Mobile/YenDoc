package ar.yendoc.core

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ar.yendoc.R

class VisitasAdapter (private val myDataset: MutableList<Visita>) :
    RecyclerView.Adapter<VisitasAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): VisitasAdapter.MyViewHolder {
        val view : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_list_visits, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.view.findViewById<TextView>(R.id.paciente).text = myDataset[position].nombrePaciente
        holder.view.findViewById<TextView>(R.id.paciente_direccion).text = myDataset[position].direccionPaciente
        if(myDataset[position].estado == 0)
            holder.view.findViewById<ImageView>(R.id.estado_visita).setImageResource(R.drawable.pendiente)
        else if(myDataset[position].estado == 1)
            holder.view.findViewById<ImageView>(R.id.estado_visita).setImageResource(R.drawable.cumplida)
        else if(myDataset[position].estado == 2)
            holder.view.findViewById<ImageView>(R.id.estado_visita).setImageResource(R.drawable.no_cumplida)
        else if(myDataset[position].estado == 3)
            holder.view.findViewById<ImageView>(R.id.estado_visita).setImageResource(R.drawable.en_curso)
    }

    override fun getItemCount() = myDataset.size
}