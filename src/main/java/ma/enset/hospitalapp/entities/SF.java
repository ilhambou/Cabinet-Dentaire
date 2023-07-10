/**package ma.enset.hospitalapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SF {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "totalapayer")
    private Double totalapayer;

    private Double paye;
    private Double reste;
    @Column(name = "nom")
    private String nom;
    @OneToOne
    @JoinColumn(name = "consultation_id")
    private Consultation consultation;


    @OneToMany(mappedBy = "sf", cascade = CascadeType.ALL)
    private List<Facture> factures=new ArrayList<>();

    public Double getTotalapayer() {
        if (this.consultation != null && this.consultation.getAct() != null) {
            double actPrix = this.consultation.getAct().getPrix();
            double consultationPourcentage = this.consultation.getPourcentage();
            return actPrix - (consultationPourcentage * actPrix);
        }
        return 0.0;    }

    public Double getReste() {
        return this.getTotalapayer()-this.paye;
    }

   /** public void createFacture() {
        Facture facture = new Facture();
        facture.setNom("Nom de la facture");
        facture.setSf(this);

        if (this.getReste() == 0) {
            facture.setEtat(Etat.Payé);
        } else if (this.getReste() == this.getTotalapayer()) {
            facture.setEtat(Etat.Non_Payé);
        } else {
            facture.setEtat(Etat.Partielement_Payé);
        }

        this.getFactures().add(facture);
    }**/
/**}**/

package ma.enset.hospitalapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.enset.hospitalapp.repository.FactureRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SF {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "totalapayer")
    private Double totalapayer;

    private Double paye;
    private Double reste;
    @Column(name = "nom")
    private String nom;
    @OneToOne
    @JoinColumn(name = "consultation_id")
    private Consultation consultation;

    @OneToMany(mappedBy = "sf", cascade = CascadeType.ALL)
    private List<Facture> factures=new ArrayList<>();


    public Double getTotalapayer() {
        if (this.consultation != null && this.consultation.getAct() != null) {
            double actPrix = this.consultation.getAct().getPrix();
            double consultationPourcentage = this.consultation.getPourcentage();
            return actPrix - (consultationPourcentage * actPrix);
        }
        return 0.0;    }

    public Double getReste() {
        return this.getTotalapayer()-this.paye;
    }

    //new
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        // Add other relevant fields to the hash code calculation
        return result;
    }


}