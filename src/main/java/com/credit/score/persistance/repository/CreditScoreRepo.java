package com.credit.score.persistance.repository;

import com.credit.score.persistance.model.CreditScoreEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditScoreRepo extends CrudRepository<CreditScoreEntity, Long> {
}
