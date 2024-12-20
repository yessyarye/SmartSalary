package com.run.SalaryPredictionWebApp.Service;

import com.fasterxml.jackson.core.json.async.NonBlockingJsonParser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class CsvService {
    public List<Object> getDataFromAPI(){
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://127.0.0.1:5000";
        Object[] response = restTemplate.getForObject(apiUrl, Object[].class);
        return Arrays.asList(response);
    }
}
