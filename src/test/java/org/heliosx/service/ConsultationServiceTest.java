package org.heliosx.service;

import org.heliosx.model.Consultation;
import org.heliosx.model.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
class ConsultationServiceTest {

    @Autowired
    private ConsultationService consultationService;

    @Test
    void validate_new_consultation_is_created_and_persisted_if_not_exists() {
        Consultation consultation = consultationService.retrieveById(UUID.randomUUID());
        Consultation consultationPersisted = consultationService.retrieveById(consultation.id());
        assertThat(consultationPersisted.id()).isEqualTo(consultation.id());
    }

    @Test
    void validate_consultation_is_persisted_or_updated() {
        Consultation consultation = new Consultation(UUID.randomUUID(), new ArrayList<>());
        consultationService.persist(consultation);

        consultation.questions().add(new Question(
                UUID.randomUUID(),
                "group",
                "question text",
                false,
                false)
        );

        Consultation consultationUpdated = consultationService.retrieveById(consultation.id());

        assertThat(consultationUpdated.id()).isEqualTo(consultation.id());
        assertThat(consultationUpdated.questions().size()).isEqualTo(1);
    }

    @Test
    void validate_expected_answer_matches_questioner_answer() {

        UUID consultationId = UUID.randomUUID();
        UUID questionId_1 = UUID.randomUUID();
        UUID questionId_2 = UUID.randomUUID();
        consultationService.persist(new Consultation(consultationId, List.of(
                new Question(questionId_1,
                        "TestData",
                        "Question_1",
                        true,
                        false),
                new Question(questionId_2,
                        "TestData",
                        "Question_2",
                        false,
                        false)
        )));

        assertThat(consultationService.validateQuestion(consultationId, questionId_1, false))
                .isEqualTo(Boolean.FALSE);

        assertThat(consultationService.validateQuestion(consultationId, questionId_1, true))
                .isEqualTo(Boolean.TRUE);

        assertThat(consultationService.validateQuestion(consultationId, questionId_2, false))
                .isEqualTo(Boolean.TRUE);

        assertThat(consultationService.validateQuestion(consultationId, questionId_2, true))
                .isEqualTo(Boolean.FALSE);
    }

    @Test
    void validate_consultation_not_valid_as_answered_fields_not_match() {
        Consultation consultation = new Consultation(UUID.randomUUID(), List.of(
                new Question(UUID.randomUUID(),
                        "TestData",
                        "Question_1",
                        true,
                        false)
        ));

        consultationService.persist(consultation);

        assertThat(consultationService.validateConsultation(consultation)).isFalse();
    }


    @Test
    void validate_consultation_valid_as_answered_fields_match() {
        Consultation consultation = new Consultation(UUID.randomUUID(), List.of(
                new Question(UUID.randomUUID(),
                        "TestData",
                        "Question_1",
                        true,
                        true)
        ));

        consultationService.persist(consultation);

        assertThat(consultationService.validateConsultation(consultation)).isTrue();
    }
}