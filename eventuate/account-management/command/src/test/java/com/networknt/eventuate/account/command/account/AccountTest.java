package com.networknt.eventuate.account.command.account;


import com.networknt.eventuate.account.common.event.account.AccountOpenedEvent;
import com.networknt.eventuate.common.Event;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class AccountTest {

  @Test
  public void testInitial() {
    Account account = new Account();
    String title = "My Account";
    String customerId = "00000000-00000000";
    BigDecimal initialBalance = new BigDecimal(512);

    List<Event> events = account.process(new OpenAccountCommand(customerId, title, initialBalance, ""));

    Assert.assertEquals(1, events.size());
    Assert.assertEquals(AccountOpenedEvent.class, events.get(0).getClass());

    account.applyEvent(events.get(0));
    Assert.assertEquals(initialBalance, account.getBalance());
  }
}
