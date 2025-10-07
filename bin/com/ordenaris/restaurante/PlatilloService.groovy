package com.ordenaris.restaurante

import grails.gorm.transactions.Transactional

@Transactional
class PlatilloService {
    
    def nuevoPlatillo(data){
        try{
            if (data.costo){
                data.costo = (data.costo *100) as Integer
            }
            if (data.fechaDisponible){
                data.status = 0
            }else{
                data.status = 1
            }
            def platillo = new Platillo(data)
            if (platillo.save(flush:true)){
                return[ resp: platillo, status: 201]
            }else{
                return[ resp: platillo.errors, status: 400] 
            }
        }catch(e){
            return[
                resp:[success:false, mensaje: e.getMessage()], 
                status: 500 
            ]
        }
    }
    
    def listaPlatillos(tipo) {
        try {
            def platillos
            if(tipo){
                def menu = TipoMenu.findById(tipo.toInteger())
                platillos = Platillo.findAllByStatusNotEqualsAndTipoMenu(2, menu)
            }else{
                platillos = Platillo.findAllByStatusNotEquals(2)
            }

        def platillosFiltrados = platillos.collect { platillo ->
            return [
                id: platillo.id,
                tipoMenu: [id: platillo.tipoMenu?.id, nombre: platillo.tipoMenu?.nombre],
                descripcion: platillo.descripcion,
                status: platillo.status,
                nombre: platillo.nombre,
                costo: platillo.costo,
                platillosDisponibles: platillo.platillosDisponibles,
                fechaDisponible: platillo.fechaDisponible
            ]
        }
        
        return [resp: platillosFiltrados, status: 200]
        } catch (Exception e) {
            return [resp: [success: false, mensaje: e.getMessage()], status: 500]
        }
    }

    def editarPlatillo(data, uuid) {
        try{
            def platillo= Platillo.findByUuid(uuid)
            if(!platillo){
                return [resp: [success:false, mensaje: "El platillo no existe"], status: 404]
            }
            if (data.costo){
                data.costo = (data.costo *100) as Integer
            }
            platillo.properties = data
            platillo.save()
            return [resp: [success:true, data: platillo], status: 200]
        }catch(e){
            return [resp: [success:false, mensaje: e.getMessage()], status: 500]
        }
    }

    def editarEstatusPlatillo(estatus, uuid) {
        try{
            def platillo= Platillo.findByUuid(uuid)
            if(!platillo){
                return [resp: [success:false, mensaje: "El platillo no existe"], status: 404]
            }
            platillo.status = estatus
            platillo.save()
            return [resp: [success:true, data: platillo], status: 200]
        }catch(e){
            return [resp: [success:false, mensaje: e.getMessage()], status: 500]
        }
    }

    def cambiarDisponibilidad(uuid, platillosDisponibles) {
            try{
                def platillo= Platillo.findByUuid(uuid)
                if(!platillo){
                    return [resp: [success:false, mensaje: "El platillo no existe"], status: 404]
                }
                platillo.platillosDisponibles = platillosDisponibles
                platillo.save()
                return [resp: [success:true, data: platillo], status: 200]
            }catch(e){
                return [resp: [success:false, mensaje: e.getMessage()], status: 500]
            }
        }


    def informacionPlatillo(uuid){
        def platillo = Platillo.findByUuid(uuid)
        def lista = []
        if(!platillo) {
            return [
                resp: [ success:false, mensaje: "El platillo no existe" ],
                status: 404
            ]
        }
        if( platillo.status == 2 ) {
            return [
                resp: [ success:false, mensaje: "El platillo ha sido eliminado" ],
                status: 404
            ]
        }

        return [
            resp: [ success:true, data: platillo ],
            status: 200
        ]
    }

def platillosDisponibles() {
    try{
        def hoy = new Date()
        def siempreDisponibles = Platillo.findAllByStatusAndFechaDisponible(1, null)
        def disponiblesHoy = Platillo.findAllByFechaDisponible(hoy)
        def platillos = siempreDisponibles + disponiblesHoy
        return [
            resp: [ success: true, data: platillos ],
            status: 200
        ]
    }catch(e){
        return [
            resp: [ success: false, mensaje: e.getMessage() ],
            status: 500
        ]
    }
}

    def paginarPlatillos( pagina, columnaOrden, orden, max, estatus, query ){
        try{

            def offset = pagina * max - max
            def list = Platillo.createCriteria().list{
                //isNull("tipoPrincipal")
                if( estatus ) {
                    eq("status", estatus)
                }
                ne("status", 2)
                if( query ) {
                    like("nombre", "%${query}%")
                }
                firstResult(offset)
                maxResults(max)
                order( columnaOrden, orden )
            }
            return [
                resp: [ success: true, data: list ],
                status: 200
            ]
        }catch(e){
            return [
                resp: [ success: false, mensaje: e.getMessage() ],
                status: 500
            ]
        }
    }
    
}
