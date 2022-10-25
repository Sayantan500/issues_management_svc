package com.issues.issues_management_svc.persistence;

import com.issues.issues_management_svc.models.Issues;
import com.issues.issues_management_svc.models.UpdateIssueStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssuesDAO
{
    Issues getIssueByIssueId(String issueID);
    List<Issues> getAllIssuesOfUser(String userID, String lastIssueID);
    Issues createNewIssue(Issues newIssue);
    Issues updateIssueStatus(String issueID, UpdateIssueStatus newStatus);
    boolean deleteIssue(String issueID);
}
