package com.refugio.ui;

import com.refugio.modelo.Mascota;
import com.refugio.servicios.MascotaService;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.H1;
import org.springframework.beans.factory.annotation.Autowired;

@Route("catalogo") // Esta es la ruta: tuapp.com/catalogo
public class CatalogoClienteView extends VerticalLayout {

    private final MascotaService mascotaService;
    private final FlexLayout contenedorTarjetas = new FlexLayout();

    @Autowired
    public CatalogoClienteView(MascotaService mascotaService) {
        this.mascotaService = mascotaService;

        // Configuración de estilo global (Blanco hueso)
        getStyle().set("background-color", "#F8F9F4");
        getStyle().set("padding", "20px");
        setSizeFull();

        add(new H1("Nuestros amigos disponibles"));

        // Configuramos el contenedor para que las tarjetas se acomoden solas
        contenedorTarjetas.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        contenedorTarjetas.setWidthFull();

        add(contenedorTarjetas);
        cargarMascotas();
    }

    private void cargarMascotas() {
        // Obtenemos la lista desde el servicio
        for (Mascota m : mascotaService.obtenerMascotasDisponibles()) {
            // Aquí llamarás a tu clase TarjetaMascota
            TarjetaMascota tarjeta = new TarjetaMascota(m);
            contenedorTarjetas.add(tarjeta);
        }
    }
}