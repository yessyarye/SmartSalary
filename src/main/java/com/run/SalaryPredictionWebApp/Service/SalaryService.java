package com.run.SalaryPredictionWebApp.Service;

import com.run.SalaryPredictionWebApp.model.SalaryData;
import com.run.SalaryPredictionWebApp.repository.SalaryDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SalaryService {

    @Autowired
    private SalaryDataRepository repository;

    private final RestTemplate restTemplate;

    public SalaryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Map<String, Object>> getSalaryData() {
        String url = "http://localhost:5000/";
        List<Map<String, Object>> data = restTemplate.getForObject(url, List.class);

        // Proses data untuk memisahkan atau mengganti NaN dengan null
        return data.stream()
                .map(row -> row.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue() == null ? null : entry.getValue()
                        )))
                .collect(Collectors.toList());
    }

    // Metode untuk mengambil data hanya dari database
    public List<SalaryData> getSalaryDataFromDatabase() {
        return repository.findAll();
    }

    public List<SalaryData> findAll() {
        return repository.findAll();
    }

    public Optional<SalaryData> findById(Long id) {
        return repository.findById(id);
    }

    public SalaryData save(SalaryData data) {
        return repository.save(data);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
