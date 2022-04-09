package com.bnuz.controller;

import com.bnuz.pojo.ProductInfo;
import com.bnuz.pojo.vo.ProductInfoVo;
import com.bnuz.service.ProductInfoService;
import com.bnuz.utils.FileNameUtil;
import com.github.pagehelper.PageInfo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProductInfoAction {

    //每页显示的记录数
    public static final int PAGE_SIZE = 5;
    //异步上传的图片的名称
    String saveFileName = "";

    @Autowired
    ProductInfoService productInfoService;

    //显示全部商品不分页
    @RequestMapping("/getAll")
    public String getAll(HttpServletRequest request){
        List<ProductInfo> list = productInfoService.getAll();
        request.setAttribute("list", list);
        return "product";
    }

    //显示第一页的5条数据
    @RequestMapping("/split")
    public String split(HttpServletRequest request){
        PageInfo pageInfo = null;
        Object vo = request.getSession().getAttribute("prodVo");
        if (vo != null){
            pageInfo = productInfoService.splitPageVo((ProductInfoVo)vo, PAGE_SIZE);
            request.getSession().removeAttribute("prodVo");
        }
        else {
            pageInfo = productInfoService.splitPage(1, PAGE_SIZE);
        }
        request.setAttribute("info", pageInfo);
        return "product";
    }

    //ajax分页处理
    @ResponseBody
    @RequestMapping("/ajaxSplit")
    public void ajaxSplit(ProductInfoVo productInfoVo, HttpSession session){
        //取得当前page参数的页面数据
        PageInfo info = productInfoService.splitPageVo(productInfoVo, PAGE_SIZE);
        session.setAttribute("info", info);
    }

    //多条件查询功能实现
    @RequestMapping("/condition")
    @ResponseBody
    public void condition(ProductInfoVo productInfoVo, HttpSession session){
        List<ProductInfo> list = productInfoService.selectCondition(productInfoVo);
        session.setAttribute("list", list);
    }

    //异步Ajax文件上传处理
    @ResponseBody
    @RequestMapping("/ajaxImg")
    public Object ajaxImg(MultipartFile pimage, HttpServletRequest request) {
        //提取生成文件名UUID+上传图片的后缀.jpg  .png
        saveFileName = FileNameUtil.getUUIDFileName() + FileNameUtil.getFileType(pimage.getOriginalFilename());
        //得到项目中图片存储的路径
        String path = request.getServletContext().getRealPath("/image_big");
        //转存  E:\idea_workspace\mimissm\image_big\23sdfasferafdafdadfasfdassf.jpg
        try {
            pimage.transferTo(new File(path + File.separator + saveFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回客户端JSON对象,封装图片的路径,为了在页面实现立即回显
        JSONObject object = new JSONObject();
        object.put("imgurl", saveFileName);
        return object.toString();
    }

    @RequestMapping("/save")
    public String save(ProductInfo productInfo, HttpServletRequest request){
        productInfo.setpDate(new Date());
        productInfo.setpImage(saveFileName);
        int num = -1;
        num = productInfoService.save(productInfo);
        if (num > 0){
            request.setAttribute("msg", "添加成功");
        }
        else {
            request.setAttribute("msg", "添加失败");
        }
        saveFileName = "";
        return "forward:/prod/split.action";
    }

    @RequestMapping("/one")
    public String one(int pid, ProductInfoVo productInfoVo, Model model, HttpSession session){
        ProductInfo productInfo = productInfoService.getByID(pid);
        model.addAttribute("prod", productInfo);
        //将多条件及页码放入Session中，更新处理结束后分页时，读取条件和页码进行处理
        session.setAttribute("prodVO", productInfo);
        return "update";
    }

    @RequestMapping("/update")
    public String update(ProductInfo productInfo, HttpServletRequest request){
        if (!saveFileName.equals("")){
            productInfo.setpImage(saveFileName);
        }
        int num = -1;
        try{
            num = productInfoService.update(productInfo);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if (num > 0){
            request.setAttribute("msg", "更新成功");
        }
        else {
            request.setAttribute("msg", "更新失败");
        }
        saveFileName = "";
        return "forward:/prod/split.action";
    }

    @RequestMapping("/delete")
    public String delete(int pid, ProductInfoVo productInfoVo, HttpServletRequest request){
        int num = -1;
        try{
            num = productInfoService.delete(pid);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if (num > 0){
            request.setAttribute("msg", "删除成功");
            request.getSession().setAttribute("deleteProdVo", productInfoVo);
        }
        else {
            request.setAttribute("msg", "删除失败");
        }
        return "forward:/prod/deleteAjaxSplit.action";
    }

    @ResponseBody
    @RequestMapping(value = "/deleteAjaxSplit", produces = "text/html;charset=UTF-8")
    public Object deleteAjaxSplit(HttpServletRequest request) {
        //取得第1页的数据
        PageInfo info = null;
        Object vo = request.getSession().getAttribute("deleteProdVo");
        if (vo != null){
            info = productInfoService.splitPageVo((ProductInfoVo)vo, PAGE_SIZE);
        }else {
            info = productInfoService.splitPage(1, PAGE_SIZE);
        }
        request.getSession().setAttribute("info",info);
        return request.getAttribute("msg");
    }

    //批量删除商品
    @RequestMapping("/deleteBatch")
    public String deleteBatch(String pids, HttpServletRequest request){
        String []ps = pids.split(",");
        int num = -1;
        try {
            num = productInfoService.deleteBatch(ps);
            if (num > 0){
                request.setAttribute("msg", "批量删除成功");
            }
            else {
                request.setAttribute("msg", "批量删除失败");
            }
        }catch (Exception e){
            request.setAttribute("msg", "商品不可删除");
        }
        return "forward:/prod/deleteAjaxSplit.action";
    }


}
