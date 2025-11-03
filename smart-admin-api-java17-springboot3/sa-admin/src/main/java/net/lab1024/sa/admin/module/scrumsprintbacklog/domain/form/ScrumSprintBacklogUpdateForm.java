package net.lab1024.sa.admin.module.scrumsprintbacklog.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Sprint待办事项表 更新表单
 *
 * @Author system
 * @Date 2025-01-01 00:00:00
 * @Copyright system
 */

@Data
public class ScrumSprintBacklogUpdateForm {

    @Schema(description = "待办ID（主键）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "待办ID 不能为空")
    private Long id;

    @Schema(description = "所属Sprint ID（关联scrum_sprints.id）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "所属Sprint ID 不能为空")
    private Long sprintId;

    @Schema(description = "关联产品待办ID（关联scrum_product_backlogs.id）")
    private Long productBacklogId;

    @Schema(description = "团队成员ID（关联scrum_team_members.id）")
    private Long teamMemberId;

    @Schema(description = "任务标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "任务标题 不能为空")
    private String backlogTitle;

    @Schema(description = "父节点ID（0=顶层）")
    private Long parentId;

    @Schema(description = "子节点ID列表（逗号分隔）")
    private String childNode;

    @Schema(description = "执行状态（1=未开始，2=进行中，3=已完成）")
    private String backlogStatus;

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

    @Schema(description = "审视ID（关联scrum_review_plans.id）")
    private Long surveyId;

}