package ma.enset.hospitalapp.repository;

import ma.enset.hospitalapp.entities.Act;
import ma.enset.hospitalapp.entities.CalendarObj;
import ma.enset.hospitalapp.entities.Consultation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationRepository extends JpaRepository<Consultation,Long> {
    Page<Consultation> findBydescriptionContains(String kw, Pageable pageable);

}
