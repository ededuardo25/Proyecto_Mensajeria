package com.example.proyecto_mensajeria

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_grupo.*
import layout.grupo

class MainGrupo : AppCompatActivity() {

    var control:String=""
    var contra:String=""

    companion object{
        val EXTRA_CONTROL="control"
        val EXTRACONTRA="contraseña"
    }

    private lateinit var viewAdapter: GrupoAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    val grupoList: List<grupo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grupo)

        ////
    /*
        //PARA QUE PUEDA ENTRAR A ESTA DIRECTAMENTE
        val MEregistro=intent
        if(MEregistro!=null && MEregistro.hasExtra(MainAlumno.EXTRA_CONTROL) && MEregistro.hasExtra(
                MainAlumno.EXTRACONTRA
            )){
            control=MEregistro.getStringExtra(MainAlumno.EXTRA_CONTROL)
            contra=MEregistro.getStringExtra(MainAlumno.EXTRACONTRA)


/*            val nombre:String=MEregistro.getStringExtra(EXTRANOMBRE)
            txtbienvenida.text="BIENVENIDO SEAS"+nombre.toString()*/

        }else{
            val act1: Intent = Intent(this,MainLogin::class.java)
            startActivity(act1)
        }

     */

    /////


        viewManager = LinearLayoutManager(this)
        viewAdapter = GrupoAdapter(grupoList, this, { grup: grupo -> onItemClickListener(grup) })

        rv_grupo_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(this@MainGrupo, DividerItemDecoration.VERTICAL))
        }

        // Metodo para implementar la eliminación de un estudiante, cuando el ususario da un onswiped en
        // el recyclerview
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                val grup= viewAdapter.getTasks()
                val admin = AdminBD(baseContext)
                if (admin.Ejecutar("DELETE FROM grupo WHERE id_grupo=" + grup[position].id_grupo) == 1){
                    retrieveGrupos()
                }
            }
            //El recyclerView
        }).attachToRecyclerView(rv_grupo_list)
    }

    // Evento clic cuando damos clic en un elemento del Recyclerview
    private fun onItemClickListener(Grup: grupo) {
        Toast.makeText(this, "Clicked item" + Grup.docenteid, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        retrieveGrupos()
    }

    private fun retrieveGrupos() {
        val grupox = getGrupos()
        viewAdapter.setTask(grupox!!)
    }

    fun getGrupos(): MutableList<grupo>{
        var grupos:MutableList<grupo> = ArrayList()
        val admin = AdminBD(this)

        //                                          0       1       2      3
        val tupla = admin.Consultar("SELECT id_grupo,grupo,nocontrol,docenteid FROM grupo")
        while (tupla!!.moveToNext()) {
            val idGrup = tupla.getInt(0)
            val grup = tupla.getString(1)
            val nocon = tupla.getString(2)
            val matid = tupla.getString(3)
            val docid = tupla.getString(4)

            grupos.add(grupo(idGrup,grup,nocon,matid,docid))
        }
        tupla.close()
        admin.close()
        return grupos
    }


}
