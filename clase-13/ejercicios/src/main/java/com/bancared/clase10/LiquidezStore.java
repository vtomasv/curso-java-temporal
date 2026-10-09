package com.bancared.clase10;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import static com.bancared.clase10.LiquidacionModelos.*;
public class LiquidezStore {
    private final JdbcTemplate jdbc;private final TransactionTemplate tx;
    public LiquidezStore(JdbcTemplate j){jdbc=j;tx=new TransactionTemplate(new DataSourceTransactionManager(j.getDataSource()));j.execute("CREATE TABLE IF NOT EXISTS liquidez(id INT PRIMARY KEY,saldo BIGINT CHECK(saldo>=0))");j.execute("CREATE TABLE IF NOT EXISTS asientos_liquidez(clave VARCHAR(100) PRIMARY KEY,lote VARCHAR(60),delta BIGINT,saldo BIGINT)");if(j.queryForObject("SELECT COUNT(*) FROM liquidez",Integer.class)==0)j.update("INSERT INTO liquidez VALUES(1,2000000)");}
    public ReciboLiquidez consultar(String clave){var r=jdbc.query("SELECT * FROM asientos_liquidez WHERE clave=?",(s,n)->new ReciboLiquidez(s.getString("clave"),s.getString("lote"),s.getLong("delta"),s.getLong("saldo")),clave);return r.isEmpty()?null:r.getFirst();}
    public ReciboLiquidez mover(OrdenLiquidez c){if(c==null||c.clave()==null||!c.clave().matches("[A-Za-z0-9_:-]{1,100}")||c.lote()==null||!c.lote().matches("[A-Za-z0-9_-]{1,60}")||c.delta()==0||c.delta()>100_000_000L||c.delta()< -100_000_000L)throw new ErrorNegocio("VALIDACION","Orden de liquidez inválida.");return tx.execute(s->{long saldo=jdbc.queryForObject("SELECT saldo FROM liquidez WHERE id=1 FOR UPDATE",Long.class);var r=consultar(c.clave());if(r!=null){if(!r.lote().equals(c.lote())||r.delta()!=c.delta())throw new ErrorNegocio("CLAVE_REUTILIZADA","La clave de liquidez identifica otra orden.");return r;}long nuevo=Math.addExact(saldo,c.delta());if(nuevo<0)throw new ErrorNegocio("FONDOS_INSUFICIENTES","El banco no dispone de liquidez para el lote.");jdbc.update("UPDATE liquidez SET saldo=? WHERE id=1",nuevo);jdbc.update("INSERT INTO asientos_liquidez VALUES(?,?,?,?)",c.clave(),c.lote(),c.delta(),nuevo);return new ReciboLiquidez(c.clave(),c.lote(),c.delta(),nuevo);});}
    public Liquidez vista(){return new Liquidez(jdbc.queryForObject("SELECT saldo FROM liquidez WHERE id=1",Long.class),jdbc.query("SELECT * FROM asientos_liquidez ORDER BY clave",(r,n)->new ReciboLiquidez(r.getString("clave"),r.getString("lote"),r.getLong("delta"),r.getLong("saldo"))));}
}
