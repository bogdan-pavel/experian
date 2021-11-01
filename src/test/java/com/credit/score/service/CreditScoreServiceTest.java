package com.credit.score.service;

import com.credit.score.persistance.model.CreditScoreEntity;
import com.credit.score.persistance.repository.CreditScoreRepo;
import com.credit.score.utils.TestUtils;
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
    public void whenCreateOrUpdateCreditScore_thenReturnCreditScore() {
        var dateTime = Timestamp.from(Instant.now());
        var creditScoreRequest = TestUtils.createRequest(dateTime);

        when(creditScoreRepoMock.save(any(CreditScoreEntity.class))).thenReturn(TestUtils.createEntity(dateTime));

        var created = creditScoreService.saveOrUpdate(creditScoreRequest);
        assertEquals(creditScoreRequest.getCompany_name(), created.getCompanyName());
        assertEquals(creditScoreRequest.getScore(), created.getScore());
    }

    @Test
    public void whenSaveOrUpdateCannotBePerformed_thenThrowException() {
        when(creditScoreRepoMock.save(any(CreditScoreEntity.class))).thenThrow(PersistenceException.class);

        Assertions.assertThrows(PersistenceException.class, () ->
                creditScoreService.saveOrUpdate(TestUtils.createRequest(Timestamp.from(Instant.now())))
        );
    }
}