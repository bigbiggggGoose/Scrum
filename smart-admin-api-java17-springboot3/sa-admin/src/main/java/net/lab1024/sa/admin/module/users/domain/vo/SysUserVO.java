package net.lab1024.sa.admin.module.users.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Data;

/**
 * 用户信息表 列表VO
 *
 * @Author oyt
 * @Date 2025-10-11 17:34:30
 * @Copyright oyt
 */

@Data
public class SysUserVO {

    @Schema(description = "用户ID（主键，数值型）")
    private Long id;


    @Schema(description = "登录名（唯一）")
    private String username;

    @Schema(description = "密码（加密存储，如BCrypt）")
    private String password;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "工资表顺序")
    private Integer sidx;

    @Schema(description = "头像URL（支持长文本存储）")
    private String avatarUrl;

    @Schema(description = "生日")
    private LocalDate birth;

    @Schema(description = "性别（1=男，2=女）")
    private Integer sex;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号（唯一）")
    private String phone;

    @Schema(description = "角色ID（关联sys_role.id，数值型匹配）")
    private Long roleId;

    @Schema(description = "部门ID（关联sys_dept.id，数值型匹配）")
    private Long deptId;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "微信小程序用户ID")
    private String wxappUid;

    @Schema(description = "微信小程序OpenID")
    private String wxappOpenid;

    @Schema(description = "是否禁用（1=是，2=否）")
    private Integer isDisable;

    @Schema(description = "状态（1=在职，0=离职）")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

}
