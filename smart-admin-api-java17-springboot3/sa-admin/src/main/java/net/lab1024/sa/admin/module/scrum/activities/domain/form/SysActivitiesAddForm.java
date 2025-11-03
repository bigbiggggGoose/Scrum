package net.lab1024.sa.admin.module.scrum.activities.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 系统动态表（全局操作记录） 新建表单
 *
 * @Author a
 * @Date 2025-09-30 10:32:40
 * @Copyright a
 */

@Data
public class SysActivitiesAddForm {

    @Schema(description = "动态ID（主键）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "动态ID（主键） 不能为空")
    private Long id;

    @Schema(description = "操作用户ID（关联sys_user.id）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "操作用户ID（关联sys_user.id） 不能为空")
    private Long userId;

    @Schema(description = "用户名（冗余，便于前端显示）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用户名（冗余，便于前端显示） 不能为空")
    private String userName;

    @Schema(description = "操作类型（add=添加，delete=删除，update=修改，view=查看）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "操作类型（add=添加，delete=删除，update=修改，view=查看） 不能为空")
    private String actionType;

    @Schema(description = "操作对象类型（product=产品，sprint=Sprint，sprint_backlog=Sprint待办）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "操作对象类型（product=产品，sprint=Sprint，sprint_backlog=Sprint待办） 不能为空")
    private String targetType;

    @Schema(description = "操作对象ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "操作对象ID 不能为空")
    private Long targetId;

    @Schema(description = "动态内容（如Rick添加Sprint待办：9.23考试、李华删除产品：XX系统）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "动态内容（如Rick添加Sprint待办：9.23考试、李华删除产品：XX系统） 不能为空")
    private String activityContent;

    @Schema(description = "操作时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "操作时间 不能为空")
    private LocalDateTime createTime;

}
