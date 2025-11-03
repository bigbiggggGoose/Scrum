package net.lab1024.sa.admin.module.dept.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 部门管理 实体类
 *
 * @Author oyt
 * @Date 2025-10-11 17:57:33
 * @Copyright oyt
 */

@Data
@TableName("sys_dept")
public class SysDeptEntity {

    /**
     * 部门ID（主键）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父部门ID（0=顶级部门）
     */
    private Long parentId;

    /**
     * 部门标识（唯一）
     */
    private String deptCode;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 排序（数值越小越靠前）
     */
    private Integer orderNum;

    /**
     * 部门等级（1=总部，2=分公司，3=部门）
     */
    private Integer level;

    /**
     * 关联区域ID（多个用逗号分隔，关联sys_area.id）
     */
    private String dptarea;

    /**
     * 状态（1=正常，0=停用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

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

}
