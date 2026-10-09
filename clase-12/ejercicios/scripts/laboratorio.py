#!/usr/bin/env python3
"""Arranca los tres procesos. Ctrl+C detiene únicamente sus propios hijos."""
import argparse
import os
from pathlib import Path
import shutil
import socket
import subprocess
import time

parser=argparse.ArgumentParser()
parser.add_argument("--temporal",choices=["external","embedded"],default="external")
parser.add_argument("--reset",action="store_true",help="Reinicia únicamente los datos ficticios de .laboratorio/data")
args=parser.parse_args()
base=Path(__file__).resolve().parents[1]
jar=base/"target/clase-12-banca-1.0.0.jar"
if not jar.exists():parser.error("Primero ejecuta ./mvnw clean verify para construir el JAR.")
for port in [8080,8081,8082]:
    with socket.socket() as s:
        if s.connect_ex(("127.0.0.1",port))==0:parser.error(f"El puerto {port} está ocupado. Detén el laboratorio anterior.")
run=base/".laboratorio";data=run/"data";run.mkdir(exist_ok=True)
if args.reset and data.exists():shutil.rmtree(data)
data.mkdir(exist_ok=True)
java=str(Path(os.environ["JAVA_HOME"])/"bin"/("java.exe" if os.name=="nt" else "java")) if os.environ.get("JAVA_HOME") else "java"
children=[];logs=[]
try:
    for role,code,port in [("bank","CORDILLERA",8081),("bank","PACIFICO",8082),("portal","portal",8080)]:
        log=open(run/f"{code.lower()}.log","w",encoding="utf-8");logs.append(log)
        command=[java,"-Xms64m","-Xmx256m","-jar",str(jar),f"--server.port={port}",f"--banking.role={role}",f"--banking.bank-code={code}",f"--spring.datasource.url=jdbc:h2:file:{(data/code.lower()).as_posix()};DB_CLOSE_ON_EXIT=FALSE",f"--banking.temporal.mode={args.temporal}"]
        children.append(subprocess.Popen(command,cwd=base,stdout=log,stderr=subprocess.STDOUT))
    print("Portal: http://localhost:8080 · docente / laboratorio",flush=True)
    print("Bancos: 8081 y 8082. Logs en .laboratorio/. Ctrl+C detiene los tres.",flush=True)
    print("Temporal: "+("servidor externo y consola http://localhost:8233" if args.temporal=="external" else "servidor real de pruebas del SDK, sin consola web ni historia persistente"),flush=True)
    while True:
        time.sleep(1)
        for child in children:
            if child.poll() is not None:raise RuntimeError("Un proceso terminó. Revisa su log en .laboratorio/.")
except KeyboardInterrupt:pass
finally:
    for child in children:
        if child.poll() is None:child.terminate()
    for child in children:
        try:child.wait(timeout=15)
        except subprocess.TimeoutExpired:child.kill();child.wait()
    for log in logs:log.close()
