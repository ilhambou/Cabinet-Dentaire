package ma.enset.hospitalapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ordonnance")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ordonnance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String Description;
   //@Enumerated(EnumType.STRING)
    private String medicament;

    @OneToOne
    @JoinColumn(name="id_consultation")
    private Consultation consultation;
}
