package net.lab1024.sa.admin.module.scrumsprintbacklog.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lab1024.sa.base.common.domain.PageParam;

/**
 * Sprint待办事项表 查询表单
 *
 * @Author system
 * @Date 2025-01-01 00:00:00
 * @Copyright system
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ScrumSprintBacklogQueryForm extends PageParam {

    @Schema(description = "所属Sprint ID")
    private Long sprintId;

    @Schema(description = "任务标题")
    private String backlogTitle;

    @Schema(description = "执行状态（1=未开始，2=进行中，3=已完成）")
    private String backlogStatus;

    @Schema(description = "团队成员ID")
    private Long teamMemberId;

    @Schema(description = "优先级（1=高，2=中，3=低）")
    private Integer priority;

    @Schema(description = "产品名称（模糊匹配）")
    private String productName;

    @Schema(description = "迭代名称（模糊匹配）")
    private String sprintName;
}