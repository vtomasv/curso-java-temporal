package com.bancared.clase10;
import org.springframework.context.annotation.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
@Configuration @ConditionalOnProperty(name="banking.role",havingValue="portal")
public class CelebracionesConfiguration {
    @Bean CelebracionesStore celebracionesStore(JdbcTemplate j){return new CelebracionesStore(j);}
    @Bean CelebracionesActivitiesImpl celebracionesActivities(BankGateway b,CelebracionesStore s){return new CelebracionesActivitiesImpl(b,s);}
    @Bean ExtraWorkerModule celebracionesModule(CelebracionesActivitiesImpl a){return w->{w.registerWorkflowImplementationTypes(CumpleWorkflowImpl.class,CicloCumpleWorkflowImpl.class);w.registerActivitiesImplementations(a);};}
}
