package com.ren.service;

import com.ren.common.utils.enums.ExceptionEnum;
import com.ren.common.utils.exception.LyException;
import com.ren.item.pojo.Brand;
import com.ren.item.pojo.SpecGroup;
import com.ren.item.pojo.SpecParam;
import com.ren.mapper.SpecGroupMapper;
import com.ren.mapper.SpecParamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    public List<SpecGroup> queryGroupByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> specGroupList = specGroupMapper.select(specGroup);
        if (CollectionUtils.isEmpty(specGroupList)){
            // 查询结果为空
            throw new LyException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }
        return specGroupList;
    }

    public void add(SpecGroup group) {
        int c = specGroupMapper.insert(group);
        if (c == 0){
            throw new LyException(ExceptionEnum.CREATE_SPEC_GROUP_FAIL);
        }
    }

    public void deleteGroup(Long id) {
        int c = specGroupMapper.deleteByPrimaryKey(id);
        if (c == 0){
            // 这个需要删除的组不存在
            throw new LyException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }
    }

    public void updateGroup(SpecGroup group) {
        int c = specGroupMapper.updateByPrimaryKey(group);
        if (c == 0){
            // 这个需要修改的组不存在
            throw new LyException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }

    }
    @Autowired
    private SpecParamMapper specParamMapper;

    /**
     *  查询商品规格信息列表
     *
     * @param cid
     * @param gid
     * @param searching
     * @return
     */
    public List<SpecParam> queryParam(Long gid, Long cid, Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        List<SpecParam> list = specParamMapper.select(specParam);
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.SPEC_NOT_FOUND);
        }
        return list;
    }

    /**
     * 添加 商品规格参数
     * @param specParam
     */
    public void addParam(SpecParam specParam) {
        int c = specParamMapper.insert(specParam);
        if (c != 1){
            throw new LyException(ExceptionEnum.CREATE_SPEC_GROUP_PARAM_FAIL);
        }
    }

    public void updateParam(SpecParam specParam) {
        int c = specParamMapper.updateByPrimaryKey(specParam);
        if (c != 1) {
            // 规格参数信息 未找到
            throw new LyException(ExceptionEnum.SPEC_NOT_FOUND);
        }
    }

    public void deleteParam(Long id) {
        int c = specParamMapper.deleteByPrimaryKey(id);
        if (c != 1) {
            // 需要删除的 规格参数信息 未找到
            throw new LyException(ExceptionEnum.SPEC_NOT_FOUND);
        }
    }
}
