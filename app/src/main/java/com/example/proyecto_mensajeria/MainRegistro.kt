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
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registro.*
import org.json.JSONObject


class MainRegistro : AppCompatActivity() {

    val IP = "http://192.168.56.1"
    var VarControl: String = ""
    var VarNom: String = ""
    var VarSem: String = ""
    var VarPass: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
    }

    //El usuario se guarda en la BD local
    //No se guarda a MySQL
    // Para eso es agregarRegistro(v)
    fun btnregistrar(v: View){
        if (RegControl.text.isEmpty() || RegNombre.text.isEmpty()||RegSemestre.text.isEmpty()||RegContra.text.isEmpty()){
            Toast.makeText(this, "No puedes dejar vació ninguna celda", Toast.LENGTH_SHORT).show();

        }else{
            val control = RegControl.text.toString()
            val nom = RegNombre.text.toString()
            val sem = RegSemestre.text.toString()
            val contra=RegContra.text.toString()
            val consultaInsert = "Insert into alumno(no_control,nombre,semestre,password) values('$control','$nom','$sem','$contra')" //consulta: Variable de la Sentencia
            val admin = AdminBD(this)                                          //admin: Variable que trabaja con la BD
            if (admin.Ejecutar(consultaInsert)==1){
//REGISTRAR UN USUARIO
                Toast.makeText(this,"Usuario agregado", Toast.LENGTH_SHORT).show();
                val actHome= Intent(this,MainAlumno::class.java)
                actHome.putExtra(MainAlumno.EXTRA_CONTROL,control)
                actHome.putExtra(MainAlumno.EXTRACONTRA,contra)

                startActivity(actHome)
                //Guardandolo a la BD
                agregarRegistro(v)
            }else{
//NO SE ENCUENTRA EL USUARIO
                RegControl.setError("YA EXISTE ESTE NUMERO DE USUARIO")
                Toast.makeText(this, "No se agrego usuario", Toast.LENGTH_SHORT).show();
                RegControl.requestFocus()
            }
        }
    }


    //Codigo para poder AGREGAR los registros a la BD_MySQL
    fun agregarRegistro(v:View){
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


            /*
            Toast.makeText(this,"Usuario agregado", Toast.LENGTH_SHORT).show();
            val actHome= Intent(this,MainAlumno::class.java)
            actHome.putExtra(MainAlumno.EXTRA_CONTROL,control)
            actHome.putExtra(MainAlumno.EXTRACONTRA,contra)
            startActivity(actHome)
            */

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
    //




}
