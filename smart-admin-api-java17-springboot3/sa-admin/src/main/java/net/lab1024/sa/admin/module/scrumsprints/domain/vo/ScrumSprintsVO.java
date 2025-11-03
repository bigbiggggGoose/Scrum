package net.lab1024.sa.admin.module.scrumsprints.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 项目迭代表 列表VO
 *
 * @Author cmz
 * @Date 2025-10-11 22:41:18
 * @Copyright 1
 */

@Data
public class ScrumSprintsVO {


    @Schema(description = "Sprint ID（主键）")
    private Long id;

    @Schema(description = "Sprint名称（如数据库构建、系统设计）")
    private String sprintName;

    @Schema(description = "所属产品ID（关联scrum_products.id）")
    private Long productId;

    @Schema(description = "所属产品名称")
    private String productName;

    @Schema(description = "负责团队ID（关联scrum_teams.id）")
    private Long teamId;

    @Schema(description = "负责团队名称")
    private String teamName;

    @Schema(description = "Sprint目标")
    private String sprintGoal;

    @Schema(description = "迭代状态（not_started=未开始，in_progress=进行中，unfinished=未完成，completed=已完成，closed=已关闭，suspended=已挂起，delayed=已延期）")
    private String sprintStatus;

    @Schema(description = "迭代进度（0-100）")
    private Integer sprintProgress;

    @Schema(description = "总人日")
    private BigDecimal totalManDay;

    @Schema(description = "已消耗工时（小时）")
    private Integer consumedHours;

    @Schema(description = "开始时间")
    private LocalDate startDate;

    @Schema(description = "结束时间")
    private LocalDate endDate;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "逻辑删除")
    private Integer isDeleted;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建人（关联sys_user.id）")
    private String creator;

    @Schema(description = "修改人（关联sys_user.id）")
    private String updater;

}
