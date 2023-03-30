package com.backbase.accelerators.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.backbase.accelerators.service.ScheduledBatchTransactionsService;
import com.backbase.batch.scheduler.service.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class ScheduledBatchTransactionsServiceApiControllerTest {

    private ScheduledBatchTransactionsServiceApiController scheduledBatchTransactionsServiceApiController;

    @Mock
    private ScheduledBatchTransactionsService scheduledBatchTransactionsService;

    @BeforeEach
    public void setUp() {
        scheduledBatchTransactionsServiceApiController = new ScheduledBatchTransactionsServiceApiController(scheduledBatchTransactionsService);
    }

    @Test
    public void getScheduledBatchOrderHistoryTest() {
        LocalDate fromExecutionDate = LocalDate.of(2023, 3, 13);
        LocalDate toExecutionDate = LocalDate.of(2023, 3, 15);
        String status = "PENDING";
        String accountNumber = "123456789";
        String createdBy = "john.doe";
        String batchOrderId = "123";
        Integer from = 0;
        Integer size = 10;
        Pageable pageable = PageRequest.of(from, size);
        List<ScheduledBatchHistoryItem> scheduledBatchHistoryItems = Arrays.asList(
                new ScheduledBatchHistoryItem().id(1).batchOrderId(batchOrderId).status(status).accountNumber(accountNumber).createdBy(createdBy),
                new ScheduledBatchHistoryItem().id(2).batchOrderId(batchOrderId).status(status).accountNumber(accountNumber).createdBy(createdBy)
        );

        when(scheduledBatchTransactionsService.getScheduledBatchTransactions(any(LocalDate.class), any(LocalDate.class), any(String.class), any(String.class), any(String.class), any(String.class), any(Pageable.class)))
                .thenReturn(scheduledBatchHistoryItems);

        ResponseEntity<List<ScheduledBatchHistoryItem>> result = scheduledBatchTransactionsServiceApiController.getScheduledBatchOrderHistory(fromExecutionDate, toExecutionDate, status, accountNumber, createdBy, batchOrderId, from, size);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(scheduledBatchHistoryItems, result.getBody());
    }

    @Test
    public void postScheduledBatchOrderHistoryTest() {
        PostScheduledBatchOrderHistoryRequest postScheduledBatchOrderHistoryRequest = new PostScheduledBatchOrderHistoryRequest().batchOrderId("123").status("PENDING");

        PostScheduledBatchOrderHistoryResponse expectedResponse = new PostScheduledBatchOrderHistoryResponse().id("1");
        when(scheduledBatchTransactionsService.postScheduledBatchTransactions(any(PostScheduledBatchOrderHistoryRequest.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<PostScheduledBatchOrderHistoryResponse> result = scheduledBatchTransactionsServiceApiController.postScheduledBatchOrderHistory(postScheduledBatchOrderHistoryRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
    }
}
