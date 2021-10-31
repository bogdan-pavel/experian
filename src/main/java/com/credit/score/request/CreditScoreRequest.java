package com.credit.score.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigInteger;
import java.sql.Timestamp;

@Builder
@Getter
@Setter
public class CreditScoreRequest {

    @NotNull
    @Positive
    private BigInteger msg_id;

    @NotEmpty
    private String company_name;

    @NotNull
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSz")
    private Timestamp registration_date;

    @NotNull
    @Digits(integer = 3, fraction = 2)
    private Float score;

    @NotNull
    @Positive
    @Range(min = 1)
    private Integer directors_count;

    @NotNull
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSz")
    private Timestamp last_updated;
}
