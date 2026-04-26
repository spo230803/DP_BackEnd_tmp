
/*

            VBGIO TEST

 */


package at.spengergasse.dp_backend.DEVELOPER;

import at.spengergasse.dp_backend.models.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "test_users")
@Getter
public class TestUser extends BaseEntity {

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    protected TestUser() {}

    public TestUser(String username, String email) {
        this.username = username;
        this.email = email;
    }


}