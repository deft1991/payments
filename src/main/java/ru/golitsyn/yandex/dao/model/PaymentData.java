package ru.golitsyn.yandex.dao.model;

import java.util.UUID;
import lombok.Data;

/**
 * Created by Sergey Golitsyn (deft) on 17.03.2019
 */
@Data
public class PaymentData {

  private UUID id;
  private UUID senderId;
  private UUID receiverId;
  private Long amount;

}
