package com.zero.storage.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StorageTblMapper {
  void deduct(String commodityCode, int count);
}
