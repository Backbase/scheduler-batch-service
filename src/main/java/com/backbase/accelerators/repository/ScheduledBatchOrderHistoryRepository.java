package com.backbase.accelerators.repository;

import com.backbase.accelerators.model.entity.ScheduledBatchOrderHistory;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * This repository class is used to persist and retrieve the data from schedule_batch_order_history table.
 */
@Repository
@DynamicUpdate
public interface ScheduledBatchOrderHistoryRepository
        extends JpaRepository<ScheduledBatchOrderHistory, Long>, JpaSpecificationExecutor<ScheduledBatchOrderHistory> {

}
