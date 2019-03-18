package ru.golitsyn.yandex.dao.jdbc;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import ru.golitsyn.yandex.dao.PaymentDataDAO;
import ru.golitsyn.yandex.dao.model.PaymentData;
import ru.golitsyn.yandex.dao.util.TopReceiverMapper;

/**
 * Created by Sergey Golitsyn (deft) on 17.03.2019
 */
public class JdbcTemplatePaymentDaoImpl implements PaymentDataDAO {

  private static final String QUERY_SAVE = "insert into payment.payment_data (id, sender_id, receiver_id, amount) VALUES (?,?,?,?)";

  private JdbcTemplate jdbcTemplate;
  private DataSource dataSource;

  @Override
  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public boolean insertPaymentData(List<PaymentData> paymentDataList) {
    final int batchSize = 500;

    for (int j = 0; j < paymentDataList.size(); j += batchSize) {

      final List<PaymentData> batchList = paymentDataList.subList(j,
          j + batchSize > paymentDataList.size() ? paymentDataList.size() : j + batchSize);

      jdbcTemplate.batchUpdate(QUERY_SAVE,
          new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i)
                throws SQLException {
              PaymentData employee = batchList.get(i);
              ps.setString(1, employee.getId().toString());
              ps.setString(2, employee.getSenderId().toString());
              ps.setString(3, employee.getReceiverId().toString());
              ps.setLong(4, employee.getAmount());
            }

            @Override
            public int getBatchSize() {
              return batchList.size();
            }
          });

    }
    return true;
  }

  @Override
  public boolean insertPaymentData(PaymentData paymentDataList) {
    jdbcTemplate.update(
        QUERY_SAVE,
        paymentDataList.getId()
        , paymentDataList.getSenderId()
        , paymentDataList.getReceiverId()
        , paymentDataList.getAmount()
    );
    return true;
  }

  @Override
  public BigDecimal getSpentTotalAmountBySenderId(UUID senderId) {
    SimpleJdbcCall jdbcCall = new
        SimpleJdbcCall(dataSource).withFunctionName("total_amount_by_sender_id");

    SqlParameterSource in = new MapSqlParameterSource().addValue("sender_id", senderId);

    return jdbcCall.executeFunction(BigDecimal.class, in);
  }

  /**
   * Получаю и маплю таким способом, потому что на данный момент не нашел как обойти ошибку
   *
   * org.postgresql.util.PSQLException: Can't infer the SQL type to use for an instance of
   * ru.golitsyn.yandex.dao.util.TopReceiverMapper. Use setObject() with an explicit Types value to
   * specify the type to use.
   */
  @Override
  public List getTopTenReceivers() {

    List<Map<String, Object>> maps = jdbcTemplate
        .queryForList("SELECT * FROM top_ten_amount_receiver()");
    return maps;
  }

}
