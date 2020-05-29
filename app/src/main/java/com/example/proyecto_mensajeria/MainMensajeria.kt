package com.example.proyecto_mensajeria

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_mensajeria.*
import kotlinx.android.synthetic.main.activity_registro.*
import org.json.JSONObject

class MainMensajeria : AppCompatActivity() {

    val IP = "http://192.168.56.1"

    companion object{
        //Paso5 Crear un companion object
        val EMISOR_CONTROL="emisor_ctrl"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensajeria)
    }


    //El usuario se guarda en la BD local
    //No se guarda a MySQL
    // Para eso es agregarMensaje(v)
    fun btnEnviar(v: View){
        //Paso 2
        //Con esto guarda la variable de la otra activity
        val res = intent
        val NoControl = res.getStringExtra(EMISOR_CONTROL)
/*
        //Paso 2
        //Con esto guarda la variable de la otra activity
        val res = intent
        if(res !=null && res.hasExtra(EMISOR_CONTROL)){
            res.getStringExtra(EMISOR_CONTROL)
        }
*/

        //
        if (
            Men_Receptor.text.isEmpty()){
            Toast.makeText(this, "No puedes dejar vació ninguna celda", Toast.LENGTH_SHORT).show();

        }else{
            val Msg = Men_Mensaje.text.toString()
            val Emisor = Men_Emisor.text.toString()
            val Receptor = Men_Receptor.text.toString()
                                                   //                                     //Es mi Variable del Companion Object
            val consultaInsert = "Insert into mensajes(Emisor,Receptor,Mensaje) values('$Emisor','$Receptor','$Msg')" //consulta: Variable de la Sentencia
            val admin = AdminBD(this)                                          //admin: Variable que trabaja con la BD
            if (admin.Ejecutar(consultaInsert)==1){
//REGISTRAR UN USUARIO
                Toast.makeText(this,"Mensaje Enviado", Toast.LENGTH_SHORT).show();

                //Guardandolo a la BD
                agregarMensaje(v)
            }else{
//NO SE ENCUENTRA EL USUARIO
                Men_Emisor.setError("YA EXISTE ESTE NUMERO DE USUARIO")
                Toast.makeText(this, "No se agrego usuario", Toast.LENGTH_SHORT).show();
                RegControl.requestFocus()
            }
        }
    }


    //ESTE BOTON SIRVE PARA GUARDAR EL MENSAJE A MYSQL
    fun agregarMensaje(v:View){
        //Paso 2
        //Con esto guarda la variable de la otra activity
        val res = intent
        val NoControl = res.getStringExtra(EMISOR_CONTROL)

        /*
        //Paso 2
        //Con esto guarda la variable de la otra activity
        val res = intent
        if(res !=null && res.hasExtra(EMISOR_CONTROL)){
            res.getStringExtra(EMISOR_CONTROL)
        }
        */

        if (
            Men_Receptor.text.toString().isEmpty()){
            Men_Emisor.setError("Falta información de Ingresar")
            Toast.makeText(this, "Falta información de Ingresar",
                Toast.LENGTH_LONG).show();
            Men_Emisor.requestFocus()
        }
        else
        {



            var jsonEntrada = JSONObject()
            jsonEntrada.put("Emisor", Men_Emisor.text.toString())
            jsonEntrada.put("Receptor", Men_Receptor.text.toString())
            jsonEntrada.put("Mensaje", Men_Mensaje.text.toString())
            sendRequest(IP + "/BD_aplicacion/mensajeria/insertMensaje.php",jsonEntrada)

        }
    }

    //Rutina para mandar ejecutar un web service de tipo Insert, Update o Delete
    fun sendRequest( wsURL: String, jsonEnt: JSONObject){
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL,jsonEnt,
            Response.Listener { response ->
                val succ = response["success"]
                val msg = response["message"]
                if (succ == 200){
                    Men_Emisor.setText("")
                    Men_Receptor.setText("")
                    Men_Mensaje.setText("")
                    Men_Emisor.requestFocus()
                    Toast.makeText(this, "Success:${succ}  Message:${msg} Se envió Mensaje en el Servidor Web", Toast.LENGTH_SHORT).show();
                }
            },
            Response.ErrorListener{error ->
                Toast.makeText(this, "${error.message}", Toast.LENGTH_SHORT).show();
                Log.d("ERROR","${error.message}");
                Toast.makeText(this, "Error de capa 8 checa URL", Toast.LENGTH_SHORT).show();
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }


    //getAllProducts
    fun cargar_mensajes(v: View){
        val wsURL = IP + "/BD_aplicacion/mensajeria/getMensajes.php"
        val admin = AdminBD(this)
        admin.Ejecutar("DELETE FROM alumno")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,wsURL,null,
            Response.Listener { response ->
                val succ = response["success"]
                val msg = response["message"]
                val sensadoJson = response.getJSONArray("alumnos")
                for (i in 0 until sensadoJson.length()){
                    // Los nombres del getString son como los arroja el servicio web
                    val Msg = sensadoJson.getJSONObject(i).getString("Mensaje")
                    val Emisor = sensadoJson.getJSONObject(i).getString("Emisor")
                    val Receptor = sensadoJson.getJSONObject(i).getString("Receptor")

                    val sentencia = "Insert into alumno(Emisor,Receptor,Mensaje) values ('${Emisor}', '${Receptor}',${Msg})"
                    val res = admin.Ejecutar(sentencia)


                }
                Toast.makeText(this, "CARGADO ALUMNO", Toast.LENGTH_SHORT).show();
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error getAllProductos: " + error.message.toString() , Toast.LENGTH_LONG).show();
                Log.d("Zazueta",error.message.toString() )
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
//


//MENSAJERIA
    class MainMensajeria : AppCompatActivity() {
        private lateinit var viewAdapter: MensajeAdapter
        private lateinit var viewManager: RecyclerView.LayoutManager
        val mensajeList: List<mensaje> = ArrayList()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_mensajeria)

            viewManager = LinearLayoutManager(this)
            viewAdapter = MensajeAdapter(mensajeList, this, { mensa: mensaje -> onItemClickListener(mensa) })

            rv_mensajeria_list.apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = viewAdapter
                addItemDecoration(DividerItemDecoration(this@MainMensajeria, DividerItemDecoration.VERTICAL))
            }

            // Metodo para implementar la eliminación de un estudiante, cuando el ususario da un onswiped en
            // el recyclerview
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                    val position = viewHolder.adapterPosition
                    val mensa= viewAdapter.getTasks()
                    val admin = AdminBD(baseContext)
                    if (admin.Ejecutar("DELETE FROM mensajes WHERE id_mensaje=" + mensa[position].id_mensaje) == 1){
                        retrieveMensajes()
                    }
                }
            }).attachToRecyclerView(rv_mensajeria_list)
        }

        // Evento clic cuando damos clic en un elemento del Recyclerview
        private fun onItemClickListener(Mensa: mensaje) {
            Toast.makeText(this, "Clicked item" + Mensa.Emisor, Toast.LENGTH_LONG).show()
        }

        override fun onResume() {
            super.onResume()
            retrieveMensajes()
        }

        private fun retrieveMensajes() {
            val mensajex = getMensajes()
            viewAdapter.setTask(mensajex!!)
        }

        fun getMensajes(): MutableList<mensaje>{
            var mensajes:MutableList<mensaje> = ArrayList()
            val admin = AdminBD(this)

            //                                          0       1       2      3
            val tupla = admin.Consultar("SELECT id_mensaje,Emisor,Receptor,Mensaje FROM mensajes ORDER BY id_mensaje")
            while (tupla!!.moveToNext()) {
                val id_msg = tupla.getString(0)
                val Emi = tupla.getString(1)
                val Rec = tupla.getString(2)
                val Msg = tupla.getString(3)

                mensajes.add(mensaje(id_msg,Emi,Rec,Msg))
            }
            tupla.close()
            admin.close()
            return mensajes
        }
    }




//
}
