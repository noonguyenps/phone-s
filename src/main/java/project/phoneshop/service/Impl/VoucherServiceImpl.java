package project.phoneshop.service.Impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.phoneshop.model.entity.VoucherEntity;
import project.phoneshop.model.entity.UserEntity;
import project.phoneshop.repository.VoucherRepository;
import project.phoneshop.service.VoucherService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;

    @Override
    public List<VoucherEntity> findAllVoucher() {
        List<VoucherEntity> voucherEntityList = voucherRepository.findAll();
        return voucherEntityList;
    }

    @Override
    public VoucherEntity saveVoucher(VoucherEntity voucher) { return voucherRepository.save(voucher); }

    @Override
    public List<VoucherEntity> foundVoucher(String type) {
        return voucherRepository.findAllByType(type);
    }

    @Override
    public VoucherEntity findById(UUID id) {
        Optional<VoucherEntity> voucherEntity = voucherRepository.findById(id);
        if(voucherEntity.isEmpty()) {
            return null;
        }
        return voucherEntity.get();
    }

    @Override
    public void deleteVoucher(UUID id) { voucherRepository.deleteById(id);}

    @Override
    public VoucherEntity findByIdAndUser(UUID id, UserEntity user) {
        Optional<VoucherEntity> voucherEntity = voucherRepository.findByIdAndUserEntities(id,user);
        if(voucherEntity.isEmpty()) {
            return null;
        }
        return voucherEntity.get();
    }
    @Override
    public List<VoucherEntity> findAllVoucherPublic(){
        List<VoucherEntity> voucherEntityList = voucherRepository.findAllByStatus(true);
        return voucherEntityList;
    }
    @Override
    public List<VoucherEntity> findAllVoucherBtUser(UserEntity user){
        return  voucherRepository.findByUserEntities(user);
    }
}

