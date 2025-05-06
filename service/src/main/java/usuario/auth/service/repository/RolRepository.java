// RolRepository.java
package usuario.auth.service.repository;

import usuario.auth.service.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Rol findByNombre(String nombre);
}
