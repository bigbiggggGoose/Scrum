package net.lab1024.sa.admin.module.template.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 站内信/邮件模板表 实体类
 *
 * @Author thr
 * @Date 2025-10-12 14:33:45
 * @Copyright wu
 */

@Data
@TableName("sys_templates")
public class TemplateEntity {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模板标识（唯一，如sprint_start_notice）
     */
    private String templateCode;

    /**
     * 类型（1=站内信，2=邮件）
     */
    private Integer templateType;

    /**
     * 模板名称（如Sprint启动通知模板）
     */
    private String templateName;

    /**
     * 模板内容（支持变量占位符，如${sprintName}）
     */
    private String templateContent;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人（sys_user.id，数值型匹配）
     */
    private Long creator;

    /**
     * 修改人（sys_user.id，数值型匹配）
     */
    private Long updater;

}
