package ru.golitsyn.yandex.dao.model.object_types;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

/**
 * Created by Sergey Golitsyn (deft) on 18.03.2019
 */
@Data
public class TopReceiver {

  private UUID id;
  private BigDecimal amount;
}
