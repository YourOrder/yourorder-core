package org.example.yourordercore.company.controller;

import lombok.RequiredArgsConstructor;
import org.example.yourordercore.company.dto.CompanyRequest;
import org.example.yourordercore.company.dto.CompanyResponse;
import org.example.yourordercore.company.entity.CompanyEntity;
import org.example.yourordercore.company.service.CompanyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/core/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public CompanyResponse createCompany(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestBody CompanyRequest request
    ) {
        CompanyEntity company = companyService.createCompany(userId, request);
        return mapToResponse(company);
    }

    @GetMapping
    public String check() {
        System.out.println("АААА ЗАПРОС!");
        return "Hello world, service work!";
    }

    private CompanyResponse mapToResponse(CompanyEntity company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .createdAt(company.getCreatedAt())
                .userId(company.getUserId())
                .build();
    }
}
