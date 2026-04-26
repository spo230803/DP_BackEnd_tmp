package at.spengergasse.dp_backend.validation;

import at.spengergasse.dp_backend.exceptions.ExeException;
import jakarta.validation.constraints.NotNull;

public final class StringGuard
{
    public static boolean validateStringGuardNotBlack(String string, @NotNull String errorMessage) throws ExeException
    {
        if(string == null){throw new ExeException(errorMessage);}
        if(string.isEmpty()){throw new ExeException(errorMessage);}
        return true;
    }
}
