package restaurante

class UrlMappings {

    static mappings = {
        group "/menu", {
            group "/tipo", {
                post "/nuevo"(controller: "menu", action: "nuevoTipo")
                get "/lista"(controller: "menu", action: "listaTipos")
                get "/ver"(controller: "menu", action: "paginarTipos")
                group "/$uuid", {
                    get "/informacion"(controller: "menu", action: "informacionTipo")
                    patch "/editar"(controller: "menu", action: "editarTipo")
                    patch "/activar"(controller: "menu", action: "editarEstatusTipo"){
                        estatus = 1
                    }
                    patch "/desactivar"(controller: "menu", action: "editarEstatusTipo") {
                        estatus = 0
                    }
                    delete "/eliminar"(controller: "menu", action: "editarEstatusTipo") {
                        estatus = 2
                    }
                }
            }
        }

        group "/platillo", {
            post "/nuevo"(controller: "platillo", action: "nuevoPlatillo")
            get "/lista"(controller: "platillo", action: "listaPlatillos")
            get "/disponibles"(controller: "platillo", action: "platillosDisponibles")
            get "/ver"(controller: "platillo", action: "paginarPlatillos")
            group "/$uuid", {
                get "/informacion"(controller: "platillo", action: "informacionPlatillo")
                patch "/editar"(controller: "platillo", action: "editarPlatillo")
                patch "/cambiarDisponibilidad"(controller: "platillo", action: "cambiarDisponibilidad")
                patch "/activar"(controller: "platillo", action: "editarEstatusTipo"){
                    estatus = 1
                }
                patch "/desactivar"(controller: "platillo", action: "editarEstatusTipo") {
                    estatus = 0
                }
                delete "/eliminar"(controller: "platillo", action: "editarEstatusTipo") {
                    estatus = 2
                }
            }
        }

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
