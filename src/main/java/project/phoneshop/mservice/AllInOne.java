package project.phoneshop.mservice;

import project.phoneshop.mservice.enums.*;
import project.phoneshop.mservice.models.*;
import project.phoneshop.mservice.processor.*;
import project.phoneshop.mservice.config.Environment;
import project.phoneshop.mservice.shared.utils.LogUtils;
/**
 * Demo
 */
public class AllInOne {

    /***
     * Select environment
     * You can load config from file
     * MoMo only provide once endpoint for each envs: dev and prod
     * @param args
     * @throws
     */

    public static void main(String... args) throws Exception {
        LogUtils.init();
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(System.currentTimeMillis());
        Long transId = 2L;
        long amount = 10000;

        String partnerClientId = "partnerClientId";
        String orderInfo = "Pay With MoMo";
        String returnURL = "https://google.com/";
        String notifyURL = "https://google.com/";
        String callbackToken = "callbackToken";
        String token = "";

        Environment environment = Environment.selectEnv("dev");


//      Remember to change the IDs at enviroment.properties file

        /***
         * create payment with capture momo wallet
         */
        //PaymentResponse captureWalletMoMoResponse = CreateOrderMoMo.process(environment, orderId, requestId, Long.toString(amount), orderInfo, returnURL, notifyURL, "", RequestType.CAPTURE_WALLET, Boolean.TRUE);

        /***
         * create payment with Momo's ATM type
         */

        orderId = String.valueOf(System.currentTimeMillis());
        requestId = String.valueOf(System.currentTimeMillis());
        //PaymentResponse captureATMMoMoResponse = CreateOrderMoMo.process(environment, orderId, requestId, Long.toString(amount), orderInfo, returnURL, notifyURL, "", RequestType.PAY_WITH_ATM, null);

        /***
         * create payment with Momo's Credit type
         */

        orderId = String.valueOf(System.currentTimeMillis());
        requestId = String.valueOf(System.currentTimeMillis());
        //PaymentResponse captureCreditMoMoResponse = CreateOrderMoMo.process(environment, orderId, requestId, Long.toString(amount), orderInfo, returnURL, notifyURL, "", RequestType.PAY_WITH_CREDIT, Boolean.TRUE);

        /***
         * confirm transaction with Momo's ATM type
         */

        orderId = String.valueOf(1);
        requestId = String.valueOf(System.currentTimeMillis());
        //ConfirmResponse confirmResponse = ConfirmTransaction.process(environment, orderId, requestId, Long.toString(amount), ConfirmRequestType.CAPTURE, "");

        /***
         * query transaction
         */
        requestId = String.valueOf("1677215289762");
        orderId = String.valueOf("1677215289762");
        QueryStatusTransactionResponse queryStatusTransactionResponse = QueryTransactionStatus.process(environment, orderId, requestId);

        transId = 2L;
        /***
         * refund transaction
         */
//        RefundMoMoResponse refundMoMoResponse = RefundTransaction.process(environment, orderId, requestId, Long.toString(amount), transId, "");

        /***
         * inquiry cbToken
         */
        partnerClientId = "sang.le@mservice.com.vn";
//        CbTokenInquiryResponse cbTokenInquiryResponse = InquiryCbToken.process(environment, orderId, requestId, partnerClientId);

        /***
         * Binding Token
         */
        partnerClientId = "sang.le@mservice.com.vn";
        callbackToken = "abc"; // received from notification or cbToken Inquiry API
//        BindingTokenResponse bindingTokenResponse = BindingToken.process(environment, orderId, requestId, partnerClientId, callbackToken);

        /***
         * Pay By Token
         */
        partnerClientId = "sang.le@mservice.com.vn";
        callbackToken = "abc"; // received from notification or cbToken Inquiry API
        token = "ajkhsajshajhsaj"; // Token after received from binding and decrypt AES then encrypt RSA
//        PaymentResponse paymentTokenResponse = PayByToken.process(environment, orderId, requestId, String.valueOf(amount), orderInfo, returnURL, notifyURL, "", partnerClientId, token, Boolean.TRUE);

        /***
         * inquiry cbToken
         */
        partnerClientId = "sang.le@mservice.com.vn";
        token = "ajkhsajshajhsaj"; // Token after received from binding and decrypt AES then encrypt RSA
//        DeleteTokenResponse deleteTokenResponse = DeleteToken.process(environment, orderId, requestId, partnerClientId, token);

    }

}
