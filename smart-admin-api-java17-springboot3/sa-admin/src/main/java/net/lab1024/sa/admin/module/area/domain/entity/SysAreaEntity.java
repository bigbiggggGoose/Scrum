package net.lab1024.sa.admin.module.area.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 地区表（省/市/区三级联动） 实体类
 *
 * @Author oyt
 * @Date 2025-10-11 13:54:05
 * @Copyright oyt
 */

@Data
@TableName("sys_area")
public class SysAreaEntity {

    /**
     * 区域编码（主键）
     */
    @TableId(type = IdType.INPUT)
    private String id;

    /**
     * 区域名称（如北京市、朝阳区）
     */
    private String name;

    /**
     * 上级区域ID（0=顶级区域）
     */
    private String pid;

    /**
     * 区域简称（如北京、朝阳）
     */
    private String simplename;

    /**
     * 区域等级（1=省/直辖市，2=市，3=区/县）
     */
    private Integer level;

    /**
     * 城市编码（如110000=北京市）
     */
    private String citycode;

    /**
     * 邮政编码
     */
    private String zipcode;

    /**
     * 组合名称（如北京市朝阳区）
     */
    private String mername;

    /**
     * 经度
     */
    private BigDecimal lng;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * 区域拼音（如Beijing）
     */
    private String pinyin;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人（关联sys_user.id，数值型匹配）
     */
    private Long creator;

    /**
     * 修改人（关联sys_user.id，数值型匹配）
     */
    private Long updater;

}
