package com.transactionrequery.transactionrequery.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SterlingRequerySearchParams {

    @Builder.Default
    private List<String> sessionIds = new ArrayList<>();

    @Builder.Default
    private String accountNumber = "";

    @Builder.Default
    private String startDate = "";
}
