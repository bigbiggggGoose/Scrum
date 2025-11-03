package net.lab1024.sa.admin.module.scrumsprints.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

/**
 * 项目迭代表 新建表单
 *
 * @Author cmz
 * @Date 2025-10-11 22:41:18
 * @Copyright 1
 */

@Data
public class ScrumSprintsAddForm {

    @Schema(description = "Sprint ID（主键）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Sprint ID（主键） 不能为空")
    private Long id;

    @Schema(description = "Sprint名称（如数据库构建、系统设计）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Sprint名称（如数据库构建、系统设计） 不能为空")
    private String sprintName;

    @Schema(description = "所属产品ID（关联scrum_products.id）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "所属产品ID（关联scrum_products.id） 不能为空")
    private Long productId;

    @Schema(description = "负责团队ID（关联scrum_teams.id）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "负责团队ID（关联scrum_teams.id） 不能为空")
    private Long teamId;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开始时间 不能为空")
    private LocalDate startDate;

    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "结束时间 不能为空")
    private LocalDate endDate;

}