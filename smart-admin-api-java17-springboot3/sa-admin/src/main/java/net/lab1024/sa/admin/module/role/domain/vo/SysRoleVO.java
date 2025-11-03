package net.lab1024.sa.admin.module.role.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 角色管理 列表VO
 *
 * @Author oyt
 * @Date 2025-10-11 18:33:10
 * @Copyright oyt
 */

@Data
public class SysRoleVO {


    @Schema(description = "角色ID（主键）")
    private Long id;

    @Schema(description = "角色标识（唯一，如ROLE_ADMIN）")
    private String roleCode;

    @Schema(description = "角色名称（如超级管理员）")
    private String roleName;

    @Schema(description = "父角色ID（0=顶级角色）")
    private Long parentId;

    @Schema(description = "排序")
    private Integer orderNum;

    @Schema(description = "角色等级")
    private Integer level;

    @Schema(description = "绩效公式")
    private String jxfm;

    @Schema(description = "待遇描述")
    private String dy;

    @Schema(description = "状态（1=启用，0=禁用）")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
