package project.phoneshop.service;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project.phoneshop.model.entity.VoucherEntity;
import project.phoneshop.model.entity.UserEntity;

import java.util.List;
import java.util.UUID;

@Component
@Service
public interface VoucherService {

    VoucherEntity saveVoucher(VoucherEntity voucher);

    List<VoucherEntity> findAllVoucher();
    List<VoucherEntity> findAllVoucherPublic();

    List<VoucherEntity> foundVoucher(String type);

    VoucherEntity findById(UUID id);

    void deleteVoucher(UUID id);

    VoucherEntity findByIdAndUser(UUID id, UserEntity user);

    List<VoucherEntity> findAllVoucherBtUser(UserEntity user);
}
