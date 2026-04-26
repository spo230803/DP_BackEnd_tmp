package at.spengergasse.dp_backend.system;

import at.spengergasse.dp_backend.GlobalConstat;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

public class Sessions extends SessionNameVar
{
    protected GlobalConstat globalConstat = new GlobalConstat();

    @Autowired
    protected HttpSession session; //Load Session

}//end Class
