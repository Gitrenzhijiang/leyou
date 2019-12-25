package com.ren.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ren.common.utils.enums.ExceptionEnum;
import com.ren.common.utils.exception.LyException;
import com.ren.common.utils.vo.Result;
import com.ren.item.pojo.Brand;
import com.ren.item.pojo.Category;
import com.ren.mapper.BrandMapper;
import com.ren.mapper.CategoryMapper;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    public Result pageQuery(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        Example example = new Example(Brand.class);
        // 处理查询关键字
        if (StringUtils.isNotBlank(key)) {
            example.createCriteria().orLike("name", "%" + key + "%")
                    .orEqualTo("letter", key.toUpperCase());
        }

        if (StringUtils.isNotBlank(sortBy)) {
            String param = sortBy + (desc? " desc": " asc");
            example.setOrderByClause(param);
        }
        PageHelper.startPage(page, rows);

        List<Brand> brandList = brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(brandList)) {
            throw new LyException(ExceptionEnum.BRAND_LIST_IS_EMPTY);
        }
        PageInfo<Brand> info = new PageInfo<>(brandList);
        return Result.of().data(brandList).total(info.getTotal());
    }

    /**
     * 存储 品牌和 该品牌所拥有的商品分类
     * @param brand
     * @param cids
     */
    public void saveBrand(Brand brand, List<Long> cids) {
        brand.setId(null);
        int c = brandMapper.insert(brand);
        if (c != 1) {
            throw new LyException(ExceptionEnum.SAVE_BRAND_FAIL);
        }
        System.out.println("新添加的品牌: " + brand.getId());
        // 保存品牌 对应 商品分类
        for (Long cid : cids) {
            c = brandMapper.insertBrandCategory(cid, brand.getId());
            if (c != 1) {
                throw new LyException(ExceptionEnum.SAVE_BRAND_CATEGORY_FAIL);
            }
        }
    }

    /**
     * 获得 品牌 信息, 根据ID
     * @param id
     * @return
     */
    public Brand selectById(Long id) {

        Brand brand =  brandMapper.selectByPrimaryKey(id);
        if (brand == null) {
            throw new LyException(ExceptionEnum.BRAND_IS_EMPTY);
        }
        return brand;
    }

    /**
     * 修改品牌 信息, 修改品牌和 分类的关联
     * @param brand
     * @param cids
     */
    public void updateAndCategoryList(Brand brand, List<Long> cids) {
        // 修改 品牌
        int c = brandMapper.updateByPrimaryKey(brand);
        if (c == 0) {
            // 不存在该品牌
            throw new LyException(ExceptionEnum.BRAND_IS_EMPTY);
        }
        // 修改 品牌和 分类的关联
        brandMapper.deleteCategoryListByBrandId(brand.getId());
        for (Long cid : cids) {
            c = brandMapper.insertBrandCategory(cid, brand.getId());
            if (c != 1) {
                throw new LyException(ExceptionEnum.SAVE_BRAND_CATEGORY_FAIL);
            }
        }
    }

    /**
     * 删除品牌
     * @param bid
     */
    public void delete(Long bid) {
        int c = brandMapper.deleteByPrimaryKey(bid);
        if (c == 0) {
            // 不存在该品牌
            throw new LyException(ExceptionEnum.BRAND_IS_EMPTY);
        }
        // 删除品牌和分类的关联
        brandMapper.deleteCategoryListByBrandId(bid);
    }

    public List<Category> getCategoryListByBid(Long id) {
        return categoryMapper.getListByBrandId(id);
    }

    /**
     * 根据分类ID, 查询与之关联的品牌 列表
     * @param cid
     * @return
     */
    public List<Brand> getBrandListByCid(Long cid) {

        return brandMapper.selectBrandsByCid(cid);
    }
}
