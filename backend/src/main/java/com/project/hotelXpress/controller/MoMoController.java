package com.project.hotelXpress.controller;

import com.project.hotelXpress.constants.MoMoParameter;
import com.project.hotelXpress.models.CreateMoMoResponse;
import com.project.hotelXpress.service.MoMoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/momo")
public class MoMoController {

    private final MoMoService momoService;

    @PostMapping("create")
    public CreateMoMoResponse createMoMo(@RequestParam long amount) {
        return momoService.createQR(amount);
    }

    @GetMapping("ipn-handler")
    public String ipnHandler(@RequestParam Map<String, String> request) {
        Integer resultCode = Integer.valueOf(request.get(MoMoParameter.RESULT_CODE));
        return resultCode == 0 ? "Giao dich thanh cong" : "Giao dich that bai";
    }
}
