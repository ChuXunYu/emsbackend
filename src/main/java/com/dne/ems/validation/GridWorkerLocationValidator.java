package com.dne.ems.validation;

import com.dne.ems.dto.UserRoleUpdateRequest;
import com.dne.ems.model.enums.Role;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GridWorkerLocationValidator implements ConstraintValidator<GridWorkerLocationRequired, UserRoleUpdateRequest> {

    @Override
    public boolean isValid(UserRoleUpdateRequest value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Let other annotations handle null
        }

        if (value.getRole() == Role.GRID_WORKER) {
            return value.getGridX() != null && value.getGridY() != null;
        }

        // For any other role, this validation does not apply.
        return true;
    }
} 