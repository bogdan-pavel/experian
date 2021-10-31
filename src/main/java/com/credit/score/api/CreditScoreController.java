package com.credit.score.api;

import com.credit.score.request.CreditScoreRequest;
import com.credit.score.service.CreditScoreService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

@Api(value = "CreditScoreController")
@RestController
public class CreditScoreController {

    @Autowired
    private CreditScoreService creditScoreService;

    @PostMapping(value = "creditScores", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity saveOrUpdate(@Valid @RequestBody CreditScoreRequest creditScoreRequest) {
        return ResponseEntity.ok(creditScoreService.saveOrUpdate(creditScoreRequest));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        var errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(error -> ((FieldError) error).getField() + " " + error.getDefaultMessage()).sorted()
                .collect(Collectors.joining(","));
        return Map.of("error", errorMessage);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Map<String, String> handleException(HttpMessageNotReadableException exception) {
        return Map.of("error", "Invalid field type");
    }
}
