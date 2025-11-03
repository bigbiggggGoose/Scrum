package net.lab1024.sa.admin.module.scrumsprints.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 项目迭代表 更新表单
 *
 * @Author cmz
 * @Date 2025-10-11 22:41:18
 * @Copyright 1
 */

@Data
public class ScrumSprintsUpdateForm {

    @Schema(description = "Sprint ID（主键）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Sprint ID（主键） 不能为空")
    private Long id;

}