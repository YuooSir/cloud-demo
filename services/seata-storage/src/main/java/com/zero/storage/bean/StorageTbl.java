package com.zero.storage.bean;

import java.io.Serializable;
import lombok.Data;

/**
 * @TableName storage_tbl
 */
@Data
public class StorageTbl implements Serializable {

  private static final long serialVersionUID = 1L;
  private Integer id;
  private String commodityCode;
  private Integer count;
}