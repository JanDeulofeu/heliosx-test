package org.heliosx.model;


import java.util.UUID;

public record Question(
        UUID id,
        String group,
        String questionText,
        boolean expectedAnswer,
        boolean questionerAnswer
        ) {
}
