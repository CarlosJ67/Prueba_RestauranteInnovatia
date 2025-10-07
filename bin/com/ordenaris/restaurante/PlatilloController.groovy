package com.ordenaris.restaurante

import grails.rest.*
import grails.converters.*

class PlatilloController {
    static responseFormats = ['json', 'xml']
    
    def platilloService

    def listaPlatillos() {
        def respuesta = platilloService.listaPlatillos(params.tipoMenu)
        println respuesta
        return respond(respuesta.resp, status: respuesta.status)
    }
    
    def nuevoPlatillo() {
        def data = request.JSON
        
        if (!data.nombre) {
            return respond([success:false, mensaje: "El nombre es obligatorio"], status: 400)
        }
        if (data.nombre.soloNumeros()) {
            return respond([success:false, mensaje: "El nombre debe contener letras y no solo numeros"], status: 400)
        }
        if (data.nombre.size() > 80) {
            return respond([success:false, mensaje: "El nombre no puede ser tan largo"], status: 400)
        }
        
    
        if (data.costo == null) {
            return respond([success:false, mensaje: "El costo es obligatorio"], status: 400)
        }
        if (data.costo < 0 || data.costo > 60000) {
            return respond([success:false, mensaje: "El costo debe estar entre 0 y 60000"], status: 400)
        }
        
       
        if (data.descripcion && data.descripcion.size() > 100) {
            return respond([success:false, mensaje: "La descripción no puede ser tan larga"], status: 400)
        }
        
       
        if (!data.tipoMenu || !data.tipoMenu.id) {
            return respond([success:false, mensaje: "El tipo de menu es obligatorio"], status: 400)
        }
        
     
        if (data.platillosDisponibles != null && data.platillosDisponibles < -1) {
            return respond([success:false, mensaje: "Los platillos disponibles no pueden ser menores a -1"], status: 400)
        }
        
        def respuesta = platilloService.nuevoPlatillo(data)
        return respond(respuesta.resp, status: respuesta.status)
    }
    
    def informacionPlatillo() {
        if (params.uuid.size() != 32) {
            return respond([success:false, mensaje: "El uuid es invalido"], status: 400)
        }
        def respuesta = platilloService.informacionPlatillo(params.uuid)
        return respond(respuesta.resp, status: respuesta.status)
    }
    
    def editarPlatillo() {
        def data = request.JSON
        
        if (params.uuid.size() != 32) {
            return respond([success:false, mensaje: "El uuid es invalido"], status: 400)
        }
        
        if (data.nombre && data.nombre.soloNumeros()) {
            return respond([success:false, mensaje: "El nombre debe contener letras y no solo numeros"], status: 400)
        }
        if (data.nombre && data.nombre.size() > 80) {
            return respond([success:false, mensaje: "El nombre no puede ser tan largo"], status: 400)
        }
        
        if (data.costo != null && (data.costo < 0 || data.costo > 60000)) {
            return respond([success:false, mensaje: "El costo debe estar entre 0 y 60000"], status: 400)
        }
        
        if (data.descripcion && data.descripcion.size() > 100) {
            return respond([success:false, mensaje: "La descripción no puede ser tan larga"], status: 400)
        }
        
        def respuesta = platilloService.editarPlatillo(data, params.uuid)
        return respond(respuesta.resp, status: respuesta.status)
    }
    
    def cambiarDisponibilidad() {
        if (params.uuid.size() != 32) {
            return respond([success:false, mensaje: "El uuid es invalido"], status: 400)
        }
        
        def data = request.JSON
        if (data.platillosDisponibles == null) {
            return respond([success:false, mensaje: "Los platillos disponibles son obligatorios"], status: 400)
        }
        
        def respuesta = platilloService.cambiarDisponibilidad(params.uuid, data.platillosDisponibles)
        return respond(respuesta.resp, status: respuesta.status)
    }
    def platillosDisponibles() {
        def respuesta = platilloService.platillosDisponibles()
        return respond(respuesta.resp, status: respuesta.status)
    }
    def editarEstatusTipo() {
        def respuesta = platilloService.editarEstatusPlatillo(params.estatus, params.uuid)
        return respond(respuesta.resp, status: respuesta.status)
    }

    def paginarPlatillos(){
        if( !params.pagina ) {
            return respond([success:false, mensaje: "La pagina no puede ir vacio"], status: 400)
        }
        if( !params.pagina.soloNumeros() ) {
            return respond([success:false, mensaje: "La pagina debe contener solo numeros"], status: 400)
        }
        
        if( !params.columnaOrden ) {
            return respond([success:false, mensaje: "El columnaOrden no puede ir vacio"], status: 400)
        }
        if( !(params.columnaOrden in ["nombre", "status", "dateCreated", "fechaDisponible", "costo"]) ) {
            return respond([success:false, mensaje: "El columnaOrden solo puede ser: nombre, status, dateCreated, fechaDisponible, costo"], status: 400)
        }

        if( !params.orden ) {
            return respond([success:false, mensaje: "El orden no puede ir vacio"], status: 400)
        }
        if( !(params.orden in ["asc", "desc"]) ) {
            return respond([success:false, mensaje: "El orden solo puede ser: asc, desc"], status: 400)
        }

        if( !params.max ) {
            return respond([success:false, mensaje: "El max no puede ir vacio"], status: 400)
        }
        if( !params.max.soloNumeros() ) {
            return respond([success:false, mensaje: "El max debe contener solo numeros"], status: 400)
        }
        if( !(params.max.toInteger() in [ 2, 5, 10, 20, 50, 100 ]) ) {
            return respond([success:false, mensaje: "El max puede ser solo: 2, 5, 10, 20, 50, 100"], status: 400)
        }

        def respuesta = platilloService.paginarPlatillos( params.pagina.toInteger(), params.columnaOrden, params.orden, params.max.toInteger(), params.estatus?.toInteger(), params.query )
        return respond( respuesta.resp, status: respuesta.status )
    }
}