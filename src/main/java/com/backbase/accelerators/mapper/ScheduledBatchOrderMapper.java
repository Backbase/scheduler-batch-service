package com.backbase.accelerators.mapper;

import com.backbase.batch.scheduler.service.model.PostScheduledBatchOrderRequest;
import com.backbase.batch.scheduler.service.model.PutScheduledBatchOrderRequest;
import com.backbase.batch.scheduler.service.model.ScheduledBatchOrderItem;
import com.backbase.accelerators.model.entity.ScheduledBatchOrder;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ScheduledBatchOrderMapper extends BaseMapper{


    @Mapping(source = "additions", target = "addi" +
            "tions", qualifiedByName = "toJsonString")
    @Mapping( target = "createdBy", source = "createdBy")
    ScheduledBatchOrder toEntity(PostScheduledBatchOrderRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "additions", target = "additions", qualifiedByName = "toJsonString")
    void updateScheduledBatchOrder(PutScheduledBatchOrderRequest source, @MappingTarget ScheduledBatchOrder target);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "additions", target = "additions", qualifiedByName = "toMap")
    ScheduledBatchOrderItem toScheduledBatchOrderItem(ScheduledBatchOrder scheduledBatchOrder);


}
