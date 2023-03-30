package com.backbase.accelerators.service;

import com.backbase.batch.scheduler.service.model.*;
import com.backbase.accelerators.mapper.ScheduledBatchHistoryMapper;
import com.backbase.accelerators.model.entity.ScheduledBatchOrderHistory;
import com.backbase.accelerators.repository.ScheduledBatchOrderHistoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.*;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScheduledBatchTransactionsServiceTest {

    @InjectMocks
    private ScheduledBatchTransactionsService scheduledBatchTransactionsService;

    @Mock
    private ScheduledBatchOrderHistoryRepository scheduledBatchTransactionRepository;

    @Mock
    private ScheduledBatchHistoryMapper scheduledBatchHistoryMapper;

    @Test
    public void postScheduledBatchTransactionsTest() {
        PostScheduledBatchOrderHistoryRequest request = new PostScheduledBatchOrderHistoryRequest();
        ScheduledBatchOrderHistory historyEntity = new ScheduledBatchOrderHistory();
        ScheduledBatchOrderHistory savedHistoryEntity = new ScheduledBatchOrderHistory();
        savedHistoryEntity.setId(1L);

        when(scheduledBatchHistoryMapper.toEntity(any(PostScheduledBatchOrderHistoryRequest.class), any())).thenReturn(historyEntity);
        when(scheduledBatchTransactionRepository.save(historyEntity)).thenReturn(savedHistoryEntity);

        PostScheduledBatchOrderHistoryResponse response = scheduledBatchTransactionsService.postScheduledBatchTransactions(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        verify(scheduledBatchTransactionRepository, times(1)).save(historyEntity);
    }

    @Test
    public void getScheduledBatchTransactionsNullTest() {
        // Prepare test data
        LocalDate fromExecutionDate = null;
        LocalDate toExecutionDate = null;
        String status = null;
        String accountNumber = null;
        String createdBy = null;
        String batchOrderId = null;
        Pageable pageable = PageRequest.of(0, 10);

        List<ScheduledBatchOrderHistory> transactionList = Collections.emptyList();
        Page<ScheduledBatchOrderHistory> transactionPage = new PageImpl<>(transactionList);

        when(scheduledBatchTransactionRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(transactionPage);

        List<ScheduledBatchHistoryItem> result = scheduledBatchTransactionsService.getScheduledBatchTransactions(fromExecutionDate, toExecutionDate, status, accountNumber, createdBy, batchOrderId, pageable);

        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void postScheduledBatchTransactionsExceptionTest() {
        PostScheduledBatchOrderHistoryRequest request = new PostScheduledBatchOrderHistoryRequest();
        ScheduledBatchOrderHistory scheduledBatchOrderHistory = new ScheduledBatchOrderHistory();

        when(scheduledBatchHistoryMapper.toEntity(Mockito.any(PostScheduledBatchOrderHistoryRequest.class), Mockito.any(OffsetDateTime.class)))
                .thenReturn(scheduledBatchOrderHistory);

        doThrow(new RuntimeException("Save exception"))
                .when(scheduledBatchTransactionRepository).save(scheduledBatchOrderHistory);

        PostScheduledBatchOrderHistoryResponse response = scheduledBatchTransactionsService.postScheduledBatchTransactions(request);

        assertNull(response, "Response should be null when an exception occurs");
    }

    @Test
    public void getScheduledBatchTransactionsOnlyToDateNullTest() {
        LocalDate fromExecutionDate = LocalDate.now().minusDays(3);
        LocalDate toExecutionDate = null;
        String status = "status";
        String accountNumber = "accountNumber";
        String createdBy = "createdBy";
        String batchOrderId = "batchOrderId";
        Pageable pageable = PageRequest.of(0, 10);

        ScheduledBatchOrderHistory transaction = new ScheduledBatchOrderHistory();
        transaction.setBatchOrderId(batchOrderId);
        transaction.setStatus(status);
        transaction.setFileName(accountNumber);

        List<ScheduledBatchOrderHistory> transactionList = Collections.singletonList(transaction);
        Page<ScheduledBatchOrderHistory> transactionPage = new PageImpl<>(transactionList);

        when(scheduledBatchHistoryMapper.toScheduledBatchOrderHistory(batchOrderId, status, accountNumber, createdBy)).thenReturn(transaction);
        when(scheduledBatchTransactionRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(transactionPage);

        ScheduledBatchHistoryItem historyItem = new ScheduledBatchHistoryItem();
        historyItem.setBatchOrderId(batchOrderId);
        historyItem.setStatus(status);
        historyItem.setFileName(accountNumber);
        historyItem.setCreatedBy(createdBy);

        when(scheduledBatchHistoryMapper.toScheduledBatchOrderHistoryItem(transaction)).thenReturn(historyItem);

        List<ScheduledBatchHistoryItem> result = scheduledBatchTransactionsService.getScheduledBatchTransactions(fromExecutionDate, toExecutionDate, status, accountNumber, createdBy, batchOrderId, pageable);

        Assertions.assertEquals(1, result.size());
        ScheduledBatchHistoryItem returnedHistoryItem = result.get(0);
        Assertions.assertEquals(batchOrderId, returnedHistoryItem.getBatchOrderId());
        Assertions.assertEquals(status, returnedHistoryItem.getStatus());
        Assertions.assertEquals(accountNumber, returnedHistoryItem.getFileName());
        Assertions.assertEquals(createdBy, returnedHistoryItem.getCreatedBy());
    }

    @Test
    public void getSpecFromDatesAndExampleTest() {
        OffsetDateTime from = OffsetDateTime.parse("2023-03-14T00:00:00Z");
        OffsetDateTime to = OffsetDateTime.parse("2023-03-15T00:00:00Z");
        ScheduledBatchOrderHistory scheduledBatchOrderHistory = new ScheduledBatchOrderHistory();
        Example<ScheduledBatchOrderHistory> example = Example.of(scheduledBatchOrderHistory);

        Specification<ScheduledBatchOrderHistory> spec = scheduledBatchTransactionsService.getSpecFromDatesAndExample(from, to, example);

        assertNotNull(spec, "The specification should not be null");
    }

    @Test
    public void getSpecFromDatesAndExampleWithNullFromTest() {
        OffsetDateTime from = null;
        OffsetDateTime to = OffsetDateTime.now();
        ScheduledBatchOrderHistory exampleEntity = new ScheduledBatchOrderHistory();
        Example<ScheduledBatchOrderHistory> example = Example.of(exampleEntity);

        Specification<ScheduledBatchOrderHistory> specification = scheduledBatchTransactionsService.getSpecFromDatesAndExample(from, to, example);

        assertNotNull(specification);
    }

    @Test
    public void getSpecFromDatesAndExampleWithNullToTest() {
        OffsetDateTime from = OffsetDateTime.now().minusDays(1);
        OffsetDateTime to = null;
        ScheduledBatchOrderHistory exampleEntity = new ScheduledBatchOrderHistory();
        Example<ScheduledBatchOrderHistory> example = Example.of(exampleEntity);

        Specification<ScheduledBatchOrderHistory> specification = scheduledBatchTransactionsService.getSpecFromDatesAndExample(from, to, example);

        assertNotNull(specification);
    }

    @Test
    public void getSpecFromDatesAndExampleWithNullExampleTest() {
        OffsetDateTime from = OffsetDateTime.now().minusDays(1);
        OffsetDateTime to = OffsetDateTime.now();
        Example<ScheduledBatchOrderHistory> example = null;

        Specification<ScheduledBatchOrderHistory> specification = scheduledBatchTransactionsService.getSpecFromDatesAndExample(from, to, example);

        assertNotNull(specification);
    }

@Test
public void testConvertToOffSetDateTime() {
    LocalDate localDate = LocalDate.of(2023, 3, 14);

    OffsetDateTime expectedOffsetDateTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT)
            .atZone(ZoneId.systemDefault())
            .toOffsetDateTime();

    OffsetDateTime actualOffsetDateTime = convertToOffSetDateTime(localDate);

    assertEquals(expectedOffsetDateTime, actualOffsetDateTime);
}

    private OffsetDateTime convertToOffSetDateTime(LocalDate localDate) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.toOffsetDateTime();
    }
}
