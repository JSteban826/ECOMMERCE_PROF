#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
[[ -f .env ]] || { echo 'Copia .env.example a .env y completa los valores'; exit 1; }
while IFS= read -r line || [[ -n "$line" ]]; do
  line="${line%$'\r'}"
  [[ -z "$line" || "$line" == \#* ]] && continue
  [[ "$line" == *=* ]] || { echo 'Línea inválida en .env'; exit 1; }
  key="${line%%=*}"; value="${line#*=}"
  [[ "$key" =~ ^[A-Z_][A-Z0-9_]*$ ]] || { echo 'Nombre de variable inválido'; exit 1; }
  export "$key=$value"
done < .env
java -jar boot/target/notificaciones-boot-1.0.0-SNAPSHOT.jar
