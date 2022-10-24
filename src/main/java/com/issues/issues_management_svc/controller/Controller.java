package com.issues.issues_management_svc.controller;

import com.issues.issues_management_svc.models.Issues;
import com.issues.issues_management_svc.models.UpdateIssueStatus;
import com.issues.issues_management_svc.persistence.IssuesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class Controller
{
    @Autowired
    private IssuesDAO issuesDao;

    @GetMapping("/issues/{id}")
    public ResponseEntity<Issues> getIssuesById(@PathVariable(name = "id") String issuesID){
        final Issues issue = issuesDao.getIssueByIssueId(issuesID);
        final HttpStatus httpStatus = (issue != null) ? HttpStatus.OK: HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(issue,httpStatus);
    }

    //Todo : Need to take some parameter for pagination...
    @GetMapping("/users/{uid}/issues")
    public ResponseEntity<List<Issues>> getAllIssuesByUserId(@PathVariable(name = "uid") String userID){
        final List<Issues> issuesList = issuesDao.getAllIssuesOfUser(userID);
        final ResponseEntity<List<Issues>> response;
        response = new ResponseEntity<>(issuesList,HttpStatus.OK);

        return response;
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

    @DeleteMapping("/issues/{id}")
    public ResponseEntity<Object> deleteIssueById(@PathVariable(name = "id") String issueID){
        final HttpStatus httpStatus =
                issuesDao.deleteIssue(issueID) ?
                        HttpStatus.NO_CONTENT :
                        HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(null,httpStatus);
    }
}
