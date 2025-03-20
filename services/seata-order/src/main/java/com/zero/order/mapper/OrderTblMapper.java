package com.zero.order.mapper;

import com.zero.order.bean.OrderTbl;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderTblMapper {

    int insert(OrderTbl record);

}
