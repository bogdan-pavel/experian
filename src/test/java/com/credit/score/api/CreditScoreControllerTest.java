package com.credit.score.api;

import com.credit.score.utils.TestUtils;
import com.credit.score.request.CreditScoreRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;

import static com.credit.score.utils.TestUtils.createCreditScoreRequestJson;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CreditScoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenPostInvalid_thenBadResponse() throws Exception {
        var creditScoreRequestJson = createCreditScoreRequestJson(CreditScoreRequest.builder().build());
        var error = "company_name must not be empty,directors_count must not be null,last_updated must not be null,msg_id must not be null,registration_date must not be null,score must not be null";

        postAssert(creditScoreRequestJson, error);
    }


    @Test
    public void whenPostInvalidMsgID_thenBadResponse() throws Exception {
        var creditScoreRequest = TestUtils.createRequest(Timestamp.from(Instant.now()));
        creditScoreRequest.setMsg_id(BigInteger.valueOf(-1));
        var creditScoreRequestJson = createCreditScoreRequestJson(creditScoreRequest);

        postAssert(creditScoreRequestJson, "must be greater than 0");
    }

    @Test
    public void whenPostEmptyCompanyName_thenBadResponse() throws Exception {
        var creditScoreRequest = TestUtils.createRequest(Timestamp.from(Instant.now()));
        creditScoreRequest.setCompany_name("");
        var creditScoreRequestJson = createCreditScoreRequestJson(creditScoreRequest);

        postAssert(creditScoreRequestJson, "must not be empty");
    }

    @Test
    public void whenPostInvalidDate_thenBadResponse() throws Exception {
        var creditScoreRequest = TestUtils.createRequest(Timestamp.from(Instant.now()));
        creditScoreRequest.setRegistration_date(Timestamp.valueOf("2022-01-01 01:01:01"));
        var creditScoreRequestJson = createCreditScoreRequestJson(creditScoreRequest);

        postAssert(creditScoreRequestJson, "must be a past date");
    }

    @Test
    public void whenPostInvalidDateFormat_thenBadResponse() throws Exception {
        var creditScoreRequestJson = "{\"registration_date\":\"2021-10-10 11:11:11.111Z\"}";

        postAssert(creditScoreRequestJson, "Invalid field type");
    }

    @Test
    public void whenPostInvalidFieldTypeFormat_thenBadResponse() throws Exception {
        var creditScoreRequestJson = "{\"score\":\"invalidScore\"}";

        postAssert(creditScoreRequestJson, "{\"error\":\"Invalid field type\"}");
    }

    @Test
    public void whenPostValidCreditScore_thenOkResponse() throws Exception {
        var nowTime = Timestamp.from(Instant.now());
        var creditScoreRequestJson = createCreditScoreRequestJson(TestUtils.createRequest(nowTime));
        var creditScoreModelJson = TestUtils.createCreditScoreModelJson(TestUtils.createModel(nowTime));

        mockMvc.perform(post("/creditScores").content(creditScoreRequestJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(creditScoreModelJson));
    }

    @Test
    public void whenPostValidCreditScoreUpdate_thenOkResponse() throws Exception {

        var nowTime = Timestamp.from(Instant.now());
        var request = TestUtils.createRequest(nowTime);
        var creditScoreRequestJson = createCreditScoreRequestJson(request);
        var response = TestUtils.createModel(nowTime);
        var creditScoreModelJson = TestUtils.createCreditScoreModelJson(response);

        mockMvc.perform(post("/creditScores").content(creditScoreRequestJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(creditScoreModelJson));

        request.setCompany_name("Experian Update");
        request.setScore(200f);
        response.setCompanyName("Experian Update");
        response.setScore(200f);

        var creditScoreRequestJsonUpdate = createCreditScoreRequestJson(request);
        var creditScoreModelJsonUpdate = TestUtils.createCreditScoreModelJson(response);

        mockMvc.perform(post("/creditScores").content(creditScoreRequestJsonUpdate)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(creditScoreModelJsonUpdate));


    }

    private void postAssert(String creditScore, String errorMessage) throws Exception {
        mockMvc.perform(post("/creditScores").content(creditScore)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(errorMessage)));
    }
}