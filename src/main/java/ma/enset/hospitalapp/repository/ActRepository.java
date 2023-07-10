package ma.enset.hospitalapp.repository;

import ma.enset.hospitalapp.entities.Act;
import ma.enset.hospitalapp.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActRepository extends JpaRepository<Act,Long> {
    Page<Act> findByDescriptionContains(String kw, Pageable pageable);

}
