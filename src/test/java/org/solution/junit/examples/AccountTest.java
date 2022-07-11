package org.solution.junit.examples;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.solution.junit.examples.exceptions.InsufficientMoneyException;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class AccountTest {

    @DisplayName("Name: Andres expected")
    @RepeatedTest(value = 3)
    void testAccountName(RepetitionInfo info) {
        String espected;

        if(info.getCurrentRepetition() == 2){
            espected = "Gustavo";
        }else{
            espected = "Andres";
        }
        Account ac = new Account("Andres", new BigDecimal("1000.145"));

        espected = "Andres";

        String actual = ac.getPerson();
        //Use lambda to just create string when test fails
        assertEquals(espected,actual , () -> "El nombre de la cuenta no es el esperado");
    }

    @Test
    void testAccountBalance(){
        boolean isEnvDev = "dev".equals(System.getProperty("ENV"));
        assumingThat(isEnvDev,() -> {
            Account ac = new Account("Andres", new BigDecimal("1000.145"));

            assertEquals(1000.145,ac.getBalance().doubleValue());

            assertFalse(ac.getBalance().compareTo(BigDecimal.ZERO) < 0);

        });

    }

    @Test
    @EnabledOnOs({OS.WINDOWS, OS.LINUX})
    void testAccountRef() {
      Account account = new Account("Jhon Doe", new BigDecimal("8980.45700"));
      Account account2 = new Account("Jhona Doe", new BigDecimal("8980.45700"));

      assertNotEquals(account,account2);
    }

    @ParameterizedTest(name = "{index} Debit Account {argumentsWithNames}")
    @ValueSource(strings = {"100","200","300"})
    void testDebitAccount(String amount) {
        Account account = new Account("Jhon Doe", new BigDecimal("8980.45700"));
        account.debit(new BigDecimal(amount));
        assertNotNull(account.getBalance());
        assertEquals(8880,account.getBalance().intValue());
    }

    @Test
    void testCreditAccount() {
        Account account = new Account("Jhon Doe", new BigDecimal("8980.45700"));
        account.credit(new BigDecimal("100"));
        assertNotNull(account.getBalance());
        assertEquals(9080,account.getBalance().intValue());
    }

    @DisabledOnOs(OS.WINDOWS)
    @Test
    void testInsufficientMoneyException() {
        Account ac = new Account("Andres", new BigDecimal("1000.145"));
        Exception ex = assertThrows(InsufficientMoneyException.class,
                ()->{
                    ac.debit(new BigDecimal(1500));
                });
        assertEquals("insufficient funds", ex.getMessage());
    }

    @Test
    @EnabledOnJre({JRE.JAVA_15, JRE.JAVA_8} )
    void testAccountMoneyTransfer(){
        Account account = new Account("Raymundo", new BigDecimal("8980.45700"));
        Account account2 = new Account("Azael", new BigDecimal("3073.1234"));

       Bank bank = new Bank("BBVA Bancomer");
        bank.addAccount(account);
        bank.addAccount(account2);

        bank.transfer(account,account2, new BigDecimal(5000));

        assertAll(
                () -> assertEquals("Azael", bank.getAccounts().stream()
                        .filter(ac -> ac.getPerson().equals("Azael"))
                        .findFirst().get().getPerson()),
                () -> assertEquals("8073.1234",account2.getBalance().toPlainString()));


    }
}
@Tag("timeout")
class TimeOutTest{
    @Test
    @Timeout(5)
    void timeOutException() throws InterruptedException{
        TimeUnit.SECONDS.sleep(6);
    }
    @Test
    void timeOutAssertions(){
        assertTimeout(Duration.ofSeconds(5), () -> {
            TimeUnit.SECONDS.sleep(4);
        });
    }
}