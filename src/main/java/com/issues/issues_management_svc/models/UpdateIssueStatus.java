package com.issues.issues_management_svc.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateIssueStatus
{
    @JsonProperty("new_status") private String newStatus;
}
