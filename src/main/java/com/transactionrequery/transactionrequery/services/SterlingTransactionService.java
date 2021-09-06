package com.transactionrequery.transactionrequery.services;

import com.transactionrequery.transactionrequery.dtos.SterlingRequeryRequest;
import com.transactionrequery.transactionrequery.dtos.SterlingRequeryResponseData;
import com.transactionrequery.transactionrequery.dtos.SterlingRequerySearchParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SterlingTransactionService {
    @Autowired
    private SterlingApiService sterlingApiService;

    public String queryBySearchParams(SterlingRequerySearchParams sterlingRequerySearchParams) throws Exception {
        SterlingRequeryRequest sterlingRequeryRequestParams = null;
        if (sterlingRequerySearchParams.getSessionIds().size() > 0) {
            return querySessionIds(new HashSet<>(sterlingRequerySearchParams.getSessionIds()));
        }
        if (!Strings.isBlank(sterlingRequerySearchParams.getStartDate())) {
            sterlingRequeryRequestParams = SterlingRequeryRequest.builder()
                    .startDate(sterlingRequerySearchParams.getStartDate())
                    .build();
        } else if (!Strings.isBlank(sterlingRequerySearchParams.getAccountNumber())) {
            sterlingRequeryRequestParams = SterlingRequeryRequest.builder()
                    .accountNumber(sterlingRequerySearchParams.getAccountNumber())
                    .build();
        }

        try {
            List<SterlingRequeryResponseData> sterlingRequeryResponseDataList = sterlingApiService.requerySterlingTransactions(sterlingRequeryRequestParams);
            List<SterlingRequeryResponseData>  sterlingRequeryResponseDataFilteredList = sterlingRequeryResponseDataList.stream().filter(transaction -> !Strings.isBlank(transaction.getAccountNumber()))
            .collect(Collectors.toList());
            return buildQuery(sterlingRequeryResponseDataFilteredList);
        }  catch (Exception ex) {
            log.error("Error occurred while retrieving transaction look up details");
        }
       return "";
    }

    public String querySessionIds(Set<String> sessionIds) throws Exception {
        List<SterlingRequeryResponseData> sterlingRequeryResponseDataList = new ArrayList<>();

        for (String sessionId: sessionIds) {
            try {
                log.info("Treating session id : {}", sessionId);
                SterlingRequeryRequest sterlingRequeryRequest = SterlingRequeryRequest.builder()
                        .sessionId(sessionId)
                        .build();
                List<SterlingRequeryResponseData> sterlingRequeryResponseData = sterlingApiService.requerySterlingTransactions(sterlingRequeryRequest);
                List<SterlingRequeryResponseData> requeryResponseDataFilteredList = sterlingRequeryResponseData.stream().filter(transaction -> !Strings.isBlank(transaction.getAccountNumber()))
                        .collect(Collectors.toList());
                sterlingRequeryResponseDataList.addAll(requeryResponseDataFilteredList);
            } catch (Exception ex) {
                log.error("Error occurred while retrieving details for session id {}", sessionId);
            }

        }

        if (sterlingRequeryResponseDataList.size() > 0) {
            return buildQuery(sterlingRequeryResponseDataList);
        }
        return "";
    }

    private String buildQuery(List<SterlingRequeryResponseData> sterlingRequeryResponseDataList) {
        StringBuilder stringBuilder = new StringBuilder("insert into push_notifications(created_at,updated_at,session_id,amount_minor,currency,virtual_acct_no,merchant_id,"+
                "transfer_service_id,source_acct_name,source_acct_no,source_bank_code,narration,transaction_status,channel,notification_acknowledgement,being_sent_to_merchant,num_retries)" +
                "\nvalues");

        for (SterlingRequeryResponseData sterlingRequeryResponseData: sterlingRequeryResponseDataList) {
            BigDecimal amount = new BigDecimal(sterlingRequeryResponseData.getAmount()).multiply(BigDecimal.valueOf(100));
            int transactionStatus = sterlingRequeryResponseData.getResponseCode().equals("00") ? 1: 0;
            stringBuilder.append("(current_timestamp(),current_timestamp(),\""+sterlingRequeryResponseData.getSessionId()+"\","+
                    amount+",\"NGN\",\""+sterlingRequeryResponseData.getAccountNumber()+"\",1,2,\""+sterlingRequeryResponseData.getSourceCustomerName()+
                    "\",\""+sterlingRequeryResponseData.getSourceCustomerAccountNumber()+"\",\""+sterlingRequeryResponseData.getSenderBank()+
                    "\",\""+sterlingRequeryResponseData.getPaymentRef()+"\","+transactionStatus+",\"NIP\",3,0,0),\n");
        }
        return stringBuilder.toString();
    }
}
