package net.lab1024.sa.admin.module.scrum.activities.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 系统动态表（全局操作记录） 更新表单
 *
 * @Author a
 * @Date 2025-09-30 10:32:40
 * @Copyright a
 */

@Data
public class SysActivitiesUpdateForm {

    @Schema(description = "动态ID（主键）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "动态ID（主键） 不能为空")
    private Long id;

}
