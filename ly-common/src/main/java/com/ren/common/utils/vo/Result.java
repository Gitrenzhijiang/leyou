package com.ren.common.utils.vo;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 作为 ajax 请求返回的对象
 */
@Getter
@NoArgsConstructor
public class Result<T> {

    private List<T> data;
    private Long total; // 总记录数
    private Integer totalPage; // 总页数

    public Result(List<T> data, Long total) {
        this.data = data;
        this.total = total;
    }

    public Result(List<T> data, Long total, Integer totalPage){
        this.data = data;
        this.total = total;
        this.totalPage = totalPage;
    }

    public static Result of(){
        return new Result();
    }


    public Result data(List<T> data){
        this.data = data;
        return this;
    }

    public Result total(long total){
        this.total = total;
        return this;
    }
}
