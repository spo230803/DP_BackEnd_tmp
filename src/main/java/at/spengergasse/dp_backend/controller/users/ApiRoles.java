package at.spengergasse.dp_backend.controller.users;

import at.spengergasse.dp_backend.controller.BaseController;
import at.spengergasse.dp_backend.dto.users.RolesRequest;
import at.spengergasse.dp_backend.exceptions.ExeException;
import at.spengergasse.dp_backend.models.users.Roles;
import at.spengergasse.dp_backend.service.users.RolesService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Roles")
public class ApiRoles extends BaseController
{ }//end class

