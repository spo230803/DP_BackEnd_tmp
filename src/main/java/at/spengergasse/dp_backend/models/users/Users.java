package at.spengergasse.dp_backend.models.users;

import at.spengergasse.dp_backend.models.BaseEntity;
import at.spengergasse.dp_backend.validation.NumberGuard;
import at.spengergasse.dp_backend.validation.StringGuard;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Entity
@Table(name = "users")
@Getter
public class Users extends BaseEntity
{
    @Column(unique = true, name = "discord_id",  nullable = false)
    private Long discordId;

    @Size(min = 1, max = 100)
    @Column(name="name", nullable = false, length = 100)
    private String name;

    @Size(max = 255)
    @Column(name = "email", nullable = true, length = 255)
    private String email;

    //JPA
    protected Users(){}

    public Users(Long discordId, String name, String email) throws RuntimeException
    {
        try{
            StringGuard.validateStringGuardNotBlack(name,"Name cannot be blank");
            NumberGuard.validateLongNotNegativ(discordId ,"Discord ID cannot be negative or null");
        }catch (RuntimeException ex) {
            throw ex;
        }catch (Throwable ex){
            throw new RuntimeException(ex);
        }
        this.discordId = discordId;
        this.name = name;
        this.email = email;
    }//end users
}//end class
