package at.spengergasse.dp_backend.validation;

import at.spengergasse.dp_backend.exceptions.ExeException;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public final class NumberGuard
{
    public static boolean validateBigDecinalNotNull(BigDecimal value, @NotNull String errorMessage) throws ExeException
    {
        if(value == null){throw new ExeException(errorMessage);}
        return true;
    }

    public static boolean validateBigDecinalNotNegativ(BigDecimal value, @NotNull String errorMessage) throws ExeException
    {
        try{NumberGuard.validateBigDecinalNotNull(value, errorMessage);}
        catch(ExeException e){throw e;}
        if(value.floatValue() < 0 ){throw new ExeException(errorMessage);}
        return  true;
    }

    public static boolean validateLongNotNegativ(Long value, @NotNull String errorMessage) throws ExeException
    {
        if(value == null){throw new ExeException(errorMessage);}
        if(value.longValue() < 0){throw new ExeException(errorMessage);}
        return true;
    }
}
