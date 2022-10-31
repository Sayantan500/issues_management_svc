package com.issues.issues_management_svc.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateIssueStatus
{
    @JsonProperty("new_status") private String newStatus;

    public UpdateIssueStatus() {}

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }
}
