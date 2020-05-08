package com.example.proyecto_mensajeria

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_alumno.*
import kotlinx.android.synthetic.main.activity_grupo.*
import layout.grupo

class MainAlumno : AppCompatActivity() {

    var control:String=""
    var contra:String=""

    companion object{
        val EXTRA_CONTROL="control"
        val EXTRACONTRA="contraseña"
    }


    private lateinit var viewAdapter: AlumnoAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    val alumnoList: List<alumno> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alumno)

        ////
        //PARA QUE PUEDA ENTRAR A ESTA DIRECTAMENTE
        val MEregistro=intent
        if(MEregistro!=null && MEregistro.hasExtra(EXTRA_CONTROL) && MEregistro.hasExtra(EXTRACONTRA)){
            control=MEregistro.getStringExtra(EXTRA_CONTROL)
            contra=MEregistro.getStringExtra(EXTRACONTRA)


/*            val nombre:String=MEregistro.getStringExtra(EXTRANOMBRE)
            txtbienvenida.text="BIENVENIDO SEAS"+nombre.toString()*/

        }else{
            val act1: Intent = Intent(this,MainLogin::class.java)
            startActivity(act1)
        }
    ////


    ////
        viewManager = LinearLayoutManager(this)
        viewAdapter = AlumnoAdapter(alumnoList, this, { alu: alumno -> onItemClickListener(alu) })

        rv_alumno_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(this@MainAlumno, DividerItemDecoration.VERTICAL))
        }

        // Metodo para implementar la eliminación de un estudiante, cuando el ususario da un onswiped en
        // el recyclerview
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val alu= viewAdapter.getTasks()
                val admin = AdminBD(baseContext)
                if (admin.Ejecutar("DELETE FROM alumno WHERE no_control=" + alu[position].no_control) == 1){
                    retrieveAlumnos()
                }
            }
            //El recyclerView
        }).attachToRecyclerView(rv_grupo_list)
    }


//DIRIGIRSE/REALIZAR ACCION
    // Evento clic cuando damos clic en un elemento del Recyclerview
    private fun onItemClickListener(Alum: alumno) {
        Toast.makeText(this, "Clicked item" + Alum.nombre, Toast.LENGTH_LONG).show()
        val acti:Intent=Intent(this,MainGrupo::class.java)
        startActivity(acti)
    }

    override fun onResume() {
        super.onResume()
        retrieveAlumnos()
    }

    private fun retrieveAlumnos() {
        val alumnox = getAlumnos()
        viewAdapter.setTask(alumnox!!)
    }

    fun getAlumnos(): MutableList<alumno>{
        var alumnos:MutableList<alumno> = ArrayList()
        val admin = AdminBD(this)

        //                                          0       1       2      3
        val tupla = admin.Consultar("SELECT no_control,nombre,semestre,password FROM alumno")
        while (tupla!!.moveToNext()) {
            val nocon = tupla.getString(0)
            val nombre = tupla.getString(1)
            val sem = tupla.getInt(2)
            val pass = tupla.getString(3)

            alumnos.add(alumno(nocon,nombre,sem,pass))
        }
        tupla.close()
        admin.close()
        return alumnos
    }
        ////
}




    /*fun btnAgregarMat(view: View) {
        val acti:Intent=Intent(this,MainMateria::class.java)
        startActivity(acti)
    }

    fun btnAgregarAlu(view: View) {
        val acti:Intent=Intent(this,MainAlumno::class.java)
        startActivity(acti)
    }
     */

