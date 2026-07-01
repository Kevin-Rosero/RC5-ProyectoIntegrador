package com.refugio.repositorios;

import com.refugio.modelo.Mascota;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MascotaRepository extends MongoRepository<Mascota, String> {
    Optional<Mascota> findByNombre(String nombre);
    List<Mascota> findByEstado(String estado);
    List<Mascota> findByEspecie(String especie);
    List<Mascota> findByEdadLessThanEqual(int edad);
}

