package project.phoneshop.mapping;
import project.phoneshop.model.entity.AddressEntity;
import project.phoneshop.model.entity.AddressTypeEntity;
import project.phoneshop.model.payload.request.address.AddNewAddressRequest;
import project.phoneshop.model.payload.request.address.InfoAddressRequest;

public class AddressMapping {

    public static AddressEntity ModelToEntity(AddNewAddressRequest addNewAddressRequest, AddressTypeEntity addressType) {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setFullName(addNewAddressRequest.getFullName());
        addressEntity.setCompanyName(addNewAddressRequest.getCompanyName());
        addressEntity.setPhoneNumber(addNewAddressRequest.getPhone());
        addressEntity.setAddressDetail(addNewAddressRequest.getAddressDetail());
        addressEntity.setCommune(addNewAddressRequest.getCommune());
        addressEntity.setProvince(addNewAddressRequest.getProvince());
        addressEntity.setDistrict(addNewAddressRequest.getDistrict());
        addressEntity.setAddressType(addressType);
        return addressEntity;
    }
    public static AddressEntity UpdateAddressEntity(AddressEntity address, InfoAddressRequest addressInfo, AddressTypeEntity addressType)
    {
        address.setFullName(addressInfo.getFullName());
        address.setPhoneNumber(addressInfo.getPhone());
        address.setAddressDetail(addressInfo.getAddressDetail());
        address.setCompanyName(addressInfo.getCompanyName());
        address.setCommune(addressInfo.getCommune());
        address.setProvince(addressInfo.getProvince());
        address.setDistrict(addressInfo.getDistrict());
        address.setAddressType(addressType);
        return address;
    }

}
