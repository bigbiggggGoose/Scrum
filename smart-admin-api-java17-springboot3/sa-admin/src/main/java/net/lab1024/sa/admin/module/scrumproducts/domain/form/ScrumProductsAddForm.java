package net.lab1024.sa.admin.module.scrumproducts.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

/**
 * 产品表 新建表单
 *
 * @Author cmz
 * @Date 2025-10-11 22:39:41
 * @Copyright 1
 */

@Data
public class ScrumProductsAddForm {

    @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "产品名称 不能为空")
    private String productName;

    @Schema(description = "产品编号")
    @NotBlank(message = "产品编号 不能为空")
    private String productCode;

    @Schema(description = "产品负责人PO", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "产品负责人PO 不能为空")
    private Long productOwnerId;

    @Schema(description = "Scrum Master")
    private Long scrumMasterId;

    @Schema(description = "负责团队ID")
    private Long teamId;

    @Schema(description = "产品来源")
    private String productSource;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "状态 不能为空")
    private String productStatus;

    @Schema(description = "产品进度", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "产品进度 不能为空")
    private Integer productProgress;

    @Schema(description = "总人日")
    private BigDecimal totalManDay;

    @Schema(description = "产品价值")
    private BigDecimal productValue;

    @Schema(description = "产品目标")
    private String productGoal;

    @Schema(description = "用户故事")
    private String userStory;

    @Schema(description = "验收标准")
    private String acceptanceStandard;

    @Schema(description = "边界设定")
    private String boundary;

    @Schema(description = "干系人")
    private String stakeholders;

    @Schema(description = "用户和客户")
    private String customers;

    @Schema(description = "计划开始日期")
    private LocalDate planStartDate;

    @Schema(description = "计划结束日期")
    private LocalDate planEndDate;

    @Schema(description = "项目所需总天数")
    private BigDecimal totalWorkDays;

    @Schema(description = "已工作天数")
    private BigDecimal workedDays;

    @Schema(description = "产品备注")
    private String productRemarks;
}