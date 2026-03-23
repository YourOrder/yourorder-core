package org.example.yourordercore.company.service;

import lombok.RequiredArgsConstructor;
import org.example.yourordercore.company.dto.CompanyRequest;
import org.example.yourordercore.company.entity.CompanyEntity;
import org.example.yourordercore.company.repository.CompanyRepository;
import org.example.yourordercore.kafka.producer.CompanyEventProducer;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyEventProducer companyEventProducer;


    public CompanyEntity createCompany(String userId, CompanyRequest request) {
        CompanyEntity company = new CompanyEntity();
        company.setName(request.getCompanyName());
        company.setUserId(UUID.fromString(userId));


        CompanyEntity saved = companyRepository.save(company);

        companyEventProducer.sendCompanyCreated(
                saved.getId(),
                saved.getUserId()
        );

        return saved;
    }
}
