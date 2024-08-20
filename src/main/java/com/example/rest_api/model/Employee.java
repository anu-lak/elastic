package com.example.rest_api.model;

import java.time.OffsetDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Document(indexName = "empdb")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Employee {
    @Id
    private int id;
    
    @NotNull(message="Name cannot be null")
    //@NotBlank(message="Name cannot be blank")
    private String name;


    @NotNull(message="Designation cannot be null")
    @Pattern(regexp= "^(account manager||associate)$", message = "Designation have to account manager or associate")
    private String designation;

    @NotNull(message="Designation cannot be null")
    @Pattern(regexp = "^(BA||QA||engineering||sales||delivery)$",message="Invalid department")
    private String department;

    @Email(message ="Invalid email address")
    private String email;

    @Pattern(regexp = "(^\\d{10}$)")
    private String mobile;

    @NotBlank(message = "Location cannot be null")
    private String location;

    //@NotBlank(message = "manager id is required field")
    private int managerId;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private OffsetDateTime dateOfJoining;
 
   
    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private OffsetDateTime createdTime;

    private int yearsOfExperience;


}
