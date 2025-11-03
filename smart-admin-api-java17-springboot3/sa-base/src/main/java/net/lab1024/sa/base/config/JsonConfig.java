package net.lab1024.sa.base.config;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import net.lab1024.sa.base.common.json.serializer.LongJsonSerializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.time.OffsetDateTime;
import java.time.ZoneId;

/**
 * json 序列化配置
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2017-11-28 15:21:10
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Configuration
public class JsonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.deserializers(new LocalDateDeserializer(DatePattern.NORM_DATE_FORMAT.getDateTimeFormatter()));
            builder.deserializers(new LocalDateTimeDeserializer(DatePattern.NORM_DATETIME_FORMAT.getDateTimeFormatter()));
            builder.serializers(new LocalDateSerializer(DatePattern.NORM_DATE_FORMAT.getDateTimeFormatter()));
            builder.serializers(new LocalDateTimeSerializer(DatePattern.NORM_DATETIME_FORMAT.getDateTimeFormatter()));
            builder.serializerByType(Long.class, LongJsonSerializer.INSTANCE);
            builder.serializerByType(Long.TYPE, LongJsonSerializer.INSTANCE);
            builder.serializerByType(BigInteger.class, ToStringSerializer.instance);
            builder.serializerByType(BigDecimal.class, ToStringSerializer.instance);
        };
    }


    /**
     * string 转为 LocalDateTime 配置类
     *
     * @author 卓大
     */
    @Configuration
    public static class StringToLocalDateTime implements Converter<String, LocalDateTime> {

        @Override
        public LocalDateTime convert(String str) {
            if (StringUtils.isBlank(str)) {
                return null;
            }
            // 依次尝试多种常见时间格式，兼容前端不同字符串格式
            // 1) yyyy-MM-dd HH:mm:ss
            try {
                return LocalDateTimeUtil.parse(str, DatePattern.NORM_DATETIME_FORMAT.getDateTimeFormatter());
            } catch (DateTimeParseException ignored) { }
            // 2) 带毫秒：yyyy-MM-dd HH:mm:ss.SSS
            try {
                return LocalDateTimeUtil.parse(str, DatePattern.NORM_DATETIME_MS_FORMAT.getDateTimeFormatter());
            } catch (DateTimeParseException ignored) { }
            // 3) ISO_LOCAL_DATE_TIME：yyyy-MM-dd'T'HH:mm:ss
            try {
                return LocalDateTime.parse(str, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (DateTimeParseException ignored) { }
            // 4) ISO_OFFSET_DATE_TIME：带时区偏移，如 2025-10-11T13:54:05+08:00 或 2025-10-11T05:54:05Z
            try {
                OffsetDateTime odt = OffsetDateTime.parse(str, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                return odt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            } catch (DateTimeParseException ignored) { }

            throw new RuntimeException("请输入正确的日期时间格式：支持 yyyy-MM-dd HH:mm:ss、yyyy-MM-dd HH:mm:ss.SSS、yyyy-MM-dd'T'HH:mm:ss、ISO_OFFSET_DATE_TIME");
        }
    }


    /**
     * string 转为 LocalDate 配置类
     *
     * @author 卓大
     */
    @Configuration
    public static class StringToLocalDate implements Converter<String, LocalDate> {

        @Override
        public LocalDate convert(String str) {
            if (StringUtils.isBlank(str)) {
                return null;
            }
            // 1) yyyy-MM-dd
            try {
                return LocalDateTimeUtil.parseDate(str, DatePattern.NORM_DATE_FORMAT.getDateTimeFormatter());
            } catch (DateTimeParseException ignored) { }
            // 2) ISO_LOCAL_DATE：yyyy-MM-dd
            try {
                return LocalDate.parse(str, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException ignored) { }

            throw new RuntimeException("请输入正确的日期格式：支持 yyyy-MM-dd、ISO_LOCAL_DATE");
        }
    }
}