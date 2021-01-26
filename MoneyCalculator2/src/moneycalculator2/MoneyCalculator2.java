/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moneycalculator2;

import moneycalculator2.model.CurrencyList;
import moneycalculator2.model.ExchangeRate;
import moneycalculator2.model.Currency;
import moneycalculator2.model.Money;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.*;
import java.util.*;

/**
 *
 * @author delSe
 */
public class MoneyCalculator2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        MoneyCalculator2 moneyCalculator = new MoneyCalculator2();
        moneyCalculator.control(); 
    }
    
    private final CurrencyList currencyList;
    private Money money;
    private ExchangeRate exchangeRate;
    //private Currency currencyFrom;
    private Currency currencyTo;
    
    public MoneyCalculator2(){
        this.currencyList = new CurrencyList();
    }
    private void control() throws Exception{
        input();
        process();
        output();
    }
    
    private void input(){
        System.out.println("Introduzca cantidad");
        Scanner scanner = new Scanner(System.in);
        double amount = Double.parseDouble(scanner.next());
        
        while(true){
            System.out.println("Introduzca divisa origen");
            Currency currency = currencyList.get(scanner.next());
            money = new Money(amount, currency);
            if(currency != null) break;
            System.out.println("Divisa no conocida.");
        }
        
        while(true){
            System.out.println("Introduzca divisa destino");
            currencyTo = currencyList.get(scanner.next()); 
            if(currencyTo != null) break;
            System.out.println("Divisa no conocida.");
        } 
    }
    
    private void process() throws Exception{
        exchangeRate = getExchangeRate(money.getCurrency(), currencyTo);
    }
    
    private void output(){
        System.out.println(money.getAmount() + " " + money.getCurrency().getSymbol() + " equivalen a " + money.getAmount() * exchangeRate.getRate() + currencyTo.getSymbol());
    }
    
    private static ExchangeRate getExchangeRate(Currency from, Currency to) throws IOException {
        URL url = 
            new URL("http://free.currencyconverterapi.com/api/v5/convert?q=" +
                    from + "_" + to + "&compact=y");
        URLConnection connection = url.openConnection();
        try (BufferedReader reader = 
                new BufferedReader(
                        new InputStreamReader(connection.getInputStream()))) {
            String line = reader.readLine();
            String line1 = line.substring(line.indexOf(to.getCode())+12, line.indexOf("}"));
            return new ExchangeRate(from, to, 
                    LocalDate.of(2018, Month.SEPTEMBER, 26), 
                    Double.parseDouble(line1));
        }
    }
}
