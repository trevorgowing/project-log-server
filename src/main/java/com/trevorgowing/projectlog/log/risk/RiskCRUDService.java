package com.trevorgowing.projectlog.log.risk;

import com.trevorgowing.projectlog.log.LogCRUDService;
import com.trevorgowing.projectlog.log.LogDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RiskCRUDService implements LogCRUDService {

    private final RiskRepository riskRepository;

    public RiskCRUDService(RiskRepository riskRepository) {
        this.riskRepository = riskRepository;
    }

    @Override
    public List<LogDTO> getLogDTOs() {
        return new ArrayList<>(getIdentifiedRiskDTOs());
    }

    public List<IdentifiedRiskDTO> getIdentifiedRiskDTOs() {
        return riskRepository.findIdentifiedRiskDTOs();
    }
}