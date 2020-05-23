package com.example.proyecto_mensajeria

class mensaje (id_mensaje :String, Emisor:String,Receptor:String,Mensaje:String) {

    var id_mensaje: String = ""
    var Emisor: String=""
    var Receptor: String =""
    var Mensaje: String = ""

    init{
        this.id_mensaje = id_mensaje
        this.Emisor = Emisor
        this.Receptor = Receptor
        this.Mensaje = Mensaje
    }

}