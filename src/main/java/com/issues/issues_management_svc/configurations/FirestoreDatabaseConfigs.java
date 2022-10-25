package com.issues.issues_management_svc.configurations;

import com.google.cloud.firestore.CollectionReference;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class FirestoreDatabaseConfigs
{
    @Value("${issues.collection_name}")
    private String COLLECTION_NAME_ISSUES;

    @Value("${issues.pagination_limit}")
    private String paginationLimit;

    @Bean(name = "issues_collection_name")
    public String getIssuesCollectionName(){
        return COLLECTION_NAME_ISSUES;
    }

    @Bean(name = "firestore_collection_ref")
    public CollectionReference getCollectionReference(){
        return FirestoreClient
                .getFirestore()
                .collection(getIssuesCollectionName());
    }

    @Bean(name = "pagination_limit")
    public int getPaginationLimit(){
        return Integer.parseInt(paginationLimit);
    }
}
