/*
 * Copyright (c) 2022-present Charles7c Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.charles7c.cnadmin.webapi.controller.system;

import java.util.List;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import top.charles7c.cnadmin.common.model.vo.R;
import top.charles7c.cnadmin.system.model.query.DeptQuery;
import top.charles7c.cnadmin.system.model.request.CreateDeptRequest;
import top.charles7c.cnadmin.system.model.vo.DeptVO;
import top.charles7c.cnadmin.system.service.DeptService;

/**
 * 部门管理 API
 *
 * @author Charles7c
 * @since 2023/1/22 17:50
 */
@Tag(name = "部门管理 API")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/system/dept", produces = MediaType.APPLICATION_JSON_VALUE)
public class DeptController {

    private final DeptService deptService;

    @Operation(summary = "查询部门列表")
    @GetMapping
    public R<List<DeptVO>> list(@Validated DeptQuery query) {
        List<DeptVO> list = deptService.list(query);
        return R.ok(deptService.buildListTree(list));
    }

    @Operation(summary = "新增部门")
    @PostMapping
    public R<Long> create(@Validated @RequestBody CreateDeptRequest request) {
        // 校验
        String deptName = request.getDeptName();
        boolean isExist = deptService.checkDeptNameExist(deptName, request.getParentId(), null);
        if (isExist) {
            return R.fail(String.format("新增失败，'%s'已存在", deptName));
        }

        return R.ok("新增成功", deptService.create(request));
    }
}