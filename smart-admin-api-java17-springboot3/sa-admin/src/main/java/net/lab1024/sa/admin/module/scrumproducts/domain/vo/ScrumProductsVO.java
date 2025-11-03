package net.lab1024.sa.admin.module.scrumproducts.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 产品表 列表VO
 *
 * @Author cmz
 * @Date 2025-10-11 22:39:41
 * @Copyright 1
 */

@Data
public class ScrumProductsVO {


    @Schema(description = "产品ID（主键）")
    private Long id;

    @Schema(description = "产品名称")
    private String productName;

    @Schema(description = "产品编号（唯一）")
    private String productCode;

    @Schema(description = "产品负责人（PO，关联sys_user.id）")
    private Long productOwnerId;

    @Schema(description = "Scrum Master（关联sys_user.id）")
    private Long scrumMasterId;

    @Schema(description = "负责团队ID（关联scrum_teams.id）")
    private Long teamId;

    @Schema(description = "产品来源（如客户需求、内部立项）")
    private String productSource;

    @Schema(description = "状态（1=未开始，2=进行中，3=未完成，4=已完成，5=已关闭，6=已挂起，7=已延期）")
    private String productStatus;

    @Schema(description = "产品进度（0-100）")
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

    @Schema(description = "边界设定（DOD）")
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

    @Schema(description = "逻辑删除（0=未删，1=已删）")
    private Integer isDeleted;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建人（关联sys_user.id）")
    private String creator;

    @Schema(description = "修改人（关联sys_user.id）")
    private String updater;

    @Schema(description = "负责团队名称（派生）")
    private String teamName;

    @Schema(description = "产品负责人姓名（派生）")
    private String poName;

    @Schema(description = "Scrum Master 姓名（派生，来自团队成员角色=SM）")
    private String smName;

    @Schema(description = "负责团队人数（派生，统计is_active=1成员数）")
    private Integer teamMemberCount;
}
