package com.ren.controller;
import	java.util.List;

import com.ren.common.utils.vo.Result;
import com.ren.item.pojo.Brand;
import com.ren.item.pojo.Category;
import com.ren.service.BrandService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;
    /**
     * @param key  搜索关键字
     * @param page 第几页, 从1开始
     * @param rows 每一页记录数, 默认5
     * @param sortBy 根据关键字排序
     * @param desc 是否降序, 默认升序排序
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<Result> page(@RequestParam(value = "key", defaultValue = "") String key,
                                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                                       @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                       @RequestParam(value = "sortBy", required = false) String sortBy,
                                       @RequestParam(value = "desc", defaultValue = "false") Boolean desc){
        Result result = brandService.pageQuery(key, page, rows, sortBy, desc);

        return ResponseEntity.ok(result);
    }

    /**
     * 保存品牌
     * @param brand
     * @param cids
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids){
        brandService.saveBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改品牌和 类别信息
     * @param brand
     * @param cids
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateBrand(Brand brand, @RequestParam(value = "cids")
            List<Long> cids){
        brandService.updateAndCategoryList(brand, cids);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 拿到品牌, 的所有分类信息
     * @param id
     * @return
     */
    @GetMapping("clistByBid/{id}")
    public ResponseEntity<List<Category>> getCategoryListByBid(@PathVariable("id") Long id){

        return ResponseEntity.ok().body(brandService.getCategoryListByBid(id));
    }

    @DeleteMapping("{bid}")
    public ResponseEntity<Brand> deleteBrand(@PathVariable("bid") Long bid) {
        brandService.delete(bid);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据分类获取 品牌列表
     * @param cid
     * @return
     */
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> getBrandsByCid(@PathVariable("cid") Long cid){
        List<Brand> list = brandService.getBrandListByCid(cid);
        return ResponseEntity.ok(list);
    }
}
