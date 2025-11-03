package net.lab1024.sa.admin.module.scrumproductbacklogs.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 产品待办事项表 新建表单
 *
 * @Author cmz
 * @Date 2025-10-11 22:40:46
 * @Copyright 1
 */

@Data
public class ScrumProductBacklogsAddForm {

    @Schema(description = "待办ID（主键）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "待办ID（主键） 不能为空")
    private Long id;

    @Schema(description = "所属产品ID（关联scrum_products.id）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "所属产品ID（关联scrum_products.id） 不能为空")
    private Long productId;

    @Schema(description = "待办名称（用户故事标题）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "待办名称（用户故事标题） 不能为空")
    private String backlogName;

    @Schema(description = "优先级（数字越大优先级越低）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "优先级（数字越大优先级越低） 不能为空")
    private Integer priority;

}