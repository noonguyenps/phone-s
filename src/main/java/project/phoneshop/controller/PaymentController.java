package project.phoneshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.phoneshop.mapping.PaymentMapping;
import project.phoneshop.model.entity.PaymentEntity;
import project.phoneshop.model.payload.request.payment.AddPaymentRequest;
import project.phoneshop.model.payload.response.SuccessResponse;
import project.phoneshop.service.PaymentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    PaymentMapping paymentMapping;

    @GetMapping("/list")
    public ResponseEntity<SuccessResponse> getAllPayment(){
        List<PaymentEntity> list = paymentService.getAll();
        Map<String,Object> data = new HashMap<>();
        data.put("listPayment",list);
        return new ResponseEntity<>(new SuccessResponse(true,HttpStatus.OK.value(),"List Payment",data),HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<SuccessResponse> getPaymentById(@PathVariable("id")int id){
        PaymentEntity payment = paymentService.getPaymentById(id);
        SuccessResponse response=new SuccessResponse();
        if(payment==null){
            response.setMessage("Payment not found");
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        }
        else{
            response.setMessage("Successful");
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK.value());
            response.getData().put("Payment " + payment.getPaymentId(),payment.getPaymentName());
            return new ResponseEntity<>(response, HttpStatus.OK);

        }
    }

    @PostMapping("/create")
    public ResponseEntity<SuccessResponse> createNotification(@RequestBody AddPaymentRequest request){


        PaymentEntity payment = paymentMapping.modelToEntity(request);
        SuccessResponse response = new SuccessResponse();
        try {
            paymentService.create(payment);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("add payment successful");
            response.setSuccess(true);
            response.getData().put("Payment",payment.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse> updatePayment(@RequestBody AddPaymentRequest request,@PathVariable("id") int id){


        PaymentEntity payment = paymentMapping.updateToEntity(request,id);
        SuccessResponse response = new SuccessResponse();
        try {
            paymentService.update(payment);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("update payment successful");
            response.setSuccess(true);
            response.getData().put("Payment",payment.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deletePayment(@PathVariable("id")int id) throws Exception{
        SuccessResponse response = new SuccessResponse();
        try {
            paymentService.delete(id);
            response.setMessage("Delete payment success");
            response.setStatus(HttpStatus.OK.value());
            response.setSuccess(true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            throw new Exception(e.getMessage() + "\nDelete Payment fail");
        }
    }
}
