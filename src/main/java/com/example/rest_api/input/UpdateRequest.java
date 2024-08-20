package com.example.rest_api.input;

import lombok.Data;

@Data
public class UpdateRequest {
    private Integer employeeId;
    private Integer managerId;
}
