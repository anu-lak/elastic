package com.example.rest_api.projection;

import java.time.LocalDateTime;

public interface EmployeeProjection {
    String getName();
    String getId();
    String getDesignation();
    String getDepartment();
    String getEmail();
    String getMobile();
    String getLocation();
    LocalDateTime getDateOfJoining();
    LocalDateTime getCreatedTime();
}
