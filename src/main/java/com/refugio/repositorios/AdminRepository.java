package com.refugio.repositorios;

import com.refugio.modelo.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdminRepository extends MongoRepository<Admin, String> {
    Optional<Admin> findByCorreo(String correo);
    Optional<Admin> findByCedula(String cedula);
}

