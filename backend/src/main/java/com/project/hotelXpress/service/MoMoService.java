package com.project.hotelXpress.service;

import com.project.hotelXpress.client.MoMoApi;
import com.project.hotelXpress.models.CreateMoMoRequest;
import com.project.hotelXpress.models.CreateMoMoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MoMoService {
    @Value(value = "MOMO")
    private String PARTNER_CODE;
    @Value(value = "F8BBA842ECF85")
    private String ACCESS_KEY;
    @Value(value = "K951B6PE1waDMi640xX08PD3vg6EkVlz")
    private String SECRET_KEY;
    @Value(value = "http://localhost:3000/profile")
    private String REDIRECT_URL;
    @Value(value = "http://localhost:8080/api/momo/ipn-handler")
    private String IPN_URL;
    @Value(value = "captureWallet")
    private String REQUEST_TYPE;

    private final MoMoApi momoApi;

    public CreateMoMoResponse createQR(long amount) {
        String orderId = UUID.randomUUID().toString();
        String orderInfo = "Thanh toan don hang: " + orderId;
        String requestId = UUID.randomUUID().toString();
        String extraData = "Khong co khuyen mai";
        String rawSignature = String.format(
                "accessKey=%s&amount=%s&extraData=%s&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=%s",
                ACCESS_KEY, amount, extraData, IPN_URL, orderId, orderInfo, PARTNER_CODE, REDIRECT_URL, requestId, REQUEST_TYPE);
        String prettySignature = "";
        try {
            prettySignature = signHmacSHA256(rawSignature, SECRET_KEY);
        } catch (Exception e) {
            log.error("Co loi khi hash code");
            return null;
        }

        if (prettySignature.isBlank()) {
            log.error("Signature trong");
            return null;
        }

        CreateMoMoRequest request = CreateMoMoRequest.builder()
                .partnerCode(PARTNER_CODE)
                .requestType(REQUEST_TYPE)
                .ipnUrl(IPN_URL)
                .redirectUrl(REDIRECT_URL)
                .orderId(orderId)
                .orderInfo(orderInfo)
                .requestId(requestId)
                .extraData(extraData)
                .amount(amount)
                .signature(prettySignature)
                .lang("vi")
                .build();

        return momoApi.createMoMoQR(request);
    }

    private static String signHmacSHA256(String data, String key) throws Exception {
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSHA256.init(secretKey);
        byte[] hash = hmacSHA256.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
