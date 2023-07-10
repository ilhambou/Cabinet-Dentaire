package ma.enset.hospitalapp.repository;

import ma.enset.hospitalapp.entities.Facture;
import ma.enset.hospitalapp.entities.SF;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactureRepository extends JpaRepository<Facture,Long> {
    Page<Facture> findByNomContains(String kw, Pageable pageable);
}
