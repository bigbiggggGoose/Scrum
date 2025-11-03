package net.lab1024.sa.admin.module.scrumproductbacklogs.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 产品待办事项表 列表VO
 *
 * @Author cmz
 * @Date 2025-10-11 22:40:46
 * @Copyright 1
 */

@Data
public class ScrumProductBacklogsVO {


    @Schema(description = "待办ID（主键）")
    private Long id;

    @Schema(description = "所属产品ID（关联scrum_products.id）")
    private Long productId;

    @Schema(description = "父待办ID（0=顶层，支撑树形结构）")
    private Long parentId;

    @Schema(description = "待办名称（用户故事标题）")
    private String backlogName;

    @Schema(description = "优先级（数字越大优先级越低）")
    private Integer priority;

    @Schema(description = "人日")
    private BigDecimal manDay;

    @Schema(description = "待办内容")
    private String content;

    @Schema(description = "分级（最大4级）")
    private Integer level;

    @Schema(description = "单元格样式：1=靠左白背景，2=居中白背景，3=居中加粗白背景，4=居中加粗蓝背景（#b7dee8）等")
    private Integer cellStyle;

    @Schema(description = "同级排序权重（数值越小越靠前）")
    private Integer backlogWeight;

    @Schema(description = "预估工时（小时）")
    private Integer estimatedHours;

    @Schema(description = "状态（关联sys_dict：todo=待办，in_progress=进行中，completed=已完成）")
    private String backlogStatus;

    @Schema(description = "备注")
    private String backlogRemarks;

    @Schema(description = "逻辑删除")
    private Integer isDeleted;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建人（关联sys_user.id）")
    private String creator;

    @Schema(description = "修改人（关联sys_user.id）")
    private String updater;

}
