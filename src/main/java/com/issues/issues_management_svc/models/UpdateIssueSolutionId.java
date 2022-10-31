package com.issues.issues_management_svc.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateIssueSolutionId
{
    @JsonProperty("new_solution_id") private String newSolutionId;

    public UpdateIssueSolutionId() {}

    public String getNewSolutionId() {
        return newSolutionId;
    }

    public void setNewSolutionId(String newSolutionId) {
        this.newSolutionId = newSolutionId;
    }
}
