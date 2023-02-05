package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;


@Data
@Builder
@ToString
@JsonDeserialize(builder =RatesError.RatesErrorBuilder.class)
public class RatesError {

    @JsonProperty
    private final RatesErrorCode code;

    @JsonProperty
    private final String message;
}
