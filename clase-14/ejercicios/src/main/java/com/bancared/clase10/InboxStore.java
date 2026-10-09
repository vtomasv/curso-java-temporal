package com.bancared.clase10;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import static com.bancared.clase10.EventosModelos.*;
public class InboxStore {
    private final JdbcTemplate jdbc;private final TransactionTemplate tx;
    public InboxStore(JdbcTemplate j){jdbc=j;tx=new TransactionTemplate(new DataSourceTransactionManager(j.getDataSource()));j.execute("CREATE TABLE IF NOT EXISTS inbox(id VARCHAR(120) PRIMARY KEY,tipo VARCHAR(40),referencia VARCHAR(80),monto BIGINT,version INT,texto VARCHAR(200))");j.execute("CREATE TABLE IF NOT EXISTS inbox_lock(id INT PRIMARY KEY)");if(j.queryForObject("SELECT COUNT(*) FROM inbox_lock",Integer.class)==0)j.update("INSERT INTO inbox_lock VALUES(1)");}
    public Notificacion recibir(Evento e){return tx.execute(s->{jdbc.queryForObject("SELECT id FROM inbox_lock WHERE id=1 FOR UPDATE",Integer.class);var anteriores=jdbc.query("SELECT * FROM inbox WHERE id=?",(r,n)->new Evento(r.getString("id"),r.getString("tipo"),r.getString("referencia"),r.getLong("monto"),r.getInt("version")),e.id());
        if(!anteriores.isEmpty()){
            // TODO(C14-E02): comparar payload y recuperar la notificación previa sin insertar ni emitir otra.
            throw new ErrorNegocio("EVENTO_REPETIDO","La base protege el duplicado; completa C14-E02 para recuperar el resultado.");
        }
        String texto=e.tipo()+" · "+e.referencia()+" · CLP "+e.monto();jdbc.update("INSERT INTO inbox VALUES(?,?,?,?,?,?)",e.id(),e.tipo(),e.referencia(),e.monto(),e.version(),texto);return new Notificacion(e.id(),e.referencia(),texto);
    });}
    public List<Notificacion> todas(){return jdbc.query("SELECT * FROM inbox ORDER BY id DESC",(r,n)->new Notificacion(r.getString("id"),r.getString("referencia"),r.getString("texto")));}
}
