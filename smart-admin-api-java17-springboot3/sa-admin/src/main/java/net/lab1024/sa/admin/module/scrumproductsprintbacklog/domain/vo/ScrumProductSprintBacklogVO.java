package net.lab1024.sa.admin.module.scrumproductsprintbacklog.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Sprint与产品待办中间表 列表VO
 *
 * @Author cmz
 * @Date 2025-10-11 22:41:46
 * @Copyright 1
 */

@Data
public class ScrumProductSprintBacklogVO {


    @Schema(description = "主键")
    private Long id;

    @Schema(description = "Sprint项目ID（关联scrum_sprints.id）")
    private Long sprintId;

    @Schema(description = "产品待办ID（关联scrum_product_backlogs.id）")
    private Long backlogId;

}
