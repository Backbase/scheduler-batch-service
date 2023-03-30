package com.backbase.accelerators.repository;

import com.backbase.accelerators.model.entity.ScheduledBatchOrder;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@DynamicUpdate
public interface ScheduledBatchOrderRepository
        extends JpaRepository<ScheduledBatchOrder, Long>, JpaSpecificationExecutor<ScheduledBatchOrder> {

    /**
     * Get Scheduled Batch Order Based on batch order Id
     *
     * @param batchOrderId
     * @return
     */
    ScheduledBatchOrder findByBatchOrderId(String batchOrderId);

    /**
     * Get List of Batch Orders Based in the given input params
     *
     * @param nextExecutionDate
     * @param status
     * @return
     */
    Page<ScheduledBatchOrder> findAllByNextExecutionDateAndStatusIn(LocalDate nextExecutionDate, List<String> status, Pageable pageable);
}
