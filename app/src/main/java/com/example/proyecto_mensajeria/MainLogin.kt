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
import kotlinx.android.synthetic.main.activity_alumno.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registro.*

class MainLogin : AppCompatActivity() {

    val IP = "http://192.168.56.1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


    }


    fun btn_login(v: View){
        //INICIO DE BLOQUE: LOGIN
        if (LogControl.text.isEmpty() || LogPass.text.isEmpty()){
            LogControl.setError("Faltan datos por ingresar")
            LogControl.requestFocus()
            LogPass.requestFocus()
        }else{
                //BLOQUE DE CÓDIGO PARA HACER LOGIN
            var control=LogControl.text.toString()
            var pass=LogPass.text.toString()
            val admin=AdminBD(this)
            val query =("Select * from alumno where no_control='$control' and password='$pass'")
            val result=admin.Consultar(query)
            if(result!!.moveToFirst()){
                control=result.getString(0)
                pass=result.getString(3)
//IR A...
                //Paso1 Definir variable para que se guarde en  una caja de texto
                //val noControl = LogControl.text.toString()
                //Paso2 Se inicializa para mandar a otra activity
                val act1=Intent(this,MainAlumno::class.java)//Si se dirige correctamente ira a la actividad Principal
                act1.putExtra(MainAlumno.EXTRA_CONTROL,control)
                act1.putExtra(MainAlumno.EXTRACONTRA,pass)
                //Paso3
                act1.putExtra(MainMensajeria.EMISOR_CONTROL,control)

                //Paso4 Inicio la actividad
                startActivity(act1)
        //FIN BLOQUE LOGUEO

            }else{
                Toast.makeText(this, "Correo o Contraseña Invalido", Toast.LENGTH_SHORT).show();
            }
        }
    }

    fun btn_registro(v:View){
        val acti:Intent=Intent(this,MainRegistro::class.java)
        startActivity(acti)
    }


        //Cargare los alumnos y los grupos a la Base de Datos para que se almacenen al dispositivo
    fun cargarTODO(v:View){
            cargar_Alumnos(v)
            cargar_Grupos(v)
    }


    //Lo que hace esta funcion es cargar los datos del MySQL y los sube al dispositivo
    //De otra manera el usuario no podria ingresar si se hizo un cambio en MySQL y no al dispositivo
    fun cargar_Alumnos(v:View){
        val wsURL = IP + "/BD_aplicacion/alumno/getAlumnos.php"
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


    //getAllProducts
    //CUIDADO ESTOY HACIENDO PRUEBAS CON cargar_Grupos
    //No olvides cambiar la funcion del boton a cambiar_TODO cuando funcione
    //CORREGIDO,DEJO LOS MENSAJES PARA NO VOLVER A HACERLO XD 20/05/2020
    fun cargar_Grupos(v:View){
        val wsURL = IP + "/BD_aplicacion/grupo/getGrupos.php"
        val admin = AdminBD(this)
        admin.Ejecutar("DELETE FROM grupo")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,wsURL,null,
            Response.Listener { response ->
                val succ = response["success"]
                val msg = response["message"]
                val sensadoJson = response.getJSONArray("grupos")
                for (i in 0 until sensadoJson.length()){
                    // Los nombres del getString son como los arroja el servicio web
                    val idgrupo = sensadoJson.getJSONObject(i).getString("id_grupo")
                    val grupo = sensadoJson.getJSONObject(i).getString("grupo")
                    val materiaid = sensadoJson.getJSONObject(i).getString("materiaid")
                    val docenteid = sensadoJson.getJSONObject(i).getString("docenteid")

                    val sentencia = "Insert into grupo(id_grupo,grupo,nocontrol,materiaid,docenteid) values ('${idgrupo}', '${grupo}','${materiaid}', '${docenteid}')"
                    val res = admin.Ejecutar(sentencia)


                }
                Toast.makeText(this, "CARGADO GRUPO", Toast.LENGTH_SHORT).show();
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error getAllProductos: " + error.message.toString() , Toast.LENGTH_LONG).show();
                Log.d("Zazueta",error.message.toString() )
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

}
