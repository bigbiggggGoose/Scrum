package net.lab1024.sa.admin.module.template.controller;

import net.lab1024.sa.base.common.domain.ValidateList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import net.lab1024.sa.admin.module.template.domain.form.TemplateAddForm;
import net.lab1024.sa.admin.module.template.domain.form.TemplateQueryForm;
import net.lab1024.sa.admin.module.template.domain.form.TemplateUpdateForm;
import net.lab1024.sa.admin.module.template.domain.vo.TemplateVO;
import net.lab1024.sa.admin.module.template.service.TemplateService;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;

/**
 * 站内信/邮件模板表 Controller
 *
 * @Author thr
 * @Date 2025-10-12 14:33:45
 * @Copyright wu
 */

@RestController
@Tag(name = "站内信/邮件模板表")
public class TemplateController {

    @Resource
    private TemplateService templateService;

    @Operation(summary = "分页查询 @author thr")
    @PostMapping("/template/queryPage")
    @SaCheckPermission("template:query")
    public ResponseDTO<PageResult<TemplateVO>> queryPage(@RequestBody @Valid TemplateQueryForm queryForm) {
        return ResponseDTO.ok(templateService.queryPage(queryForm));
    }

    @Operation(summary = "添加 @author thr")
    @PostMapping("/template/add")
    @SaCheckPermission("template:add")
    public ResponseDTO<String> add(@RequestBody @Valid TemplateAddForm addForm) {
        return templateService.add(addForm);
    }

    @Operation(summary = "更新 @author thr")
    @PostMapping("/template/update")
    @SaCheckPermission("template:update")
    public ResponseDTO<String> update(@RequestBody @Valid TemplateUpdateForm updateForm) {
        return templateService.update(updateForm);
    }

    @Operation(summary = "批量删除 @author thr")
    @PostMapping("/template/batchDelete")
    @SaCheckPermission("template:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> idList) {
        return templateService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author thr")
    @GetMapping("/template/delete/{id}")
    @SaCheckPermission("template:delete")
    public ResponseDTO<String> batchDelete(@PathVariable Long id) {
        return templateService.delete(id);
    }
}
