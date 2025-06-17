package com.dne.ems.dto;

import com.dne.ems.model.enums.Role;
import com.dne.ems.validation.GridWorkerLocationRequired;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@GridWorkerLocationRequired
public class UserRoleUpdateRequest {

    @NotNull(message = "角色不能为空")
    private Role role;

    private Integer gridX;

    private Integer gridY;
} 