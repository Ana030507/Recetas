package co.edu.usco.springBoot_securityMemory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.usco.springBoot_securityMemory.model.Receta;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {
    // No es necesario escribir nada, ya incluye métodos como:
    // findAll(), save(), deleteById(), findById(), etc.
}
