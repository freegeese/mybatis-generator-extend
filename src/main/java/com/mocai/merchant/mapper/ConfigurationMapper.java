package com.mocai.merchant.mapper;

import com.mocai.merchant.model.Configuration;

public interface ConfigurationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Configuration record);

    int insertSelective(Configuration record);

    Configuration selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Configuration record);

    int updateByPrimaryKey(Configuration record);
}