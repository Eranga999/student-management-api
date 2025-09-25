package com.studentmanagement.api.repository;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.Timestamp;
import com.google.common.util.concurrent.MoreExecutors;
import com.studentmanagement.api.dto.PageRequest;
import com.studentmanagement.api.model.Course;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CourseRepository {

    private final Firestore firestore;
    private static final String COLLECTION_NAME = "courses";

    public CompletableFuture<String> save(Course course) {
        if (course.getId() == null) {
            course.setId(UUID.randomUUID().toString());
            course.setCreatedAt(Timestamp.now());
        }
        course.setUpdatedAt(Timestamp.now());

        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(course.getId());
        ApiFuture<WriteResult> future = docRef.set(course);

        return apiFutureToCompletableFuture(future)
                .thenApply(writeResult -> course.getId());
    }

    public CompletableFuture<Course> findById(String id) {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();

        return apiFutureToCompletableFuture(future)
                .thenApply(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        return documentSnapshot.toObject(Course.class);
                    }
                    return null;
                });
    }

    public CompletableFuture<List<Course>> findAll() {
        CollectionReference collection = firestore.collection(COLLECTION_NAME);
        ApiFuture<QuerySnapshot> future = collection.get();

        return apiFutureToCompletableFuture(future)
                .thenApply(querySnapshot ->
                        querySnapshot.getDocuments().stream()
                                .map(doc -> doc.toObject(Course.class))
                                .collect(Collectors.toList()));
    }

    public CompletableFuture<Void> deleteById(String id) {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        ApiFuture<WriteResult> future = docRef.delete();

        return apiFutureToCompletableFuture(future)
                .thenApply(writeResult -> null);
    }

    public CompletableFuture<List<Course>> findByLecturerId(String lecturerId) {
        CollectionReference collection = firestore.collection(COLLECTION_NAME);
        ApiFuture<QuerySnapshot> future = collection.whereEqualTo("lecturerId", lecturerId).get();

        return apiFutureToCompletableFuture(future)
                .thenApply(querySnapshot ->
                        querySnapshot.getDocuments().stream()
                                .map(doc -> doc.toObject(Course.class))
                                .collect(Collectors.toList()));
    }

    public CompletableFuture<List<Course>> findByName(String name) {
        CollectionReference collection = firestore.collection(COLLECTION_NAME);
        ApiFuture<QuerySnapshot> future = collection.whereEqualTo("name", name).get();

        return apiFutureToCompletableFuture(future)
                .thenApply(querySnapshot ->
                        querySnapshot.getDocuments().stream()
                                .map(doc -> doc.toObject(Course.class))
                                .collect(Collectors.toList()));
    }

    public CompletableFuture<Boolean> existsById(String id) {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();

        return apiFutureToCompletableFuture(future)
                .thenApply(DocumentSnapshot::exists);
    }

    public CompletableFuture<Long> count() {
        CollectionReference collection = firestore.collection(COLLECTION_NAME);
        ApiFuture<QuerySnapshot> future = collection.get();

        return apiFutureToCompletableFuture(future)
                .thenApply(querySnapshot -> (long) querySnapshot.getDocuments().size());
    }
    
    public CompletableFuture<List<Course>> findAllWithPagination(PageRequest pageRequest) {
        CollectionReference collection = firestore.collection(COLLECTION_NAME);
        
        // Build query with sorting
        Query query = collection.orderBy(pageRequest.getSortBy(), 
            pageRequest.getSortDirection() == PageRequest.SortDirection.DESC ? 
                Query.Direction.DESCENDING : Query.Direction.ASCENDING);
        
        // Apply pagination
        query = query.offset(pageRequest.getPage() * pageRequest.getSize())
                     .limit(pageRequest.getSize());
        
        ApiFuture<QuerySnapshot> future = query.get();
        
        return apiFutureToCompletableFuture(future)
            .thenApply(querySnapshot -> 
                querySnapshot.getDocuments().stream()
                    .map(doc -> doc.toObject(Course.class))
                    .collect(Collectors.toList()));
    }

    // Utility method to convert ApiFuture to CompletableFuture
    private <T> CompletableFuture<T> apiFutureToCompletableFuture(ApiFuture<T> apiFuture) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();

        ApiFutures.addCallback(apiFuture, new ApiFutureCallback<T>() {
            @Override
            public void onSuccess(T result) {
                completableFuture.complete(result);
            }

            @Override
            public void onFailure(Throwable throwable) {
                completableFuture.completeExceptionally(throwable);
            }
        }, MoreExecutors.directExecutor());

        return completableFuture;
    }
}
