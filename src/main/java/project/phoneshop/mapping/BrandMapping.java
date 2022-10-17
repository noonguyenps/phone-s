package project.phoneshop.mapping;

import project.phoneshop.model.entity.BrandEntity;
import project.phoneshop.model.payload.request.brand.AddNewBrandRequest;
import project.phoneshop.model.payload.request.brand.UpdateBrandRequest;

import java.time.LocalDate;
import java.util.UUID;

public class BrandMapping {
    public static BrandEntity addBrandToEntity(AddNewBrandRequest addNewBrand) {
        BrandEntity brand = new BrandEntity();
        brand.setId(getIdFromURL(addNewBrand.getImg()));
        brand.setBrandCountry(addNewBrand.getCountry());
        brand.setBrandProvince(addNewBrand.getProvince());
        brand.setBrandDistrict(addNewBrand.getDistrict());
        brand.setBrandCommune(addNewBrand.getCommune());
        brand.setName(addNewBrand.getName());
        brand.setPhone(addNewBrand.getPhone());
        brand.setDescription(addNewBrand.getDescription());
        brand.setYearCreate(LocalDate.now().getYear());
        brand.setImg(addNewBrand.getImg());
        brand.setAddressDetails(addNewBrand.getAddressDetails());
        return brand;
    }
    public static BrandEntity updateBrandToEntity(BrandEntity brand, UpdateBrandRequest updateBrandRequest) {
        brand.setBrandCountry(updateBrandRequest.getCountry());
        brand.setBrandProvince(updateBrandRequest.getProvince());
        brand.setBrandDistrict(updateBrandRequest.getDistrict());
        brand.setBrandCommune(updateBrandRequest.getCommune());
        brand.setName(updateBrandRequest.getName());
        brand.setPhone(updateBrandRequest.getPhone());
        brand.setDescription(updateBrandRequest.getDescription());
        brand.setYearCreate(LocalDate.now().getYear());
        brand.setAddressDetails(updateBrandRequest.getAddressDetails());
        brand.setImg(updateBrandRequest.getImg());
        return brand;
    }
    public static UUID getIdFromURL(String url){
        String[] arrOfStr = url.split("/");
        String[] publicId = arrOfStr[arrOfStr.length-1].split("\\.(?=[^\\.]+$)");
        return UUID.fromString(publicId[0]);
    }
}
