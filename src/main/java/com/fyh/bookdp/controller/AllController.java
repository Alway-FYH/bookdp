package com.fyh.bookdp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fyh.bookdp.entity.*;
import com.fyh.bookdp.mapper.OrderMapper;
import com.fyh.bookdp.mapper.ProductMapper;
import com.fyh.bookdp.service.OrderDetailService;
import com.fyh.bookdp.service.OrderService;
import com.fyh.bookdp.service.ProductCategoryService;
import com.fyh.bookdp.service.ProductService;
import com.fyh.bookdp.vo.ProductListVO;
import com.fyh.bookdp.vo.UserBuyVO;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/all")
public class AllController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;


    @GetMapping("/adminList")
    public ModelAndView adminList(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("adminHome");
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("state",0);
        modelAndView.addObject("productList",productService.list(wrapper));
        return modelAndView;
    }

    @GetMapping("/list/{type}/{id}")
    public ModelAndView list(@PathVariable("type") String type ,
                             @PathVariable("id") Integer id
    ){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("adminHome");
        QueryWrapper wrapper = new QueryWrapper();
        String str = "categorylevel"+type+"_id";
        wrapper.eq(str,id);
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());
        modelAndView.addObject("productList",productService.list(wrapper));
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable("id") Integer id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/all/adminList");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("id",id);
        Product product = productService.getOne(wrapper);
        product.setState(1);
        productMapper.updateById(product);
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());
        wrapper.eq("state",0);
        modelAndView.addObject("productList",productService.list(wrapper));
        return modelAndView;
    }

    @GetMapping("/one")
    public ModelAndView add(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("one");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("type",1);
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());
        modelAndView.addObject("one",productCategoryService.list(wrapper));
        return modelAndView;
    }

    @GetMapping("/two/{id}")
    public ModelAndView two(@PathVariable("id")Integer id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("two");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("parent_id",id);
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());
        modelAndView.addObject("two",productCategoryService.list(wrapper));
        wrapper = new QueryWrapper();
        wrapper.eq("id",id);
        modelAndView.addObject("parent",productCategoryService.getOne(wrapper));
        return modelAndView;
    }

    @GetMapping("/three/{id}")
    public ModelAndView three(@PathVariable("id")Integer id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("three");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("parent_id",id);
        modelAndView.addObject("three",productCategoryService.list(wrapper));
        wrapper = new QueryWrapper();
        wrapper.eq("id",id);
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());
        modelAndView.addObject("parent",productCategoryService.getOne(wrapper));
        return modelAndView;
    }

    //得到三级分类的id
    @GetMapping("/add/{threeId}")
    public ModelAndView add(@PathVariable("threeId")Integer threeId){

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("id",threeId);
        //得到具体三级的全部信息
        ProductCategory three = productCategoryService.getOne(wrapper);
        wrapper = new QueryWrapper();
        wrapper.eq("id",three.getParentId());
        ProductCategory two = productCategoryService.getOne(wrapper);
        wrapper = new QueryWrapper();
        wrapper.eq("id",two.getParentId());
        ProductCategory one = productCategoryService.getOne(wrapper);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add");
        modelAndView.addObject("one",one);
        modelAndView.addObject("two",two);
        modelAndView.addObject("three",three);
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());
        return modelAndView;
    }

    @PostMapping("/addBook")
    public String addBook(Product product, MultipartFile img){
        //得到图片名字
        productService.save(product);
        Integer id = product.getId();
        String name = img.getOriginalFilename();
        int i = name.lastIndexOf(".");
        String sub = name.substring(i);
        String filename = "book"+id+sub;
        product.setFileName(filename);
        //图片保存的路径
        String file =System.getProperty("user.dir")+"/src/main/resources/static/images/"+filename;
        try {
            //获取上传文件
            InputStream inputStream = img.getInputStream();
            //保存的路径
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            while (inputStream.read(bytes) != -1){
                outputStream.write(bytes);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        product.setFileName(filename);
        productService.updateById(product);
        return "redirect:/all/adminList";
    }

    @GetMapping("/userGoods")
    public ModelAndView userGoods(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("userBuy");
        List<UserBuyVO> list = new ArrayList();
        QueryWrapper wrapper = new QueryWrapper();
        List<Orders> ordersList = orderService.list(wrapper);
        for (Orders orders : ordersList) {
            List<ProductListVO> productList = new ArrayList();
            wrapper = new QueryWrapper();
            wrapper.eq("order_id",orders.getId());
            List<OrderDetail> orderServiceList = orderDetailService.list(wrapper);
            for (OrderDetail orderDetail : orderServiceList) {
                wrapper = new QueryWrapper();
                wrapper.eq("id",orderDetail.getProductId());
                Product product = productService.getOne(wrapper);
                ProductListVO productListVO = new ProductListVO(product.getName(),product.getFileName(),orderDetail.getQuantity(),orderDetail.getCost());
                productList.add(productListVO);
            }
            UserBuyVO userBuyVO = new UserBuyVO(orders.getId(),orders.getLoginName(),orders.getUserAddress(),orders.getCost(),orders.getSerialnumber(),orders.getState(),orders.getPay(),productList);
            list.add(userBuyVO);
        }
        modelAndView.addObject("userBuyList",list);
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());

        return modelAndView;
    }


    @PostMapping("/addOneParent")
    public String addOneParent(ProductCategory productCategory, MultipartFile img){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("type",1);
        int size = productCategoryService.list(wrapper).size();
        String filename = "top"+size+".png";
        String file =System.getProperty("user.dir")+"/src/main/resources/static/images/"+filename;
        try {
            //获取上传文件
            InputStream inputStream = img.getInputStream();
            //保存的路径
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            while (inputStream.read(bytes) != -1){
                outputStream.write(bytes);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        productCategory.setParentId(0);
        productCategory.setType(1);
        productCategoryService.save(productCategory);
        return "redirect:/all/adminList";
    }

    @PostMapping("/addTwoParent")
    public String addTwoParent(ProductCategory productCategory,HttpServletRequest request){
        String parent = request.getParameter("parent");
        int i = Integer.parseInt(parent);
        productCategory.setParentId(i);
        productCategory.setType(2);
        productCategoryService.save(productCategory);
        return "redirect:/all/one";
    }

    @PostMapping("/addThreeParent")
    public String addThreeParent(ProductCategory productCategory,HttpServletRequest request){
        String parent = request.getParameter("parent");
        int i = Integer.parseInt(parent);
        productCategory.setParentId(i);
        productCategory.setType(3);
        productCategoryService.save(productCategory);
        return "redirect:/all/one";
    }

    @GetMapping("/update/{id}")
    public ModelAndView update(@PathVariable("id")Integer id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("update");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("id",id);
        modelAndView.addObject("updateProduct",productService.getOne(wrapper));
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());
        return modelAndView;
    }

    @PostMapping("/updateOne")
    public ModelAndView updateOne(Product product, MultipartFile img){
        if (img.getSize()>0){
            String file =System.getProperty("user.dir")+"/src/main/resources/static/images/"+product.getFileName();
            try {
                //获取上传文件
                InputStream inputStream = img.getInputStream();
                //保存的路径
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] bytes = new byte[1024];
                while (inputStream.read(bytes) != -1){
                    outputStream.write(bytes);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        productMapper.updateById(product);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("adminHome");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("id",product.getId());
        modelAndView.addObject("productList",productService.list(wrapper));
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());
        return modelAndView;
    }

    @GetMapping("/send/{id}")
    public String send(@PathVariable("id")Integer id){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("id",id);
        List list = orderService.list(wrapper);
        Orders  orders = (Orders)list.get(0);
        orders.setState(1);
        orderMapper.updateById(orders);
        return "redirect:/all/userGoods";
    }

    @GetMapping("/notSend")
    public ModelAndView notSend(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("userBuy");
        List<UserBuyVO> list = new ArrayList();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("state",0);
        List<Orders> ordersList = orderService.list(wrapper);
        for (Orders orders : ordersList) {
            List<ProductListVO> productList = new ArrayList();
            wrapper = new QueryWrapper();
            wrapper.eq("order_id",orders.getId());
            List<OrderDetail> orderServiceList = orderDetailService.list(wrapper);
            for (OrderDetail orderDetail : orderServiceList) {
                wrapper = new QueryWrapper();
                wrapper.eq("id",orderDetail.getProductId());
                Product product = productService.getOne(wrapper);
                ProductListVO productListVO = new ProductListVO(product.getName(),product.getFileName(),orderDetail.getQuantity(),orderDetail.getCost());
                productList.add(productListVO);
            }
            UserBuyVO userBuyVO = new UserBuyVO(orders.getId(),orders.getLoginName(),orders.getUserAddress(),orders.getCost(),orders.getSerialnumber(),orders.getState(),orders.getPay(),productList);
            list.add(userBuyVO);
        }
        modelAndView.addObject("userBuyList",list);
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());

        return modelAndView;
    }

    @GetMapping("/overSend")
    public ModelAndView overSend(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("userBuy");
        List<UserBuyVO> list = new ArrayList();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("state",1);
        List<Orders> ordersList = orderService.list(wrapper);
        for (Orders orders : ordersList) {
            List<ProductListVO> productList = new ArrayList();
            wrapper = new QueryWrapper();
            wrapper.eq("order_id",orders.getId());
            List<OrderDetail> orderServiceList = orderDetailService.list(wrapper);
            for (OrderDetail orderDetail : orderServiceList) {
                wrapper = new QueryWrapper();
                wrapper.eq("id",orderDetail.getProductId());
                Product product = productService.getOne(wrapper);
                ProductListVO productListVO = new ProductListVO(product.getName(),product.getFileName(),orderDetail.getQuantity(),orderDetail.getCost());
                productList.add(productListVO);
            }
            UserBuyVO userBuyVO = new UserBuyVO(orders.getId(),orders.getLoginName(),orders.getUserAddress(),orders.getCost(),orders.getSerialnumber(),orders.getState(),orders.getPay(),productList);
            list.add(userBuyVO);
        }
        modelAndView.addObject("userBuyList",list);
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());

        return modelAndView;
    }

    @GetMapping("/exist")
    public ModelAndView exist(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("adminHome");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("state",0);
        modelAndView.addObject("productList",productService.list(wrapper));
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());
        return modelAndView;
    }
    @GetMapping("/notExist")
    public ModelAndView notExist(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("adminHome");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("state",1);
        modelAndView.addObject("productList",productService.list(wrapper));
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());
        return modelAndView;
    }

    @GetMapping("/addAgain/{id}")
    public ModelAndView addAgain(@PathVariable("id") Integer id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/all/notExist");
        Product product = productService.getById(id);
        product.setState(0);
        productMapper.updateById(product);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("state",0);
        modelAndView.addObject("productList",productService.list(wrapper));
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());
        return modelAndView;
    }

    @GetMapping("/pay")
    public ModelAndView pay(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("userBuy");
        List<UserBuyVO> list = new ArrayList();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("pay",1);
        List<Orders> ordersList = orderService.list(wrapper);
        for (Orders orders : ordersList) {
            List<ProductListVO> productList = new ArrayList();
            wrapper = new QueryWrapper();
            wrapper.eq("order_id",orders.getId());
            List<OrderDetail> orderServiceList = orderDetailService.list(wrapper);
            for (OrderDetail orderDetail : orderServiceList) {
                wrapper = new QueryWrapper();
                wrapper.eq("id",orderDetail.getProductId());
                Product product = productService.getOne(wrapper);
                ProductListVO productListVO = new ProductListVO(product.getName(),product.getFileName(),orderDetail.getQuantity(),orderDetail.getCost());
                productList.add(productListVO);
            }
            UserBuyVO userBuyVO = new UserBuyVO(orders.getId(),orders.getLoginName(),orders.getUserAddress(),orders.getCost(),orders.getSerialnumber(),orders.getState(),orders.getPay(),productList);
            list.add(userBuyVO);
        }
        modelAndView.addObject("userBuyList",list);
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());

        return modelAndView;
    }

    @GetMapping("/notPay")
    public ModelAndView notPay(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("userBuy");
        List<UserBuyVO> list = new ArrayList();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("pay",0);
        List<Orders> ordersList = orderService.list(wrapper);
        for (Orders orders : ordersList) {
            List<ProductListVO> productList = new ArrayList();
            wrapper = new QueryWrapper();
            wrapper.eq("order_id",orders.getId());
            List<OrderDetail> orderServiceList = orderDetailService.list(wrapper);
            for (OrderDetail orderDetail : orderServiceList) {
                wrapper = new QueryWrapper();
                wrapper.eq("id",orderDetail.getProductId());
                Product product = productService.getOne(wrapper);
                ProductListVO productListVO = new ProductListVO(product.getName(),product.getFileName(),orderDetail.getQuantity(),orderDetail.getCost());
                productList.add(productListVO);
            }
            UserBuyVO userBuyVO = new UserBuyVO(orders.getId(),orders.getLoginName(),orders.getUserAddress(),orders.getCost(),orders.getSerialnumber(),orders.getState(),orders.getPay(),productList);
            list.add(userBuyVO);
        }
        modelAndView.addObject("userBuyList",list);
        modelAndView.addObject("list",productCategoryService.getAllProductCategoryVO());

        return modelAndView;
    }
}
