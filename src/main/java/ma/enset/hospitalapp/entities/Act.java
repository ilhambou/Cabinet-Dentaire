package ma.enset.hospitalapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Act {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @NotEmpty
        private String libelle;
        //soit numbeer soit double
        @NonNull
        @Digits(integer = 10, fraction = 2, message = "Le prix doit être un nombre valide")
        @Column(columnDefinition = "DECIMAL(10,2)")
        private Double prix;
        @NotEmpty
        private String description;
        @ManyToMany( cascade = CascadeType.ALL)
        private List<Event> events = new ArrayList<>();

         @OneToMany(mappedBy = "act")
         private Set<Consultation> events2;
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
                return "Act{" +
                        "id=" + id +
                        ", libelle='" + libelle + '\'' +
                        ", prix=" + prix +
                        ", description='" + description + '\'' +
                        '}';
        }


}
