package net.lab1024.sa.admin.module.role.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 角色管理 实体类
 *
 * @Author oyt
 * @Date 2025-10-11 18:33:10
 * @Copyright oyt
 */

@Data
@TableName("sys_role")
public class SysRoleEntity {

    /**
     * 角色ID（主键）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色标识（唯一，如ROLE_ADMIN）
     */
    private String roleCode;

    /**
     * 角色名称（如超级管理员）
     */
    private String roleName;

    /**
     * 父角色ID（0=顶级角色）
     */
    private Long parentId;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 角色等级
     */
    private Integer level;

    /**
     * 绩效公式
     */
    private String jxfm;

    /**
     * 待遇描述
     */
    private String dy;

    /**
     * 状态（1=启用，0=禁用）
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
