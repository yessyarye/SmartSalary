package com.run.SalaryPredictionWebApp.config;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// Memberi tahu Spring bahwa kelas ini adalah kelas konfigurasi
@Configuration
public class RestClientConfig {

    // Mendefinisikan bean untuk RestTemplate
    @Bean
    public RestTemplate restTemplate() {
        // Mengembalikan instance RestTemplate untuk digunakan dalam HTTP request
        return new RestTemplate();
    }

    // Mendefinisikan bean untuk ObjectMapper
    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                // Mengaktifkan fitur untuk membaca nilai non-numerik seperti NaN atau Infinity
                .enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS.mappedFeature())  // Enable parsing NaN, Infinity
                .build();
    }
}
