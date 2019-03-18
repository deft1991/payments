package ru.golitsyn.yandex.dao.model;

import lombok.Data;

import java.util.UUID;

/**
 * Created by Sergey Golitsyn (deft) on 17.03.2019
 */
@Data
public class PaymentData {
  UUID id;
  UUID senderId;
  UUID receiverId;
  Long amount;

}
