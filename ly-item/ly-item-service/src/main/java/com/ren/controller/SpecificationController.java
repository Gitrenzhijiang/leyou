package com.ren.controller;

import com.ren.item.pojo.SpecGroup;
import com.ren.item.pojo.SpecParam;
import com.ren.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecificationController {
    @Autowired
    private SpecificationService specificationService;

    /**
     * 根据分类ID 查询该分类的规格参数组信息
     * 返回规格参数组列表
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(@PathVariable("cid") Long cid){
        System.out.println("查询商品规格组 信息");
        return ResponseEntity.ok().body(specificationService.queryGroupByCid(cid));
    }

    /**
     * 新增 规格参数组信息
     * @return
     */
    @PostMapping("group")
    public ResponseEntity<Void> addGroup(@RequestBody SpecGroup group){
        specificationService.add(group);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @DeleteMapping("group/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable("id") Long id){
        specificationService.deleteGroup(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PutMapping("group")
    public ResponseEntity<Void> updateGroup(@RequestBody SpecGroup group){
        specificationService.updateGroup(group);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 查询参数
     * @param gid
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParam(@RequestParam(value = "gid", required = false) Long gid,
                                                           @RequestParam(value = "cid", required = false) Long cid,
                                                           @RequestParam(value = "searching", required = false) Boolean searching) {
        return ResponseEntity.ok(specificationService.queryParam(gid, cid, searching));
    }

    @PostMapping("param")
    public ResponseEntity<Void> addParam(@RequestBody SpecParam specParam){
        specificationService.addParam(specParam);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改规格参数
     * @param specParam
     * @return
     */
    @PutMapping("param")
    public ResponseEntity<Void> updateParam(@RequestBody SpecParam specParam){
        specificationService.updateParam(specParam);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @DeleteMapping("param/{id}")
    public ResponseEntity<Void> deleteParam(@PathVariable("id") Long id){
        specificationService.deleteParam(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
