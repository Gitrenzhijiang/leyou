package com.ren.item.vo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
public class SpuVo {
    private Long id;
    private Long brandId; // 品牌ID
    private Long cid1;
    private Long cid2;
    private Long cid3;
    private String title;
    private String subTitle;
    private Boolean saleable; // 是否上架
    private Boolean valid; // 是否有效, 逻辑删除

    private Date createTime;
    private Date lastUpdateTime;

    private String cname; // 分类名称


}
