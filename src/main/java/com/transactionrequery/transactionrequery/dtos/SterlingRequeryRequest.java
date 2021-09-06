package com.transactionrequery.transactionrequery.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SterlingRequeryRequest {
    @JsonProperty("sessionID")
    private String sessionId;

    @Builder.Default
    private String accountNumber = "";

    @Builder.Default
    private String startDate = "";
}
