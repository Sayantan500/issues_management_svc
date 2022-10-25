package com.issues.issues_management_svc.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UpdateIssueSolutionId
{
    @JsonProperty("new_solution_id") private String newSolutionId;
}
