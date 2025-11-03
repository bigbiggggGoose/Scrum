package net.lab1024.sa.admin.module.dept.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 部门管理 列表VO
 *
 * @Author oyt
 * @Date 2025-10-11 17:57:33
 * @Copyright oyt
 */

@Data
public class SysDeptVO {


    @Schema(description = "部门ID（主键）")
    private Long id;

    @Schema(description = "父部门ID（0=顶级部门）")
    private Long parentId;

    @Schema(description = "部门标识（唯一）")
    private String deptCode;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "排序（数值越小越靠前）")
    private Integer orderNum;

    @Schema(description = "部门等级（1=总部，2=分公司，3=部门）")
    private Integer level;

    @Schema(description = "关联区域ID（多个用逗号分隔，关联sys_area.id）")
    private String dptarea;

    @Schema(description = "状态（1=正常，0=停用）")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

}
