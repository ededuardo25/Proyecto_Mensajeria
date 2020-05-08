package com.example.proyecto_mensajeria

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.celda_prototipo_alumno.view.*

class AlumnoAdapter(private var mListaAlumnos:List<alumno>,
                    private val mContext: Context, private val clickListener: (alumno) -> Unit)
    : RecyclerView.Adapter<AlumnoAdapter.AlumnoViewHolder>() {


    /**
     * onCreateViewHolder() que como su nombre indica lo que hará será devolvernos
     * un objeto ViewHolder al cual le pasamos la celda prototió que hemos creado.
     *
     * @return Un nuevo EstudianteViewHolder que contiene la vista para cada estudiante
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumnoViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        return AlumnoViewHolder(layoutInflater.inflate(R.layout.celda_prototipo_alumno, parent, false))
    }

    /**
     * La clase RecyclerView. onBindViewHolder() se encarga de coger cada una de las
     * posiciones de la lista de estudiantes y pasarlas a la clase ViewHolder(
     *
     * @param holder   Vincular los datos del cursor al ViewHolder
     * @param position La posición de los datos en la lista
     */
    override fun onBindViewHolder(holder: AlumnoViewHolder, position: Int) {
        holder.bind(mListaAlumnos[position], mContext, clickListener)
    }

    /**
     * El método getItemCount() nos devuelve el tamaño de la lista, que lo necesita
     * el RecyclerView.
     */
    override fun getItemCount(): Int = mListaAlumnos.size

    /**
     * Cuando los datos cambian, este metodo actualiza la lista de estudiantes
     * y notifica al adaptador a usar estos nuevos valores
     */
    fun setTask(alumnos: List<alumno>){
        mListaAlumnos = alumnos
        notifyDataSetChanged()
    }

    fun getTasks(): List<alumno> = mListaAlumnos

    /**
     * Clase interna para crear ViewHolders
     */
    class AlumnoViewHolder (itemView: View) :RecyclerView.ViewHolder(itemView) {

        fun bind (alu:alumno, context: Context, clickListener: (alumno) -> Unit){
            //Asigna los valores a los elementos del la celda_prototipo_estudiante
            itemView.rvNoControl.text = alu.no_control.toString()
            itemView.rvNombre.text = alu.nombre.toString()
            itemView.rvSemestre.text = alu.semestre.toString()


            itemView.setOnClickListener{ clickListener(alu)}
        }
    }

}
