package com.refugio.repositorios;

import com.refugio.modelo.Adoptante;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdoptanteRepository extends MongoRepository<Adoptante, String> {
    Optional<Adoptante> findByCorreo(String correo);
    Optional<Adoptante> findByCedula(String cedula);
}

