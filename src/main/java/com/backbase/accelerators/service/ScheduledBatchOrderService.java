package com.backbase.accelerators.service;

import com.backbase.batch.scheduler.service.model.*;
import com.backbase.accelerators.mapper.ScheduledBatchHistoryMapper;
import com.backbase.accelerators.mapper.ScheduledBatchOrderMapper;
import com.backbase.accelerators.model.entity.ScheduledBatchOrder;
import com.backbase.accelerators.repository.ScheduledBatchOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledBatchOrderService {

    private final ScheduledBatchOrderRepository scheduledBatchOrderRepository;

    private final ScheduledBatchOrderMapper scheduledPaymentOrderMapper;

    private final ScheduledBatchHistoryMapper scheduledBatchHistoryMapper;
    private final ScheduledBatchTransactionsService scheduledBatchTransactionsService;
    private static final String STATUS_ACKNOWLEDGED = "ACKNOWLEDGED";
    private static final String STATUS_DOWNLOADING = "DOWNLOADING";

    private static final String PMT_MODE = "RECURRING";
    private static final List<String> getQuerystatusList = Arrays.asList(STATUS_ACKNOWLEDGED, STATUS_DOWNLOADING);

    public List<ScheduledBatchOrderItem> getScheduledBatchOrders(
            LocalDate nextExecutionDate, Integer from, Integer size) {
        log.info("Pull the Scheduled batch Order list based on executionDate {}, from {}, size {}", nextExecutionDate, from, size);
        Pageable pageable = PageRequest.of(from, size);
        Page<ScheduledBatchOrder> result = scheduledBatchOrderRepository.findAllByNextExecutionDateAndStatusIn(LocalDate.now(), getQuerystatusList, pageable);
        List<ScheduledBatchOrderItem> lists = result.get().map(scheduledPaymentOrderMapper::toScheduledBatchOrderItem).collect(Collectors.toList());
        return lists;
    }

    public PostScheduledBatchOrderResponse postScheduledPaymentOrder(
            PostScheduledBatchOrderRequest postScheduledBatchOrderRequest) {


        ScheduledBatchOrder response = scheduledBatchOrderRepository.save(
                scheduledPaymentOrderMapper.toEntity(postScheduledBatchOrderRequest));

        log.debug("Persisted scheduled Batch order record: {}", response);
        Optional<ScheduledBatchOrder> batchOrder = scheduledBatchOrderRepository.findById(response.getId());
        if (batchOrder.isPresent())
            return new PostScheduledBatchOrderResponse().batchOrderItem(scheduledPaymentOrderMapper.toScheduledBatchOrderItem(batchOrder.get()));
        return null;
    }

    public PutScheduledBatchOrderResponse putScheduledPaymentOrder(
            PutScheduledBatchOrderRequest putScheduledBatchOrderRequest) {

        ScheduledBatchOrder scheduledBatchOrder = scheduledBatchOrderRepository.findByBatchOrderId(putScheduledBatchOrderRequest.getBatchOrderId());
        scheduledPaymentOrderMapper.updateScheduledBatchOrder(putScheduledBatchOrderRequest, scheduledBatchOrder);
        if (PMT_MODE.equals(scheduledBatchOrder.getPmtMode()) && null != putScheduledBatchOrderRequest.getNextExecutionDate())
            scheduledBatchOrder.setStatus(STATUS_ACKNOWLEDGED);
        if (null == putScheduledBatchOrderRequest.getNextExecutionDate())
            scheduledBatchOrder.setNextExecutionDate(null);
        ScheduledBatchOrder response = scheduledBatchOrderRepository.save(scheduledBatchOrder);

        log.debug("Persisted scheduled payment order transaction record: {}", response);
        /**
         * Add transaction record as well **/
        PostScheduledBatchOrderHistoryRequest request = scheduledBatchHistoryMapper.toPostScheduledBatchOrderHistoryRequest(putScheduledBatchOrderRequest);
        scheduledBatchTransactionsService.postScheduledBatchTransactions(request);

        return new PutScheduledBatchOrderResponse()
                .id(String.valueOf(response.getId()));
    }
}
