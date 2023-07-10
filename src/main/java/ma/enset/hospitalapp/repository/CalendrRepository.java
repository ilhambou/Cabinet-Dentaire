package ma.enset.hospitalapp.repository;

import ma.enset.hospitalapp.entities.CalendarObj;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendrRepository extends JpaRepository<CalendarObj,Long> {
}
