package com.example.proyecto_mensajeria

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception

class AdminBD(context: Context) : SQLiteOpenHelper(context, DataBase, null, 1) {
    companion object {
        val DataBase = "Alumnos"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table alumno(no_control text primary key,nombre text,semestre text,password text)")
        db?.execSQL("create table materia(id_materia text primary key,nom_materia text,nom_maestro text)")
        db?.execSQL("create table docente(id_doc text primary key,nom_docente text,password text)")
        db?.execSQL("create table grupo(id_grupo text primary key,grupo text,nocontrol text,materiaid text,docenteid text," +
                "foreign key(nocontrol) references alumno(no_control),foreign key(materiaid) references materia(id_materia)," +
                "foreign key(docenteid) references docente(id_doc))")


    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    fun Ejecutar(sentencia: String): Int {
        try {
            val db = this.writableDatabase
            db.execSQL(sentencia)
            return 1
        } catch (ex: Exception) {
            return 0
        }
    }

    fun Consultar(query: String): Cursor? {
        try {
            val db = this.readableDatabase
            return db.rawQuery(query, null)
        } catch (ex: java.lang.Exception) {
            return null
        }
    }

}