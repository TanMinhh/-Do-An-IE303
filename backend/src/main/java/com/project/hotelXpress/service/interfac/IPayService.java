package com.project.hotelXpress.service.interfac;

import com.paypal.base.rest.PayPalRESTException;
import com.paypal.api.payments.Payment;

public interface IPayService {
    Payment createPaymentWithPayPal(Double total, String currency, String method, String intent, String description, String cancelUrl, String successUrl) throws PayPalRESTException;
    Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
}
