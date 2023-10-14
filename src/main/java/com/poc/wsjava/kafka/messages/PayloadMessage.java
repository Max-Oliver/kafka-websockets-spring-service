package com.poc.wsjava.kafka.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PayloadMessage(@JsonProperty("message") String message,
                             @JsonProperty("identifier") int identifier) {
}
