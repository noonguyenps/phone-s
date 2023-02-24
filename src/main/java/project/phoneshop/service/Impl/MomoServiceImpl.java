package project.phoneshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.phoneshop.model.entity.CartEntity;
import project.phoneshop.model.entity.OrderEntity;
import project.phoneshop.mservice.config.Environment;
import project.phoneshop.mservice.enums.RequestType;
import project.phoneshop.mservice.models.PaymentResponse;
import project.phoneshop.mservice.models.QueryStatusTransactionResponse;
import project.phoneshop.mservice.processor.CreateOrderMoMo;
import project.phoneshop.mservice.processor.QueryTransactionStatus;
import project.phoneshop.mservice.shared.utils.LogUtils;
import project.phoneshop.service.MomoService;
@Service
@Transactional
@RequiredArgsConstructor
public class MomoServiceImpl implements MomoService {
    @Override
    public String createMomoPayment(OrderEntity orderEntity) throws Exception {
        LogUtils.init();
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(4720000 +orderEntity.getOrderId());
        Long transId = 2L;
        long amount = (long)orderEntity.getTotal();

        String partnerClientId = "partnerClientId";
        String orderInfo = "Đơn hàng : ";
        for(CartEntity cart : orderEntity.getCartOrder()){
            orderInfo += " - " +cart.getProductCart().getName()+" ";
        }
        String returnURL = "https://phone-s.herokuapp.com/api/order/momo/pay";
        String notifyURL = "https://phone-s.herokuapp.com/api/order/momo/pay";
        String callbackToken = "callbackToken";
        String token = "";

        Environment environment = Environment.selectEnv("dev");
        PaymentResponse captureWalletMoMoResponse = CreateOrderMoMo.process(environment, orderId, requestId, Long.toString(amount), orderInfo, returnURL, notifyURL, "", RequestType.CAPTURE_WALLET, Boolean.TRUE);
        return captureWalletMoMoResponse.getPayUrl();
    }
    @Override
    public String createMomoATMPayment(OrderEntity orderEntity) throws Exception {
        LogUtils.init();
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(4720000 +orderEntity.getOrderId());
        Long transId = 2L;
        long amount = (long)orderEntity.getTotal();

        String partnerClientId = "partnerClientId";
        String orderInfo = "Đơn hàng : ";
        for(CartEntity cart : orderEntity.getCartOrder()){
            orderInfo += " - " +cart.getProductCart().getName()+" ";
        }
        String returnURL = "https://phone-s.herokuapp.com/api/order/momo/pay";
        String notifyURL = "https://phone-s.herokuapp.com/api/order/momo/pay";
        String callbackToken = "callbackToken";
        String token = "";

        Environment environment = Environment.selectEnv("dev");
        PaymentResponse captureATMMoMoResponse = CreateOrderMoMo.process(environment,orderId, requestId, Long.toString(amount), orderInfo, returnURL, notifyURL, "", RequestType.PAY_WITH_ATM, null);
        return captureATMMoMoResponse.getPayUrl();
    }
    @Override
    public int getResultCode(int orderId, String requestId) throws Exception {
        LogUtils.init();
        Environment environment = Environment.selectEnv("dev");
        QueryStatusTransactionResponse queryStatusTransactionResponse = QueryTransactionStatus.process(environment, "19110472000025", requestId);
        return queryStatusTransactionResponse.getResultCode();
    }
}
