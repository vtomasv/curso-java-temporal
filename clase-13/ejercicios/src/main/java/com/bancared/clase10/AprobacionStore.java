package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DuplicateKeyException;
public class AprobacionStore {
    private final JdbcTemplate jdbc;private final TransferStore transfers;
    public AprobacionStore(JdbcTemplate j,TransferStore t){jdbc=j;transfers=t;j.execute("CREATE TABLE IF NOT EXISTS solicitudes(clave VARCHAR(40) PRIMARY KEY,origen VARCHAR(20),cuenta_origen VARCHAR(30),destino VARCHAR(20),cuenta_destino VARCHAR(30),monto BIGINT,descripcion VARCHAR(120),plazo INT,estado VARCHAR(30),detalle VARCHAR(300))");}
    public void crear(SolicitudTransferencia s,int plazo){PoliticaAprobacion.validar(s.monto(),plazo);
        if(s.commandId()==null||!s.commandId().matches("[A-Za-z0-9_-]{1,40}")||!Set.of("CORDILLERA:A001","PACIFICO:B001").contains(s.bancoOrigen()+":"+s.cuentaOrigen())||!Set.of("CORDILLERA:A001","PACIFICO:B001").contains(s.bancoDestino()+":"+s.cuentaDestino())||s.bancoOrigen().equals(s.bancoDestino())||s.descripcion()==null||s.descripcion().length()>120)throw new ErrorNegocio("VALIDACION","Solicitud de aprobación inválida.");
        try{jdbc.update("INSERT INTO solicitudes VALUES(?,?,?,?,?,?,?,?,?,?)",s.commandId(),s.bancoOrigen(),s.cuentaOrigen(),s.bancoDestino(),s.cuentaDestino(),s.monto(),s.descripcion(),plazo,"INICIADA","");}
        catch(DuplicateKeyException e){throw new ErrorNegocio("CLAVE_REUTILIZADA","Usa una clave nueva para cada solicitud.");}
    }
    public List<Map<String,Object>> todas(){return jdbc.queryForList("SELECT * FROM solicitudes ORDER BY clave DESC");}
    public String origen(String clave){var l=jdbc.query("SELECT cuenta_origen FROM solicitudes WHERE clave=?",(r,n)->r.getString(1),clave);if(l.isEmpty())throw new ErrorNegocio("SOLICITUD_INEXISTENTE","No existe la solicitud.");return l.getFirst();}
    public ResultadoWorkflow estado(String clave,String estado,String detalle){jdbc.update("UPDATE solicitudes SET estado=?,detalle=? WHERE clave=?",estado,detalle,clave);return new ResultadoWorkflow("tx-"+clave,estado,detalle);}
    public Transferencia preparar(SolicitudTransferencia s){jdbc.update("UPDATE solicitudes SET monto=? WHERE clave=?",s.monto(),s.commandId());return transfers.crear(s);}
}
