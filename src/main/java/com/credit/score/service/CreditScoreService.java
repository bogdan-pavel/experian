package com.credit.score.service;

import com.credit.score.model.CreditScore;
import com.credit.score.persistance.model.CreditScoreEntity;
import com.credit.score.persistance.repository.CreditScoreRepo;
import com.credit.score.request.CreditScoreRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class CreditScoreService {

    @Autowired
    CreditScoreRepo creditScoreRepo;

    public CreditScore saveOrUpdate(CreditScoreRequest creditScoreRequest) {
        return map(save(creditScoreRequest));
    }

    private CreditScoreEntity save(CreditScoreRequest creditScoreRequest) {
        return creditScoreRepo.save(map(creditScoreRequest));
    }

    private CreditScoreEntity map(CreditScoreRequest request) {
        return CreditScoreEntity.builder()
                .id(request.getMsg_id().longValue())
                .score(request.getScore())
                .companyName(request.getCompany_name())
                .directorsCount(request.getDirectors_count())
                .registrationDate(request.getRegistration_date())
                .lastUpdated(request.getLast_updated())
                .build();
    }

    private CreditScore map(CreditScoreEntity entity) {
        return CreditScore.builder()
                .msg_id(BigInteger.valueOf(entity.getId()))
                .companyName(entity.getCompanyName())
                .registrationDate(entity.getRegistrationDate())
                .score(entity.getScore())
                .directorsCount(entity.getDirectorsCount())
                .lastUpdated(entity.getLastUpdated())
                .build();
    }
}
