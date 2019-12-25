package com.ren.controller;

import com.ren.item.pojo.Category;
import com.ren.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    /**
     * 根据父商品分类ID，返回其下的所有商品分类
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> categoryListByParentId(@RequestParam("pid") Long pid){
        System.out.println("??? list of category list");
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoryService.queryListByParentId(pid));
    }
}
