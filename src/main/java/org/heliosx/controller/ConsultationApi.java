package org.heliosx.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.heliosx.model.Consultation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "Consultation", description = "Consultation management APIs")
public interface ConsultationApi {

    @Operation(
            summary = "Create a consultation by Id",
            description = "Generates a consultation questioner.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Consultation.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
     Consultation initiateConsultation(@PathVariable("id") String id);


    @Operation(
            summary = "Validate question answer",
            description = "Validates a question answer for a given consultation.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Boolean.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    Boolean isValidQuestionAnswer(@PathVariable("consultationId") String consultationId,
                                         @PathVariable("questionId") String questionId,
                                         @PathVariable("answerQuestion") boolean answerQuestion);

    @Operation(
            summary = "Validate consultation",
            description = "Validates all answered questions for a given consultation.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema(implementation = String.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    ResponseEntity<String> validateConsultation(@RequestBody final Consultation consultation);
}
