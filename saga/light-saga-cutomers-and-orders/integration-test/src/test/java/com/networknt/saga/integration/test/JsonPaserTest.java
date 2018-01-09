package com.networknt.saga.integration.test;



import com.networknt.eventuate.common.impl.JSonMapper;
import com.networknt.example.sagas.ordersandcustomers.customer.command.ReserveCreditCommand;
import org.junit.Test;


public class JsonPaserTest {



  @Test
  public void test1() throws Exception {
    String str = "{\"orderId\":1,\"orderTotal\":{\"amount\":12.34},\"customerId\":0}";
    ReserveCreditCommand command = JSonMapper.fromJson(str, ReserveCreditCommand.class);
    System.out.println(command.getOrderTotal());

  }
}
