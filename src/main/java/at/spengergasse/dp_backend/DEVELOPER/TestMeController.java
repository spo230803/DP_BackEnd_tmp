package at.spengergasse.dp_backend.DEVELOPER;

/*
        TEST LOGIN MI DISCOR

 */

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class TestMeController
{
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>>   me(@AuthenticationPrincipal OAuth2User user) {
        if(user==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user.getAttributes());
    }
}
