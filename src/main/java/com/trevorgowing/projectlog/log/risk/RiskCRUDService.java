package com.trevorgowing.projectlog.log.risk;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiskCRUDService {

    private final RiskRepository riskRepository;

    public RiskCRUDService(RiskRepository riskRepository) {
        this.riskRepository = riskRepository;
    }

    public List<IdentifiedRiskDTO> getIdentifiedRiskDTOs() {
        return riskRepository.findIdentifiedRiskDTOs();
    }
}