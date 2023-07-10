package ma.enset.hospitalapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Patient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty @Size(min = 4, max = 20)
    private String nom;
    @NotEmpty @Size(min = 4, max = 20)
    private String prenom;
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;
    @Column(unique = true)
    @Email(message = "Adresse email invalide")
    @NotEmpty
    private String email;
    @NotEmpty
    private String sexe;
    @NotEmpty(message = "Phone number cannot be empty")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be a 10-digit number")
    private String tel;
    @NotEmpty
    private String CIN;

    @OneToMany(mappedBy = "patient", orphanRemoval = true,cascade = CascadeType.PERSIST, fetch=FetchType.EAGER)
    private Collection<Event> events = new ArrayList<>();



}
