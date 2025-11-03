package net.lab1024.sa.admin.module.scrum.activities.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 系统动态表（全局操作记录） 列表VO
 *
 * @Author a
 * @Date 2025-09-30 10:32:40
 * @Copyright a
 */

@Data
public class SysActivitiesVO {


    @Schema(description = "动态ID（主键）")
    private Long id;

    @Schema(description = "操作用户ID（关联sys_user.id）")
    private Long userId;

    @Schema(description = "用户名（冗余，便于前端显示）")
    private String userName;

    @Schema(description = "操作类型（add=添加，delete=删除，update=修改，view=查看）")
    private String actionType;

    @Schema(description = "操作对象类型（product=产品，sprint=Sprint，sprint_backlog=Sprint待办）")
    private String targetType;

    @Schema(description = "操作对象ID")
    private Long targetId;

    @Schema(description = "动态内容（如Rick添加Sprint待办：9.23考试、李华删除产品：XX系统）")
    private String activityContent;

    @Schema(description = "操作时间")
    private LocalDateTime createTime;

}
