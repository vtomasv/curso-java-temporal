package com.bancared.clase10;
import static com.bancared.clase10.Modelos.*;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;
/** Un banco tiene su propia conexión y transacciones locales. */
public class BankStore {
    private final JdbcTemplate jdbc;
    private final TransactionTemplate tx;
    public BankStore(JdbcTemplate jdbc, TransactionTemplate tx, String codigo) {
        this.jdbc = jdbc; this.tx = tx;
        jdbc.execute("CREATE TABLE IF NOT EXISTS cuentas(id VARCHAR(30) PRIMARY KEY, titular VARCHAR(100), saldo BIGINT NOT NULL CHECK(saldo>=0), nacimiento VARCHAR(10))");
        jdbc.execute("CREATE TABLE IF NOT EXISTS comandos(command_id VARCHAR(100) PRIMARY KEY, transfer_id VARCHAR(80), cuenta_id VARCHAR(30), monto BIGINT NOT NULL CHECK(monto>0), direccion VARCHAR(12), saldo_resultante BIGINT NOT NULL, UNIQUE(transfer_id,cuenta_id,direccion))");
        if (jdbc.queryForObject("SELECT COUNT(*) FROM cuentas", Integer.class) == 0) {
            if ("CORDILLERA".equals(codigo)) {
                jdbc.update("INSERT INTO cuentas VALUES(?,?,?,?)", "A001", "Ana Muñoz", 1_000_000L, "1990-10-08");
                jdbc.update("INSERT INTO cuentas VALUES(?,?,?,?)", "A002", "Carla Rojas", 300_000L, "1992-04-12");
            } else jdbc.update("INSERT INTO cuentas VALUES(?,?,?,?)", "B001", "Bruno Soto", 500_000L, "1988-12-03");
        }
    }
    public List<Cuenta> cuentas() {return jdbc.query("SELECT * FROM cuentas ORDER BY id", (r,n)->new Cuenta(r.getString("id"),r.getString("titular"),r.getLong("saldo"),r.getString("nacimiento")));}
    public List<ReciboBanco> movimientos() {return jdbc.query("SELECT * FROM comandos ORDER BY command_id", (r,n)->recibo(r));}
    public ConsultaComando consultar(String id) {
        List<ReciboBanco> encontrados=jdbc.query("SELECT * FROM comandos WHERE command_id=?",(r,n)->recibo(r),id);
        return encontrados.isEmpty()?new ConsultaComando(false,null):new ConsultaComando(true,encontrados.getFirst());
    }
    private ReciboBanco recibo(java.sql.ResultSet r) throws java.sql.SQLException {
        return new ReciboBanco(r.getString("command_id"),r.getString("transfer_id"),r.getString("cuenta_id"),r.getLong("monto"),Direccion.valueOf(r.getString("direccion")),r.getLong("saldo_resultante"));
    }
    public ReciboBanco ejecutar(ComandoBanco comando) {
        if (comando==null || comando.commandId()==null || comando.transferId()==null || comando.cuentaId()==null || comando.direccion()==null || comando.monto()<=0 || comando.monto()>100_000_000L)
            throw new ErrorNegocio("VALIDACION", "Comando incompleto o monto fuera del rango del laboratorio.");
        return tx.execute(estado->{
            List<Long> saldos=jdbc.query("SELECT saldo FROM cuentas WHERE id=? FOR UPDATE",(r,n)->r.getLong(1),comando.cuentaId());
            if(saldos.isEmpty()) throw new ErrorNegocio("CUENTA_INEXISTENTE","La cuenta no pertenece a este banco.");
            ConsultaComando anterior=consultar(comando.commandId());
            if(anterior.aplicado()) return Idempotencia.repetido(comando,anterior.recibo());
            int efectos=jdbc.queryForObject("SELECT COUNT(*) FROM comandos WHERE transfer_id=? AND cuenta_id=? AND direccion=?",Integer.class,comando.transferId(),comando.cuentaId(),comando.direccion().name());
            if(efectos>0) throw new ErrorNegocio("EFECTO_DUPLICADO","La operación ya tiene este movimiento con otra clave.");
            long actual=saldos.getFirst();
            if(comando.direccion()==Direccion.DEBITO && actual<comando.monto()) throw new ErrorNegocio("FONDOS_INSUFICIENTES","El saldo disponible no alcanza.");
            long nuevo;
            try { nuevo=comando.direccion()==Direccion.DEBITO?Math.subtractExact(actual,comando.monto()):Math.addExact(actual,comando.monto()); }
            catch(ArithmeticException e){throw new ErrorNegocio("MONTO_FUERA_RANGO","El saldo excede el rango permitido.");}
            jdbc.update("UPDATE cuentas SET saldo=? WHERE id=?",nuevo,comando.cuentaId());
            jdbc.update("INSERT INTO comandos VALUES(?,?,?,?,?,?)",comando.commandId(),comando.transferId(),comando.cuentaId(),comando.monto(),comando.direccion().name(),nuevo);
            return new ReciboBanco(comando.commandId(),comando.transferId(),comando.cuentaId(),comando.monto(),comando.direccion(),nuevo);
        });
    }
}
