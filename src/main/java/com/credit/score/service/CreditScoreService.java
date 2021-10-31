package com.credit.score.service;

import com.credit.score.model.CreditScore;
import com.credit.score.persistance.repository.CreditScoreRepo;
import com.credit.score.request.CreditScoreRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.credit.score.mapper.CreditScoreMapper.map;


@Service
public class CreditScoreService {

    @Autowired
    private CreditScoreRepo creditScoreRepo;

    public CreditScore saveOrUpdate(CreditScoreRequest creditScoreRequest) {
        var entity = map(creditScoreRequest);
        var saveEntity = creditScoreRepo.save(entity);

        return map(saveEntity);
    }
}
