package com.project.hotelXpress.models;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMoMoRequest {
    private String partnerCode;
    private String ipnUrl;
    private String requestType;
    private long amount;
    private String orderId;
    private String orderInfo;
    private String requestId;
    private String redirectUrl;
    private String lang;
    private String extraData;
    private String signature;
}
