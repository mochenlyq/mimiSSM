package com.bnuz.test;

import com.bnuz.mapper.ProductInfoMapper;
import com.bnuz.pojo.ProductInfo;
import com.bnuz.pojo.vo.ProductInfoVo;
import com.bnuz.utils.MD5Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext_dao.xml", "classpath:applicationContext_service.xml"})
public class MyTest {
    @Autowired
    ProductInfoMapper productInfoMapper;

    @Test
    public void testMD5(){
        ProductInfoVo productInfoVo = new ProductInfoVo();
        productInfoVo.setLprice(2000);
//        productInfoVo.setHprice(3000);
        List<ProductInfo> list = productInfoMapper.selectCondition(productInfoVo);
        list.forEach(productInfo -> System.out.println(productInfo));
    }


}
