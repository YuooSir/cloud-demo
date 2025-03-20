package com.zero.account.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountTblMapper {
  void debit(String userId, int money);
}
