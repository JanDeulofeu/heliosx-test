package org.heliosx.service;

import org.heliosx.model.Consultation;
import org.heliosx.model.Question;
import org.heliosx.repository.ConsultationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ConsultationService {

    private final ConsultationRepository consultationRepository;

    public ConsultationService(ConsultationRepository consultationRepository) {
        this.consultationRepository = consultationRepository;
    }


    public Consultation retrieveById(final UUID id) {
        return consultationRepository.retrieveConsultationById(id)
                .orElseGet(() -> {
                    Consultation consultation = consultationRepository.generateConsultation(id);
                    consultationRepository.persist(consultation);
                    return consultation;
                });
    }


    public void persist(final Consultation consultation) {
        consultationRepository.persist(consultation);
    }

    public Boolean validateQuestion(final UUID consultationId, final UUID questionId, final boolean answerQuestion) {
        return consultationRepository.retrieveConsultationById(consultationId).stream()
                .flatMap(questions -> questions.questions().stream())
                .filter(question -> question.id().compareTo(questionId) == 0)
                .map(Question::expectedAnswer)
                .anyMatch(question -> question.equals(answerQuestion));
    }

    public Boolean validateConsultation(final Consultation consultation) {
        return consultationRepository.retrieveConsultationById(consultation.id()).stream()
                .flatMap(consultations -> consultations.questions().stream())
                .allMatch(question -> question.expectedAnswer() == question.questionerAnswer());
    }
}
