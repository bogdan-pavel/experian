package com.credit.score.persistance.repository;

import com.credit.score.persistance.model.CreditScoreEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
public class CreditScoreRepoIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CreditScoreRepo creditScoreRepo;
    final String EXPERIAN = "Experian";

    @Test
    public void whenFindById_thenReturnEntity() {
        CreditScoreEntity creditScoreEntity =
                CreditScoreEntity.builder().id(1L).companyName(EXPERIAN).registrationDate(Timestamp.from(Instant.now())).score(100f)
                        .directorsCount(5).lastUpdated(Timestamp.from(Instant.now())).build();
        entityManager.persistAndFlush(creditScoreEntity);
        entityManager.clear();

        Optional<CreditScoreEntity> found = creditScoreRepo.findById(1L);
        assertTrue(found.isPresent());
        assertEquals(found.get().getCompanyName(), creditScoreEntity.getCompanyName());
        assertEquals(found.get().getId(), creditScoreEntity.getId());
        assertEquals(found.get().getRegistrationDate().getTime(), creditScoreEntity.getRegistrationDate().getTime());
    }

    @Test
    public void whenUpdateCompanyScore_thenReturnTheUpdatedScore() {
        CreditScoreEntity creditScoreEntity =
                CreditScoreEntity.builder().id(1L).companyName(EXPERIAN).registrationDate(Timestamp.from(Instant.now())).score(100f)
                        .directorsCount(5).lastUpdated(Timestamp.from(Instant.now())).build();
        entityManager.persistAndFlush(creditScoreEntity);
        creditScoreEntity.setScore(120f);
        entityManager.persistAndFlush(creditScoreEntity);
        entityManager.clear();

        Optional<CreditScoreEntity> found = creditScoreRepo.findById(1L);
        assertTrue(found.isPresent());
        assertEquals(found.get().getCompanyName(), creditScoreEntity.getCompanyName());
        assertEquals(found.get().getScore(), creditScoreEntity.getScore());
        assertEquals(found.get().getId(), creditScoreEntity.getId());
        assertEquals(found.get().getLastUpdated().getTime(), creditScoreEntity.getLastUpdated().getTime());
    }

    @Test
    public void whenCompanyScoreNotFound_thenReturnEmpty() {
        assertTrue(creditScoreRepo.findById(1L).isEmpty());
    }


    @Test
    public void whenCreateSameCreditScore_thenThrowException() {
        CreditScoreEntity creditScoreEntity =
                CreditScoreEntity.builder().id(1L).companyName(EXPERIAN).build();
        CreditScoreEntity creditScoreEntity2 =
                CreditScoreEntity.builder().id(1L).companyName(EXPERIAN).build();

        entityManager.persist(creditScoreEntity);

        EntityExistsException entityExistsException = Assertions.assertThrows(EntityExistsException.class, () -> {
            entityManager.persist(creditScoreEntity2);
        });
        entityManager.clear();
        assertEquals("A different object with the same identifier value was already associated with the session : [com.credit.score.persistance.model.CreditScoreEntity#1]", entityExistsException.getMessage());
    }

    @Test
    public void whenCreateCreditScoreWithoutId_thenThrowException() {
        CreditScoreEntity creditScoreEntity =
                CreditScoreEntity.builder().companyName(EXPERIAN).build();
        PersistenceException persistenceException = Assertions.assertThrows(PersistenceException.class, () -> {
            entityManager.persistAndFlush(creditScoreEntity);
        });
        entityManager.clear();

        assertEquals("org.hibernate.id.IdentifierGenerationException: ids for this class must be manually assigned before calling save(): com.credit.score.persistance.model.CreditScoreEntity", persistenceException.getMessage());
    }
}
