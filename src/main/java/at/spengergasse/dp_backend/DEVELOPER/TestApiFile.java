package at.spengergasse.dp_backend.DEVELOPER;


import at.spengergasse.dp_backend.system.config.UploadProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;

@RestController
@RequestMapping("/api/sys/fs")
public class TestApiFile
{
    private final UploadProperties props;

    public TestApiFile(UploadProperties props) {
        this.props = props;
    }

    @GetMapping("/write/{text}")
    public ResponseEntity<String> write(@PathVariable String text) throws Exception {
        if(text == null || text.isEmpty()){
            text = "This is a test file.";
        }
        Path dir = Paths.get(props.getUploadDir()).toAbsolutePath().normalize();
        Files.createDirectories(dir);

        Path file = dir.resolve("fs_test.txt");
        Files.writeString(file, text + System.lineSeparator(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

        return ResponseEntity.ok("OK scritto: " + file);
    }

    @GetMapping("/read")
    public ResponseEntity<String> read(){
        try {
            System.out.println("Debug : " + this.props.getUploadDir());
            Path file = Paths.get(props.getUploadDir()).toAbsolutePath().normalize().resolve("fs_test.txt");
            if (!Files.exists(file)) {
                return ResponseEntity.notFound().build();
            }
            String content = Files.readString(file, StandardCharsets.UTF_8);
            return ResponseEntity.ok(content);
        }catch (Exception e){
            return ResponseEntity.ok().body("ERROR : Exceptio "+ e.getClass().getName()+"<br> Messagge : " +e.getMessage());
        }
    }
}//end class
