package ru.golitsyn.yandex.dao.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;
import ru.golitsyn.yandex.dao.model.PaymentData;

/**
 * Created by Sergey Golitsyn (deft) on 18.03.2019
 */
public class PaymentDataMapper implements RowMapper {

  private final static String ID = "id";
  private final static String SENDER_ID = "sender_id";
  private final static String RECEIVER_ID = "receiver_id";
  private final static String AMOUNT = "amount";

  @Override
  public PaymentData mapRow(ResultSet rs, int rowNum) throws SQLException {
    PaymentData paymentData = new PaymentData();
    paymentData.setId(UUID.fromString(rs.getString(ID)));
    paymentData.setSenderId(UUID.fromString(rs.getString(SENDER_ID)));
    paymentData.setReceiverId(UUID.fromString(rs.getString(RECEIVER_ID)));
    paymentData.setAmount(rs.getLong(AMOUNT));
    return paymentData;
  }
}
