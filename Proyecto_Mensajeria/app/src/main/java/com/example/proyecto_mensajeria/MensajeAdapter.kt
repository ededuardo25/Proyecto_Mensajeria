package com.example.proyecto_mensajeria

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.celda_prototipo_mensaje.view.*

class MensajeAdapter(private var mListaMensajes:List<mensaje>,
                     private val mContext: Context, private val clickListener: (mensaje) -> Unit)
    : RecyclerView.Adapter<MensajeAdapter.MensajeViewHolder>() {


    /**
     * onCreateViewHolder() que como su nombre indica lo que hará será devolvernos
     * un objeto ViewHolder al cual le pasamos la celda prototió que hemos creado.
     *
     * @return Un nuevo MensajeViewHolder que contiene la vista para cada estudiante
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensajeViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        return MensajeViewHolder(layoutInflater.inflate(R.layout.celda_prototipo_mensaje, parent, false))
    }

    /**
     * La clase RecyclerView. onBindViewHolder() se encarga de coger cada una de las
     * posiciones de la lista de estudiantes y pasarlas a la clase ViewHolder(
     *
     * @param holder   Vincular los datos del cursor al ViewHolder
     * @param position La posición de los datos en la lista
     */
    override fun onBindViewHolder(holder: MensajeViewHolder, position: Int) {
        holder.bind(mListaMensajes[position], mContext, clickListener)
    }

    /**
     * El método getItemCount() nos devuelve el tamaño de la lista, que lo necesita
     * el RecyclerView.
     */
    override fun getItemCount(): Int = mListaMensajes.size

    /**
     * Cuando los datos cambian, este metodo actualiza la lista de estudiantes
     * y notifica al adaptador a usar estos nuevos valores
     */
    fun setTask(mensajes: List<mensaje>){
        mListaMensajes = mensajes
        notifyDataSetChanged()
    }

    fun getTasks(): List<mensaje> = mListaMensajes

    /**
     * Clase interna para crear ViewHolders
     */
    class MensajeViewHolder (itemView: View) :RecyclerView.ViewHolder(itemView) {

        fun bind (mensa:mensaje, context: Context, clickListener: (mensaje) -> Unit){
            //Asigna los valores a los elementos del la celda_prototipo_estudiante
            itemView.Emisor.text = mensa.Emisor.toString()
            itemView.Receptor.text = mensa.Receptor.toString()
            itemView.Txt_MsgEmisor.text = mensa.Mensaje.toString()
            itemView.Txt_MsgRecep.text = mensa.Mensaje.toString()


            itemView.setOnClickListener{ clickListener(mensa)}
        }
    }

}
