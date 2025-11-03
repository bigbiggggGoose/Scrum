package net.lab1024.sa.admin.module.template.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 站内信/邮件模板表 新建表单
 *
 * @Author thr
 * @Date 2025-10-12 14:33:45
 * @Copyright wu
 */

@Data
public class TemplateAddForm {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "主键 不能为空")
    private Long id;

    @Schema(description = "模板标识（唯一，如sprint_start_notice）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "模板标识（唯一，如sprint_start_notice） 不能为空")
    private String templateCode;

    @Schema(description = "类型（1=站内信，2=邮件）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "类型（1=站内信，2=邮件） 不能为空")
    private Integer templateType;

    @Schema(description = "模板名称（如Sprint启动通知模板）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "模板名称（如Sprint启动通知模板） 不能为空")
    private String templateName;

    @Schema(description = "模板内容（支持变量占位符，如${sprintName}）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "模板内容（支持变量占位符，如${sprintName}） 不能为空")
    private String templateContent;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "创建时间 不能为空")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "更新时间 不能为空")
    private LocalDateTime updateTime;

}