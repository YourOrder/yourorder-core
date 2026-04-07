package org.example.yourordercore.company.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.yourordercore.company.dto.CompanyRequest;
import org.example.yourordercore.company.dto.CompanyResponse;
import org.example.yourordercore.company.entity.CompanyEntity;
import org.example.yourordercore.company.service.CompanyService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/core/companies")
@RequiredArgsConstructor
@Tag(name = "Company API", description = "API for managing companies")
public class CompanyController {

    private final CompanyService companyService;

    @Operation(
            summary = "Create company",
            description = "Creates a company and returns created company data"
    )
    @PostMapping
    public CompanyResponse createCompany(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @Valid @RequestBody CompanyRequest request
    ) {
        CompanyEntity company = companyService.createCompany(userId, request);
        return mapToResponse(company);
    }

    @Operation(
            summary = "Check service",
            description = "Returns a simple message to verify that service is running"
    )
    @GetMapping
    public String check() {
        log.debug("Received check request");
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
