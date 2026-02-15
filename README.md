# 🎮 War of Tanks

> Videojuego de acción 2D desarrollado completamente en Java — sin motores ni frameworks externos.


![Java](https://img.shields.io/badge/Java-17+-orange?style=flat-square&logo=java)
![License](https://img.shields.io/badge/Licencia-MIT-blue?style=flat-square)
![Estado](https://img.shields.io/badge/Estado-En%20desarrollo-yellow?style=flat-square)

---

## ¿De qué trata?

War of Tanks es un juego de oleadas en 2D donde controlas un tanque y debes sobrevivir el mayor tiempo posible. Los enemigos escalan en dificultad con cada oleada, los misiles cruzan el mapa en distintas direcciones y los power-ups aparecen estratégicamente para darte ventaja.

---

## 🎯 Características principales

**Gameplay**
- 3 tipos de enemigos con comportamientos de IA distintos
- Sistema de oleadas con dificultad progresiva (3 niveles: Fácil, Normal, Avanzado)
- Misiles cruzados que aparecen periódicamente desde los bordes
- Habilidad especial desbloqueada por puntuación: onda explosiva que limpia la pantalla

**Power-ups (7 tipos)**
- 🛡️ Escudo — destruye enemigos y proyectiles al contacto
- 🔫 Doble cañón
- 🔫 Disparo múltiple (12 direcciones)
- ⚡ Disparo rápido
- ✖️ Puntuación x2
- ❤️ Vida extra
- 💯 +1000 puntos instantáneos

**Editor de mapas**
- Diseña tus propios escenarios con obstáculos y rocas indestructibles
- Guardado y carga de mapas en formato JSON
- Los mapas personalizados se usan directamente en partida

**Tabla de puntuaciones**
- Top 10 persistente guardado localmente
- Nicknames personalizables

---

## 🕹️ Controles

| Tecla | Acción |
|-------|--------|
| `↑` | Acelerar |
| `← →` | Rotar |
| `Z` | Disparar |
| `X` | Onda explosiva *(desde 500 pts)* |
| `ESC` | Pausar |

---

## 🛠️ Tecnologías y herramientas

| Tecnología | Uso |
|------------|-----|
| Java 17 | Lenguaje principal |
| Java AWT / Graphics2D | Renderizado 2D, transformaciones afines |
| javax.sound.sampled | Audio y efectos de sonido |
| org.json | Serialización de datos (mapas y puntuaciones) |
| BufferStrategy (triple buffer) | Renderizado sin parpadeo |

Sin librerías de juego externas. Todo el motor está escrito a mano.

---

## 🚀 Ejecutar el proyecto

### Requisitos
- Java 17 o superior

### Desde el código fuente

```bash
git clone https://github.com/johnivansn/War-Of-Tanks.git
cd War-Of-Tanks
```

### Dependencias (org.json)

El proyecto usa `org.json` desde `libs/json-20251224.jar`. Asegúrate de incluirlo en el classpath al compilar y ejecutar.

### Compilar y ejecutar (con `javac`/`java`)

```bash
# Compilar
javac -cp "libs/json-20251224.jar" -d bin (Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })

# Ejecutar
java -cp "bin;libs/json-20251224.jar" main.Window
```

### Desde JAR

```bash
java -jar war-of-tanks.jar
```

---

## 📁 Estructura del proyecto

```
src/
├── main/          # Window.java — punto de entrada y game loop
├── states/        # Pantallas: menú, juego, editor, puntuaciones…
├── gameObjects/   # Todas las entidades: Player, Enemy, PowerUp, etc.
├── graphics/      # Assets, animaciones, sonido, fuentes
├── input/         # Teclado, ratón, constantes de configuración
├── math/          # Vector2D — operaciones vectoriales 2D
├── resources/     # Imágenes, sonidos, fuentes
├── io/            # Lectura/escritura de JSON
└── ui/            # Botones, componentes visuales
```

---

## 📄 Licencia

MIT — libre para usar, modificar y distribuir. Ver [LICENSE](LICENSE).
