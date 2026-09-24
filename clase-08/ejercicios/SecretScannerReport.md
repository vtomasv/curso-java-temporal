# E07: rotación y prevención (plantilla para el alumno)

No introducir secretos reales. La contraseña `clase08-demo` es pública y sólo pertenece al perfil de laboratorio.

1. Ejecutar `bash scripts/check-secrets.sh`. El escáner detecta patrones de claves privadas y claves AWS de ejemplo; no es una auditoría exhaustiva.
2. Configurar `LAB_PASSWORD` sin escribirla en archivos versionados y reiniciar. Verificar login con el nuevo valor.
3. Emitir un JWT, reiniciar y validar el anterior: esperado 401 por cambio de clave RSA.
4. Simular en este documento un incidente: secreto afectado, alcance, revocación, rotación, revisión de logs e historial. No reescribir el historial compartido como parte de la práctica.

| Campo | Evidencia del alumno |
|---|---|
| Activo y alcance del incidente simulado | Completar |
| Revocación antes de limpiar el historial | Completar |
| Rotación y consumidores afectados | Completar |
| Tratamiento coordinado de historial y clones | Completar |
| Prevención en CI y comprobación posterior | Completar |
