package org.heliosx.controller;

import org.heliosx.model.Consultation;
import org.heliosx.service.ConsultationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ConsultationController implements ConsultationApi {

    private final ConsultationService consultationService;

    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @Override
    @GetMapping(path = "/consultation/{id}")
    public Consultation initiateConsultation(@PathVariable("id") String id) {
        return consultationService.retrieveById(UUID.fromString(id));
    }

    @Override
    @GetMapping(path = "/validation/consultation/{consultationId}/question/{questionId}/answer/{answerQuestion}")
    public Boolean isValidQuestionAnswer(@PathVariable("consultationId") String consultationId,
                                         @PathVariable("questionId") String questionId,
                                         @PathVariable("answerQuestion") boolean answerQuestion) {
        return consultationService.validateQuestion(
                UUID.fromString(consultationId),
                UUID.fromString(questionId),
                answerQuestion);

    }

    @Override
    @PutMapping(path = "/validation")
    public ResponseEntity<String> validateConsultation(@RequestBody final Consultation consultation) {
        consultationService.persist(consultation);
        return consultationService.validateConsultation(consultation) ?
                ResponseEntity.ok().body("Valid consultation") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body("Consultation not valid");
    }
}
