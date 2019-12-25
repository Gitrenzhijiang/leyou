package com.ren.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Table(name = "tb_sku")
@Data
public class Sku {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private Long spuId;
    private String title;
    private String images;
    private Long price;
    // 0_0_0 特殊规格参数待选值 index 列表.
    private String indexes;
    /**
     * {"机身颜色":"白色", "内存":"3GB"}
     */
    private String ownSpec;

    private Boolean enable;

    private Date createTime;

    private Date lastUpdateTime;
    @Transient
    private Integer stock; // 库存
}
