# WebServer8D 🚀

Servidor web multi-hilos en Java con ThreadPool, que maneja solicitudes HTTP utilizando el puerto `7770`.  
Incluye soporte para respuestas HTTP (`200 OK`, `404 Not Found`, etc.), manejo eficiente de hilos con **ThreadPool**, y pruebas automatizadas con **JUnit**.  

---

## 📌 Características
- 🧕 **Multi-threading con ThreadPool**: Manejo eficiente de múltiples clientes simultáneamente.  
- 🌐 **Soporte HTTP**: Procesamiento de solicitudes HTTP con respuestas adecuadas (`200 OK`, `404 Not Found`, `501 Not Implemented`).  
- 📁 **Manejo de archivos como html**
- ✅ **Pruebas unitarias con JUnit 5**: Validación automática del funcionamiento del servidor.  
- 🌍 **Publicación en Internet con `ngrok`**.  

---

## 🛠 Instalación
### 1️⃣ Clonar el repositorio
```sh
git clone https://github.com/OscarMURA/WebServer8D
cd WebServer8D
```

### 2️⃣ Compilar el código
```sh
javac -cp "bin;lib/junit-1.9.1.jar" -d bin *.java
```

### 3️⃣ Ejecutar el servidor
```sh
java -cp bin WebServer
```

---

## 🔍 Ejecutar pruebas con JUnit
Para validar que el servidor responde correctamente (`200 OK` y `404 Not Found`), ejecuta:

```sh
java -cp "bin;lib/junit-1.9.1.jar" org.junit.platform.console.ConsoleLauncher --select-class=TestServer
```

Si todo funciona correctamente, deberías ver una salida indicando que todas las pruebas han pasado. ✅

---

## 🌍 Publicar el servidor en Internet con `ngrok`
### **1️⃣ Instalar `ngrok`**
- Descarga desde: [https://ngrok.com/download](https://ngrok.com/download)  
- Extrae y coloca el archivo `ngrok.exe` en la carpeta del proyecto.  

### **2️⃣ Ejecutar `ngrok`**
Con el servidor web corriendo, en otra terminal ejecuta:

```sh
ngrok http 7770
```

Esto generará una URL pública, por ejemplo:

```
Forwarding    https://random-subdomain.ngrok.io -> http://localhost:7770
```

Copia la **URL pública** y compártela para que cualquiera pueda acceder a tu servidor. 🌎

---

## 🏠 Estructura del Proyecto
```
WebServer8D/
│── bin/            # Archivos compilados (.class)
│── lib/            # Librerías externas (JUnit)
│── src/            # Código fuente del servidor
│── test/           # Código de pruebas unitarias
│── resources/      # Archivos estáticos (HTML, CSS, JS, imágenes)
│── README.md       # Documentación del proyecto
│── WebServer.java  # Punto de entrada del servidor
│── ClientHandler.java  # Manejo de clientes en hilos
│── HttpUtils.java  # Utilidades para respuestas HTTP
│── TestServer.java  # Pruebas automatizadas
```

---


## 🐝 Autor
📌 **Desarrollado por:** [Oscar Stiven Muñoz Ramirez](https://github.com/OscarMURA)  
```

