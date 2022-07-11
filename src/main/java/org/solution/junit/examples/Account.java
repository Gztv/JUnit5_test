package org.solution.junit.examples;

import org.solution.junit.examples.exceptions.InsufficientMoneyException;

import java.math.BigDecimal;

public class Account {
    private String person;
    private BigDecimal balance;
    private Bank bank;


    public Account(String person, BigDecimal balance) {
        this.balance = balance;
        this.person = person;
    }

    public void debit(BigDecimal amount){
       BigDecimal newBalance = this.balance.subtract(amount);

       if(newBalance.compareTo(BigDecimal.ZERO) < 0){throw new InsufficientMoneyException("insufficient funds");}

       this.balance = newBalance;
    }
    public void credit(BigDecimal amount){
        this.balance = this.balance.add(amount);
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @Override
    public boolean equals(Object obj) {
        Account ac = (Account) obj;

        if( !(obj instanceof Account)){ return false; }
        if(this.person == null || this.balance == null){
            return false;
        }
        return this.person.equals(ac.getPerson()) && this.balance.equals(ac.getBalance());
    }
}
