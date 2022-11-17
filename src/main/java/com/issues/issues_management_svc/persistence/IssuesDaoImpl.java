package com.issues.issues_management_svc.persistence;

import com.google.cloud.firestore.*;
import com.issues.issues_management_svc.models.Issues;
import com.issues.issues_management_svc.models.UpdateIssueSolutionId;
import com.issues.issues_management_svc.models.UpdateIssueStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
class IssuesDaoImpl implements IssuesDAO
{
    private final CollectionReference firestoreCollectionReference;
    private final int paginationLimit;

    public IssuesDaoImpl(
            @Qualifier("firestore_collection_ref")
            CollectionReference firestoreCollectionReference,
            @Qualifier("pagination_limit")
            int paginationLimit
    )
    {
        this.firestoreCollectionReference = firestoreCollectionReference;
        this.paginationLimit = paginationLimit;
    }

    @Override
    public Issues getIssueByIssueId(String issueID)
    {
        try
        {
            return firestoreCollectionReference
                    .document(issueID)
                    .get()
                    .get()
                    .toObject(Issues.class);
        }
        catch (InterruptedException | ExecutionException e)
        {
            System.out.println(">>> [ IssuesDaoImpl.getIssueByIssueId ] " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Issues> getIssuesByDepartment(String departmentName, String lastIssueID) {
        if(departmentName==null || departmentName.length()==0)
            return null;

        try
        {
            DocumentSnapshot documentSnapshotOfLastIssue = null;
            if (lastIssueID!=null)
            {
                documentSnapshotOfLastIssue =
                        firestoreCollectionReference
                                .document(lastIssueID)
                                .get()
                                .get();
                if(! documentSnapshotOfLastIssue.exists())
                    lastIssueID = null;
            }


            Query queryToGetAllIssuesOfADepartment =
                    firestoreCollectionReference
                            .whereEqualTo("submittedToDepartment",departmentName)
                            .orderBy("createdAt", Query.Direction.ASCENDING);

            if(lastIssueID==null)
                queryToGetAllIssuesOfADepartment = queryToGetAllIssuesOfADepartment.limit(paginationLimit);
            else
            {
                queryToGetAllIssuesOfADepartment =
                        queryToGetAllIssuesOfADepartment
                                .startAfter(documentSnapshotOfLastIssue)
                                .limit(paginationLimit);
            }

            final List<QueryDocumentSnapshot> queryDocumentSnapshots
                    = queryToGetAllIssuesOfADepartment
                    .get()
                    .get()
                    .getDocuments();

            final List<Issues> resultSet = new ArrayList<>();
            queryDocumentSnapshots
                    .forEach(
                            queryDocumentSnapshot -> resultSet.add(queryDocumentSnapshot.toObject(Issues.class))
                    );
            System.out.println(
                    ">>> [ IssuesDaoImpl.getIssuesByDepartment ] All Issues of dept. " +
                            departmentName +
                            " is fetched."
            );
            return resultSet;
        }
        catch (Exception e)
        {
            System.out.println(">>> [ IssuesDaoImpl.getIssuesByDepartment ] " + e.getMessage());
            throw  new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Issues> getAllIssuesOfUser(String userID, String lastIssueID)
    {
        try
        {
            DocumentSnapshot documentSnapshotOfLastIssue = null;
            if (lastIssueID!=null)
            {
                documentSnapshotOfLastIssue =
                        firestoreCollectionReference
                                .document(lastIssueID)
                                .get()
                                .get();
                if(! documentSnapshotOfLastIssue.exists())
                    lastIssueID = null;
            }


            Query queryToGetAllIssuesOfAUser =
                    firestoreCollectionReference
                            .whereEqualTo("submittedBy",userID)
                            .orderBy("createdAt", Query.Direction.DESCENDING);

            if(lastIssueID==null)
                queryToGetAllIssuesOfAUser = queryToGetAllIssuesOfAUser.limit(paginationLimit);
            else
            {
                queryToGetAllIssuesOfAUser =
                        queryToGetAllIssuesOfAUser
                                .startAfter(documentSnapshotOfLastIssue)
                                .limit(paginationLimit);
            }

            final List<QueryDocumentSnapshot> queryDocumentSnapshots
                    = queryToGetAllIssuesOfAUser
                    .get()
                    .get()
                    .getDocuments();

            final List<Issues> resultSet = new ArrayList<>();
            queryDocumentSnapshots
                    .forEach(
                            queryDocumentSnapshot -> resultSet.add(queryDocumentSnapshot.toObject(Issues.class))
                    );
            System.out.println(
                    ">>> [ IssuesDaoImpl.getAllIssuesOfUser ] All Issues of user " +
                            userID +
                            " is fetched"
            );
            return resultSet;
        }
        catch (Exception e)
        {
            System.out.println(">>> [ IssuesDaoImpl.getAllIssuesOfUser ] " + e.getMessage());
            throw  new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Issues createNewIssue(Issues newIssue) {
        String newDocID = firestoreCollectionReference.document().getId();
        newIssue.set_id(newDocID);
        newIssue.setStatus("Delivered");
        newIssue.setTimeStamp();
        try {
            firestoreCollectionReference
                    .document(newDocID)
                    .set(newIssue)
                    .get();
            System.out.println(
                    ">>> [ IssuesDaoImpl.createNewIssue ] New Issue Created with ID " +
                            newIssue.get_id()
            );
            return newIssue;
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(">>> [ IssuesDaoImpl.createNewIssue ] " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Issues updateIssueStatus(String issueID, UpdateIssueStatus newStatus)
    {
        try
        {
            final DocumentReference documentReference = verifyIssueID(issueID);
            if(documentReference==null)
                return null; // means doc is not present...
            documentReference.update("status",newStatus.getNewStatus()).get();
            Issues issueAfterUpdatingStatus = getIssueByIssueId(issueID);
            System.out.println(
                    ">>> [ IssuesDaoImpl.updateIssueStatus ] Issue with ID " +
                            issueAfterUpdatingStatus.get_id() +
                            " is updated with New Status."
            );
            return issueAfterUpdatingStatus;
        }
        catch (InterruptedException | ExecutionException e)
        {
            System.out.println(">>> [ IssuesDaoImpl.updateIssueStatus ] " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Issues updateSolutionIdOfIssue(String issueID, UpdateIssueSolutionId newSolution)
    {
        try
        {
            final DocumentReference documentReference = verifyIssueID(issueID);
            if(documentReference==null)
                return null; // means doc is not present...
            documentReference.update("solutionId",newSolution.getNewSolutionId()).get();
            Issues issueAfterUpdatingStatus = getIssueByIssueId(issueID);
            System.out.println(
                    ">>> [ IssuesDaoImpl.updateSolutionIdOfIssue ] Issue with ID " +
                            issueAfterUpdatingStatus.get_id() +
                            " is updated with New Solution ID."
            );
            return issueAfterUpdatingStatus;
        }
        catch (InterruptedException | ExecutionException e)
        {
            System.out.println(">>> [ IssuesDaoImpl.updateSolutionIdOfIssue ] " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteIssue(String issueID)
    {
        try
        {
            final DocumentReference documentReference = verifyIssueID(issueID);
            if(documentReference==null)
            {
                System.out.println(
                        ">>> [ IssuesDaoImpl.deleteIssue ] Issue with ID " +
                                issueID +
                                "is Already Deleted..."
                );
                return false; // means the doc does not exist.
            }
            documentReference.delete().get();
            System.out.println(
                        ">>> [ IssuesDaoImpl.deleteIssue ] Issue with ID " +
                                issueID +
                                " is Deleted."
                );
            return true; // means the doc was present and is successfully deleted.
        }
        catch (InterruptedException | ExecutionException e)
        {
            System.out.println(">>> [ IssuesDaoImpl.deleteIssue ] " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private DocumentReference verifyIssueID(String issueID)
    {
        final DocumentReference documentReference = firestoreCollectionReference.document(issueID);
        try {
            if(documentReference.get().get().exists())
            {
                System.out.println(">>> [ IssuesDaoImpl.verifyIssueID ] Doc with id " + issueID + " is present");
                return documentReference;
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(">>> [ IssuesDaoImpl.verifyIssueID ] " + e.getMessage());
        }
        System.out.println(">>> [ IssuesDaoImpl.verifyIssueID ] Doc with id " + issueID + " Not Present...");
        return null;
    }
}
