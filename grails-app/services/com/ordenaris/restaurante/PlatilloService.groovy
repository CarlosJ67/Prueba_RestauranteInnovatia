package com.ordenaris.restaurante

import grails.gorm.transactions.Transactional

@Transactional
class PlatilloService {
    
    def nuevoPlatillo(data){
        try{
            if (data.costo){
                data.costo = (data.costo *100) as Integer
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
    
    def listaPlatillos() {
        try {
            def platillos = Platillo.findAllByStatus(1)
            return [resp: platillos, status: 200]
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
            def platillos= Platillo.createCriteria().list{
                eq("status", 1)
                or{
                    isNull("fechaDisponible")
                    between('fechaDisponible', 
                    hoy.clearTime(), 
                    hoy.clearTime() + 1)
                }
            }
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

    /*def paginarPlatillos( pagina, columnaOrden, orden, max, estatus, query ){
        try{

            def offset = pagina * max - max
            def list = Platillo.createCriteria().list{
                isNull("tipoPrincipal")
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
            }.collect{tipo -> mapTipoMenu(tipo, [])}
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
    }*/

    
}
