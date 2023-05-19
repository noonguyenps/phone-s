package project.phoneshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.phoneshop.handler.AuthorizationHeader;
import project.phoneshop.mapping.ProductMapping;
import project.phoneshop.model.entity.*;
import project.phoneshop.model.payload.request.product.*;
import project.phoneshop.model.payload.response.SuccessResponse;
import project.phoneshop.model.payload.response.product.ProductResponse;
import project.phoneshop.service.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.*;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {
    @Autowired
    AuthorizationHeader authorizationHeader;
    private final UserService userService;
    private final ProductService productService;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final ImageStorageService imageStorageService;
    private final AttributeService attributeService;
    //Get All Product Active in SPhone
    @GetMapping("/product/all")
    private ResponseEntity<SuccessResponse> showAllProduct(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "30") int size,
                                                           @RequestParam(defaultValue = "product_id") String sort){
        if(!listProSort().contains(sort))
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Properties sort Not found",null), HttpStatus.FOUND);
        List<ProductEntity> listProduct = productService.findByProductStatus(page, size, 1);
        if(listProduct.size() == 0)
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.FOUND.value(),"List Product is Empty",null), HttpStatus.FOUND);
        List<ProductResponse> listResponse = new ArrayList<>();
        for (ProductEntity product : listProduct)
            if(product.getStatus()==1) {
                listResponse.add(productService.productResponse(product));
            }
        Map<String, Object> data = new HashMap<>();
        data.put("listProduct",listResponse);
        return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Query Successfully",data), HttpStatus.OK);
    }
    //Get All Product in SPhone
    @GetMapping("/manager/product/all")
    private ResponseEntity<SuccessResponse> showAllProductWithAdmin(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "30") int size,
                                                           @RequestParam(defaultValue = "product_id") String sort){
        if(!listProSort().contains(sort))
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Properties sort Not found",null), HttpStatus.FOUND);
        List<ProductEntity> listProduct = productService.findPaginated(page, size, sort);
        if(listProduct.size() == 0)
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.FOUND.value(),"List Product is Empty",null), HttpStatus.FOUND);
        List<ProductResponse> listResponse = new ArrayList<>();
        for (ProductEntity product : listProduct)
                listResponse.add(productService.productResponse(product));
        Map<String, Object> data = new HashMap<>();
        data.put("listProduct",listResponse);
        return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Query Successfully",data), HttpStatus.OK);
    }
    @GetMapping("/manager/product/all/status")
    private ResponseEntity<SuccessResponse> showAllProductByStatus(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "30") int size,
                                                                   @RequestParam(defaultValue = "1") int status){
        List<ProductEntity> listProduct = productService.findByProductStatus(page, size, status);
        if(listProduct.size() == 0)
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.OK.value(),"List Product is Empty",null), HttpStatus.OK);
        List<ProductResponse> listResponse = new ArrayList<>();
        for (ProductEntity product : listProduct)
            listResponse.add(productService.productResponse(product));
        Map<String, Object> data = new HashMap<>();
        data.put("listProduct",listResponse);
        return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Query Successfully",data), HttpStatus.OK);
    }
    @GetMapping("/product/byCategory")
    private ResponseEntity<SuccessResponse> showAllProductByCategory(@RequestParam UUID idCategory,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "30") int size,
                                                                     @RequestParam(defaultValue = "product_id") String sort){
        if(!listProSort().contains(sort))
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Properties sort Not found",null), HttpStatus.FOUND);
        CategoryEntity categoryEntity = categoryService.findById(idCategory);
        if(categoryEntity == null)
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Category Not Found",null), HttpStatus.NOT_FOUND);
        List<ProductEntity> listProduct = productService.findProductByCategory(categoryEntity,page,size,sort);
        if(listProduct.size() == 0)
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.FOUND.value(),"List Product is Empty",null), HttpStatus.FOUND);
        List<ProductResponse> listResponse = new ArrayList<>();
        for (ProductEntity product : listProduct)
            listResponse.add(productService.productResponse(product));
        Map<String, Object> data = new HashMap<>();
        data.put("listProduct",listResponse);
        return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(), "Query Successfully",data), HttpStatus.OK);
    }
    @PostMapping("/product/category/{id}")
    private ResponseEntity<SuccessResponse> showAllProductByCategory(@PathVariable UUID id,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "30") int size,
                                                                     @RequestParam(defaultValue = "product_id") String sort,
                                                                     @RequestBody List<String> listAttribute){
        if(!listProSort().contains(sort))
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Properties sort Not found",null), HttpStatus.FOUND);
        CategoryEntity categoryEntity = categoryService.findById(id);
        if(categoryEntity == null)
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Category Not Found",null), HttpStatus.NOT_FOUND);
        List<ProductEntity> listProduct = new ArrayList<>();
        if(listAttribute.isEmpty())
            listProduct = productService.findProductByCategory(categoryEntity,page,size,sort);
        else
            listProduct = productService.findProductByAttributes(categoryEntity,listAttribute,page,size,sort);
        if(listProduct.size() == 0)
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.FOUND.value(),"List Product is Empty", null), HttpStatus.FOUND);
        List<ProductResponse> listResponse = new ArrayList<>();
        for (ProductEntity product : listProduct)
            listResponse.add(productService.productResponse(product));
        Map<String, Object> data = new HashMap<>();
        data.put("listProduct",listResponse);
        return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Query Successfully",data), HttpStatus.OK);
    }
    @GetMapping("/product/brand/{id}")
    private ResponseEntity<SuccessResponse> showAllProductByBrand(@PathVariable UUID id,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "30") int size,
                                                                  @RequestParam(defaultValue = "product_id") String sort){
        if(!listProSort().contains(sort))
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Properties sort Not found",null), HttpStatus.FOUND);
        BrandEntity brand = brandService.findById(id);
        if(brand == null)
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Brand Not Found",null), HttpStatus.NOT_FOUND);
        List<ProductEntity> listProduct = productService.findProductByBrand(brand,page,size,sort);
        if(listProduct.size() == 0)
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.FOUND.value(),"List Product is Empty",null), HttpStatus.FOUND);
        List<ProductResponse> listResponse = new ArrayList<>();
        for (ProductEntity product : listProduct)
            listResponse.add(productService.productResponse(product));
        Map<String,Object> data = new HashMap<>();
        data.put("listProduct", listResponse);
        return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Query Successfully",data), HttpStatus.OK);
    }
    @GetMapping("/product/key/{keyword}")
    private ResponseEntity<SuccessResponse> showAllProductByKeyword(@PathVariable String keyword,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "30") int size,
                                                                    @RequestParam(defaultValue = "product_id") String sort){
        if(!listProSort().contains(sort))
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Properties sort Not found",null), HttpStatus.FOUND);
        List<ProductEntity> listProduct = productService.findProductByKeyword(keyword,page,size,sort);
        if(listProduct.size() == 0)
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.FOUND.value(),"List Product is Empty",null), HttpStatus.FOUND);
        List<ProductResponse> listResponse = new ArrayList<>();
        for (ProductEntity product : listProduct)
            listResponse.add(productService.productResponse(product));
        Map<String,Object> data = new HashMap<>();
        data.put("listProduct",listResponse);
        return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Query Successfully",data), HttpStatus.OK);
    }
    @GetMapping("/product/{id}")
    private ResponseEntity<SuccessResponse> showProductById(@PathVariable UUID id){
        ProductEntity product = productService.findById(id);
        if(product == null)
            return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.FOUND.value(),"Product is Not Found",null), HttpStatus.FOUND);
        Map<String, Object> data = new HashMap<>();
        data.put("product",productService.productResponse(product));
        return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Query Successfully",data), HttpStatus.OK);
    }
    @PostMapping("/manager/product/insert")
    private ResponseEntity<SuccessResponse> insertProduct(HttpServletRequest request, @RequestBody AddNewProductRequest addNewProductRequest){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            BrandEntity brand = brandService.findById(addNewProductRequest.getBrand());
            CategoryEntity category = categoryService.findById(addNewProductRequest.getCategory());
            ProductEntity product = ProductMapping.addProductToEntity(addNewProductRequest,category,brand);
            productService.saveProduct(product);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Add Product Successfully",null), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PostMapping("/manager/product/insert/all")
    private ResponseEntity<SuccessResponse> insertProductJson(HttpServletRequest request,
                                                              @RequestBody ProductFromJson productReq){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            BrandEntity brand = brandService.findById(productReq.getBrand());
            if(brand == null)
                return new ResponseEntity<>(new SuccessResponse(false,
                        HttpStatus.NOT_FOUND.value(),"Brand is Not Found",
                        null), HttpStatus.NOT_FOUND);
            CategoryEntity category = categoryService.findById(productReq.getCategory());
            if(category == null)
                return new ResponseEntity<>(new SuccessResponse(false,
                        HttpStatus.NOT_FOUND.value(),"Category is Not Found",
                        null), HttpStatus.NOT_FOUND);
            Set<AttributeOptionEntity> listAttributeOption = new HashSet<>();
            for (String attributeOptionId : productReq.getAttribute()){
                AttributeOptionEntity attributeOption = attributeService.findByIdAttributeOption(attributeOptionId);
                if(attributeOption == null)
                    return new ResponseEntity<>(new SuccessResponse(false,
                            HttpStatus.NOT_FOUND.value(),"Attribute Options is Not Found",
                            null), HttpStatus.NOT_FOUND);
                else
                    listAttributeOption.add(attributeOption);
            }
            ProductEntity product = ProductMapping.addJsonProductToEntity(productReq,
                    category,brand,listAttributeOption);
            productService.saveProduct(product);
            return new ResponseEntity<>(new SuccessResponse(true,
                    HttpStatus.OK.value(),"Add Product Successfully",
                    null), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PostMapping("/manager/product/insert/v1")
    private ResponseEntity<SuccessResponse> insertProduct(HttpServletRequest request,@RequestBody AddProductRequest productReq){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            BrandEntity brand = brandService.findById(productReq.getBrand());
            if(brand == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Brand is Not Found",null), HttpStatus.NOT_FOUND);
            CategoryEntity category = categoryService.findById(productReq.getCategory());
            if(category == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Category is Not Found",null), HttpStatus.NOT_FOUND);
            Set<AttributeOptionEntity> listAttributeOption = new HashSet<>();
            for (String attributeId: productReq.getAttribute()){
                AttributeOptionEntity attributeOption = attributeService.findByIdAttributeOption(attributeId);
                if(attributeOption == null)
                    new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Attribute Options is Not Found",null), HttpStatus.NOT_FOUND);
                else
                    listAttributeOption.add(attributeOption);
            }
            ProductEntity product = ProductMapping.addJsonProductToEntity(productReq,category,brand,listAttributeOption);
            productService.saveProduct(product);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Add Product Successfully",null), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PostMapping("/manager/product/insert/v2")
    private ResponseEntity<SuccessResponse> insertProductV2(HttpServletRequest request,@RequestBody AddProductV2Request productReq){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            BrandEntity brand = brandService.findById(productReq.getBrand());
            if(brand == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Brand is Not Found",null), HttpStatus.NOT_FOUND);
            CategoryEntity category = categoryService.findById(productReq.getCategory());
            if(category == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Category is Not Found",null), HttpStatus.NOT_FOUND);
            Set<AttributeOptionEntity> listAttributeOption = new HashSet<>();
            for (String attributeId: productReq.getAttribute()){
                AttributeOptionEntity attributeOption = attributeService.findByIdAttributeOption(attributeId);
                if(attributeOption == null)
                    new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Attribute Options is Not Found",null), HttpStatus.NOT_FOUND);
                else
                    listAttributeOption.add(attributeOption);
            }
            Set<AttributeEntity> listAttribute = new HashSet<>();
            for (String attributeId: productReq.getAttributeDetails()){
                AttributeEntity attribute = attributeService.findById(attributeId);
                if(attribute == null)
                    new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Attribute is Not Found",null), HttpStatus.NOT_FOUND);
                else
                    listAttribute.add(attribute);
            }
            ProductEntity product = ProductMapping.addJsonProductToEntity(productReq,category,brand,listAttributeOption,listAttribute);
            productService.saveProduct(product);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Add Product Successfully",null), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/product/count/1")
    public ResponseEntity<SuccessResponse> getCountProductaa(){
        Map<String, Object> data = new HashMap<>();
        List<Object> countProduct = productService.getTotalByCategory();
        List<Map<String,Object>> listCategoryPrice = new ArrayList<>();
        for(Object a : countProduct){
            boolean stop = false;
            for(Map<String,Object> b: listCategoryPrice){
                if(b.get("name").equals(((Object[])a)[0].toString())){
                    double temp = (double) b.get("value");
                    b.put("value",temp + Double.parseDouble(((Object[])a)[1].toString()));
                    stop = true;
                    break;
                }
            }
            if(!stop){
                Map<String,Object> map = new HashMap<>();
                map.put("name",((Object[])a)[0].toString());
                map.put("value",Double.parseDouble(((Object[])a)[1].toString()));
                listCategoryPrice.add(map);
            }
        }
        data.put("categoryPrice",listCategoryPrice);
        return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(),"Category Price",data),HttpStatus.OK);
    }
    @GetMapping("/product/count")
    public ResponseEntity<SuccessResponse> getCountProduct(){
        Map<String, Object> data = new HashMap<>();
        long countProduct = productService.countProduct();
        data.put("countProduct",countProduct);
        return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(),"Count Product",data),HttpStatus.OK);
    }
    @PutMapping("/manager/product/update/{id}")
    public ResponseEntity<SuccessResponse> updateProduct(HttpServletRequest request, @PathVariable UUID id, @RequestBody UpdateProductRequest updateProductRequest){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            ProductEntity product = productService.findById(id);
            if(product == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Product is Not Found",null), HttpStatus.NOT_FOUND);
            BrandEntity brand = brandService.findById(updateProductRequest.getBrand());
            if(brand == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Brand is Not Found",null), HttpStatus.NOT_FOUND);
            CategoryEntity category = categoryService.findById(updateProductRequest.getCategory());
            if(category == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Category is Not Found",null), HttpStatus.NOT_FOUND);
            product = ProductMapping.updateProduct(product,updateProductRequest,brand,category);
            productService.saveProduct(product);
            Map<String, Object> data = new HashMap<>();
            data.put("product",product);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(), "Update Product Successfully",data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @DeleteMapping("/manager/product/delete/{id}")
    public ResponseEntity<SuccessResponse> deleteProductById(HttpServletRequest request,@PathVariable UUID id){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            ProductEntity product = productService.findById(id);
            if(product == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Product is Not Found",null), HttpStatus.NOT_FOUND);
            product.setStatus(0);
            productService.saveProduct(product);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Product is deleted",null), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PostMapping("/manager/product/addAttribute/{id}")
    public ResponseEntity<SuccessResponse> addAttribute(HttpServletRequest request,@PathVariable UUID id,@RequestBody List<String> listAttributeOptions){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            ProductEntity product = productService.findById(id);
            if(product == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Product is Not Found",null), HttpStatus.NOT_FOUND);
            for ( String idOption : listAttributeOptions)
                productService.addAttribute(product,idOption);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Add Options Product is Successfully",null), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @DeleteMapping("/manager/product/deleteOption/{id}")
    public ResponseEntity<SuccessResponse> deleteAttribute(HttpServletRequest request,@PathVariable UUID id,@RequestBody List<String> listAttribute){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            ProductEntity product = productService.findById(id);
            if(product == null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Product is Not Found",null), HttpStatus.NOT_FOUND);
            for (String idOption : listAttribute)
                productService.deleteAttribute(product,idOption);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(), "List attribute was deleted", null), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PostMapping(value = "/manager/product/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<SuccessResponse> uploadListImgProduct(HttpServletRequest request,@RequestPart(required = true) List<MultipartFile> multipleFiles){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            List<String> urls = new ArrayList<>();
            for (MultipartFile file : multipleFiles)
                if(!imageStorageService.isImageFile(file))
                    return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),"The file is not an image",null), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            for (MultipartFile file : multipleFiles){
                String url = imageStorageService.saveImgProduct(file,String.valueOf(UUID.randomUUID()));
                urls.add(url);
                if(url.equals(""))
                    return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Upload Image Failure",null), HttpStatus.NOT_FOUND);
            }
            Map<String, Object> data = new HashMap<>();
            data.put("imgUrl",urls);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Save image Successfully",data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PostMapping(value = "/manager/product/uploadImg")
    public ResponseEntity<SuccessResponse> uploadImgProduct(HttpServletRequest request,@RequestPart(required = true) MultipartFile file){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            if(!imageStorageService.isImageFile(file))
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),"The file is not an image",null), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            UUID uuid = UUID.randomUUID();
            LocalDate date = LocalDate.now();
            String url = imageStorageService.saveProductImg(file, date.toString()+"/"+String.valueOf(uuid));
            if(url.equals(""))
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Upload Image Failure",null), HttpStatus.NOT_FOUND);
            Map<String, Object> data = new HashMap<>();
            data.put("url",url);
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(), "Upload Logo Successfully",data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    private List<String> listProSort(){
        List<String> list = new ArrayList<>();
        list.add("product_id");
        list.add("product_price_up");
        list.add("product_price_down");
        list.add("product_sell_amount");
        list.add("create_at");
        list.add("product_discount");
        return list;
    }
}
