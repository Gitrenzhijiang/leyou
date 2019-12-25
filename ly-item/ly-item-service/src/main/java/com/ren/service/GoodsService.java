package com.ren.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ren.common.utils.enums.ExceptionEnum;
import com.ren.common.utils.exception.LyException;
import com.ren.common.utils.vo.Result;
import com.ren.item.pojo.*;
import com.ren.mapper.SkuMapper;
import com.ren.mapper.SpuDetailMapper;
import com.ren.mapper.SpuMapper;
import com.ren.mapper.StockMapper;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品服务
 *  包括SPU  SKU
 */
@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;
    @Autowired
    private StockMapper stockMapper;



    public Result<Spu> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
        // 分页
        PageHelper.startPage(page, rows);
        // 过滤
        Example example = new Example(Spu.class);
        if (StringUtils.isNotBlank(key)){
            example.createCriteria().andLike("title", "%" + key + "%");
        }
        if (saleable != null) {
            example.createCriteria().andEqualTo("saleable", saleable);
        }
        // 按时间排序
        example.setOrderByClause("last_update_time desc");
        List<Spu> list = spuMapper.selectByExample(example);

        // 填充商品分类名称和品牌名称
        loadCnameAndBname(list);

        PageInfo pageInfo = new PageInfo(list);

        return Result.of().data(list).total(pageInfo.getTotal());
    }
    // 填充 多级CNAME 和 BNAME
    private void loadCnameAndBname(List<Spu> list) {
        for (Spu spu : list){
            List<Category> clist = categoryService
                    .queryCategoryListByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            List<String> names = clist.stream().map(Category::getName).collect(Collectors.toList());
            spu.setCname(StringUtils.join(names, "/"));
            spu.setBname(brandService.selectById(spu.getBrandId()).getName());
        }
    }

    /**
     * 根据SPU id 查询 SpuDetail
     * @param spuId
     * @return
     */
    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
        if (spuDetail == null){
            // 商品不存在
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        return spuDetail;
    }

    /**
     * 根据SPU Id 查询相关的SKU 列表
     * @param spuId
     * @return
     */
    public List<Sku> querySkuListBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);

//        for (Sku s : skuList){
//            Stock stock = stockMapper.selectByPrimaryKey(sku.getId());
//            if (stock == null) {
//                throw new LyException(ExceptionEnum.GOODS_STOCK_NOT_FOUND);
//            }
//            s.setStock(stock.getStock());
//        }
        List<Long> ids = skuList.stream().map(s -> s.getId()).collect(Collectors.toList());
        Map<Long, Integer> stockMap = stockMapper.selectByIdList(ids)
                .stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        if (stockMap.isEmpty()) {
            throw new LyException(ExceptionEnum.GOODS_STOCK_NOT_FOUND);
        }
        skuList.forEach(e -> {e.setStock(stockMap.get(e.getId()));});
        if (CollectionUtils.isEmpty(skuList)) {
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        return skuList;
    }
    @Transactional
    public void saveGood(Spu spu) {
        // 新增SPU
        Date now = new Date();
        spu.setCreateTime(now);
        spu.setLastUpdateTime(now);
        spu.setId(null);
        spu.setSaleable(true); // 默认上架
        spu.setValid(false);
        int c = spuMapper.insert(spu);
        if (c != 1){
            throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
        // 新增Spu detail
        SpuDetail details = spu.getSpuDetail();
        details.setSpuId(spu.getId());
        c = spuDetailMapper.insert(details);
        // 新增 sku
        List<Stock> stocks = new ArrayList<>();
        List<Sku> skus = spu.getSkus();
        for (Sku sku : skus){
            sku.setSpuId(spu.getId());
            sku.setLastUpdateTime(now);
            sku.setCreateTime(now);

            int count = skuMapper.insert(sku);
            if (count != 1){
                throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
            }

            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());

            stocks.add(stock);
        }
        // 新增 库存
        c = stockMapper.insertList(stocks);
        if (c != stocks.size()){
            throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
    }

    /**
     * 修改商品
     * @param spu
     */
    @Transactional
    public void updateGood(Spu spu) {
        final Date now = new Date();
        // 修改SPU
        int c = 0;
        spu.setLastUpdateTime(now);
        c = spuMapper.updateByPrimaryKeySelective(spu);
        if (c != 1){
            throw new LyException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }
        // 修改Spu detail
        SpuDetail spuDetail = spu.getSpuDetail();
        c = spuDetailMapper.updateByPrimaryKey(spuDetail);
        if (c != 1){
            throw new LyException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }
        // 修改 sku
        // 删除后新增
        List<Sku> olds = this.querySkuListBySpuId(spu.getId());
        skuMapper.deleteByIdList(olds.stream().map(e->e.getId()).collect(Collectors.toList()));

        List<Sku> skus = spu.getSkus();
        skus.forEach((e) -> {
            e.setSpuId(spu.getId());
            e.setCreateTime(now);
            e.setLastUpdateTime(now);
        });
          // 添加新的sku
        for (Sku s: skus){
            c = skuMapper.insert(s);
            if (c != 1){
                throw new LyException(ExceptionEnum.GOODS_UPDATE_ERROR);
            }
        }
        // 修改 stock
        // 删除原先 stock

        stockMapper.deleteByIdList(olds.stream().map(e->e.getId()).collect(Collectors.toList()));
        List<Stock> stocks = new ArrayList<>();
        for (Sku sku : skus) {
            Stock stock = new Stock();;
            // 新增
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stocks.add(stock);
        }
        c = stockMapper.insertList(stocks);
        if (c != stocks.size()){
            throw new LyException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }
    }

}
