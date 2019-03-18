package ru.golitsyn.yandex.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.golitsyn.yandex.dao.jdbc.JdbcTemplatePaymentDaoImpl;
import ru.golitsyn.yandex.dao.model.PaymentData;

/**
 * Created by Sergey Golitsyn (deft) on 17.03.2019
 */
@Service
@RequiredArgsConstructor
public class PaymentDataService {

  private JdbcTemplatePaymentDaoImpl jdbcTemplatePaymentDao;

  private final JdbcTemplate oneJdbcTemplate;

  private final JdbcTemplate twoJdbcTemplate;

  private final JdbcTemplate threeJdbcTemplate;

  public boolean insertPaymentData(List<PaymentData> paymentDataList) {
    for (int i = 0; i < paymentDataList.size(); i++) {
      if (i % 2 == 0) {
        getWriterThread(paymentDataList, i, oneJdbcTemplate).start();
      } else if (i % 3 == 0) {
        getWriterThread(paymentDataList, i, twoJdbcTemplate).start();
      } else {
        getWriterThread(paymentDataList, i, threeJdbcTemplate).start();
      }
    }
    return true;
  }

  private Thread getWriterThread(List<PaymentData> paymentDataList, int finalI,
      JdbcTemplate jdbcTemplate) {
    return new Thread(() -> {
      jdbcTemplatePaymentDao = new JdbcTemplatePaymentDaoImpl();
      jdbcTemplatePaymentDao.setJdbcTemplate(jdbcTemplate);
      jdbcTemplatePaymentDao.insertPaymentData(paymentDataList.get(finalI), jdbcTemplate);
    });
  }

  public BigDecimal getSpentTotalAmountBySenderId(UUID senderId) {
    jdbcTemplatePaymentDao = new JdbcTemplatePaymentDaoImpl();
    jdbcTemplatePaymentDao.setJdbcTemplate(oneJdbcTemplate);
    return jdbcTemplatePaymentDao.getSpentTotalAmountBySenderId(senderId);
  }

  public List getTopTenReceivers(boolean isConsiderCost) {
    jdbcTemplatePaymentDao = new JdbcTemplatePaymentDaoImpl();
    jdbcTemplatePaymentDao.setJdbcTemplate(oneJdbcTemplate);
    return jdbcTemplatePaymentDao.getTopTenReceivers(isConsiderCost);
  }
}
