package net.lab1024.sa.admin.module.scrumproducts.controller;

import net.lab1024.sa.admin.module.scrumproducts.domain.form.ScrumProductsAddForm;
import net.lab1024.sa.admin.module.scrumproducts.domain.form.ScrumProductsQueryForm;
import net.lab1024.sa.admin.module.scrumproducts.domain.form.ScrumProductsUpdateForm;
import net.lab1024.sa.admin.module.scrumproducts.domain.vo.ScrumProductsVO;
import net.lab1024.sa.admin.module.scrumproducts.service.ScrumProductsService;
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
 * 产品表 Controller
 *
 * @Author cmz
 * @Date 2025-10-11 22:39:41
 * @Copyright 1
 */

@RestController
@Tag(name = "产品表")
public class ScrumProductsController {

    @Resource
    private ScrumProductsService scrumProductsService;

    @Operation(summary = "分页查询 @author cmz")
    @PostMapping("/scrumProducts/queryPage")
    @SaCheckPermission("scrumProducts:query")
    public ResponseDTO<PageResult<ScrumProductsVO>> queryPage(@RequestBody @Valid ScrumProductsQueryForm queryForm) {
        return ResponseDTO.ok(scrumProductsService.queryPage(queryForm));
    }

    @Operation(summary = "添加 @author cmz")
    @PostMapping("/scrumProducts/add")
    @SaCheckPermission("scrumProducts:add")
    public ResponseDTO<String> add(@RequestBody @Valid ScrumProductsAddForm addForm) {
        return scrumProductsService.add(addForm);
    }

    @Operation(summary = "更新 @author cmz")
    @PostMapping("/scrumProducts/update")
    @SaCheckPermission("scrumProducts:update")
    public ResponseDTO<String> update(@RequestBody @Valid ScrumProductsUpdateForm updateForm) {
        return scrumProductsService.update(updateForm);
    }

    @Operation(summary = "批量删除 @author cmz")
    @PostMapping("/scrumProducts/batchDelete")
    @SaCheckPermission("scrumProducts:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> idList) {
        return scrumProductsService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author cmz")
    @GetMapping("/scrumProducts/delete/{id}")
    @SaCheckPermission("scrumProducts:delete")
    public ResponseDTO<String> batchDelete(@PathVariable Long id) {
        return scrumProductsService.delete(id);
    }
}
