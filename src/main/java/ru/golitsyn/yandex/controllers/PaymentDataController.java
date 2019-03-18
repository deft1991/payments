package ru.golitsyn.yandex.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.golitsyn.yandex.dao.model.PaymentData;
import ru.golitsyn.yandex.services.PaymentDataService;

/**
 * Created by Sergey Golitsyn (deft) on 17.03.2019
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("payment-data")
public class PaymentDataController {

  private final PaymentDataService paymentDataService;

  @PostMapping
  public boolean insertPaymentData(@RequestBody List<PaymentData> paymentDataList) {
    return paymentDataService.insertPaymentData(paymentDataList);
  }

  @GetMapping("/top-amount/{senderId}")
  public BigDecimal getSpentTotalAmountBySenderId(@PathVariable("senderId") UUID senderId) {
    return paymentDataService.getSpentTotalAmountBySenderId(senderId);
  }

  @GetMapping("top-receivers")
  public List getTopTenReceivers(@RequestParam("isConsiderCost") boolean isConsiderCost) {
    return paymentDataService.getTopTenReceivers(isConsiderCost);
  }
}
