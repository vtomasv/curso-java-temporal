package com.bancared.clase10;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
@Configuration @ConditionalOnProperty(name="banking.role",havingValue="bank")
public class BankConfiguration {
    @Bean BankStore bankStore(JdbcTemplate jdbc,PlatformTransactionManager manager,@Value("${banking.bank-code}") String codigo){return new BankStore(jdbc,new TransactionTemplate(manager),codigo);}
    @Bean FallosBanco fallosBanco(){return new FallosBanco();}
}
