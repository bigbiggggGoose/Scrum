package net.lab1024.sa.admin.module.scrumsprints.controller;

import net.lab1024.sa.admin.module.scrumsprints.domain.form.ScrumSprintsAddForm;
import net.lab1024.sa.admin.module.scrumsprints.domain.form.ScrumSprintsQueryForm;
import net.lab1024.sa.admin.module.scrumsprints.domain.form.ScrumSprintsUpdateForm;
import net.lab1024.sa.admin.module.scrumsprints.domain.vo.ScrumSprintsVO;
import net.lab1024.sa.admin.module.scrumsprints.service.ScrumSprintsService;
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
 * 项目迭代表 Controller
 *
 * @Author cmz
 * @Date 2025-10-11 22:41:18
 * @Copyright 1
 */

@RestController
@Tag(name = "项目迭代表")
public class ScrumSprintsController {

    @Resource
    private ScrumSprintsService scrumSprintsService;

    @Operation(summary = "分页查询 @author cmz")
    @PostMapping("/scrumSprints/queryPage")
    @SaCheckPermission("scrumSprints:query")
    public ResponseDTO<PageResult<ScrumSprintsVO>> queryPage(@RequestBody @Valid ScrumSprintsQueryForm queryForm) {
        return ResponseDTO.ok(scrumSprintsService.queryPage(queryForm));
    }

    @Operation(summary = "添加 @author cmz")
    @PostMapping("/scrumSprints/add")
    @SaCheckPermission("scrumSprints:add")
    public ResponseDTO<String> add(@RequestBody @Valid ScrumSprintsAddForm addForm) {
        return scrumSprintsService.add(addForm);
    }

    @Operation(summary = "更新 @author cmz")
    @PostMapping("/scrumSprints/update")
    @SaCheckPermission("scrumSprints:update")
    public ResponseDTO<String> update(@RequestBody @Valid ScrumSprintsUpdateForm updateForm) {
        return scrumSprintsService.update(updateForm);
    }

    @Operation(summary = "批量删除 @author cmz")
    @PostMapping("/scrumSprints/batchDelete")
    @SaCheckPermission("scrumSprints:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> idList) {
        return scrumSprintsService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author cmz")
    @GetMapping("/scrumSprints/delete/{id}")
    @SaCheckPermission("scrumSprints:delete")
    public ResponseDTO<String> batchDelete(@PathVariable Long id) {
        return scrumSprintsService.delete(id);
    }
}
