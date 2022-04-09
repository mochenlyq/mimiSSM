package com.bnuz.service;

import com.bnuz.pojo.ProductInfo;
import com.bnuz.pojo.vo.ProductInfoVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductInfoService {

    //显示全部商品（不分页）
    List<ProductInfo> getAll();

    //分页功能的实现
    PageInfo splitPage(int pageNum, int pageSize);

    //增加商品
    int save(ProductInfo productInfo);

    //按主键ID查询商品
    ProductInfo getByID(int pid);

    //更新商品
    int update(ProductInfo productInfo);

    //删除商品
    int delete(int pid);

    //批量删除商品的功能
    int deleteBatch(String []ids);

    //多条件商品查询
    List<ProductInfo> selectCondition(ProductInfoVo productInfoVo);

    //多条件查询分页
    PageInfo splitPageVo(ProductInfoVo productInfoVo, int pageSize);
}
