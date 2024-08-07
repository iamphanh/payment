package vn.vnpay.demo1.config;

import vn.vnpay.demo1.model.Bank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "banks")
public class BankConfig {
    private List<Bank> banks;

    public Bank getBankByCode(String bankCode) {
        return banks.stream()
                .filter(bank -> bankCode.equals(bank.getBankCode()))
                .findFirst()
                .orElse(null);
    }
}
