package net.lab1024.sa.admin.module.scrumproductbacklogs.controller;

import net.lab1024.sa.admin.module.scrumproductbacklogs.domain.form.ScrumProductBacklogsAddForm;
import net.lab1024.sa.admin.module.scrumproductbacklogs.domain.form.ScrumProductBacklogsQueryForm;
import net.lab1024.sa.admin.module.scrumproductbacklogs.domain.form.ScrumProductBacklogsUpdateForm;
import net.lab1024.sa.admin.module.scrumproductbacklogs.domain.vo.ScrumProductBacklogsVO;
import net.lab1024.sa.admin.module.scrumproductbacklogs.service.ScrumProductBacklogsService;
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
 * 产品待办事项表 Controller
 *
 * @Author cmz
 * @Date 2025-10-11 22:40:46
 * @Copyright 1
 */

@RestController
@Tag(name = "产品待办事项表")
public class ScrumProductBacklogsController {

    @Resource
    private ScrumProductBacklogsService scrumProductBacklogsService;

    @Operation(summary = "分页查询 @author cmz")
    @PostMapping("/scrumProductBacklogs/queryPage")
    @SaCheckPermission("scrumProductBacklogs:query")
    public ResponseDTO<PageResult<ScrumProductBacklogsVO>> queryPage(@RequestBody @Valid ScrumProductBacklogsQueryForm queryForm) {
        return ResponseDTO.ok(scrumProductBacklogsService.queryPage(queryForm));
    }

    @Operation(summary = "添加 @author cmz")
    @PostMapping("/scrumProductBacklogs/add")
    @SaCheckPermission("scrumProductBacklogs:add")
    public ResponseDTO<String> add(@RequestBody @Valid ScrumProductBacklogsAddForm addForm) {
        return scrumProductBacklogsService.add(addForm);
    }

    @Operation(summary = "更新 @author cmz")
    @PostMapping("/scrumProductBacklogs/update")
    @SaCheckPermission("scrumProductBacklogs:update")
    public ResponseDTO<String> update(@RequestBody @Valid ScrumProductBacklogsUpdateForm updateForm) {
        return scrumProductBacklogsService.update(updateForm);
    }

    @Operation(summary = "批量删除 @author cmz")
    @PostMapping("/scrumProductBacklogs/batchDelete")
    @SaCheckPermission("scrumProductBacklogs:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> idList) {
        return scrumProductBacklogsService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author cmz")
    @GetMapping("/scrumProductBacklogs/delete/{id}")
    @SaCheckPermission("scrumProductBacklogs:delete")
    public ResponseDTO<String> batchDelete(@PathVariable Long id) {
        return scrumProductBacklogsService.delete(id);
    }
}
