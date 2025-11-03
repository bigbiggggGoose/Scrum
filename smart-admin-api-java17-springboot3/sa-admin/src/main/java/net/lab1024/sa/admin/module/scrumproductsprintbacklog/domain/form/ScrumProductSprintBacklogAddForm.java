package net.lab1024.sa.admin.module.scrumproductsprintbacklog.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Sprint与产品待办中间表 新建表单
 *
 * @Author cmz
 * @Date 2025-10-11 22:41:46
 * @Copyright 1
 */

@Data
public class ScrumProductSprintBacklogAddForm {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "主键 不能为空")
    private Long id;

    @Schema(description = "Sprint项目ID（关联scrum_sprints.id）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Sprint项目ID（关联scrum_sprints.id） 不能为空")
    private Long sprintId;

    @Schema(description = "产品待办ID（关联scrum_product_backlogs.id）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "产品待办ID（关联scrum_product_backlogs.id） 不能为空")
    private Long backlogId;

}