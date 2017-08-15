package com.networknt.eventuate.queryservice.account;


import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;


public class MoneyUtilTest {


    @BeforeClass
    public static void setUp() {


    }

    @Test
    public void testBigDecimal() {

        BigDecimal balance = new BigDecimal("1000.20");
        System.out.println("result:" + MoneyUtil.toIntegerRepr(balance));
        System.out.println(balance.longValue());

    }


}
