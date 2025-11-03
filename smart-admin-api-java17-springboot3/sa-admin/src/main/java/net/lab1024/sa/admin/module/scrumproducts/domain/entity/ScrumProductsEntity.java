package net.lab1024.sa.admin.module.scrumproducts.domain.entity;

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
 * 产品表 实体类
 *
 * @Author cmz
 * @Date 2025-10-11 22:39:41
 * @Copyright 1
 */

@Data
@TableName("scrum_products")
public class ScrumProductsEntity {

    /**
     * 产品ID（主键）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品编号（唯一）
     */
    private String productCode;

    /**
     * 产品负责人（PO，关联sys_user.id）
     */
    private Long productOwnerId;

    /**
     * Scrum Master（关联sys_user.id）
     */
    private Long scrumMasterId;

    /**
     * 负责团队ID（关联scrum_teams.id）
     */
    private Long teamId;

    /**
     * 产品来源（如客户需求、内部立项）
     */
    private String productSource;

    /**
     * 状态（1=未开始，2=进行中，3=未完成，4=已完成，5=已关闭，6=已挂起，7=已延期）
     */
    private String productStatus;

    /**
     * 产品进度（0-100）
     */
    private Integer productProgress;

    /**
     * 总人日
     */
    private BigDecimal totalManDay;

    /**
     * 产品价值
     */
    private BigDecimal productValue;

    /**
     * 产品目标
     */
    private String productGoal;

    /**
     * 用户故事
     */
    private String userStory;

    /**
     * 验收标准
     */
    private String acceptanceStandard;

    /**
     * 边界设定（DOD）
     */
    private String boundary;

    /**
     * 干系人
     */
    private String stakeholders;

    /**
     * 用户和客户
     */
    private String customers;

    /**
     * 计划开始日期
     */
    private LocalDate planStartDate;

    /**
     * 计划结束日期
     */
    private LocalDate planEndDate;

    /**
     * 项目所需总天数
     */
    private BigDecimal totalWorkDays;

    /**
     * 已工作天数
     */
    private BigDecimal workedDays;

    /**
     * 产品备注
     */
    private String productRemarks;

    /**
     * 逻辑删除（0=未删，1=已删）
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
