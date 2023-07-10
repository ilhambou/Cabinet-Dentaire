package ma.enset.hospitalapp.repository;

import ma.enset.hospitalapp.entities.SF;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SFRepository extends JpaRepository<SF,Long> {
    Page<SF> findByNomContains(String kw, Pageable pageable);
}
