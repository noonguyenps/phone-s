package project.phoneshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.phoneshop.model.entity.AddressEntity;
import project.phoneshop.model.entity.AddressTypeEntity;
import project.phoneshop.repository.AddressRepository;
import project.phoneshop.repository.AddressTypeRepository;
import project.phoneshop.service.AddressService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    final AddressRepository addressRepository;
    final AddressTypeRepository addressTypeRepository;

    @Override
    public AddressEntity findById(UUID id) {
        Optional<AddressEntity> addressEntity= addressRepository.findById(id);
        if (addressEntity==null)
            return null;
        return addressEntity.get();
    }
    @Override
    public List<AddressEntity> getAll() {
        return addressRepository.findAll();
    }

    @Override
    public AddressEntity saveAddress(AddressEntity address) {
        return addressRepository.save(address);
    }
    @Override
    public AddressTypeEntity findByTid(int id) {
        Optional<AddressTypeEntity> addressType = addressTypeRepository.findById(id);
        if (addressType == null)
            return null;
        return addressType.get();
    }
    @Override
    public void deleteAddress(UUID id) {
        addressRepository.deleteById(id);
    }
}
