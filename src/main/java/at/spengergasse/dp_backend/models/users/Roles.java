package at.spengergasse.dp_backend.models.users;

import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.BaseEntity;
import at.spengergasse.dp_backend.validation.StringGuard;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Entity
@Getter
//@Table(name = "roles")
public class Roles  extends BaseEntity
{}//end Class
