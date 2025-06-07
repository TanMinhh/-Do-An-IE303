package com.project.hotelXpress.client;

import com.project.hotelXpress.models.CreateMoMoRequest;
import com.project.hotelXpress.models.CreateMoMoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "momo", url = "${DEV_MOMO_ENDPOINT}")
public interface MoMoApi {
    @PostMapping("/create")
    CreateMoMoResponse createMoMoQR(@RequestBody CreateMoMoRequest createMoMoRequest);
}