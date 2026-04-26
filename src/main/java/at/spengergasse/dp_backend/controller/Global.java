/**
 *  Cerato il       2025-11-26
 *  Aggiornato      2025-11-26
 *  Autoere         S. Giorgio Matteo
 *  Versione        1.0.0
 *
 *  API per
 *
 */

package at.spengergasse.dp_backend.controller;

import at.spengergasse.dp_backend.system.Autor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import at.spengergasse.dp_backend.GlobalConstat;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/sys")
public class Global extends BaseController
{

    private final GlobalConstat globalConstat = new GlobalConstat();
    private final JdbcTemplate jdbcTemplate;


    @GetMapping("/ver")
    public  String ver()
    {
        return globalConstat.getSysVersion();
    }

    @GetMapping("/verWhitDate")
    public String verWhitDate(){return globalConstat.getSysVersion() + " von "+globalConstat.getSysLatupdate();}

    @GetMapping("/verDate")
    public String verDate() {return globalConstat.getSysLatupdate();}

    @GetMapping("/verinfo")
    public List<String> verInfo()
    {
        return List.of(globalConstat.getSysVersion().split("."));
    }

    @GetMapping("/hello")
    public String hello()
    {
        return "<h1>Hello World!</h1><h2>Von BackEnd</h2>By <i>"+ globalConstat.getSysAppName()+"</i> (Version : "+ globalConstat.getSysVersion()+")";
    }

    @GetMapping("/appname")
    public String appName(){return globalConstat.getSysAppName();}

    @GetMapping("/autors")
    public List<Autor> autors() {return globalConstat.getSysAutor();}

    @GetMapping("/db")
    public Map<String, Object> testDBVerbidung()
    {
        return jdbcTemplate.queryForMap("""
            SELECT
              now()                AS server_time,
              current_user         AS db_user,
              inet_server_addr()   AS server_ip
        """);
    }


}//end Global
