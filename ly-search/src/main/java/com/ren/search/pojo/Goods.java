package com.ren.search.pojo;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Data
@Document(indexName = "goods", type = "docs", shards = 1, replicas = 0)
public class Goods {
    @Id
    private Long id;// spuID

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String all; // 所有需要被搜索的信息, 包含标题, 分类, 甚至品牌.

    @Field(type = FieldType.Keyword, index = false)
    private String sutTitle; // 卖点

    private Long brandId;
    private Long cid1;
    private Long cid2;
    private Long cid3;
    private Date createTime; // spu 创建时间
    private Set<Long> price; // 价格
    @Field(type = FieldType.Keyword, index = false)
    private String skus; // sku 信息的 json

    private Map<String, Object> spes; // 可搜索的规格参数, key 参数名, value 参数值
}
