package com.ren.mapper;

import com.ren.item.pojo.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category, Long> {

    /**
     * 获取品牌的分类信息列表
     */
    @Select("select a.* from tb_category as a, tb_category_brand as b where a.id = b.category_id and b.brand_id = #{brand_id} ")
    List<Category> getListByBrandId(@Param("brand_id") Long brand_id);
}
