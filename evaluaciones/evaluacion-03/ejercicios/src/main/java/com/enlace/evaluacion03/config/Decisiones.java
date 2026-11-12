package com.enlace.evaluacion03.config;

public final class Decisiones {

    private Decisiones() {
    }

    public enum Dominio { PENDIENTE, INVARIANTES_EN_ENTIDAD, POLITICA_DE_DOMINIO }
    public enum Acceso { PENDIENTE, RBAC_CENTRAL, ESTRATEGIA_POR_CLASIFICACION }
    public enum Reautenticacion { PENDIENTE, POR_CADA_LECTURA, VENTANA_CORTA_POR_ORDEN }
    public enum Firmas { PENDIENTE, ROLES_FIJOS, UMBRAL_CONFIGURABLE }
    public enum Idempotencia { PENDIENTE, CLAVE_ORDEN_UNIDAD, COMANDO_EXPLICITO }
    public enum Orquestacion { PENDIENTE, SECUENCIAL, PARALELA, CHILD_WORKFLOW_POR_UNIDAD }
    public enum Confirmacion { PENDIENTE, TODAS_LAS_UNIDADES, QUORUM_CONFIGURABLE }
    public enum Incertidumbre { PENDIENTE, REVISION_MANUAL, COMPENSAR_SOLO_CONFIRMADOS }

    // DECISION(EV03-E01): reemplace PENDIENTE y documente la alternativa descartada.
    public static final Dominio DOMINIO = Dominio.PENDIENTE;
    // DECISION(EV03-E02): reemplace PENDIENTE y documente la alternativa descartada.
    public static final Acceso ACCESO = Acceso.PENDIENTE;
    // DECISION(EV03-E03): reemplace PENDIENTE y documente alcance y expiración.
    public static final Reautenticacion REAUTENTICACION = Reautenticacion.PENDIENTE;
    // DECISION(EV03-E04): reemplace PENDIENTE y documente roles u origen del umbral.
    public static final Firmas FIRMAS = Firmas.PENDIENTE;
    // DECISION(EV03-E05): reemplace PENDIENTE y documente la clave estable.
    public static final Idempotencia IDEMPOTENCIA = Idempotencia.PENDIENTE;
    // DECISION(EV03-E06): reemplace PENDIENTE y explique orden, paralelismo o hijos.
    public static final Orquestacion ORQUESTACION = Orquestacion.PENDIENTE;
    // DECISION(EV03-E07): ambas decisiones deben quedar justificadas y probadas.
    public static final Confirmacion CONFIRMACION = Confirmacion.PENDIENTE;
    public static final Incertidumbre INCERTIDUMBRE = Incertidumbre.PENDIENTE;
}
