package net.lab1024.sa.admin.module.role.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 角色管理 新建表单
 *
 * @Author oyt
 * @Date 2025-10-11 18:33:10
 * @Copyright oyt
 */

@Data
public class SysRoleAddForm {
    @Schema(description = "角色标识（唯一，如ROLE_ADMIN）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "角色标识（唯一，如ROLE_ADMIN） 不能为空")
    private String roleCode;

    @Schema(description = "角色名称（如超级管理员）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "角色名称（如超级管理员） 不能为空")
    private String roleName;

    @Schema(description = "父角色ID（0=顶级角色）")
    private Long parentId;

    @Schema(description = "排序（数值越小越靠前）")
    private Integer orderNum;

    @Schema(description = "角色等级", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "角色等级不能为空")
    private Integer level;

    @Schema(description = "绩效公式")
    private String jxfm;

    @Schema(description = "待遇描述")
    private String dy;

    @Schema(description = "状态（1=启用，0=禁用）")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

}