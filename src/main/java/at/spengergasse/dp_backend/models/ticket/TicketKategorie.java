package at.spengergasse.dp_backend.models.ticket;


import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.BaseEntity;
import at.spengergasse.dp_backend.validation.NumberGuard;
import at.spengergasse.dp_backend.validation.StringGuard;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Table(name = "ticket_kategorien")
@Getter
public class TicketKategorie extends BaseEntity
{

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    //JPA
    protected TicketKategorie(){}

    //Bussines
    public TicketKategorie(String name,  String description) throws ExeException {
        try {
            StringGuard.validateStringGuardNotBlack(name, "Der Kategoriename darf nicht leer sein.");
            //NumberGuard.validateBigDecinalNotNull(price, "Price nicht leer sein.");
            //NumberGuard.validateBigDecinalNotNegativ(price,"Price nicht Negativ sein.");
        } catch (RuntimeException e) {
            throw e;
        }catch (Throwable e){
            throw new RuntimeException(e);
        }

        this.name = name;
        //this.price = price;
        this.description = description;
    }

    public void setName(@NotBlank String name){ this.name= name;}
    public void setDescription(String description){ this.description = description;}

}
