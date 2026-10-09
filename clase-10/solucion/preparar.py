#!/usr/bin/env python3
"""Genera una base docente completa sin modificar los ejercicios del alumno."""
import argparse
import shutil
from pathlib import Path

parser=argparse.ArgumentParser()
parser.add_argument("destino",help="Carpeta nueva para la solución completa")
parser.add_argument("--hasta",choices=["e01","e02","e03"],default="e03")
args=parser.parse_args()
here=Path(__file__).resolve().parent
source=here.parent/"ejercicios"
target=Path(args.destino).resolve()
if target.exists():parser.error("El destino ya existe. Usa una carpeta nueva para conservar el trabajo anterior.")
if target==source or source in target.parents:parser.error("El destino debe quedar fuera de ejercicios/.")
shutil.copytree(source,target,ignore=shutil.ignore_patterns("target","data",".laboratorio","*.log"))
names=["PoliticaActivities.java"]
if args.hasta in ["e02","e03"]:names.append("Idempotencia.java")
if args.hasta=="e03":names.append("ReversaWorkflowImpl.java")
for name in names:shutil.copy2(here/"referencia"/name,target/"src/main/java/com/bancared/clase10"/name)
print(f"Base oficial {args.hasta}: {target}")
