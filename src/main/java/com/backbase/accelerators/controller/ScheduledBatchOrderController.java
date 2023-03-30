package com.backbase.accelerators.controller;

import com.backbase.batch.scheduler.service.api.ScheduledBatchOrderApi;
import com.backbase.batch.scheduler.service.model.*;
import com.backbase.accelerators.service.ScheduledBatchOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ScheduledBatchOrderController implements ScheduledBatchOrderApi {

    private final ScheduledBatchOrderService service;


    @Override
    public ResponseEntity<List<ScheduledBatchOrderItem>> getScheduledBatchOrders(LocalDate nextExecutionDate, Integer from, Integer size) {
        log.debug("Get Scheduled batch orders list based on next execution date: {}", nextExecutionDate);
        return ResponseEntity.ok(service.getScheduledBatchOrders(nextExecutionDate, from, size));
    }

    @Override
    public ResponseEntity<PostScheduledBatchOrderResponse> postScheduledBatchOrder(PostScheduledBatchOrderRequest postScheduledBatchOrderRequest) {
        log.debug("Entering PostScheduledBatchOrderRequest() with request: {}", postScheduledBatchOrderRequest);
        return ResponseEntity.ok(service.postScheduledPaymentOrder(postScheduledBatchOrderRequest));
    }

    @Override
    public ResponseEntity<PutScheduledBatchOrderResponse> putScheduledBatchOrder(PutScheduledBatchOrderRequest putScheduledBatchOrderRequest) {
        log.debug("Entering PutScheduledBatchOrderRequest() with request: {}", putScheduledBatchOrderRequest);
        return ResponseEntity.ok(service.putScheduledPaymentOrder(putScheduledBatchOrderRequest));
    }


}
