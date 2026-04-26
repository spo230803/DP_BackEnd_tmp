package at.spengergasse.dp_backend.exceptions;

public abstract class BaseException extends RuntimeException
{
    public BaseException(String message)
    {
        super(message);
    }
}
