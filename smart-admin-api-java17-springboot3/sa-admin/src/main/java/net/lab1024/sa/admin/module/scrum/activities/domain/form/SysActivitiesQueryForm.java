package net.lab1024.sa.admin.module.scrum.activities.domain.form;

import net.lab1024.sa.base.common.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统动态表（全局操作记录） 分页查询表单
 *
 * @Author a
 * @Date 2025-09-30 10:32:40
 * @Copyright a
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class SysActivitiesQueryForm extends PageParam {

    @Schema(description = "动态ID（主键）")
    private Long id;

    @Schema(description = "操作用户ID（关联sys_user.id）")
    private Long userId;

    @Schema(description = "用户名（模糊匹配）")
    private String userName;

    @Schema(description = "操作类型（add/delete/update/view）")
    private String actionType;

    @Schema(description = "操作对象类型（product/sprint/sprint_backlog）")
    private String targetType;

    @Schema(description = "操作对象ID")
    private Long targetId;

    @Schema(description = "动态内容（模糊匹配）")
    private String activityContent;
}
