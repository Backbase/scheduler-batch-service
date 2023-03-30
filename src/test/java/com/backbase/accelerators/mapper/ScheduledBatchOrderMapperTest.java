package com.backbase.accelerators.mapper;

import com.backbase.batch.scheduler.service.model.PostScheduledBatchOrderRequest;
import com.backbase.batch.scheduler.service.model.PutScheduledBatchOrderRequest;
import com.backbase.batch.scheduler.service.model.ScheduledBatchOrderItem;
import com.backbase.accelerators.model.entity.ScheduledBatchOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
class ScheduledBatchOrderMapperTest {

    @InjectMocks
    ScheduledBatchOrderMapperImpl scheduledBatchOrderMapper;


    @Test
    void toEntityTest() {

        PostScheduledBatchOrderRequest postScheduledBatchOrderRequest =
                new PostScheduledBatchOrderRequest();
        postScheduledBatchOrderRequest.setBatchOrderId("batch-order-id");
        ScheduledBatchOrder scheduledBatchOrder=scheduledBatchOrderMapper.toEntity(postScheduledBatchOrderRequest);
        assertAll(
                ()-> assertThat(scheduledBatchOrder).hasFieldOrPropertyWithValue("batchOrderId", "batch-order-id")
        );
    }


}
