# Guía de instalación del entorno de desarrollo

Esta guía te lleva paso a paso a dejar tu máquina lista para todas las clases del curso. Cada sección incluye el comando de verificación: **no avances hasta que la verificación funcione**. Si algo falla, copia el error completo y pídele a tu asistente de IA que te ayude a diagnosticarlo indicando tu sistema operativo.

## 1. JDK 21

### Windows
1. Descarga Eclipse Temurin 21 (LTS) desde https://adoptium.net.
2. Ejecuta el instalador y marca las opciones *Set JAVA_HOME* y *Add to PATH*.
3. Abre una terminal nueva (PowerShell) y verifica.

### macOS
```bash
brew install temurin@21
```

### Linux (Ubuntu/Debian)
```bash
sudo apt update && sudo apt install -y temurin-21-jdk || sudo apt install -y openjdk-21-jdk
```

**Verificación:**
```bash
java --version
# Debe mostrar: openjdk 21.x.x
```

## 2. Maven 3.9+

Los proyectos incluyen Maven Wrapper (`./mvnw`), por lo que Maven global es opcional pero recomendado.

```bash
# macOS
brew install maven
# Linux
sudo apt install -y maven
# Windows: descargar de https://maven.apache.org/download.cgi y agregar bin/ al PATH
```

**Verificación:**
```bash
mvn --version
```

## 3. Docker y Docker Compose

Necesario desde la clase 6 (PostgreSQL) y para servicios de apoyo.

1. Instala Docker Desktop (Windows/macOS) desde https://www.docker.com/products/docker-desktop/ o Docker Engine en Linux:
```bash
curl -fsSL https://get.docker.com | sh
sudo usermod -aG docker $USER   # cerrar sesión y volver a entrar
```

**Verificación:**
```bash
docker run --rm hello-world
docker compose version
```

## 4. Temporal CLI

Necesario desde la clase 9. El CLI incluye un servidor de desarrollo completo con interfaz web.

```bash
# macOS
brew install temporal
# Linux
curl -sSf https://temporal.download/cli.sh | sh
# Windows: descargar desde https://docs.temporal.io/cli e incluir temporal.exe en el PATH
```

**Verificación:**
```bash
temporal --version
temporal server start-dev
# Abrir http://localhost:8233 en el navegador: debe verse la Web UI de Temporal.
# Detener con CTRL+C.
```

> **Nota:** `temporal server start-dev` usa una base de datos en memoria. Para conservar los workflows entre reinicios usa `temporal server start-dev --db-filename temporal.db`.

## 5. IDE

Cualquiera de las dos opciones:

| IDE | Descarga | Plugins recomendados |
|---|---|---|
| IntelliJ IDEA Community | https://www.jetbrains.com/idea/download | Lombok (si aplica) |
| VS Code | https://code.visualstudio.com | Extension Pack for Java, Spring Boot Extension Pack |

## 6. Asistente de IA

El curso se trabaja siempre con asistencia de IA. Configura al menos uno:

| Herramienta | Tipo | Instalación |
|---|---|---|
| Claude Code | Agente de terminal | `npm install -g @anthropic-ai/claude-code` |
| Codex CLI | Agente de terminal | `npm install -g @openai/codex` |
| ChatGPT / Claude / Gemini (web) | Chat | Navegador |

Este repositorio incluye `AGENTS.md` y `CLAUDE.md` con el contexto del curso: los agentes los leen automáticamente al trabajar dentro del repositorio.

## 7. Clonar el repositorio y verificar todo

```bash
git clone <URL-DEL-REPOSITORIO>
cd curso-java-temporal
cd clase-01/ejercicios
mvn -q test
```

Si los tests de la clase 1 compilan y se ejecutan (aunque fallen los que debes completar tú), tu entorno está listo.

## Solución de problemas frecuentes

| Síntoma | Causa probable | Acción |
|---|---|---|
| `java: command not found` | PATH sin configurar | Reabrir terminal; verificar JAVA_HOME |
| Maven descarga eternamente | Proxy/red institucional | Configurar `~/.m2/settings.xml` con el proxy |
| `port 7233 already in use` | Otro servidor Temporal activo | `temporal server` previo sin cerrar; matar el proceso |
| Docker: `permission denied` | Usuario fuera del grupo docker | `sudo usermod -aG docker $USER` y relogin |

Si el problema persiste, comparte con tu IA: sistema operativo, comando ejecutado, salida completa del error y qué ya intentaste.
