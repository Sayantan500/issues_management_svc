package com.issues.issues_management_svc.controller;

import com.issues.issues_management_svc.models.Issues;
import com.issues.issues_management_svc.models.UpdateIssueSolutionId;
import com.issues.issues_management_svc.models.UpdateIssueStatus;
import com.issues.issues_management_svc.persistence.IssuesDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class Controller
{
    private final IssuesDAO issuesDao;
    public Controller(IssuesDAO issuesDao) {
        this.issuesDao = issuesDao;
    }

    @GetMapping("/issues/{id}")
    public ResponseEntity<Issues> getIssuesById(@PathVariable(name = "id") String issuesID){
        final Issues issue = issuesDao.getIssueByIssueId(issuesID);
        final HttpStatus httpStatus = (issue != null) ? HttpStatus.OK: HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(issue,httpStatus);
    }

    @GetMapping("/issues")
    public ResponseEntity<List<Issues>> getAllIssuesByUserIdAndLastIssueID(
            @RequestParam(name = "uid",defaultValue = "") String userID,
            @RequestParam(name = "dept",defaultValue = "") String deptName,
            @RequestParam(name = "last-issue-id",required = false) String lastIssueID
    )
    {
        lastIssueID = (lastIssueID==null || lastIssueID.compareTo("")==0) ? null : lastIssueID;
        final List<Issues> issuesList ;
        final boolean isDeptNamePresent = deptName.compareTo("")!=0;
        final boolean isUserIdPresent = userID.compareTo("")!=0;

        if((!isDeptNamePresent && !isUserIdPresent) ||
                (isDeptNamePresent && isUserIdPresent)) // both the uid & deptName params are empty or both are present
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        issuesList =
                isUserIdPresent ?
                        issuesDao.getAllIssuesOfUser(userID, lastIssueID) :
                        issuesDao.getIssuesByDepartment(deptName, lastIssueID);

        return new ResponseEntity<>(issuesList,HttpStatus.OK);
    }

    @PostMapping("/issues/new")
    public ResponseEntity<Issues> createNewIssue(@RequestBody Issues newIssue){
        final Issues issueAfterCreatingInDB = issuesDao.createNewIssue(newIssue);
        final HttpStatus httpStatus =
                issueAfterCreatingInDB != null ?
                        HttpStatus.CREATED :
                        HttpStatus.NO_CONTENT;

        return new ResponseEntity<>(issueAfterCreatingInDB,httpStatus);
    }

    @PatchMapping("/issues/{id}/status")
    public ResponseEntity<Issues> updateIssueStatus(
            @PathVariable(name = "id") String issueID,
            @RequestBody UpdateIssueStatus updatedStatusObject
    )
    {
        Issues issueWithUpdatedStatus = issuesDao.updateIssueStatus(issueID,updatedStatusObject);
        final HttpStatus httpStatus =
                issueWithUpdatedStatus != null ?
                        HttpStatus.OK :
                        HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(issueWithUpdatedStatus,httpStatus);
    }

    @PatchMapping("/issues/{id}/solution-id")
    public ResponseEntity<Issues> updateSolutionIdOfIssue(
            @PathVariable(name = "id") String issueID,
            @RequestBody UpdateIssueSolutionId updateIssueSolutionIdObject
    )
    {
        Issues issueWithUpdatedStatus = issuesDao.updateSolutionIdOfIssue(issueID,updateIssueSolutionIdObject);
        final HttpStatus httpStatus =
                issueWithUpdatedStatus != null ?
                        HttpStatus.OK :
                        HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(issueWithUpdatedStatus,httpStatus);
    }

    @DeleteMapping("/issues/{id}")
    public ResponseEntity<Object> deleteIssueById(@PathVariable(name = "id") String issueID){
        final HttpStatus httpStatus =
                issuesDao.deleteIssue(issueID) ?
                        HttpStatus.NO_CONTENT :
                        HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(null,httpStatus);
    }
}
