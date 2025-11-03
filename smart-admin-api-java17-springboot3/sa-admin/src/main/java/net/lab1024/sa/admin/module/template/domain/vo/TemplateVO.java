package net.lab1024.sa.admin.module.template.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 站内信/邮件模板表 列表VO
 *
 * @Author thr
 * @Date 2025-10-12 14:33:45
 * @Copyright wu
 */

@Data
public class TemplateVO {


    @Schema(description = "主键")
    private Long id;

    @Schema(description = "模板标识（唯一，如sprint_start_notice）")
    private String templateCode;

    @Schema(description = "类型（1=站内信，2=邮件）")
    private Integer templateType;

    @Schema(description = "模板名称（如Sprint启动通知模板）")
    private String templateName;

    @Schema(description = "模板内容（支持变量占位符，如${sprintName}）")
    private String templateContent;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建人（sys_user.id，数值型匹配）")
    private Long creator;

    @Schema(description = "修改人（sys_user.id，数值型匹配）")
    private Long updater;

}
