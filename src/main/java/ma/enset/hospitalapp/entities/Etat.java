package ma.enset.hospitalapp.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



public enum Etat {
    Payé,Non_Payé,Partielement_Payé
}
