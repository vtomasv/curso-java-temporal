package com.bancared.clase10;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DuplicateKeyException;
import static com.bancared.clase10.EventosModelos.*;
public class OutboxStore {
    private final JdbcTemplate jdbc;
    public OutboxStore(JdbcTemplate j){jdbc=j;j.execute("CREATE TABLE IF NOT EXISTS outbox(id VARCHAR(120) PRIMARY KEY,tipo VARCHAR(40),referencia VARCHAR(80),monto BIGINT,version INT,estado VARCHAR(20))");}
    public void agregar(Evento e){try{jdbc.update("INSERT INTO outbox VALUES(?,?,?,?,?,?)",e.id(),e.tipo(),e.referencia(),e.monto(),e.version(),"PENDIENTE");}catch(DuplicateKeyException x){var previa=pendientesYPublicados().stream().filter(p->p.id().equals(e.id())).findFirst().orElseThrow();if(!previa.equals(e))throw new ErrorNegocio("CLAVE_REUTILIZADA","Evento con payload diferente.");}}
    private List<Evento> pendientesYPublicados(){return jdbc.query("SELECT * FROM outbox ORDER BY id",(r,n)->new Evento(r.getString("id"),r.getString("tipo"),r.getString("referencia"),r.getLong("monto"),r.getInt("version")));}
    public List<Evento> pendientes(){return jdbc.query("SELECT * FROM outbox WHERE estado='PENDIENTE' ORDER BY id LIMIT 25",(r,n)->new Evento(r.getString("id"),r.getString("tipo"),r.getString("referencia"),r.getLong("monto"),r.getInt("version")));}
    public void confirmar(String id,boolean confirmado){
        if(confirmado)jdbc.update("UPDATE outbox SET estado='PUBLICADO' WHERE id=? AND estado='PENDIENTE'",id);
    }
    public List<Map<String,Object>> vista(){return jdbc.queryForList("SELECT * FROM outbox ORDER BY id DESC");}
}
