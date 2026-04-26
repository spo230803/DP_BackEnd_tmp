package at.spengergasse.dp_backend.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ExeExceptionHandler
{
    @ExceptionHandler(ExeException.class)
    public ProblemDetail handleExeException(ExeException ex, HttpServletRequest request)
    {
        return toProblem(ex.getStatus(), ex.getCode(), ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    public ProblemDetail handleBadRequest(Exception ex, HttpServletRequest request) {
        String detail = ex.getMessage();

        if (ex instanceof MethodArgumentNotValidException manv) {
            detail =
                    manv.getBindingResult().getFieldErrors().stream()
                            .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                            .collect(Collectors.joining(", "));
        }

        return toProblem(HttpStatus.BAD_REQUEST, "BAD_REQUEST", detail, request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleAny(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception on {} {}", request.getMethod(), request.getRequestURI(), ex);

        return toProblem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR",
                ex.getClass().getSimpleName() + ": " + ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler({org.springframework.dao.DataIntegrityViolationException.class})
    public ProblemDetail handleDataIntegrity(org.springframework.dao.DataIntegrityViolationException ex,
                                             HttpServletRequest request) {
        log.error("DB constraint violation on {}", request.getRequestURI(), ex);
        return toProblem(HttpStatus.BAD_REQUEST, "DATA_INTEGRITY", "Ungültige Daten (DB-Constraints)", request.getRequestURI());
    }


//###############################      PRIVATE      ########################
    private ProblemDetail toProblem(HttpStatus status, String code, String detail, String path) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(status.getReasonPhrase());
        problemDetail.setDetail(detail);
        problemDetail.setProperty("code", code);
        problemDetail.setProperty("path", path);
        log.warn(problemDetail.toString());
        return problemDetail;
    }
}//End Class
