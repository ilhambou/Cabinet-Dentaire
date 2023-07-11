package ma.enset.hospitalapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String summary;

    //private String description;

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDateTime;
    @NotNull
    private String notif;
    @NotNull
    private StatusRDV Etat;
    @NotNull

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(updatable = false)

    private Patient patient;

    @ManyToMany
    @JoinTable(
            name = "event_act",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "act_id")
    )
    private List<Act> acts = new ArrayList<>();
    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL)
    Set<Consultation> consultations;

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + patient.getNom() + '\'' +" } ";
    }


    public String getSummary() {
        return summary;
    }

}