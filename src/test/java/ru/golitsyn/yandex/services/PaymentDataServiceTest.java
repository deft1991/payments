package ru.golitsyn.yandex.services;

import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.golitsyn.yandex.config.MultipleDBConfig;
import ru.golitsyn.yandex.dao.jdbc.JdbcTemplatePaymentDaoImpl;
import ru.golitsyn.yandex.dao.model.PaymentData;
import ru.golitsyn.yandex.dao.util.PaymentDataMapper;

/**
 * Created by Sergey Golitsyn (deft) on 18.03.2019
 */

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MultipleDBConfig.class)
public class PaymentDataServiceTest {

  @Autowired
  @Qualifier("testJdbcTemplate")
  private JdbcTemplate testJdbcTemplate;

  private JdbcTemplatePaymentDaoImpl jdbcTemplatePaymentDao;

  @Before
  public void setup() {
    jdbcTemplatePaymentDao = new JdbcTemplatePaymentDaoImpl();
    jdbcTemplatePaymentDao.setJdbcTemplate(testJdbcTemplate);
  }

  @Test
  public void insertPaymentData() {
//    JdbcTestUtils.deleteFromTables(testJdbcTemplate, "PAYMENT_DATA");

    PaymentData paymentData = new PaymentData();
    paymentData.setId(UUID.fromString("000002f4-48e2-11e9-8646-d663bd873d93"));
    paymentData.setSenderId(UUID.fromString("000002f4-48e2-11e9-8646-d663bd873d93"));
    paymentData.setReceiverId(UUID.fromString("000002f4-48e2-11e9-8646-d663bd873d93"));
    paymentData.setAmount(1L);

    jdbcTemplatePaymentDao.insertPaymentData(paymentData, testJdbcTemplate);

    String sql = "SELECT id, sender_id, receiver_id, amount FROM payment.payment_data WHERE ID=?";

    PaymentData paymentData1 = (PaymentData) testJdbcTemplate.queryForObject(
        sql, new Object[]{"000002f4-48e2-11e9-8646-d663bd873d93"}, new PaymentDataMapper());
    assert paymentData1 != null;
    Assert.assertEquals("000002f4-48e2-11e9-8646-d663bd873d93", paymentData1.getId().toString());
    Assert
        .assertEquals("000002f4-48e2-11e9-8646-d663bd873d93",
            paymentData1.getSenderId().toString());
    Assert.assertEquals("000002f4-48e2-11e9-8646-d663bd873d93",
        paymentData1.getReceiverId().toString());
    Assert.assertEquals(1L, (long) (paymentData1.getAmount()));
  }

  /**
   * todo нет способа протестить dblink в embedded data base =( прикрутить тестовую бд, под
   * тестывыми профилями
   */
  @Test
  public void getSpentTotalAmountBySenderId() {

  }

  /**
   * todo нет способа протестить dblink в embedded data base =( прикрутить тестовую бд, под
   * тестывыми профилями
   */
  @Test
  public void getTopTenReceivers() {

  }
}