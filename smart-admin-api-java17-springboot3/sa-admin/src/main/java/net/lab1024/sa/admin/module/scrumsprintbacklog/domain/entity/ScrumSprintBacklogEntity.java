package net.lab1024.sa.admin.module.scrumsprintbacklog.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * Sprint待办事项表 实体类
 *
 * @Author system
 * @Date 2025-01-01 00:00:00
 * @Copyright system
 */

@Data
@TableName("scrum_sprint_backlogs")
public class ScrumSprintBacklogEntity {

    /**
     * 待办ID（主键）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属Sprint ID（关联scrum_sprints.id）
     */
    private Long sprintId;

    /**
     * 关联产品待办ID（关联scrum_product_backlogs.id）
     */
    private Long productBacklogId;

    /**
     * 团队成员ID（关联scrum_team_members.id）
     */
    private Long teamMemberId;

    /**
     * 任务标题（如9.23考试、接口开发）
     */
    private String backlogTitle;

    /**
     * 父节点ID（0=顶层）
     */
    private Long parentId;

    /**
     * 子节点ID列表（逗号分隔，存储scrum_sprint_backlogs.id）
     */
    private String childNode;

    /**
     * 执行状态（1=未开始，2=进行中，3=已完成）
     */
    private String backlogStatus;

    /**
     * 任务目标
     */
    private String backlogGoal;

    /**
     * 预计工时
     */
    private Double estimatedHours;

    /**
     * 实际工时
     */
    private Double actualHours;

    /**
     * 任务描述
     */
    private String backlogDescription;

    /**
     * 优先级（1=高，2=中，3=低）
     */
    private Integer priority;

    /**
     * 审视ID（关联scrum_review_plans.id）
     */
    private Long surveyId;

    /**
     * 逻辑删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人（关联sys_user.id）
     */
    private Long creator;

    /**
     * 修改人（关联sys_user.id）
     */
    private Long updater;

}