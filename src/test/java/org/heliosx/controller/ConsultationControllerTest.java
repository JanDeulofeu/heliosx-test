package org.heliosx.controller;

import io.restassured.RestAssured;
import jakarta.annotation.PostConstruct;
import org.hamcrest.Matchers;
import org.heliosx.model.Consultation;
import org.heliosx.model.Question;
import org.heliosx.service.ConsultationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static io.restassured.RestAssured.given;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ConsultationControllerTest {

    private static final UUID id = UUID.randomUUID();
    private Consultation consultation;

    @Autowired
    private ConsultationService consultationService;

    @PostConstruct
    public void init() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeEach
    public void beforeAll() {
        consultation = consultationService.retrieveById(id);
    }

    @Test
    void validate_consultation_questioner_is_generated_for_a_given_id() {

        given()
                .when()
                .pathParam("id", id)
                .get("/consultation/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", Matchers.equalTo(id.toString()))
                .body("questions[0].group", Matchers.equalTo("PersonalData"))
                .body("questions[0].questionText", Matchers.equalTo("Are you aged between 18-75?"))
                .body("questions[0].expectedAnswer", Matchers.equalToObject(true))
                .body("questions[0].questionerAnswer", Matchers.equalToObject(false))

                .body("questions[1].group", Matchers.equalTo("PersonalData"))
                .body("questions[1].questionText", Matchers.equalTo("Do you smoke or drink?"))
                .body("questions[1].expectedAnswer", Matchers.equalToObject(false))
                .body("questions[1].questionerAnswer", Matchers.equalToObject(false))

                .body("questions[2].group", Matchers.equalTo("Symptoms"))
                .body("questions[2].questionText", Matchers.equalTo("Do you have high fever?"))
                .body("questions[2].expectedAnswer", Matchers.equalToObject(true))
                .body("questions[2].questionerAnswer", Matchers.equalToObject(false))

                .body("questions[3].group", Matchers.equalTo("Medication"))
                .body("questions[3].questionText", Matchers.equalTo("Are you taking any medication?"))
                .body("questions[3].expectedAnswer", Matchers.equalToObject(false))
                .body("questions[3].questionerAnswer", Matchers.equalToObject(false));
    }

    @Test
    void validate_expected_correct_answer_when_request_for_expected_result() {
        given()
                .when()
                .pathParam("consultationId", id.toString())
                .pathParam("questionId", consultation.questions().get(0).id().toString())
                .pathParam("answerQuestion", Boolean.FALSE.toString())
                .get("/validation/consultation/{consultationId}/question/{questionId}/answer/{answerQuestion}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo("false"));
    }


    @Test
    void validate_consultation_validation_is_not_success() {
        given()
                .contentType("application/json")
                .when()
                .body(consultation)
                .put("/validation")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(Matchers.equalTo("Consultation not valid"));
    }

    @Test
    void validate_consultation_validation_is_success() {

        Consultation correctConsultation = new Consultation(UUID.randomUUID(),
                consultation.questions().stream()
                        .map(question -> new Question(
                                question.id(),
                                question.group(),
                                question.questionText(),
                                question.expectedAnswer(),
                                question.expectedAnswer())
                        ).toList());

        given()
                .contentType("application/json")
                .when()
                .body(correctConsultation)
                .put("/validation")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo("Valid consultation"));
    }
}