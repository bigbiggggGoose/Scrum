package net.lab1024.sa.admin.module.systemconfig.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 网站与邮箱配置表 实体类
 *
 * @Author thr
 * @Date 2025-10-12 14:40:06
 * @Copyright wu
 */

@Data
@TableName("sys_system_config")
public class SystemConfigEntity {

    /**
     * 配置ID（主键）
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 网站标题（如Scrum项目管理系统）
     */
    private String siteTitle;

    /**
     * 系统名称
     */
    private String systemName;

    /**
     * 网站图标URL
     */
    private String siteLogo;

    /**
     * 备案号（如京ICP备xxxx号）
     */
    private String icpNumber;

    /**
     * 底部内容（如版权信息）
     */
    private String footerContent;

    /**
     * 邮箱服务器（如smtp.qq.com）
     */
    private String mailServer;

    /**
     * 邮箱端口（如465）
     */
    private Integer mailPort;

    /**
     * 发送邮箱（如xxx@qq.com）
     */
    private String mailFrom;

    /**
     * 邮箱账号
     */
    private String mailUser;

    /**
     * 邮箱授权码（非登录密码）
     */
    private String mailPassword;

}
