package com.ren.service;

import com.ren.common.utils.enums.ExceptionEnum;
import com.ren.common.utils.exception.LyException;
import com.ren.item.pojo.Category;
import com.ren.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询商品分类, 根据父分类ID
     * @param pid
     * @return
     */
    public List<Category> queryListByParentId(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        // 根据这个对象的非空类字段 当做查询条件.
        List<Category> list = categoryMapper.select(category);
        if (CollectionUtils.isEmpty(list)){
            // 返回 404
            throw new LyException(ExceptionEnum.CATEGORY_LIST_IS_EMPTY);
        }
        return list;
    }

    /**
     * 根据ID 集合查询 Category 集合
     * @param ids
     * @return
     */
    public List<Category> queryCategoryListByIds(List<Long> ids){
        List<Category> list = categoryMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(list)){
            // 返回 404
            throw new LyException(ExceptionEnum.CATEGORY_LIST_IS_EMPTY);
        }
        return list;
    }
}
