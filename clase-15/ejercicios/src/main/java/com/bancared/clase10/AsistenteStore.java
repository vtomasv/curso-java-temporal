package com.bancared.clase10;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DuplicateKeyException;
import tools.jackson.databind.json.JsonMapper;
import static com.bancared.clase10.AsistenteModelos.*;
public class AsistenteStore {
    private final JdbcTemplate jdbc;private final JsonMapper json=JsonMapper.builder().build();
    public AsistenteStore(JdbcTemplate j){jdbc=j;j.execute("CREATE TABLE IF NOT EXISTS consultas_ia(clave VARCHAR(40) PRIMARY KEY,usuario VARCHAR(40),pregunta VARCHAR(300),respuesta VARCHAR(800),fuentes VARCHAR(300),estado VARCHAR(30),proveedor VARCHAR(40))");}
    public void iniciar(Consulta c,String usuario){if(c.clave()==null||!c.clave().matches("[A-Za-z0-9_-]{1,40}")||c.pregunta()==null||c.pregunta().isBlank()||c.pregunta().length()>300||!Set.of("VALIDA","FUENTE_INVENTADA","SALIDA_INVALIDA","NO_DISPONIBLE").contains(c.escenario()))throw new ErrorNegocio("VALIDACION","Revisa clave, pregunta (1..300 caracteres) y escenario.");try{jdbc.update("INSERT INTO consultas_ia VALUES(?,?,?,?,?,?,?)",c.clave(),usuario,c.pregunta(),"","[]","EN_PROCESO","");}catch(DuplicateKeyException e){throw new ErrorNegocio("CLAVE_REUTILIZADA","Usa una clave nueva para la consulta.");}}
    public Respuesta guardar(Consulta c,Propuesta p){String estado="FALLBACK_LOCAL".equals(p.proveedor())?"FALLBACK":"RESPONDIDA";jdbc.update("UPDATE consultas_ia SET respuesta=?,fuentes=?,estado=?,proveedor=? WHERE clave=?",p.respuesta(),json.writeValueAsString(p.fuentes()),estado,p.proveedor(),c.clave());return new Respuesta(c.clave(),p.respuesta(),p.fuentes(),estado,p.proveedor());}
    public List<Map<String,Object>> consultas(String usuario,boolean docente){return docente?jdbc.queryForList("SELECT * FROM consultas_ia ORDER BY clave DESC"):jdbc.queryForList("SELECT * FROM consultas_ia WHERE usuario=? ORDER BY clave DESC",usuario);}
}
