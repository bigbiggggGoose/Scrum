package net.lab1024.sa.admin.module.scrum.activities.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 系统动态表（全局操作记录） 实体类
 *
 * @Author a
 * @Date 2025-09-30 10:32:40
 * @Copyright a
 */

@Data
@TableName("sys_activities")
public class SysActivitiesEntity {

    /**
     * 动态ID（主键）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 操作用户ID（关联sys_user.id）
     */
    private Long userId;

    /**
     * 用户名（冗余，便于前端显示）
     */
    private String userName;

    /**
     * 操作类型（add=添加，delete=删除，update=修改，view=查看）
     */
    private String actionType;

    /**
     * 操作对象类型（product=产品，sprint=Sprint，sprint_backlog=Sprint待办）
     */
    private String targetType;

    /**
     * 操作对象ID
     */
    private Long targetId;

    /**
     * 动态内容（如"Rick添加Sprint待办：9.23考试""李华删除产品：XX系统"）
     */
    private String activityContent;

    /**
     * 操作时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
