package com.backbase.accelerators.mapper;

import com.backbase.batch.scheduler.service.model.PostScheduledBatchOrderHistoryRequest;
import com.backbase.batch.scheduler.service.model.PutScheduledBatchOrderRequest;
import com.backbase.batch.scheduler.service.model.ScheduledBatchHistoryItem;
import com.backbase.accelerators.model.entity.ScheduledBatchOrderHistory;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.OffsetDateTime;

@Mapper(componentModel = "spring")
public interface ScheduledBatchHistoryMapper extends BaseMapper {


    @Mapping(target = "additions", source = "request.additions", qualifiedByName = "toJsonString")
    @Mapping( target = "executionDateTime", source = "offsetDateTime")
    ScheduledBatchOrderHistory toEntity(PostScheduledBatchOrderHistoryRequest request, OffsetDateTime offsetDateTime);

    PostScheduledBatchOrderHistoryRequest toPostScheduledBatchOrderHistoryRequest(PutScheduledBatchOrderRequest request);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "additions", target = "additions", qualifiedByName = "toMap")
    @Mapping(source = "batchOrder.additions", target = "batchOrder.additions", qualifiedByName = "toMap")
    ScheduledBatchHistoryItem toScheduledBatchOrderHistoryItem(ScheduledBatchOrderHistory scheduledBatchOrder);

    @Mapping( target = "batchOrderId",source = "batchOrderId")
    @Mapping( target = "status",source = "status")
    @Mapping( target = "batchOrder.accountNumber",source = "accountNumber")
    @Mapping( target = "batchOrder.createdBy",source = "createdBy")
    ScheduledBatchOrderHistory toScheduledBatchOrderHistory(String batchOrderId, String status,
                                                                    String accountNumber, String createdBy);
}


