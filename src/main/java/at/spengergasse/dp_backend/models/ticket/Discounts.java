package at.spengergasse.dp_backend.models.ticket;

import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.BaseEntity;
import at.spengergasse.dp_backend.validation.NumberGuard;
import at.spengergasse.dp_backend.validation.StringGuard;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.pqc.crypto.newhope.NHSecretKeyProcessor;

import java.math.BigDecimal;

@Entity
@Table(name = "discounts" )
@Getter
public class Discounts extends BaseEntity
{
    @Size(min = 5, max = 50)
    @Column(name = "code",  nullable = false,  unique = true)
    private String code;

    @Min(0)
    @Max(100)
    @Column(name = "percentage", nullable = false)
    private Integer percentage;

    @Column(name = "conditions", nullable = true)
    private String conditions;

    //JPA
    public Discounts(){}

    //Bussinens
    public Discounts(String code, Integer percentage, String conditions) throws ExeException
    {
        try{
            StringGuard.validateStringGuardNotBlack(code, "Code kan nicht leer sein!");
            NumberGuard.validateLongNotNegativ((long) percentage , "percentage muss positive sein!");
        }catch(ExeException e){ throw  e;}
        this.code = code;
        this.percentage = percentage;
        this.conditions = conditions;
    }

    public void setCode(@NotBlank String code){this.code = code;}
    public void setPercentage(@NotNull @Min(0) Integer percentage){this.percentage = percentage;}
    public void setConditions(String conditions){ this.conditions = conditions;}

}//end class
