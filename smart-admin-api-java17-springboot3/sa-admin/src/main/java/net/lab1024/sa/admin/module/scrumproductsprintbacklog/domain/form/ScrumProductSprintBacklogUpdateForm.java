package net.lab1024.sa.admin.module.scrumproductsprintbacklog.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Sprint与产品待办中间表 更新表单
 *
 * @Author cmz
 * @Date 2025-10-11 22:41:46
 * @Copyright 1
 */

@Data
public class ScrumProductSprintBacklogUpdateForm {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "主键 不能为空")
    private Long id;

}