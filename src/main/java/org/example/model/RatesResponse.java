package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@Builder
@ToString
@JsonDeserialize(builder = RatesResponse.RatesResponseBuilder.class)
public class RatesResponse {

   @JsonProperty
   private final Boolean success;

   @JsonProperty
   private final Long timestamp;

   @JsonProperty
   private final String base;

   @JsonProperty
   private final String date;

   @JsonProperty
   private final Map<String, Double> rates;

   @JsonProperty
   private final RatesError error;

   @JsonProperty
   private final String message;
}
