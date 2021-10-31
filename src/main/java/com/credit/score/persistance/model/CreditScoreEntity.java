package com.credit.score.persistance.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "credit_score")
public class CreditScoreEntity {

    @Id
    private Long id;
    private String companyName;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSz")
    private Timestamp registrationDate;
    @Column(precision = 2)
    private Float score;
    private Integer directorsCount;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSz")
    private Timestamp lastUpdated;
}
