package com.refugio.repositorios;

import com.refugio.modelo.SolicitudAdopcion;
import com.refugio.modelo.Adoptante;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SolicitudAdopcionRepository extends MongoRepository<SolicitudAdopcion, String> {
    List<SolicitudAdopcion> findByEstadoTramite(String estadoTramite);
    List<SolicitudAdopcion> findByAdoptante(Adoptante adoptante);
    List<SolicitudAdopcion> findByAdoptanteAndEstadoTramite(Adoptante adoptante, String estadoTramite);
    List<SolicitudAdopcion> findByAdoptanteId(String adoptanteId);
}

