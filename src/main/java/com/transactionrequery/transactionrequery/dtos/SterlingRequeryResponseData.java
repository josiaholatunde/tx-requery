package com.transactionrequery.transactionrequery.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor()
@Builder
public class SterlingRequeryResponseData {

    @JsonProperty("SourceCustomerAccountNumber")
    private String sourceCustomerAccountNumber;

    @JsonProperty("SourceCustomerName")
    private String sourceCustomerName;

    @JsonProperty("AccountNumber")
    private String accountNumber;

    @JsonProperty("SenderBank")
    private String senderBank;

    @JsonProperty("Amount")
    private String amount;

    @JsonProperty("SessionID")
    private String sessionId;

    @JsonProperty("PaymentRef")
    private String paymentRef;

    @JsonProperty("Requery")
    private String requery;

    @JsonProperty("ResponseCode")
    private String responseCode;
}
