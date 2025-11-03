package net.lab1024.sa.admin.module.users.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

/**
 * 用户信息表 新建表单
 *
 * @Author oyt
 * @Date 2025-10-11 17:34:30
 * @Copyright oyt
 */

@Data
public class SysUserAddForm {
    @Schema(description = "登录名（唯一）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "登录名（唯一） 不能为空")
    private String username;

    @Schema(description = "密码（加密存储，如BCrypt）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "密码（加密存储，如BCrypt） 不能为空")
    private String password;

    @Schema(description = "真实姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "真实姓名 不能为空")
    private String realName;

    @Schema(description = "工资表顺序")
    private Integer sidx;

    @Schema(description = "生日")
    private LocalDate birth;

    @Schema(description = "性别（1=男，2=女）")
    private Integer sex;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号（唯一）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "手机号（唯一） 不能为空")
    private String phone;

    @Schema(description = "角色ID（关联sys_role.id，数值型匹配）")
    private Long roleId;

    @Schema(description = "部门ID（关联sys_dept.id，数值型匹配）")
    private Long deptId;

    @Schema(description = "是否禁用（1=是，2=否）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否禁用（1=是，2=否） 不能为空")
    private Integer isDisable;

    @Schema(description = "状态（1=在职，0=离职）")
    private Integer status;

}