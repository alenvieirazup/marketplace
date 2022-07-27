package br.com.zup.marketplace.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.RetryableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.LocalTime.now;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RetryableException.class)
    public ResponseEntity handleFeignServiceStatusException(RetryableException e, HttpServletRequest request) {
        Map<String, String> result = getMessage("Não foi possível efetuar sua venda, tente novamente depois.",
                INTERNAL_SERVER_ERROR,
                request);
        return ResponseEntity.internalServerError().body(result);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest webRequest) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        Integer totalErros = fieldErrors.size();
        String palavraErro = totalErros == 1 ? "error" : "errors";
        String mensagemGeral = "Validation failed with " + totalErros + " " + palavraErro + ".";
        ErroPadronizado erroPadronizado = new ErroPadronizado(
                httpStatus.value(), httpStatus.getReasonPhrase(), mensagemGeral,
                webRequest.getDescription(false).replace("uri=", ""));
        fieldErrors.forEach(erroPadronizado::adicionarErro);

        return ResponseEntity.status(httpStatus).body(erroPadronizado);
    }

    public Map<String, String> getMessage(String message, HttpStatus status, HttpServletRequest request) {
        return Map.of("status", String.valueOf(status.getReasonPhrase()),
                "error", status.name(),
                "message", message,
                "timestamp", now().toString(),
                "path", request.getServletPath());
    }

}
