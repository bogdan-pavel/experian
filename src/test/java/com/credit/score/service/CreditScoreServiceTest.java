package com.credit.score.service;

import com.credit.score.Utils.MyUtil;
import com.credit.score.model.CreditScore;
import com.credit.score.persistance.model.CreditScoreEntity;
import com.credit.score.persistance.repository.CreditScoreRepo;
import com.credit.score.request.CreditScoreRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.PersistenceException;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditScoreServiceTest {

    @Mock
    CreditScoreRepo creditScoreRepoMock;
    @InjectMocks
    CreditScoreService creditScoreService;

    @Test
    public void whenCreateOrUpdateCreditScore_thenShouldReturnCreditScore() {
        Timestamp dateTime = Timestamp.from(Instant.now());
        CreditScoreRequest creditScoreRequest = MyUtil.createRequest(dateTime);
        when(creditScoreRepoMock.save(any(CreditScoreEntity.class))).thenReturn(MyUtil.createEntity(dateTime));

        CreditScore created = creditScoreService.saveOrUpdate(creditScoreRequest);

        assertEquals(creditScoreRequest.getCompany_name(), created.getCompanyName());
        assertEquals(creditScoreRequest.getScore(), created.getScore());
    }

    @Test
    public void whenSaveOrUpdateCannotBePerformed_thenThrowCannotPerformTransactionException() {
        when(creditScoreRepoMock.save(any(CreditScoreEntity.class))).thenThrow(PersistenceException.class);

        Assertions.assertThrows(PersistenceException.class, () -> {
            creditScoreService.saveOrUpdate(MyUtil.createRequest(Timestamp.from(Instant.now())));
        });

    }


}