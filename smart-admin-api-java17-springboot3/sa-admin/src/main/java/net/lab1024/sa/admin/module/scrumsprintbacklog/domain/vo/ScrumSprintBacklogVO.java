package net.lab1024.sa.admin.module.scrumsprintbacklog.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * Sprint待办事项表 视图对象
 *
 * @Author system
 * @Date 2025-01-01 00:00:00
 * @Copyright system
 */

@Data
public class ScrumSprintBacklogVO {

    @Schema(description = "待办ID（主键）")
    private Long id;

    @Schema(description = "所属Sprint ID（关联scrum_sprints.id）")
    private Long sprintId;

    @Schema(description = "Sprint名称")
    private String sprintName;

    @Schema(description = "所属产品名称")
    private String productName;

    @Schema(description = "负责团队名称")
    private String teamName;

    @Schema(description = "关联产品待办ID（关联scrum_product_backlogs.id）")
    private Long productBacklogId;

    @Schema(description = "产品待办名称")
    private String productBacklogName;

    @Schema(description = "团队成员ID（关联scrum_team_members.id）")
    private Long teamMemberId;

    @Schema(description = "团队成员名称")
    private String teamMemberName;

    @Schema(description = "任务标题（如9.23考试、接口开发）")
    private String backlogTitle;

    @Schema(description = "父节点ID（0=顶层）")
    private Long parentId;

    @Schema(description = "子节点ID列表（逗号分隔，存储scrum_sprint_backlogs.id）")
    private String childNode;

    @Schema(description = "执行状态（1=未开始，2=进行中，3=已完成）")
    private String backlogStatus;

    @Schema(description = "执行状态名称")
    private String backlogStatusName;

    @Schema(description = "任务目标")
    private String backlogGoal;

    @Schema(description = "预计工时")
    private Double estimatedHours;

    @Schema(description = "实际工时")
    private Double actualHours;

    @Schema(description = "任务描述")
    private String backlogDescription;

    @Schema(description = "优先级（1=高，2=中，3=低）")
    private Integer priority;

    @Schema(description = "优先级名称")
    private String priorityName;

    @Schema(description = "审视ID（关联scrum_review_plans.id）")
    private Long surveyId;

    @Schema(description = "逻辑删除")
    private Integer isDeleted;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建人（关联sys_user.id）")
    private Long creator;

    @Schema(description = "修改人（关联sys_user.id）")
    private Long updater;

}