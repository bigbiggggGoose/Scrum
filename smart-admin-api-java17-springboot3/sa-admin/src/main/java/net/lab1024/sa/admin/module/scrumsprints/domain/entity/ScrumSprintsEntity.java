package net.lab1024.sa.admin.module.scrumsprints.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 项目迭代表 实体类
 *
 * @Author cmz
 * @Date 2025-10-11 22:41:18
 * @Copyright 1
 */

@Data
@TableName("scrum_sprints")
public class ScrumSprintsEntity {

    /**
     * Sprint ID（主键）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * Sprint名称（如数据库构建、系统设计）
     */
    private String sprintName;

    /**
     * 所属产品ID（关联scrum_products.id）
     */
    private Long productId;

    /**
     * 负责团队ID（关联scrum_teams.id）
     */
    private Long teamId;

    /**
     * Sprint目标
     */
    private String sprintGoal;

    /**
     * 迭代状态（not_started=未开始，in_progress=进行中，unfinished=未完成，completed=已完成，closed=已关闭，suspended=已挂起，delayed=已延期）
     */
    private String sprintStatus;

    /**
     * 迭代进度（0-100）
     */
    private Integer sprintProgress;

    /**
     * 总人日
     */
    private BigDecimal totalManDay;

    /**
     * 已消耗工时（小时）
     */
    private Integer consumedHours;

    /**
     * 开始时间
     */
    private LocalDate startDate;

    /**
     * 结束时间
     */
    private LocalDate endDate;

    /**
     * 备注
     */
    private String remark;

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
    private String creator;

    /**
     * 修改人（关联sys_user.id）
     */
    private String updater;

}
