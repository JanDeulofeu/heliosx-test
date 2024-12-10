package org.heliosx.repository;

import org.heliosx.model.Consultation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ConsultationRepositoryTest {

    @Autowired
    private ConsultationRepository repository;

    @Test
    void validate_consultation_is_returned_when_exists_in_repository() {
        UUID id = UUID.randomUUID();
        repository.persist(new Consultation(id, List.of()));
        assertThat(repository.retrieveConsultationById(id)).isNotEmpty();
    }

    @Test
    void validate_consultation_is_not_returned_when_not_exists_in_repository() {
        assertThat(repository.retrieveConsultationById(UUID.randomUUID())).isEmpty();
    }


    @Test
    void validate_questions_are_generate_per_consultation(){
        Consultation consultation = repository.generateConsultation(UUID.randomUUID());
        assertThat(consultation).isNotNull();
        assertThat(consultation.questions()).isNotEmpty();
    }
}