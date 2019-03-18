package ru.golitsyn.yandex.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.golitsyn.yandex.dao.model.PaymentData;

/**
 * Created by Sergey Golitsyn (deft) on 17.03.2019
 */
public interface PaymentDataDAO {

  void setJdbcTemplate(JdbcTemplate jdbcTemplate);

  void insertPaymentData(List<PaymentData> paymentDataList);

  void insertPaymentData(PaymentData paymentDataList, JdbcTemplate jdbcTemplate);

  BigDecimal getSpentTotalAmountBySenderId(UUID senderId);

  List getTopTenReceivers();

}
