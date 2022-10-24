package com.issues.issues_management_svc.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class Issues
{
    @Setter @JsonProperty("_id")    private String _id;

    @JsonProperty("title")          private String issueTitle;
    @JsonProperty("description")    private String issueDescription;
    @JsonProperty("submitted_by")   private String submittedBy;
    @JsonProperty("to_department")  private String submittedToDepartment;

    @JsonProperty("solution_id")    private String solutionId;

    @Setter @JsonProperty("status") private String status;
    @JsonProperty("created_at")     private Timestamp createdAt;

    public Issues(String issueTitle, String issueDescription, String submittedBy, String submittedToDepartment) {
        this.issueTitle = issueTitle;
        this.issueDescription = issueDescription;
        this.submittedBy = submittedBy;
        this.submittedToDepartment = submittedToDepartment;
    }

    public void setTimeStamp() {
        this.createdAt = Timestamp.now();
    }
}
