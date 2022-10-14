package project.phoneshop.mapping;

import project.phoneshop.model.entity.*;
import project.phoneshop.model.payload.request.product.AddNewProductRequest;
import project.phoneshop.model.payload.request.product.ProductFromJson;
import project.phoneshop.model.payload.request.product.UpdateProductRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class ProductMapping {
    public static ProductEntity addProductToEntity(AddNewProductRequest addNewProductRequest, CategoryEntity category, BrandEntity brand){
        return new ProductEntity(brand,category,addNewProductRequest.getName(),addNewProductRequest.getPrice(),addNewProductRequest.getDescription(),addNewProductRequest.getInventory());
    }
    public  static ProductEntity addJsonProductToEntity(ProductFromJson productFromJson, CategoryEntity category, BrandEntity brand, Set<AttributeOptionEntity> listAttributeOption){
        ProductEntity product = new ProductEntity();
        product.setName(productFromJson.getName());
        product.setProductBrand(brand);
        product.setProductCategory(category);
        product.setPrice(productFromJson.getPrice());
        product.setDescription(productFromJson.getDescription());
        product.setDiscount(productFromJson.getDiscount());
        product.setInventory(productFromJson.getInventory());
        product.setCreate(new Date());
        product.setSellAmount(0);
        List<ImageProductEntity> listImageProduct = new ArrayList<>();
        for(String url : productFromJson.getImgUrl()){
            ImageProductEntity img = new ImageProductEntity();
            img.setUrl(url);
            listImageProduct.add(img);
            img.setProduct(product);
        }
        product.setImageProductEntityList(listImageProduct);
//        product.setAttributeOptionEntities(listAttributeOption);
        return product;
    }
    public static ProductEntity updateProduct(ProductEntity product, UpdateProductRequest updateProductRequest, BrandEntity brand, CategoryEntity category){
        product.setProductBrand(brand);
        product.setProductCategory(category);
        product.setDescription(updateProductRequest.getDescription());
        product.setName(updateProductRequest.getName());
        product.setPrice(updateProductRequest.getPrice());
        product.setDiscount(updateProductRequest.getDiscount());
        product.setInventory(updateProductRequest.getInventory());
        return product;
    }
}
