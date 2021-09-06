package com.transactionrequery.transactionrequery.services;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transactionrequery.transactionrequery.dtos.SterlingRequeryBaseResponse;
import com.transactionrequery.transactionrequery.dtos.SterlingRequeryRequest;
import com.transactionrequery.transactionrequery.dtos.SterlingRequeryResponse;
import com.transactionrequery.transactionrequery.dtos.SterlingRequeryResponseData;
import com.transactionrequery.transactionrequery.utils.CustomHttpGetWithEntity;
import com.transactionrequery.transactionrequery.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.HttpClient;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SterlingApiService {

    private final  String requeryUrl = "https://epayments.sterling.ng/NIPRequery/api/GetTransactionController/GetTransactionHistory";

    private ObjectMapper objectMapper = new ObjectMapper();


    public SterlingApiService () {

    }

    public List<SterlingRequeryResponseData> requerySterlingTransactions(SterlingRequeryRequest sterlingRequeryRequest) throws Exception {
        try {
            HttpClient client = new DefaultHttpClient();
            CustomHttpGetWithEntity customHttpGetWithEntity = new CustomHttpGetWithEntity(requeryUrl);
            StringEntity input = new StringEntity(Util.objectToJson(sterlingRequeryRequest));
            input.setContentType("application/json");
            customHttpGetWithEntity.setEntity(input);
            HttpResponse newResponse = client.execute(customHttpGetWithEntity);

            org.apache.http.HttpEntity httpEntity = newResponse.getEntity();

            Header encodingHeader = httpEntity.getContentEncoding();

            Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 :
                    Charsets.toCharset(encodingHeader.getValue());

            String json = EntityUtils.toString(httpEntity, encoding);
            objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

            SterlingRequeryBaseResponse sterlingBaseResponse = objectMapper.readValue(json, SterlingRequeryBaseResponse.class);
            if (sterlingBaseResponse == null) {
                String message = "No response gotten from provider.";
                log.error(message);

            }
            if (sterlingBaseResponse.isSuccess()) {
                SterlingRequeryResponse sterlingResponse = objectMapper.readValue(json, SterlingRequeryResponse.class);
                return sterlingResponse.getData();
            } else {
                log.error("No data was found");
                return new ArrayList<>();
            }

        } catch (Exception ex) {
            log.error(String.format("An error occurred while reuqerying for session id %s error message %s", sterlingRequeryRequest.getSessionId(), ex.getMessage()));
            throw new Exception(ex.getMessage());
        }
    }

}


