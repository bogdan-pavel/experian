package com.credit.score.api;

import com.credit.score.Utils.MyUtil;
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
    public void whenPostInvalid_theBadResponseWithMessageError() throws Exception {
        String creditScoreRequestJson = MyUtil.createCreditScoreRequestJson(CreditScoreRequest.builder().build());
        String error = "company_name must not be empty,directors_count must not be null,last_updated must not be null,msg_id must not be null,registration_date must not be null,score must not be null";
        postAssertWithErro(creditScoreRequestJson, error);
    }


    @Test
    public void whenPostInvalidMsgID_theBadResponseWithMessageError() throws Exception {
        CreditScoreRequest creditScoreRequest = MyUtil.createRequest(Timestamp.from(Instant.now()));
        creditScoreRequest.setMsg_id(BigInteger.valueOf(-1));
        String creditScoreRequestJson = MyUtil.createCreditScoreRequestJson(creditScoreRequest);
        postAssertWithErro(creditScoreRequestJson, "must be greater than 0");
    }

    @Test
    public void whenPostEmptyCompanyName_theBadResponseWithMessageError() throws Exception {
        CreditScoreRequest creditScoreRequest = MyUtil.createRequest(Timestamp.from(Instant.now()));
        creditScoreRequest.setCompany_name("");
        String creditScoreRequestJson = MyUtil.createCreditScoreRequestJson(creditScoreRequest);
        postAssertWithErro(creditScoreRequestJson, "must not be empty");
    }

    @Test
    public void whenPostInvalidDate_theBadResponseWithMessageError() throws Exception {
        CreditScoreRequest creditScoreRequest = MyUtil.createRequest(Timestamp.from(Instant.now()));
        creditScoreRequest.setRegistration_date(Timestamp.valueOf("2022-01-01 01:01:01"));
        String creditScoreRequestJson = MyUtil.createCreditScoreRequestJson(creditScoreRequest);
        postAssertWithErro(creditScoreRequestJson, "must be a past date");
    }

    @Test
    public void whenPostInvalidDateFormat_theBadResponseWithMessageError() throws Exception {
        String creditScoreRequestJson = "{\"registration_date\":\"2021-10-10 11:11:11.111Z\"}";
        postAssertWithErro(creditScoreRequestJson, "Invalid field type");
    }

    @Test
    public void whenPostInvalidFieldTypeFormat_theBadResponseWithMessageError() throws Exception {
        String creditScoreRequestJson = "{\"score\":\"invalidScore\"}";
        postAssertWithErro(creditScoreRequestJson, "{\"error\":\"Invalid field type\"}");
    }

    @Test
    public void whenPostValidCreditScore_thenOkResponse() throws Exception {

        Timestamp nowTime = Timestamp.from(Instant.now());
        String creditScoreRequestJson = MyUtil.createCreditScoreRequestJson(MyUtil.createRequest(nowTime));
        String creditScoreModelJson = MyUtil.createCreditScoreModelJson(MyUtil.createModel(nowTime));

        mockMvc.perform(post("/creditScores").content(creditScoreRequestJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(creditScoreModelJson));
    }

    private void postAssertWithErro(String creditScore, String errorMessage) throws Exception {
        mockMvc.perform(post("/creditScores").content(creditScore)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(errorMessage)));
    }
}