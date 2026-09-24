#!/usr/bin/env bash
# E07: comprobación didáctica acotada a código/configuración de la clase.
set -euo pipefail
cd "$(dirname "$0")/.."
pattern='-----BEGIN (RSA |EC |OPENSSH )?PRIVATE KEY-----|AKIA[0-9A-Z]{16}'
if grep -REn -- "$pattern" src/main; then
  echo 'Posible secreto detectado. Revocar/rotar y revisar antes de publicar.' >&2
  exit 1
fi
echo 'Sin patrones detectados por el escáner didáctico (cobertura limitada).'
