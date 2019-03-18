package ru.golitsyn.yandex.dao.util;

import org.springframework.jdbc.core.RowMapper;
import ru.golitsyn.yandex.dao.model.PaymentData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Sergey Golitsyn (deft) on 17.03.2019
 */
public class PaymentDataMapper implements RowMapper {

  @Override
  public PaymentData mapRow(ResultSet rs, int rowNum) throws SQLException {
	PaymentData paymentData = new PaymentData();
	paymentData.setId(UUID.fromString(rs.getString("id")));
	paymentData.setSenderId(UUID.fromString(rs.getString("sender_id")));
	paymentData.setReceiverId(UUID.fromString(rs.getString("receiver_id")));
	paymentData.setAmount(rs.getLong("amount"));
	return paymentData;
  }
}
