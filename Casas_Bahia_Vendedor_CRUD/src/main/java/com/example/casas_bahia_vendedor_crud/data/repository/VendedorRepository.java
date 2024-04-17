package com.example.casas_bahia_vendedor_crud.data.repository;

import com.example.casas_bahia_vendedor_crud.data.Vendedor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VendedorRepository extends MongoRepository<Vendedor, String> {
    Optional<Vendedor> findByMatricula(String matricula);
}
