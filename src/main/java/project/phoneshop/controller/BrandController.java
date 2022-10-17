package project.phoneshop.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.phoneshop.handler.AuthorizationHeader;
import project.phoneshop.mapping.BrandMapping;
import project.phoneshop.model.entity.BrandEntity;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.model.payload.request.brand.AddNewBrandRequest;
import project.phoneshop.model.payload.request.brand.UpdateBrandRequest;
import project.phoneshop.model.payload.response.SuccessResponse;
import project.phoneshop.service.AddressService;
import project.phoneshop.service.BrandService;
import project.phoneshop.service.ImageStorageService;
import project.phoneshop.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;
    private final ImageStorageService imageStorageService;
    private final AddressService addressService;
    private final UserService userService;
    @Autowired
    AuthorizationHeader authorizationHeader;
    @GetMapping("brand/{id}")
    public ResponseEntity<SuccessResponse> getBrandByID(@PathVariable UUID id){
        BrandEntity brand = brandService.findById(id);
        if(brand == null)
            return new ResponseEntity<>(new SuccessResponse(false, HttpStatus.NOT_FOUND.value(),"Brand Not found", null), HttpStatus.NOT_FOUND);
        Map<String, Object> data = new HashMap<>();
        data.put("Brand", brand);
        return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Query Brand Successfully",data), HttpStatus.OK);
    }
    //CRUD Brand Here
    @GetMapping("/admin/brand/all")
    public ResponseEntity<SuccessResponse> getAllBrand(HttpServletRequest request,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            List<BrandEntity> brandEntityList = brandService.findAll(page,size);
            if(brandEntityList.size() == 0)
                return new ResponseEntity<>(new SuccessResponse(false, HttpStatus.FOUND.value(),"List Brand is Empty",null), HttpStatus.NOT_FOUND);
            Map<String,Object> data = new HashMap<>();
            data.put("listBrand", brandEntityList);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Query Brand Successfully",data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/admin/brand/{id}")
    public ResponseEntity<SuccessResponse> getBrandByID(HttpServletRequest request,
                                                        @PathVariable UUID id){

        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            BrandEntity brand = brandService.findById(id);
            if(brand == null)
                return new ResponseEntity<>(new SuccessResponse(false, HttpStatus.NOT_FOUND.value(),"Brand Not found", null), HttpStatus.NOT_FOUND);
            Map<String, Object> data = new HashMap<>();
            data.put("brand", brand);
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Query Brand Successfully",data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PutMapping("/admin/brand/update/{id}")
    public ResponseEntity<SuccessResponse> updateBrand(HttpServletRequest request,
                                                       @PathVariable UUID id,
                                                       @RequestBody UpdateBrandRequest updateBrandRequest){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            BrandEntity brand = brandService.findById(id);
            if(brand == null)
                return new ResponseEntity<>(new SuccessResponse(false, HttpStatus.NOT_FOUND.value(),"Brand Not found", null), HttpStatus.NOT_FOUND);
            brand = BrandMapping.updateBrandToEntity(brand,updateBrandRequest);
            brandService.saveBrand(brand);
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(),"Update Brand Successfully",null), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PostMapping("/admin/brand/uploadLogo")
    public ResponseEntity<SuccessResponse> uploadImgLogo(HttpServletRequest request,
                                                         @RequestPart MultipartFile file) throws Exception{
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            if(!imageStorageService.isImageFile(file))
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),"The file is not an image",null), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            UUID uuid = UUID.randomUUID();
            String url = imageStorageService.saveLogo(file, String.valueOf(uuid));
            if(url.equals(""))
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Upload Logo Failure",null), HttpStatus.NOT_FOUND);
            Map<String, Object> data = new HashMap<>();
            data.put("url",url);
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(), "Upload Logo Successfully",data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PostMapping("/admin/brand/uploadLogo/{id}")
    public ResponseEntity<SuccessResponse> uploadImgLogoByID(HttpServletRequest request,@PathVariable UUID id,
                                                         @RequestPart MultipartFile file) throws Exception{
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            if(!imageStorageService.isImageFile(file))
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),"The file is not an image",null), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            BrandEntity brand = brandService.findById(id);
            if(brand==null)
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(), "Brand was not found",null),HttpStatus.NOT_FOUND);
            String url = imageStorageService.saveLogo(file, String.valueOf(brand.getId()));
            if(url.equals(""))
                return new ResponseEntity<>(new SuccessResponse(false,HttpStatus.NOT_FOUND.value(),"Upload Logo Failure",null), HttpStatus.NOT_FOUND);
            Map<String, Object> data = new HashMap<>();
            data.put("url",url);
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(), "Upload Logo Successfully",data), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @PostMapping(value = "/admin/brand/insert")
    public ResponseEntity<SuccessResponse> insertBrandNew(HttpServletRequest request,
                                                          @RequestBody AddNewBrandRequest addNewBrandRequest){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            BrandEntity brand = BrandMapping.addBrandToEntity(addNewBrandRequest);
            brandService.saveBrand(brand);
            return new ResponseEntity<>(new SuccessResponse(true, HttpStatus.OK.value(),"Insert Brand Successfully",null ),HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @DeleteMapping("/admin/brand/delete/{id}")
    public ResponseEntity<SuccessResponse> deleteBrandById(HttpServletRequest request,
                                                           @PathVariable UUID id){
        UserEntity user = authorizationHeader.AuthorizationHeader(request);
        if(user != null){
            BrandEntity brand = brandService.findById(id);
            if(brand == null)
                return new ResponseEntity<>(new SuccessResponse(false, HttpStatus.NOT_FOUND.value(),"Brand Not found",null ), HttpStatus.NOT_FOUND);
            try {
                brandService.deleteBrand(id);
            } catch (Exception e) {
                return new ResponseEntity<>(new SuccessResponse(false, HttpStatus.NOT_ACCEPTABLE.value(),"List product in Brand not Empty", null), HttpStatus.NOT_ACCEPTABLE);
            }
            return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"Brand is deleted",null), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
