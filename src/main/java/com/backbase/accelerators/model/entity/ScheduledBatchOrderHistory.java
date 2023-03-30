package com.backbase.accelerators.model.entity;

import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "schedule_batch_order_history")
@DynamicUpdate
public class ScheduledBatchOrderHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "batch_order_id")
    private String batchOrderId;

    @NotNull
    @Column(name = "execution_date_time")
    private OffsetDateTime executionDateTime;

    @NotNull
    @Column(name = "status")
    private String status;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "reason_text")
    private String reasonText;

    @Column(name = "error_description")
    private String errorDescription;
    @Size(max = 500)
    @Column(name = "additions")
    private String additions;

    @OneToOne
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn(name = "batch_order_id", referencedColumnName = "batch_order_id", nullable = false,
            insertable=false, updatable=false)
    private ScheduledBatchOrder batchOrder;
}
