package com.bancared.clase10;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.*;
import org.springframework.transaction.support.TransactionTemplate;
final class TestStores {
    static BankStore bank(String codigo){return bankUrl("jdbc:h2:mem:"+UUID.randomUUID()+";DB_CLOSE_DELAY=-1",codigo);}
    static BankStore bankUrl(String url,String codigo){var ds=new DriverManagerDataSource(url,"sa","");return new BankStore(new JdbcTemplate(ds),new TransactionTemplate(new DataSourceTransactionManager(ds)),codigo);}
    static TransferStore portal(){var ds=new DriverManagerDataSource("jdbc:h2:mem:"+UUID.randomUUID()+";DB_CLOSE_DELAY=-1","sa","");return new TransferStore(new JdbcTemplate(ds));}
}
