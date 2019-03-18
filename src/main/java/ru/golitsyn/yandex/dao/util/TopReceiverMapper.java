package ru.golitsyn.yandex.dao.util;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;
import ru.golitsyn.yandex.dao.model.object_types.TopReceiver;

/**
 * Created by Sergey Golitsyn (deft) on 18.03.2019
 */
public class TopReceiverMapper implements RowMapper {
  private final static String ID = "id";
  private final static String AMOUNT = "amount";

  @Override
  public TopReceiver mapRow(ResultSet rs, int rowNum) throws SQLException {
    TopReceiver topReceiver = new TopReceiver();

    topReceiver.setId(UUID.fromString(rs.getString(ID)));
    topReceiver.setAmount(BigDecimal.valueOf(rs.getLong(AMOUNT)));
    return topReceiver;
  }
}
