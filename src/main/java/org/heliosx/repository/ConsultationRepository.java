package org.heliosx.repository;

import org.heliosx.model.Consultation;
import org.heliosx.model.Question;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConsultationRepository {

    private final Map<UUID, Consultation> repository;

    public ConsultationRepository() {
        this.repository = new ConcurrentHashMap<>();
    }

    public Optional<Consultation> retrieveConsultationById(final UUID id) {
        return repository.entrySet().stream()
                .filter(entry -> entry.getKey().equals(id))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    public Consultation generateConsultation(final UUID id) {
        return new Consultation(id, generateQuestions());
    }

    public void persist(final Consultation consultation) {
        repository.put(consultation.id(), consultation);
    }


    private List<Question> generateQuestions() {
        return
                List.of(
                        new Question(UUID.randomUUID(),
                                "PersonalData",
                                "Are you aged between 18-75?",
                                true,
                                false),
                        new Question(UUID.randomUUID(),
                                "PersonalData",
                                "Do you smoke or drink?",
                                false,
                                false),
                        new Question(UUID.randomUUID(),
                                "Symptoms",
                                "Do you have high fever?",
                                true,
                                false),
                        new Question(UUID.randomUUID(),
                                "Medication",
                                "Are you taking any medication?",
                                false,
                                false)
                );
    }
}
