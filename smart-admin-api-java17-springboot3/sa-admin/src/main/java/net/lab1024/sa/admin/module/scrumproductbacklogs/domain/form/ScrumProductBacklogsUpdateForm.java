package net.lab1024.sa.admin.module.scrumproductbacklogs.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 产品待办事项表 更新表单
 *
 * @Author cmz
 * @Date 2025-10-11 22:40:46
 * @Copyright 1
 */

@Data
public class ScrumProductBacklogsUpdateForm {

    @Schema(description = "待办ID（主键）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "待办ID（主键） 不能为空")
    private Long id;

}