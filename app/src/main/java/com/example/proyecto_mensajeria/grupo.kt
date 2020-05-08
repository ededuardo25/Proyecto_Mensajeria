package layout
class grupo (id_grupo :Int, grupo:String,nocontrol:String,materiaid:String,docenteid : String) {


    var id_grupo: Int = 0
    var grupo: String=""
    var nocontrol: String =""
    var materiaid: String = ""
    var docenteid :String=""

    init{
        this.id_grupo = id_grupo
        this.grupo = grupo
        this.nocontrol = nocontrol
        this.materiaid = materiaid
        this.docenteid = docenteid
    }

}



/*
class alumno (no_control :String, nombre:String,Semestre:Int,Password:String) {

    var no_control: String = ""
    var nombre: String=""
    var semestre: Int =0
    var password: String = ""

    init{
        this.no_control = no_control
        this.nombre = nombre
        this.semestre = semestre
        this.password = password
    }

}
 */