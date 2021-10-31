package com.credit.score.mapper;

import com.credit.score.model.CreditScore;
import com.credit.score.persistance.model.CreditScoreEntity;
import com.credit.score.request.CreditScoreRequest;

import java.math.BigInteger;

public class CreditScoreMapper {

    public static CreditScoreEntity map(CreditScoreRequest request) {
        return CreditScoreEntity.builder()
                .id(request.getMsg_id().longValue())
                .score(request.getScore())
                .companyName(request.getCompany_name())
                .directorsCount(request.getDirectors_count())
                .registrationDate(request.getRegistration_date())
                .lastUpdated(request.getLast_updated())
                .build();
    }

    public static CreditScore map(CreditScoreEntity entity) {
        return CreditScore.builder()
                .msgId(BigInteger.valueOf(entity.getId()))
                .companyName(entity.getCompanyName())
                .registrationDate(entity.getRegistrationDate())
                .score(entity.getScore())
                .directorsCount(entity.getDirectorsCount())
                .lastUpdated(entity.getLastUpdated())
                .build();
    }
}
