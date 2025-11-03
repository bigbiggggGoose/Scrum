package net.lab1024.sa.admin.module.scrumproductbacklogs.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 产品待办事项表 实体类
 *
 * @Author cmz
 * @Date 2025-10-11 22:40:46
 * @Copyright 1
 */

@Data
@TableName("scrum_product_backlogs")
public class ScrumProductBacklogsEntity {

    /**
     * 待办ID（主键）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属产品ID（关联scrum_products.id）
     */
    private Long productId;

    /**
     * 父待办ID（0=顶层，支撑树形结构）
     */
    private Long parentId;

    /**
     * 待办名称（用户故事标题）
     */
    private String backlogName;

    /**
     * 优先级（数字越大优先级越低）
     */
    private Integer priority;

    /**
     * 人日
     */
    private BigDecimal manDay;

    /**
     * 待办内容
     */
    private String content;

    /**
     * 分级（最大4级）
     */
    private Integer level;

    /**
     * 单元格样式：1=靠左白背景，2=居中白背景，3=居中加粗白背景，4=居中加粗蓝背景（#b7dee8）等
     */
    private Integer cellStyle;

    /**
     * 同级排序权重（数值越小越靠前）
     */
    private Integer backlogWeight;

    /**
     * 预估工时（小时）
     */
    private Integer estimatedHours;

    /**
     * 状态（关联sys_dict：todo=待办，in_progress=进行中，completed=已完成）
     */
    private String backlogStatus;

    /**
     * 备注
     */
    private String backlogRemarks;

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
