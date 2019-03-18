package ru.golitsyn.yandex.dao.jdbc;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.util.ObjectUtils;
import ru.golitsyn.yandex.dao.PaymentDataDAO;
import ru.golitsyn.yandex.dao.model.PaymentData;

/**
 * Created by Sergey Golitsyn (deft) on 17.03.2019
 */
public class JdbcTemplatePaymentDaoImpl implements PaymentDataDAO {

  private static final String QUERY_SAVE = "insert into payment.payment_data (id, sender_id, receiver_id, amount) VALUES (?,?,?,?)";
  private static final String SELECT_TOP_TEN_RECEIVER = "SELECT * FROM top_ten_amount_receiver()";
  private static final String SELECT_TOP_TEN_RECEIVER_WITH_COSTS = "SELECT * FROM amount_receivers()";
  private static final String TOTAL_AMOUNT_BY_SENDER_ID = "total_amount_by_sender_id";
  private static final String FIELD_SENDER_ID = "sender_id";
  private static final String RECEIVER_ID = "receiver_id";
  private static final String AMOUNT = "amount";

  private JdbcTemplate jdbcTemplate;

  @Override
  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * Метод для записи данных в бд, без разделения по разным базам
   *
   * @param paymentDataList - лист данных
   */
  @Override
  public void insertPaymentData(List<PaymentData> paymentDataList) {
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
  }

  /**
   * Инсерт записи в бд
   */
  @Override
  public void insertPaymentData(PaymentData paymentData, JdbcTemplate jdbcTemplate) {
    jdbcTemplate.update(
        QUERY_SAVE,
        paymentData.getId() == null ? UUID.randomUUID() : paymentData.getId()
        , paymentData.getSenderId()
        , paymentData.getReceiverId()
        , paymentData.getAmount()
    );
  }

  /**
   * @param senderId - id отправителя
   * @return общая сумма потраченных средств
   */
  @Override
  public BigDecimal getSpentTotalAmountBySenderId(UUID senderId) {
    SimpleJdbcCall jdbcCall = new
        SimpleJdbcCall(Objects.requireNonNull(jdbcTemplate.getDataSource()))
        .withFunctionName(TOTAL_AMOUNT_BY_SENDER_ID);

    SqlParameterSource in = new MapSqlParameterSource().addValue(FIELD_SENDER_ID, senderId);
    return jdbcCall.executeFunction(BigDecimal.class, in);
  }

  /**
   * Получение топ-10 получателей по сумме заработаных средств (не учитываю то что получатель
   * потратил) todo необходимо добавить функцию учитывающую потраченные средства
   *
   * Получаю и маплю таким способом, потому что на данный момент не нашел как обойти ошибку
   *
   * org.postgresql.util.PSQLException: Can't infer the SQL type to use for an instance of
   * ru.golitsyn.yandex.dao.util.TopReceiverMapper. Use setObject() with an explicit Types value to
   * specify the type to use.
   */
  @Override
  public List getTopTenReceivers(boolean isConsiderCost) {
    if (isConsiderCost) {
      return getTopTenReceiversWithCosts();
    } else {
      return getTopTenReceiversWithoutCosts();
    }
  }

  /**
   * Топ 10 получателей с учиетом отправленных средств
   *
   * Не успел к сожалению найти способ, что бы учитывать затраты непосредственно в БД, но я уверен
   * он есть
   *
   * @return пары
   */
  private List getTopTenReceiversWithCosts() {
    List<Map<String, Object>> receivers = jdbcTemplate
        .queryForList(SELECT_TOP_TEN_RECEIVER_WITH_COSTS);
    substractCosts(receivers);
    sortByAmount(receivers);
    return receivers.size() > 10 ? receivers.subList(0, 10) : receivers;
  }

  private void substractCosts(List<Map<String, Object>> receivers) {
    for (Map<String, Object> receiver : receivers) {
      BigDecimal spentTotalAmount = getSpentTotalAmountBySenderId(
          UUID.fromString(receiver.get(RECEIVER_ID).toString()));
      if (!ObjectUtils.isEmpty(spentTotalAmount)) {
        BigDecimal rez = new BigDecimal(receiver.get(AMOUNT).toString())
            .subtract(spentTotalAmount);
        receiver.put(AMOUNT, rez);
      }
    }
  }

  private void sortByAmount(List<Map<String, Object>> receivers) {
    receivers.sort((o1, o2) -> {
      BigDecimal values1 = (BigDecimal) o1.get(AMOUNT);
      BigDecimal values2 = (BigDecimal) o2.get(AMOUNT);
      if (!ObjectUtils.isEmpty(values1) && !ObjectUtils.isEmpty(values2)) {
        return values2.compareTo(values1);
      } else {
        return 0;
      }
    });
  }

  private List getTopTenReceiversWithoutCosts() {
    return jdbcTemplate
        .queryForList(SELECT_TOP_TEN_RECEIVER);
  }

}
