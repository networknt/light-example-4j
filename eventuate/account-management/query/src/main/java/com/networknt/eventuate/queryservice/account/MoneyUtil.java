package com.networknt.eventuate.queryservice.account;

import java.math.BigDecimal;

public class MoneyUtil {

  public static long toIntegerRepr(BigDecimal d) {
    return d.multiply(new BigDecimal(100)).longValueExact();
  }

  public static long toLongRepr(BigDecimal d) {
    if (d!=null) {
      return d.longValue();
    } else {
      return 0;
    }
  }


}
