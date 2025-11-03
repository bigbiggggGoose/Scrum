package net.lab1024.sa.admin.module.scrumproductsprintbacklog.controller;

import net.lab1024.sa.admin.module.scrumproductsprintbacklog.domain.form.ScrumProductSprintBacklogAddForm;
import net.lab1024.sa.admin.module.scrumproductsprintbacklog.domain.form.ScrumProductSprintBacklogQueryForm;
import net.lab1024.sa.admin.module.scrumproductsprintbacklog.domain.form.ScrumProductSprintBacklogUpdateForm;
import net.lab1024.sa.admin.module.scrumproductsprintbacklog.domain.vo.ScrumProductSprintBacklogVO;
import net.lab1024.sa.admin.module.scrumproductsprintbacklog.service.ScrumProductSprintBacklogService;
import net.lab1024.sa.base.common.domain.ValidateList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
 * Sprint与产品待办中间表 Controller
 *
 * @Author cmz
 * @Date 2025-10-11 22:41:46
 * @Copyright 1
 */

@RestController
@Tag(name = "Sprint与产品待办中间表")
public class ScrumProductSprintBacklogController {

    @Resource
    private ScrumProductSprintBacklogService scrumProductSprintBacklogService;

    @Operation(summary = "分页查询 @author cmz")
    @PostMapping("/scrumProductSprintBacklog/queryPage")
    @SaCheckPermission("scrumProductSprintBacklog:query")
    public ResponseDTO<PageResult<ScrumProductSprintBacklogVO>> queryPage(@RequestBody @Valid ScrumProductSprintBacklogQueryForm queryForm) {
        return ResponseDTO.ok(scrumProductSprintBacklogService.queryPage(queryForm));
    }

    @Operation(summary = "添加 @author cmz")
    @PostMapping("/scrumProductSprintBacklog/add")
    @SaCheckPermission("scrumProductSprintBacklog:add")
    public ResponseDTO<String> add(@RequestBody @Valid ScrumProductSprintBacklogAddForm addForm) {
        return scrumProductSprintBacklogService.add(addForm);
    }

    @Operation(summary = "更新 @author cmz")
    @PostMapping("/scrumProductSprintBacklog/update")
    @SaCheckPermission("scrumProductSprintBacklog:update")
    public ResponseDTO<String> update(@RequestBody @Valid ScrumProductSprintBacklogUpdateForm updateForm) {
        return scrumProductSprintBacklogService.update(updateForm);
    }

    @Operation(summary = "批量删除 @author cmz")
    @PostMapping("/scrumProductSprintBacklog/batchDelete")
    @SaCheckPermission("scrumProductSprintBacklog:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> idList) {
        return scrumProductSprintBacklogService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author cmz")
    @GetMapping("/scrumProductSprintBacklog/delete/{id}")
    @SaCheckPermission("scrumProductSprintBacklog:delete")
    public ResponseDTO<String> batchDelete(@PathVariable Long id) {
        return scrumProductSprintBacklogService.delete(id);
    }
}
