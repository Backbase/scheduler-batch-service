package com.backbase.accelerators.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.backbase.batch.scheduler.service.model.*;
import com.backbase.accelerators.service.ScheduledBatchOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class ScheduledBatchOrderControllerTest {

    private ScheduledBatchOrderController scheduledBatchOrderController;

    @Mock
    private ScheduledBatchOrderService scheduledBatchOrderService;

    @BeforeEach
    public void setUp() {
        scheduledBatchOrderController = new ScheduledBatchOrderController(scheduledBatchOrderService);
    }

    @Test
    public void getScheduledBatchOrdersTest() {
        LocalDate nextExecutionDate = LocalDate.of(2023, 3, 13);
        Integer from = 0;
        Integer size = 10;
        List<ScheduledBatchOrderItem> scheduledBatchOrderItems = Arrays.asList(
                new ScheduledBatchOrderItem().id(1).nextExecutionDate(LocalDate.now()),
                new ScheduledBatchOrderItem().id(2).nextExecutionDate(LocalDate.now())
        );

        when(scheduledBatchOrderService.getScheduledBatchOrders(any(LocalDate.class), any(Integer.class), any(Integer.class)))
                .thenReturn(scheduledBatchOrderItems);

        ResponseEntity<List<ScheduledBatchOrderItem>> result = scheduledBatchOrderController.getScheduledBatchOrders(nextExecutionDate, from, size);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(scheduledBatchOrderItems, result.getBody());
    }

    @Test
    public void postScheduledBatchOrderTest() {
        PostScheduledBatchOrderRequest postScheduledBatchOrderRequest = new PostScheduledBatchOrderRequest();
        PostScheduledBatchOrderResponse postScheduledBatchOrderResponse = new PostScheduledBatchOrderResponse().batchOrderItem(new ScheduledBatchOrderItem().id(1).nextExecutionDate(LocalDate.now()));

        when(scheduledBatchOrderService.postScheduledPaymentOrder(any(PostScheduledBatchOrderRequest.class)))
                .thenReturn(postScheduledBatchOrderResponse);

        ResponseEntity<PostScheduledBatchOrderResponse> result = scheduledBatchOrderController.postScheduledBatchOrder(postScheduledBatchOrderRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(postScheduledBatchOrderResponse, result.getBody());
    }

    @Test
    public void testPutScheduledBatchOrder() {

        PutScheduledBatchOrderRequest putScheduledBatchOrderRequest = new PutScheduledBatchOrderRequest();
        PutScheduledBatchOrderResponse putScheduledBatchOrderResponse = new PutScheduledBatchOrderResponse();

        when(scheduledBatchOrderService.putScheduledPaymentOrder(any(PutScheduledBatchOrderRequest.class)))
                .thenReturn(putScheduledBatchOrderResponse);


        ResponseEntity<PutScheduledBatchOrderResponse> result = scheduledBatchOrderController.putScheduledBatchOrder(putScheduledBatchOrderRequest);


        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(putScheduledBatchOrderResponse, result.getBody());
    }
}