package com.bancared.clase10;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
public class CelebracionesStore {
    private final JdbcTemplate jdbc;private final TransactionTemplate tx;
    public CelebracionesStore(JdbcTemplate jdbc){this.jdbc=jdbc;tx=new TransactionTemplate(new DataSourceTransactionManager(jdbc.getDataSource()));
        jdbc.execute("CREATE TABLE IF NOT EXISTS saludos(cuenta VARCHAR(30) PRIMARY KEY,fecha VARCHAR(10),mensaje VARCHAR(100))");
        jdbc.execute("CREATE TABLE IF NOT EXISTS calendario(id INT PRIMARY KEY,fecha VARCHAR(10),ejecuciones INT)");
        if(jdbc.queryForObject("SELECT COUNT(*) FROM calendario",Integer.class)==0)jdbc.update("INSERT INTO calendario VALUES(1,'',0)");
    }
    public Map<String,Object> vista(){return Map.of("fecha",jdbc.queryForObject("SELECT fecha FROM calendario WHERE id=1",String.class),"ejecuciones",jdbc.queryForObject("SELECT ejecuciones FROM calendario WHERE id=1",Integer.class),"saludos",jdbc.queryForList("SELECT cuenta,fecha,mensaje FROM saludos ORDER BY cuenta"));}
    public int reemplazar(String fecha,List<String> cuentas){return tx.execute(s->{
        jdbc.queryForObject("SELECT id FROM calendario WHERE id=1 FOR UPDATE",Integer.class);
        jdbc.update("DELETE FROM saludos");for(String cuenta:cuentas)jdbc.update("INSERT INTO saludos VALUES(?,?,?)",cuenta,fecha,"¡Feliz cumpleaños! Te saludamos durante todo este día.");
        jdbc.update("UPDATE calendario SET fecha=?,ejecuciones=ejecuciones+1 WHERE id=1",fecha);return cuentas.size();
    });}
    public String saludo(String cuenta){var r=jdbc.query("SELECT mensaje FROM saludos WHERE cuenta=?",(s,n)->s.getString(1),cuenta);return r.isEmpty()?"":r.getFirst();}
}
