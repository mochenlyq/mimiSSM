package com.bnuz.service.impl;

import com.bnuz.mapper.ProductTypeMapper;
import com.bnuz.pojo.ProductType;
import com.bnuz.pojo.ProductTypeExample;
import com.bnuz.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ProductTypeServiceImpl")
public class ProductTypeServiceImpl implements ProductTypeService {

    @Autowired
    ProductTypeMapper productTypeMapper;

    @Override
    public List<ProductType> getAll() {
        return productTypeMapper.selectByExample(new ProductTypeExample());
    }

}
