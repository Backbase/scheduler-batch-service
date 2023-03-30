package com.backbase.accelerators.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import com.backbase.batch.scheduler.service.model.PostScheduledBatchOrderHistoryRequest;
import com.backbase.batch.scheduler.service.model.PutScheduledBatchOrderRequest;
import com.backbase.batch.scheduler.service.model.ScheduledBatchHistoryItem;

import com.backbase.accelerators.model.entity.ScheduledBatchOrderHistory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ScheduledBatchHistoryMapperTest {

    private final ScheduledBatchHistoryMapper mapper = Mappers.getMapper(ScheduledBatchHistoryMapper.class);

    @Test
    public void ToEntityTest() {
        assertNull(mapper.toEntity(null, null));

        PostScheduledBatchOrderHistoryRequest request = new PostScheduledBatchOrderHistoryRequest();
        request.setAdditions(Collections.singletonMap("key", "value"));
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        ScheduledBatchOrderHistory expected = new ScheduledBatchOrderHistory();
        expected.setAdditions("{\"key\":\"value\"}");
        expected.setExecutionDateTime(offsetDateTime);
        ScheduledBatchOrderHistory actual = mapper.toEntity(request, offsetDateTime);
        assertEquals(expected, actual);
    }

    @Test
    public void toPostScheduledBatchOrderHistoryRequestTest() {
        assertNull(mapper.toPostScheduledBatchOrderHistoryRequest(null));

        PutScheduledBatchOrderRequest request = new PutScheduledBatchOrderRequest();
        PostScheduledBatchOrderHistoryRequest expected = new PostScheduledBatchOrderHistoryRequest();
        PostScheduledBatchOrderHistoryRequest actual = mapper.toPostScheduledBatchOrderHistoryRequest(request);
        assertEquals(expected, actual);
    }

    @Test
    public void testToScheduledBatchOrderHistoryItem() {
        assertNull(mapper.toScheduledBatchOrderHistoryItem(null));

        ScheduledBatchOrderHistory scheduledBatchOrder = new ScheduledBatchOrderHistory();
        scheduledBatchOrder.setAdditions("{\"key\":\"value\"}");
        scheduledBatchOrder.setBatchOrderId("batchOrderId");
        scheduledBatchOrder.setExecutionDateTime(OffsetDateTime.now());
        ScheduledBatchHistoryItem expected = new ScheduledBatchHistoryItem();
        expected.setAdditions(new HashMap<>(Collections.singletonMap("key", "value")));
        expected.setBatchOrderId("batchOrderId");
        expected.setExecutionDateTime(scheduledBatchOrder.getExecutionDateTime());
        ScheduledBatchHistoryItem actual = mapper.toScheduledBatchOrderHistoryItem(scheduledBatchOrder);
        assertEquals(expected, actual);
    }

//    @Test
//    public void testToScheduledBatchOrderHistory() {
//
//        assertNull(mapper.toScheduledBatchOrderHistory(null, null, null, null));
//
//
//        String batchOrderId = "batchOrderId";
//        String status = "status";
//        String accountNumber = "accountNumber";
//        String createdBy = "createdBy";
//        ScheduledBatchOrderHistory expected = new ScheduledBatchOrderHistory();
//        expected.setBatchOrderId(batchOrderId);
//        expected.setStatus(status);
//        expected.getBatchOrder().setAccountNumber(accountNumber);
//        expected.getBatchOrder().setCreatedBy(createdBy);
//        ScheduledBatchOrderHistory actual = mapper.toScheduledBatchOrderHistory(batchOrderId, status, accountNumber, createdBy);
//        assertEquals(expected, actual);
//    }
}
