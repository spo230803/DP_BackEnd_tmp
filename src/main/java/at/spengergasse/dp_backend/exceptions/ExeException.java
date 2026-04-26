package at.spengergasse.dp_backend.exceptions;

import jakarta.mail.Message;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@Slf4j
@Getter
public class ExeException extends BaseException
{

    private final HttpStatus status;
    private final String code;
    private final Object origObject;

    /**
     * Standdar
     * @param message
     */
    public ExeException(String message)
    {
        super(message);
        log.debug(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.code = null;
        this.origObject = null;
        this.log();
    }

    /**
     * Von Prof (mit HTTP Status und Code)
     * @param message
     * @param status
     * @param code
     */
    public ExeException(String message, HttpStatus status, String code)
    {
        super(message);
        this.status = status;
        this.code = code;
        this.origObject = null;
        this.log();
    }

   public ExeException(HttpStatus status, String code, String message)
   {
        super(message);
        this.status = status;
        this.code = code;
        this.origObject = null;
        this.log();
   }

    /**
     * Get Alles RountimeExceptio
     * @param e
     */
    public ExeException(RuntimeException e)
    {
        super(e.getMessage());
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.code = e.getMessage();
        this.origObject = e.getCause();
        this.log();
    }

    public ExeException(Throwable e)
    {
        super(e.getMessage());
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.code = e.getMessage();
        this.origObject = e.getCause();
        this.log();
    }

    /**
     * Create Exceptio and where (Ojb)
     * @param message
     * @param obj   //Oggetto dove si è creato l'eccezione
     */
    public ExeException(String message, Object obj)
    {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.code = obj.toString();
        this.origObject = obj;
        this.log();
    }

    /**
     * Error in Exe
     * @param message
     * @param obj  wo die Error Kommt
     * @param status
     */
    public ExeException(String message, Object obj, HttpStatus status)
    {
        super(message);
        this.status = status;
        this.code = null;
        this.origObject = obj;
        this.log();
    }

 //################     HTTP ERROR  ###################

    public static ExeException notFound(String message){return new ExeException(HttpStatus.NOT_FOUND, "NOT_FOUND",message);}

    public static ExeException idNullOrBad(String message){ return new ExeException(HttpStatus.NOT_FOUND, "NOT_FOUND (bad ID)", "Ungültig ID oder ist Null in : "+message); }

    public static ExeException notFound(String name, Long id){ return new ExeException(HttpStatus.NOT_FOUND,"NOT_FOUND (ID not Found)", name+" nicht gefunen mit ID = "+(id==null ? "null": id)); }

    public static ExeException notAdmin() { return new ExeException("User ist nicht ADMIN", HttpStatus.FORBIDDEN);}

    public static ExeException notLoggin() {return  new ExeException("User hat nicht  eingeloggen", HttpStatus.UNAUTHORIZED);}

    public static ExeException ofConflict(String message){ return new ExeException(HttpStatus.CONFLICT, "CONFLICT", message);}

	public static ExeException ofInternalError(String message){ return new ExeException(HttpStatus.INTERNAL_SERVER_ERROR, "System Error", message); }

    public static ExeException ofBadRequest(String message){return new ExeException(HttpStatus.BAD_REQUEST, "BAD_REQUEST", message);}
 //################     PRIVETE     ######################

    private void log()
    {
        StringBuilder messageLog = new StringBuilder();
        messageLog.append("Exception an: " + LocalDateTime.now());
        messageLog.append("\n Origin Obj : " + (origObject == null ? "null" : origObject.getClass().getName()));
        messageLog.append("\n Code : "+(code == null ?  "null" : code ));
        messageLog.append("\n HTTP Status : "+(status == null ? "null" : status.toString()));
        messageLog.append("\n Message : "+(messageLog.toString()));
        log.warn(messageLog.toString());
    }

}//end Class

