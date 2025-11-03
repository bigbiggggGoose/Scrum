package net.lab1024.sa.admin.module.systemconfig.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 网站与邮箱配置表 新建表单
 *
 * @Author thr
 * @Date 2025-10-12 14:40:06
 * @Copyright wu
 */

@Data
public class SystemConfigAddForm {

    @Schema(description = "配置ID（主键）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "配置ID（主键） 不能为空")
    private Integer id;

}