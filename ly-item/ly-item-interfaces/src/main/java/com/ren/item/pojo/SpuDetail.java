package com.ren.item.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_spu_detail")
@Data
public class SpuDetail {
    @Id
    private Long spuId; // 对应SPU ID
    private String description; // 商品描述
    private String genericSpec; // 商品 规格分组-规格参数-值 的JSON
    private String specialSpec ; // 特殊规格参数 可选值 JSON
    private String packingList; // 打包信息
    private String afterService; // 售后信息
}
