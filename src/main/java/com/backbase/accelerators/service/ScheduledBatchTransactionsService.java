package com.backbase.accelerators.service;

import com.backbase.batch.scheduler.service.model.PostScheduledBatchOrderHistoryRequest;
import com.backbase.batch.scheduler.service.model.PostScheduledBatchOrderHistoryResponse;
import com.backbase.batch.scheduler.service.model.ScheduledBatchHistoryItem;
import com.backbase.accelerators.mapper.ScheduledBatchHistoryMapper;
import com.backbase.accelerators.model.entity.ScheduledBatchOrderHistory;
import com.backbase.accelerators.repository.ScheduledBatchOrderHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This repository class is used to persist and retrieve the data from schedule_batch_order table.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledBatchTransactionsService {

    private final ScheduledBatchOrderHistoryRepository scheduledBatchTransactionRepository;
    
    private final ScheduledBatchHistoryMapper scheduledBatchHistoryMapper;

    public PostScheduledBatchOrderHistoryResponse postScheduledBatchTransactions(PostScheduledBatchOrderHistoryRequest postScheduledBatchOrderHistoryRequest) {

        log.debug("Persisting scheduled Batch order transaction record: {}", postScheduledBatchOrderHistoryRequest);
        ScheduledBatchOrderHistory transaction = scheduledBatchHistoryMapper.toEntity(postScheduledBatchOrderHistoryRequest, OffsetDateTime.now());
        ScheduledBatchOrderHistory response = null;
        try {
            response = scheduledBatchTransactionRepository.save(transaction);
            return new PostScheduledBatchOrderHistoryResponse().id(String.valueOf(response.getId()));
        } catch (Exception e) {
            log.error("Error While string transaction details");
            e.printStackTrace();
        }
        log.debug("Persisted scheduled Batch order transaction record: {}", response);

        return null;

    }



    /**
     * If all the fields are null, then only set fromDate as todays date
     *
     * @param fromExecutionDate
     * @param toExecutionDate
     * @param status
     * @param accountNumber
     * @param createdBy
     * @param batchOrderId
     * @return
     */
    public List<ScheduledBatchHistoryItem> getScheduledBatchTransactions(LocalDate fromExecutionDate, LocalDate toExecutionDate, String status, String accountNumber, String createdBy, String batchOrderId, Pageable pageable) {
        /* If all the fields ae null, then only set the from date as today's date and covert to OffsetDate */
        OffsetDateTime fromDateTime = null;
        OffsetDateTime toDateTime = null;
        if (null == status && null == accountNumber && null == createdBy && null == batchOrderId && null == fromExecutionDate && null == toExecutionDate) {
            fromDateTime = convertToOffSetDateTime(LocalDate.now());
        }
        /* if todate is null, set  it to todays date and covert to OffsetDate */
        toDateTime = convertToOffSetDateTime(null == toExecutionDate ? LocalDate.now() : toExecutionDate).plusDays(1);
        /* Set Ret of the properties to Entity Object */
        ScheduledBatchOrderHistory transaction = scheduledBatchHistoryMapper.toScheduledBatchOrderHistory(batchOrderId, status, accountNumber, createdBy);
        if (null == transaction) {
            transaction = new ScheduledBatchOrderHistory();
        }
        /* Create the Example Matcher to search the records based on avialble fields from the Entity Object */
        ExampleMatcher matcher = ExampleMatcher.matchingAll();
        Example<ScheduledBatchOrderHistory> batchOrderHistory = Example.of(transaction, matcher);

        /* Since execution date need to handle greater than equal to and less than or equal to operation create the specification*/
        Page<ScheduledBatchOrderHistory> result = scheduledBatchTransactionRepository.findAll(getSpecFromDatesAndExample(fromDateTime, toDateTime, batchOrderHistory),pageable);

        return result.get().map(scheduledBatchHistoryMapper::toScheduledBatchOrderHistoryItem).collect(Collectors.toList());

    }


    public Specification<ScheduledBatchOrderHistory> getSpecFromDatesAndExample(OffsetDateTime from, OffsetDateTime to, Example<ScheduledBatchOrderHistory> example) {

        return (Specification<ScheduledBatchOrderHistory>) (root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (from != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("executionDateTime"), from));
            }
            if (to != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("executionDateTime"), to));
            }
            predicates.add(QueryByExamplePredicateBuilder.getPredicate(root, builder, example));

            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    private OffsetDateTime convertToOffSetDateTime(LocalDate localDate) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.toOffsetDateTime();
    }

}
