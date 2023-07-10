package ma.enset.hospitalapp.repository;

import ma.enset.hospitalapp.entities.Act;
import ma.enset.hospitalapp.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {
    Page<Event> findBySummaryContains(String kw, Pageable pageable);

}
