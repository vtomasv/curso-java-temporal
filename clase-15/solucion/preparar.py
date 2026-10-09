#!/usr/bin/env python3
import argparse,shutil
from pathlib import Path
p=argparse.ArgumentParser(description="Genera una base oficial sin sobrescribir el trabajo del alumno")
p.add_argument("destino");p.add_argument("--hasta",choices=["e01","e02","e03"],default="e03");a=p.parse_args()
here=Path(__file__).resolve().parent;source=here.parent/"ejercicios";target=Path(a.destino).resolve()
if target.exists():p.error("Destino existente. Usa una carpeta nueva.")
if target==source or source in target.parents:p.error("El destino debe quedar fuera de ejercicios/.")
shutil.copytree(source,target,ignore=shutil.ignore_patterns("target","data",".laboratorio","*.log"))
names=['AsistenteWorkflowImpl.java', 'RespuestaSegura.java', 'ResumenOperativo.java']
for name in names[:int(a.hasta[-1])]:shutil.copy2(here/"referencia"/name,target/"src/main/java/com/bancared/clase10"/name)
print("Base oficial "+a.hasta+": "+str(target))
