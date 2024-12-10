package org.heliosx.model;


import java.util.List;
import java.util.UUID;

public record Consultation(
        UUID id,
        List<Question> questions) {
}
