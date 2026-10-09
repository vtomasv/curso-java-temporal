package com.bancared.clase10;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import static com.bancared.clase10.LiquidacionModelos.*;
public class CompensacionStore {
    private final JdbcTemplate jdbc;private final TransferStore transfers;private final TransactionTemplate tx;
    public CompensacionStore(JdbcTemplate j,TransferStore t){jdbc=j;transfers=t;tx=new TransactionTemplate(new DataSourceTransactionManager(j.getDataSource()));j.execute("CREATE TABLE IF NOT EXISTS lotes(id VARCHAR(60) PRIMARY KEY,estado VARCHAR(30),cordillera BIGINT,pacifico BIGINT,detalle VARCHAR(300))");j.execute("CREATE TABLE IF NOT EXISTS miembros_lote(lote VARCHAR(60),transfer_id VARCHAR(80),PRIMARY KEY(lote,transfer_id))");j.execute("CREATE TABLE IF NOT EXISTS cierre_lock(id INT PRIMARY KEY)");if(j.queryForObject("SELECT COUNT(*) FROM cierre_lock",Integer.class)==0)j.update("INSERT INTO cierre_lock VALUES(1)");}
    public Lote obtener(String id){var r=jdbc.query("SELECT * FROM lotes WHERE id=?",(s,n)->new Lote(s.getString("id"),s.getString("estado"),Map.of("CORDILLERA",s.getLong("cordillera"),"PACIFICO",s.getLong("pacifico")),jdbc.query("SELECT transfer_id FROM miembros_lote WHERE lote=? ORDER BY transfer_id",(m,k)->m.getString(1),id),s.getString("detalle")),id);if(r.isEmpty())throw new ErrorNegocio("LOTE_INEXISTENTE","No existe el lote.");return r.getFirst();}
    public List<Lote> todos(){return jdbc.query("SELECT id FROM lotes ORDER BY id DESC",(s,n)->obtener(s.getString(1)));}
    public Lote preparar(String id){if(id==null||!id.matches("[A-Za-z0-9_-]{1,60}"))throw new ErrorNegocio("VALIDACION","Clave de lote inválida.");return tx.execute(s->{
        jdbc.queryForObject("SELECT id FROM cierre_lock WHERE id=1 FOR UPDATE",Integer.class);
        if(jdbc.queryForObject("SELECT COUNT(*) FROM lotes WHERE id=?",Integer.class,id)>0)return obtener(id);
        var ids=jdbc.query("SELECT id FROM transferencias WHERE lote_id IS NULL AND liquidada=FALSE ORDER BY id FOR UPDATE",(r,n)->r.getString(1));
        var seleccion=ids.stream().map(transfers::obtener).filter(SeleccionLote::elegible).toList();var netos=CalculoNeto.calcular(seleccion);
        jdbc.update("INSERT INTO lotes VALUES(?,?,?,?,?)",id,"PREPARADO",netos.get("CORDILLERA"),netos.get("PACIFICO"),"Snapshot exclusivo; saldos de clientes no cambian al liquidar.");
        for(var t:seleccion){jdbc.update("INSERT INTO miembros_lote VALUES(?,?)",id,t.id());jdbc.update("UPDATE transferencias SET lote_id=? WHERE id=?",id,t.id());}return obtener(id);
    });}
    public Lote terminar(String id,String estado,String detalle){return tx.execute(s->{jdbc.update("UPDATE lotes SET estado=?,detalle=? WHERE id=?",estado,detalle,id);if("LIQUIDADO".equals(estado))jdbc.update("UPDATE transferencias SET liquidada=TRUE WHERE lote_id=?",id);if("COMPENSADO".equals(estado)||"RECHAZADO".equals(estado))jdbc.update("UPDATE transferencias SET lote_id=NULL WHERE lote_id=?",id);return obtener(id);});}
}
