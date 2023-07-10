package ma.enset.hospitalapp.repository;

import ma.enset.hospitalapp.entities.Ordonnance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdonnanceRepository extends JpaRepository<Ordonnance,Long> {
    Page<Ordonnance> findByDescriptionContains(String kw, Pageable pageable);
}
