package com.ren.controller;

import com.ren.common.utils.vo.Result;
import com.ren.item.pojo.Sku;
import com.ren.item.pojo.Spu;
import com.ren.item.pojo.SpuDetail;
import com.ren.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 获得商品信息
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @GetMapping("/spu/page")
    public ResponseEntity<Result<Spu>> querySpuByPage (
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key
    ) {
        Result<Spu> result = goodsService.querySpuByPage(page, rows, saleable, key);
        return ResponseEntity.ok(result);
    }

    /**
     * 查看该商品详情
     * @param spuId SPU ID
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> showDetail (@PathVariable("id") Long spuId){
        SpuDetail spuDetail = goodsService.querySpuDetailBySpuId(spuId);
        return ResponseEntity.ok().body(spuDetail);
    }

    /**
     * 根据SPU ID 查询 SKU 列表
     * @param spuId
     * @return
     */
    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> getSkuListBySpuId(@RequestParam("id") Long spuId){

        return ResponseEntity.ok(goodsService.querySkuListBySpuId(spuId));
    }

    /**
     * 保存商品
     * @param spu
     * @return
     */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGood(@RequestBody Spu spu){
        goodsService.saveGood(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("goods")
    public ResponseEntity<Void> updateGood(@RequestBody Spu spu){
        goodsService.updateGood(spu);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
