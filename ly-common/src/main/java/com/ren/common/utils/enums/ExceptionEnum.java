package com.ren.common.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {
    BRAND_LIST_IS_EMPTY(400, "品牌列表为空"),
    CATEGORY_LIST_IS_EMPTY(404, "商品分类列表为空"),
    SPEC_GROUP_NOT_FOUND(404, "商品规格组未找到"),
    SPEC_NOT_FOUND(404, "商品规格信息未找到"),
    GOODS_NOT_FOUND(404, "商品不存在"),
    GOODS_STOCK_NOT_FOUND(404, "商品库存不存在"),

    GOODS_SAVE_ERROR(500, "新增商品失败"),
    SAVE_BRAND_FAIL(500, "保存品牌失败"),
    SAVE_BRAND_CATEGORY_FAIL(500, "保存品牌与分类关联信息失败"),
    INVALID_IMAGE_TYPE(415, "无效的图片类型"),
    SAVE_FILE_FAIL(500, "保存文件时出错"),
    CREATE_SPEC_GROUP_FAIL(500, "新增规格分组失败"),
    CREATE_SPEC_GROUP_PARAM_FAIL(500, "新增规格参数失败"),
    BRAND_IS_EMPTY(400, "品牌为空"),
    GOODS_UPDATE_ERROR(500, "修改商品信息失败")

    ;

    private int code;
    private String msg;
}
