package net.lab1024.sa.admin.module.systemconfig.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 网站与邮箱配置表 列表VO
 *
 * @Author thr
 * @Date 2025-10-12 14:40:06
 * @Copyright wu
 */

@Data
public class SystemConfigVO {


    @Schema(description = "配置ID（主键）")
    private Integer id;

    @Schema(description = "网站标题（如Scrum项目管理系统）")
    private String siteTitle;

    @Schema(description = "系统名称")
    private String systemName;

    @Schema(description = "网站图标URL")
    private String siteLogo;

    @Schema(description = "备案号（如京ICP备xxxx号）")
    private String icpNumber;

    @Schema(description = "底部内容（如版权信息）")
    private String footerContent;

    @Schema(description = "邮箱服务器（如smtp.qq.com）")
    private String mailServer;

    @Schema(description = "邮箱端口（如465）")
    private Integer mailPort;

    @Schema(description = "发送邮箱（如xxx@qq.com）")
    private String mailFrom;

    @Schema(description = "邮箱账号")
    private String mailUser;

    @Schema(description = "邮箱授权码（非登录密码）")
    private String mailPassword;

}
