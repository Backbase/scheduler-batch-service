package com.backbase.accelerators.controller;


import com.backbase.batch.scheduler.service.api.ScheduledBatchOrderHistoryServiceApi;
import com.backbase.batch.scheduler.service.model.PostScheduledBatchOrderHistoryRequest;
import com.backbase.batch.scheduler.service.model.PostScheduledBatchOrderHistoryResponse;
import com.backbase.batch.scheduler.service.model.ScheduledBatchHistoryItem;
import com.backbase.accelerators.service.ScheduledBatchTransactionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ScheduledBatchTransactionsServiceApiController implements ScheduledBatchOrderHistoryServiceApi {

    private final ScheduledBatchTransactionsService scheduledBatchTransactionsService;


    @Override
    public ResponseEntity<List<ScheduledBatchHistoryItem>> getScheduledBatchOrderHistory(LocalDate fromExecutionDate, LocalDate toExecutionDate, String status, String accountNumber, String createdBy, String batchOrderId, Integer from, Integer size) {
        log.info("Entering getScheduledBatchOrderHistory  ");
        Pageable pageable = PageRequest.of(from, size);
        return ResponseEntity.ok(scheduledBatchTransactionsService.getScheduledBatchTransactions(fromExecutionDate, toExecutionDate, status, accountNumber, createdBy, batchOrderId, pageable));
    }

    @Override
    public ResponseEntity<PostScheduledBatchOrderHistoryResponse> postScheduledBatchOrderHistory(PostScheduledBatchOrderHistoryRequest postScheduledBatchOrderHistoryRequest) {
        log.debug("Entering postScheduledBatchOrderTxnRequest : {}", postScheduledBatchOrderHistoryRequest);
        return ResponseEntity.ok(scheduledBatchTransactionsService.postScheduledBatchTransactions(postScheduledBatchOrderHistoryRequest));

    }
}
