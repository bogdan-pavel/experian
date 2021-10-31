package com.credit.score.utils;

import com.credit.score.model.CreditScore;
import com.credit.score.persistance.model.CreditScoreEntity;
import com.credit.score.request.CreditScoreRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigInteger;
import java.sql.Timestamp;

public class TestUtils {

    public static CreditScoreRequest createRequest(Timestamp timestamp) {
        return CreditScoreRequest.builder()
                .msg_id(BigInteger.ONE)
                .score(100.20f)
                .company_name("Experian")
                .directors_count(1)
                .registration_date(timestamp)
                .last_updated(timestamp)
                .build();
    }

    public static CreditScore createModel(Timestamp timestamp) {
        return CreditScore.builder()
                .msgId(BigInteger.ONE)
                .score(100.20f)
                .companyName("Experian")
                .directorsCount(1)
                .registrationDate(timestamp)
                .lastUpdated(timestamp)
                .build();
    }


    public static CreditScoreEntity createEntity(Timestamp timestamp) {
        return CreditScoreEntity.builder()
                .id(1L)
                .score(100.20f)
                .companyName("Experian")
                .directorsCount(1)
                .registrationDate(timestamp)
                .lastUpdated(timestamp)
                .build();
    }

    public static String createCreditScoreRequestJson(CreditScoreRequest creditScoreRequest) throws JsonProcessingException {
        var ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(creditScoreRequest);
    }

    public static String createCreditScoreModelJson(CreditScore creditScoreRequest) throws JsonProcessingException {
        var ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(creditScoreRequest);
    }
}
