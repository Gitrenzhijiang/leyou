package com.ren.mapper;

import com.ren.item.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {

    /**
     * 插入商品品牌 和 分类的关联
     * @param category_id
     * @param brand_id
     * @return
     */
    @Insert("insert into tb_category_brand (category_id,brand_id) values (#{category_id}, #{brand_id})")
    int insertBrandCategory(Long category_id, Long brand_id);

    @Delete("delete from tb_category_brand where brand_id = #{brand_id}")
    int deleteCategoryListByBrandId(Long brand_id);

    @Select("select a.* from tb_category_brand b, tb_brand as a where b.brand_id = a.id and b.category_id = #{category_id}")
    List<Brand> selectBrandsByCid(Long cid);
}
