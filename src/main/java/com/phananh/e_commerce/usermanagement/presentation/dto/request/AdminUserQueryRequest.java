package com.phananh.e_commerce.usermanagement.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.phananh.e_commerce.core.util.StringUtils;
import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class AdminUserQueryRequest {
    private String keyword;
    private Set<RoleName> roleNames;
    private Boolean enabled;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdDateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdDateTo;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime modifiedDateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime modifiedDateTo;

    @Min(value = 0, message = "Page must be >= 0")
    private Integer page;

    @Min(value = 1, message = "Size must be >= 1")
    private Integer size;

    private String sortBy;

    private String sortType;
}


