package net.lab1024.sa.admin.module.scrumproductsprintbacklog.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Sprint与产品待办中间表 实体类
 *
 * @Author cmz
 * @Date 2025-10-11 22:41:46
 * @Copyright 1
 */

@Data
@TableName("scrum_product_sprint_backlog")
public class ScrumProductSprintBacklogEntity {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * Sprint项目ID（关联scrum_sprints.id）
     */
    private Long sprintId;

    /**
     * 产品待办ID（关联scrum_product_backlogs.id）
     */
    private Long backlogId;

}
