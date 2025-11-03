package net.lab1024.sa.admin.module.role.domain.form;

import net.lab1024.sa.base.common.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色管理 分页查询表单
 *
 * @Author oyt
 * @Date 2025-10-11 18:33:10
 * @Copyright oyt
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class SysRoleQueryForm extends PageParam {

    @Schema(description = "角色名称（支持模糊）")
    private String roleName;

    @Schema(description = "角色标识（支持模糊）")
    private String roleCode;

    @Schema(description = "父角色ID（为空不筛选）")
    private Long parentId;

    @Schema(description = "状态（1=启用，0=禁用，为空不筛选）")
    private Integer status;

}
