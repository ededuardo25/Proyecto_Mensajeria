package com.example.proyecto_mensajeria

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.celda_prototipo_grupo.view.*
import layout.grupo

class GrupoAdapter(private var mListaGrupos:List<grupo>,
                   private val mContext: Context, private val clickListener: (grupo) -> Unit)
    : RecyclerView.Adapter<GrupoAdapter.GrupoViewHolder>() {


    /**
     * onCreateViewHolder() que como su nombre indica lo que hará será devolvernos
     * un objeto ViewHolder al cual le pasamos la celda prototió que hemos creado.
     *
     * @return Un nuevo EstudianteViewHolder que contiene la vista para cada estudiante
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrupoViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        return GrupoViewHolder(layoutInflater.inflate(R.layout.celda_prototipo_grupo, parent, false))
    }

    /**
     * La clase RecyclerView. onBindViewHolder() se encarga de coger cada una de las
     * posiciones de la lista de estudiantes y pasarlas a la clase ViewHolder(
     *
     * @param holder   Vincular los datos del cursor al ViewHolder
     * @param position La posición de los datos en la lista
     */
    override fun onBindViewHolder(holder: GrupoViewHolder, position: Int) {
        holder.bind(mListaGrupos[position], mContext, clickListener)
    }

    /**
     * El método getItemCount() nos devuelve el tamaño de la lista, que lo necesita
     * el RecyclerView.
     */
    override fun getItemCount(): Int = mListaGrupos.size

    /**
     * Cuando los datos cambian, este metodo actualiza la lista de estudiantes
     * y notifica al adaptador a usar estos nuevos valores
     */
    fun setTask(grupos: List<grupo>){
        mListaGrupos = grupos
        notifyDataSetChanged()
    }

    fun getTasks(): List<grupo> = mListaGrupos

    /**
     * Clase interna para crear ViewHolders
     */
    class GrupoViewHolder (itemView: View) :RecyclerView.ViewHolder(itemView) {

        fun bind (grup:grupo, context: Context, clickListener: (grupo) -> Unit){
            //Asigna los valores a los elementos del la celda_prototipo_estudiante
            itemView.RvNombreDocente.text = grup.docenteid.toString()
            itemView.RvNombreMateria.text = grup.materiaid.toString()

            itemView.setOnClickListener{ clickListener(grup)}
        }
    }

}
