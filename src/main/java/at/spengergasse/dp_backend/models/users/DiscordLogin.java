package at.spengergasse.dp_backend.models.users;

import at.spengergasse.dp_backend.models.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name ="discord_logins")
@Getter
@Setter
public class DiscordLogin extends BaseEntity
{

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="user_id", nullable = false, unique = false)
    private Users usersId; //Unsere ID von UNSERE DB  (nicht von discord)

    @Column(name = "login_time")
    private LocalDateTime loginTime;


    //JPA
    protected  DiscordLogin(){
        this.loginTime = LocalDateTime.now();
    }


    public  DiscordLogin(Users users)
    {
        this.usersId= users;
        this.loginTime = LocalDateTime.now();
    }

}//end class
