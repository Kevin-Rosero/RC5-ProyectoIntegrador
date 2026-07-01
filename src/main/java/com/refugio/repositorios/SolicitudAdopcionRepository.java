package com.refugio.repositorios;

import com.refugio.modelo.SolicitudAdopcion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SolicitudAdopcionRepository extends MongoRepository<SolicitudAdopcion, String> {
    List<SolicitudAdopcion> findByEstadoTramite(String estadoTramite);
}

