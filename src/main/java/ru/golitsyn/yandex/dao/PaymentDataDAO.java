package ru.golitsyn.yandex.dao;

import ru.golitsyn.yandex.dao.model.PaymentData;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Created by Sergey Golitsyn (deft) on 17.03.2019
 */
public interface PaymentDataDAO {
  public void setDataSource(DataSource dataSource);

  public boolean insertPaymentData(List<PaymentData> paymentDataList);

  public boolean insertPaymentData(PaymentData paymentDataList);

  public BigDecimal getSpentTotalAmountBySenderId(UUID senderId);

  public List getTopTenReceivers();

}
