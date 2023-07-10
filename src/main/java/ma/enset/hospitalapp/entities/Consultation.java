package ma.enset.hospitalapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "event_act")
public class Consultation implements Serializable {
 //@EmbeddedId
 //private EventActkey id = new EventActkey(); // Initialize the embedded ID
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @ManyToOne

 @JoinColumn(name="act_id")
 private Act act;
 @ManyToOne

 @JoinColumn(name="event_id")
 private Event event;


     private Date dateConsultation;
     private String description;
    @Column(name="pourcentage",nullable = false, columnDefinition = "double default 0.0")
    private Double pourcentage=0.0;
    @OneToOne( mappedBy = "consultation",
            cascade = { CascadeType.ALL} )
    private SF sf;
    @OneToOne( mappedBy = "consultation",
            cascade = { CascadeType.ALL} )
    private Ordonnance ordonnance;

    public Event getEvent() {
        return event;
    }

//    public Date getDateConsultation() {
//        return getEvent().getStartDate();
//    }

    //new
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        // Add other relevant fields to the hash code calculation
        return result;
    }
    @Override
    public String toString() {
        return "Consultation{" +
                "id=" + id +
                ", act=" + (act != null ? act.getId() : null) +
                ", event=" + (event != null ? event.getId() : null) +
                ", dateConsultation=" + dateConsultation +
                ", description='" + description + '\'' +
                ", pourcentage=" + pourcentage +
                '}';
    }

}
