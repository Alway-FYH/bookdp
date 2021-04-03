package com.fyh.bookdp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fyh.bookdp.entity.Product;
import com.fyh.bookdp.entity.ProductCategory;
import com.fyh.bookdp.mapper.ProductCategoryMapper;
import com.fyh.bookdp.mapper.ProductMapper;
import com.fyh.bookdp.service.ProductCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fyh.bookdp.service.ProductService;
import com.fyh.bookdp.vo.ProductCategoryVO;
import com.fyh.bookdp.vo.ProductVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fyh
 * @since 2021-03-08
 */
@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<ProductCategoryVO> getAllProductCategoryVO() {
        //一级分类
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("type",1);
        List<ProductCategory> leveOne = productCategoryMapper.selectList(wrapper);
        List<ProductCategoryVO> leveOneVO = leveOne.stream().map(e -> new ProductCategoryVO(e.getId(),e.getName())).collect(Collectors.toList());
        //图片赋值
        //商品信息赋值
        for (int i = 0 ;i < leveOneVO.size();i++){
            leveOneVO.get(i).setBannerImg("/images/banner4.png");
            leveOneVO.get(i).setTopImg("/images/top"+i+".png");
            wrapper = new QueryWrapper();
            wrapper.eq("categorylevelone_id",leveOneVO.get(i).getId());
            wrapper.eq("state",0);
            List<Product> productList = productMapper.selectList(wrapper);
            List<ProductVO> productVOList = productList.stream()
                    .map(e -> new ProductVO(e.getId()
                            ,e.getName(),
                            e.getPrice(),
                            e.getFileName()
                    )).collect(Collectors.toList());
            leveOneVO.get(i).setProductVOList(productVOList);
        }
        for (ProductCategoryVO leveOneProductCategoryVO : leveOneVO){
            wrapper = new QueryWrapper();
            wrapper.eq("type",2);
            wrapper.eq("parent_id",leveOneProductCategoryVO.getId());
            List<ProductCategory> leveTwo = productCategoryMapper.selectList(wrapper);
            List<ProductCategoryVO> leveTwoVO = leveTwo.stream().map(e -> new ProductCategoryVO(e.getId(),e.getName())).collect(Collectors.toList());
            leveOneProductCategoryVO.setChildren(leveTwoVO);
            for (ProductCategoryVO leveTwoProductCategoryVO : leveTwoVO) {
                wrapper = new QueryWrapper();
                wrapper.eq("type",3);
                wrapper.eq("parent_id",leveTwoProductCategoryVO.getId());
                List<ProductCategory> leveThree = productCategoryMapper.selectList(wrapper);
                List<ProductCategoryVO> leveThreeVO =  leveThree.stream().map(e -> new ProductCategoryVO(e.getId(),e.getName())).collect(Collectors.toList());
                leveTwoProductCategoryVO.setChildren(leveThreeVO);
            }
        }
        return leveOneVO;
    }
}
