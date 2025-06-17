package com.dne.ems.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dne.ems.dto.GridUpdateRequest;
import com.dne.ems.model.Grid;
import com.dne.ems.repository.GridRepository;
import com.dne.ems.service.GridService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GridServiceImpl implements GridService {

    private final GridRepository gridRepository;

    @Override
    public Page<Grid> getGrids(String cityName, String districtName, Pageable pageable) {
        Specification<Grid> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(cityName)) {
                predicates.add(criteriaBuilder.equal(root.get("cityName"), cityName));
            }

            if (StringUtils.hasText(districtName)) {
                predicates.add(criteriaBuilder.equal(root.get("districtName"), districtName));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };

        return gridRepository.findAll(spec, pageable);
    }

    @Override
    public Grid updateGrid(Long gridId, GridUpdateRequest request) {
        Grid grid = gridRepository.findById(gridId)
                .orElseThrow(() -> new EntityNotFoundException("Grid not found with id: " + gridId));

        if (request.getIsObstacle() != null) {
            grid.setIsObstacle(request.getIsObstacle());
        }

        if (request.getDescription() != null) {
            grid.setDescription(request.getDescription());
        }

        return gridRepository.save(grid);
    }
} 