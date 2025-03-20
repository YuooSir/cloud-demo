package com.zero.account.bean;

import java.io.Serializable;
import lombok.Data;

/**
 * @TableName account_tbl
 */
@Data
public class AccountTbl implements Serializable {

  private Integer id;

  private String userId;

  private Integer money;

}