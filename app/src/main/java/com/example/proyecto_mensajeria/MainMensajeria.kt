package com.example.proyecto_mensajeria

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_registro.*
import org.json.JSONObject

class MainMensajeria : AppCompatActivity() {

    val IP = "http://192.168.56.1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensajeria)
    }



    //ESTE BOTON SIRVE PARA GUARDAR EL MENSAJE A MYSQL
    fun agregarMensaje(v:View){
        if (RegControl.text.toString().isEmpty()||
            RegNombre.text.toString().isEmpty()||
            RegSemestre.text.toString().isEmpty()||
            RegContra.text.toString().isEmpty()){
            RegControl.setError("Falta información de Ingresar")
            Toast.makeText(this, "Falta información de Ingresar",
                Toast.LENGTH_LONG).show();
            RegControl.requestFocus()
        }
        else
        {

            val control = RegControl.text.toString()
            val contra=RegContra.text.toString()


            /*val nom = etNomProd.text.toString()
            val existencia = etExistencia.text.toString()
            val precio = etPrecio.text.toString()
            */
            var jsonEntrada = JSONObject()
            jsonEntrada.put("no_control", RegControl.text.toString())
            jsonEntrada.put("nom_alumno", RegNombre.text.toString())
            jsonEntrada.put("semestre", RegSemestre.text.toString())
            jsonEntrada.put("password",RegContra.text.toString())
            sendRequest(IP + "/BD_aplicacion/alumno/insertAlumno.php",jsonEntrada)

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
                    RegControl.setText("")
                    RegNombre.setText("")
                    RegSemestre.setText("0")
                    RegContra.setText("")
                    RegControl.requestFocus()
                    Toast.makeText(this, "Success:${succ}  Message:${msg} Se Inserto Alumno en el Servidor Web", Toast.LENGTH_SHORT).show();
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
                    val nocontrol = sensadoJson.getJSONObject(i).getString("no_control")
                    val nomalumno = sensadoJson.getJSONObject(i).getString("nom_alumno")
                    val semestre = sensadoJson.getJSONObject(i).getString("semestre")
                    val contra = sensadoJson.getJSONObject(i).getString("password")

                    val sentencia = "Insert into alumno(no_control,nombre,semestre,password) values ('${nocontrol}', '${nomalumno}',${semestre}, '${contra}')"
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
}
