package com.project.hotelXpress.payment;

import com.project.hotelXpress.response.ResponseObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    
    @GetMapping("/vn-pay")
    public ResponseObject<PaymentDTO.VNPayResponse> pay(HttpServletRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createVnPayPayment(request));
    }
    
    @GetMapping("/vn-pay-callback")
    public void payCallbackHandler(HttpServletRequest request, HttpServletResponse response) {
        try {
            String status = request.getParameter("vnp_ResponseCode");
            String redirectUrl = "http://localhost:3000/profile?payment_status=" + (status.equals("00") ? "success" : "failed");
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            try {
                response.sendRedirect("http://localhost:3000/profile?payment_status=error");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}