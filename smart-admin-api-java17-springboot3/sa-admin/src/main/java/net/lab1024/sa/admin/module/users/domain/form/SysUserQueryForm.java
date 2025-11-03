package net.lab1024.sa.admin.module.users.domain.form;

import net.lab1024.sa.base.common.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户信息表 分页查询表单
 *
 * @Author oyt
 * @Date 2025-10-11 17:34:30
 * @Copyright oyt
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class SysUserQueryForm extends PageParam {

    @Schema(description = "登录名（唯一）")
    private String username;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "性别（1=男，2=女）")
    private Integer sex;

}
