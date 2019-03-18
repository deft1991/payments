package ru.golitsyn.yandex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.golitsyn.yandex.dao.jdbc.JdbcTemplatePaymentDaoImpl;
import ru.golitsyn.yandex.dao.model.PaymentData;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Created by Sergey Golitsyn (deft) on 17.03.2019
 */
@Service
public class PaymentDataService {

  private JdbcTemplatePaymentDaoImpl jdbcTemplatePaymentDao;

  private final DataSource dataSource1;
  private final DataSource dataSource2;
  private final DataSource dataSource3;

  @Autowired
  public PaymentDataService(DataSource dataSource1,
							@Qualifier("twoDb") DataSource dataSource2,
							@Qualifier("threeDb") DataSource dataSource3) {
	this.dataSource1 = dataSource1;
	this.dataSource2 = dataSource2;
	this.dataSource3 = dataSource3;
  }


  public boolean insertPaymentData(List<PaymentData> paymentDataList) {
	for (int i = 0; i < paymentDataList.size(); i++) {
	  if (i % 2 == 0) {
		getWriterThread(paymentDataList, i, dataSource1).start();
	  } else if (i % 3 == 0) {
		getWriterThread(paymentDataList, i, dataSource2).start();
	  } else {
		getWriterThread(paymentDataList, i, dataSource3).start();
	  }
	}
	return true;
  }

  private Thread getWriterThread(List<PaymentData> paymentDataList, int finalI, DataSource dataSource1) {
	return new Thread(() -> {
	  jdbcTemplatePaymentDao = new JdbcTemplatePaymentDaoImpl();
	  jdbcTemplatePaymentDao.setDataSource(dataSource1);
	  jdbcTemplatePaymentDao.insertPaymentData(paymentDataList.get(finalI));
	});
  }

  public BigDecimal getSpentTotalAmountBySenderId(UUID senderId) {
	jdbcTemplatePaymentDao = new JdbcTemplatePaymentDaoImpl();
	jdbcTemplatePaymentDao.setDataSource(dataSource1);
	return jdbcTemplatePaymentDao.getSpentTotalAmountBySenderId(senderId);
  }

  public List getTopTenRecievers() {
	jdbcTemplatePaymentDao = new JdbcTemplatePaymentDaoImpl();
	jdbcTemplatePaymentDao.setDataSource(dataSource1);
	return jdbcTemplatePaymentDao.getTopTenReceivers();
  }
}
