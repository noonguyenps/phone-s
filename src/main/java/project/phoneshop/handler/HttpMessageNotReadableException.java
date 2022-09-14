package project.phoneshop.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HttpMessageNotReadableException extends HttpMessageConversionException
{
    public HttpMessageNotReadableException(String exception) {
        super(exception);
    }
}