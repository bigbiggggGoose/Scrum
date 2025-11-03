package net.lab1024.sa.admin.module.users.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户信息表 实体类
 *
 * @Author oyt
 * @Date 2025-10-11 17:34:30
 * @Copyright oyt
 */

@Data
@TableName("sys_user")
public class SysUserEntity {

    /**
     * 用户ID（主键，数值型）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 登录名（唯一）
     */
    private String username;

    /**
     * 密码（加密存储，如BCrypt）
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 工资表顺序
     */
    private Integer sidx;

    /**
     * 头像URL（支持长文本存储）
     */
    private String avatarUrl;

    /**
     * 生日
     */
    private LocalDate birth;

    /**
     * 性别（1=男，2=女）
     */
    private Integer sex;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号（唯一）
     */
    private String phone;

    /**
     * 角色ID（关联sys_role.id，数值型匹配）
     */
    private Long roleId;

    /**
     * 部门ID（关联sys_dept.id，数值型匹配）
     */
    private Long deptId;

    /**
     * 微信小程序用户ID
     */
    private String wxappUid;

    /**
     * 微信小程序OpenID
     */
    private String wxappOpenid;

    /**
     * 是否禁用（1=是，2=否）
     */
    private Integer isDisable;

    /**
     * 状态（1=在职，0=离职）
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
