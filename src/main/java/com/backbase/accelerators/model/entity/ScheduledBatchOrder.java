package com.backbase.accelerators.model.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "schedule_batch_order")
@DynamicUpdate
public class ScheduledBatchOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "batch_order_id")
    private String batchOrderId;

    @NotNull
    @Column(name = "req_execution_date")
    private LocalDate reqExecutionDate;

    @Column(name = "next_execution_date")
    private LocalDate nextExecutionDate;
    @NotNull
    @Column(name = "pmt_type")
    private String pmtType;
    @NotNull
    @Column(name = "status")
    private String status;

    @NotNull
    @Column(name = "pmt_mode")
    private String pmtMode;
    @NotNull
    @Column(name = "account_number")
    private String accountNumber;

    @NotNull
    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "reason_text")
    private String reasonText;

    @Column(name = "error_description")
    private String errorDescription;

    /* Below fields are specific to recurring payment */
    @Column(name = "frequency")
    private String frequency;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "repetition")
    private int repetition;

    @Column(name = "when_execute")
    private int whenExecute;

    @Column(name = "end_type")
    private String endType;

    @Size(max = 500)
    @Column(name = "additions")
    private String additions;

}
