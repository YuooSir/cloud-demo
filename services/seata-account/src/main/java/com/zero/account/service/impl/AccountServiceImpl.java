package com.zero.account.service.impl;

import com.zero.account.mapper.AccountTblMapper;
import com.zero.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl implements AccountService {

  @Autowired
  AccountTblMapper accountTblMapper;

  @Transactional
  @Override
  public void debit(String userId, int money) {
    // 扣减账户余额
    accountTblMapper.debit(userId, money);
    if (money == 100) {
      throw new RuntimeException("余额不足");
    }
  }
}
