package com.backbase.accelerators.service;

import com.backbase.batch.scheduler.service.model.*;
import com.backbase.accelerators.mapper.ScheduledBatchHistoryMapper;
import com.backbase.accelerators.mapper.*;
import com.backbase.accelerators.model.entity.ScheduledBatchOrder;
import com.backbase.accelerators.repository.ScheduledBatchOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScheduledBatchOrderServiceTest {

    @InjectMocks
    private ScheduledBatchOrderService scheduledBatchOrderService;

    @Mock
    private ScheduledBatchOrderRepository scheduledBatchOrderRepository;

    @Mock
    private ScheduledBatchOrderMapper scheduledBatchOrderMapper;
    @Mock
    ScheduledBatchHistoryMapper scheduledBatchHistoryMapper;
    @Mock
    ScheduledBatchTransactionsService scheduledBatchTransactionsService;
    private final LocalDate nextExecutionDate = LocalDate.now();
    private final Integer from = 0;
    private final Integer size = 10;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void getScheduledBatchOrdersTest() {
        List<ScheduledBatchOrder> batchOrders = Arrays.asList(mock(ScheduledBatchOrder.class), mock(ScheduledBatchOrder.class));
        Page<ScheduledBatchOrder> page = new PageImpl<>(batchOrders);
        when(scheduledBatchOrderRepository.findAllByNextExecutionDateAndStatusIn(any(LocalDate.class), anyList(), any(PageRequest.class))).thenReturn(page);
        when(scheduledBatchOrderMapper.toScheduledBatchOrderItem(any(ScheduledBatchOrder.class))).thenReturn(mock(ScheduledBatchOrderItem.class));

        List<ScheduledBatchOrderItem> result = scheduledBatchOrderService.getScheduledBatchOrders(nextExecutionDate, from, size);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(scheduledBatchOrderRepository, times(1)).findAllByNextExecutionDateAndStatusIn(any(LocalDate.class), anyList(), any(PageRequest.class));
        verify(scheduledBatchOrderMapper, times(2)).toScheduledBatchOrderItem(any(ScheduledBatchOrder.class));
    }
    @Test
    public void postScheduledPaymentOrderTest() {
        PostScheduledBatchOrderRequest request = new PostScheduledBatchOrderRequest();
        ScheduledBatchOrder savedBatchOrder = mock(ScheduledBatchOrder.class);
        ScheduledBatchOrderItem batchOrderItem = mock(ScheduledBatchOrderItem.class);

        when(scheduledBatchOrderRepository.save(any(ScheduledBatchOrder.class))).thenReturn(savedBatchOrder);
        when(scheduledBatchOrderRepository.findById(any())).thenReturn(Optional.of(savedBatchOrder));
        when(scheduledBatchOrderMapper.toEntity(request)).thenReturn(mock(ScheduledBatchOrder.class));
        when(scheduledBatchOrderMapper.toScheduledBatchOrderItem(savedBatchOrder)).thenReturn(batchOrderItem);

        PostScheduledBatchOrderResponse response = scheduledBatchOrderService.postScheduledPaymentOrder(request);

        assertNotNull(response);
        assertNotNull(response.getBatchOrderItem());
        verify(scheduledBatchOrderRepository, times(1)).save(any(ScheduledBatchOrder.class));
        verify(scheduledBatchOrderRepository, times(1)).findById(any());
        verify(scheduledBatchOrderMapper, times(1)).toEntity(request);
        verify(scheduledBatchOrderMapper, times(1)).toScheduledBatchOrderItem(savedBatchOrder);
    }

    @Test
    void postScheduledPaymentOrderNotFoundTest() {
        PostScheduledBatchOrderRequest request = new PostScheduledBatchOrderRequest();
        ScheduledBatchOrder scheduledBatchOrder = new ScheduledBatchOrder();
        scheduledBatchOrder.setId(1L);

        when(scheduledBatchOrderMapper.toEntity(request)).thenReturn(scheduledBatchOrder);
        when(scheduledBatchOrderRepository.save(scheduledBatchOrder)).thenReturn(scheduledBatchOrder);
        when(scheduledBatchOrderRepository.findById(scheduledBatchOrder.getId())).thenReturn(Optional.empty());

        PostScheduledBatchOrderResponse response = scheduledBatchOrderService.postScheduledPaymentOrder(request);

        assertNull(response);
    }

    @Test
    void putScheduledPaymentOrderNonRecurringTest() {
        PutScheduledBatchOrderRequest request = new PutScheduledBatchOrderRequest();
        request.setBatchOrderId("TEST_BATCH_ORDER_ID");

        ScheduledBatchOrder scheduledBatchOrder = new ScheduledBatchOrder();
        scheduledBatchOrder.setId(1L);
        scheduledBatchOrder.setPmtMode("NON_RECURRING");

        when(scheduledBatchOrderRepository.findByBatchOrderId(request.getBatchOrderId())).thenReturn(scheduledBatchOrder);
        when(scheduledBatchOrderRepository.save(scheduledBatchOrder)).thenReturn(scheduledBatchOrder);

        PostScheduledBatchOrderHistoryRequest historyRequest = new PostScheduledBatchOrderHistoryRequest();
        when(scheduledBatchHistoryMapper.toPostScheduledBatchOrderHistoryRequest(request)).thenReturn(historyRequest);
        PostScheduledBatchOrderHistoryResponse historyResponseMock = new PostScheduledBatchOrderHistoryResponse();
        when(scheduledBatchTransactionsService.postScheduledBatchTransactions(historyRequest)).thenReturn(historyResponseMock);

        PutScheduledBatchOrderResponse response = scheduledBatchOrderService.putScheduledPaymentOrder(request);

        assertNotNull(response);
        assertEquals("1", response.getId());
    }

    @Test
    void putScheduledPaymentOrderWithNullNextExecutionDateTest() {
        PutScheduledBatchOrderRequest request = new PutScheduledBatchOrderRequest();
        request.setBatchOrderId("TEST_BATCH_ORDER_ID");
        request.setNextExecutionDate(null);

        ScheduledBatchOrder scheduledBatchOrder = new ScheduledBatchOrder();
        scheduledBatchOrder.setId(1L);
        scheduledBatchOrder.setPmtMode("RECURRING");

        when(scheduledBatchOrderRepository.findByBatchOrderId(request.getBatchOrderId())).thenReturn(scheduledBatchOrder);
        when(scheduledBatchOrderRepository.save(scheduledBatchOrder)).thenReturn(scheduledBatchOrder);

        PostScheduledBatchOrderHistoryRequest historyRequest = new PostScheduledBatchOrderHistoryRequest();
        when(scheduledBatchHistoryMapper.toPostScheduledBatchOrderHistoryRequest(request)).thenReturn(historyRequest);
        PostScheduledBatchOrderHistoryResponse historyResponseMock = new PostScheduledBatchOrderHistoryResponse();
        when(scheduledBatchTransactionsService.postScheduledBatchTransactions(historyRequest)).thenReturn(historyResponseMock);

        PutScheduledBatchOrderResponse response = scheduledBatchOrderService.putScheduledPaymentOrder(request);

        assertNotNull(response);
        assertEquals("1", response.getId());
        assertNull(scheduledBatchOrder.getNextExecutionDate());
    }

    @Test
    void putScheduledPaymentOrderTest() {
        PutScheduledBatchOrderRequest request = new PutScheduledBatchOrderRequest();
        request.setBatchOrderId("TEST_BATCH_ORDER_ID");

        ScheduledBatchOrder scheduledBatchOrder = new ScheduledBatchOrder();
        scheduledBatchOrder.setId(1L);
        scheduledBatchOrder.setPmtMode("RECURRING");

        when(scheduledBatchOrderRepository.findByBatchOrderId(request.getBatchOrderId())).thenReturn(scheduledBatchOrder);
        when(scheduledBatchOrderRepository.save(scheduledBatchOrder)).thenReturn(scheduledBatchOrder);

        PostScheduledBatchOrderHistoryRequest historyRequest = new PostScheduledBatchOrderHistoryRequest();
        when(scheduledBatchHistoryMapper.toPostScheduledBatchOrderHistoryRequest(request)).thenReturn(historyRequest);
        PostScheduledBatchOrderHistoryResponse historyResponseMock = new PostScheduledBatchOrderHistoryResponse();
        when(scheduledBatchTransactionsService.postScheduledBatchTransactions(historyRequest)).thenReturn(historyResponseMock);

        PutScheduledBatchOrderResponse response = scheduledBatchOrderService.putScheduledPaymentOrder(request);

        assertNotNull(response);
        assertEquals("1", response.getId());
    }

}
