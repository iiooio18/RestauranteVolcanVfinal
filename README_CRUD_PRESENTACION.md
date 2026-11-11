# Presentación y Documentación del CRUD - Volcán Waffles & Café (Bodega)

---

## 1. Presentación

### 1.1. Breve contexto y descripción del proyecto
El presente documento detalla el desarrollo e implementación del **módulo CRUD (Crear, Leer, Actualizar y Deshabilitar) de Productos e Insumos** para el sistema de gestión de bodega de **Volcán Waffles & Café** (Restaurante Volcán SpA). Este módulo está diseñado específicamente para controlar de manera eficiente los ingredientes, materias primas, toppings y materiales utilizados en la operación diaria del local.

### 1.2. Importancia y relevancia del proyecto
Una correcta gestión de inventario es vital para evitar quiebres de stock en insumos críticos (como harinas, frutas o toppings) y prevenir mermas o pérdidas económicas. Este módulo otorga al administrador de bodega el control total y centralizado sobre el catálogo de productos, optimizando los tiempos de reposición y asegurando la trazabilidad operativa del negocio.

---

## 2. Objetivos de la aplicación

### 2.1. Qué problema resuelve la aplicación
Resuelve la falta de un registro digital y estructurado de los insumos y materias primas, eliminando el control manual en papel o planillas desactualizadas que suelen generar errores en el cálculo del stock y retrasos en la atención del restaurante.

### 2.2. Beneficios esperados para los usuarios
- **Agilidad:** Registro y consulta inmediata de cualquier insumo en segundos.
- **Control Visual:** Identificación rápida de productos mediante categorías y estados de disponibilidad.
- **Trazabilidad:** Capacidad de actualizar existencias y aplicar bajas lógicas sin perder el historial operativo.

### 2.3. Metas concretas que se esperan alcanzar con la aplicación
- Implementar al 100% las operaciones del CRUD (RF-03, RF-04, RF-08, RF-15).
- Garantizar una interfaz intuitiva, fluida y adaptada a los estándares de Material Design en Android.
- Mantener la integridad de los datos de inventario durante toda la sesión de uso del usuario.

---

## 3. Descripción de la aplicación

### 3.1. Funcionalidades clave de la aplicación
- **Catálogo interactivo (RF-04):** Visualización de todos los productos organizados por categorías (Repostería, Frutas, Toppings, Lácteos, etc.).
- **Registro de nuevos productos (RF-03):** Formulario validado para dar de alta insumos ingresando código, nombre, descripción, categoría, unidad de medida y stock inicial.
- **Gestión de Stock (RF-15):** Actualización dinámica de las cantidades disponibles en bodega.
- **Baja lógica (RF-08):** Deshabilitar productos que ya no se comercializan o utilizan, preservando el histórico de movimientos.

### 3.2. Descripción del diseño y navegación de la aplicación
La navegación está estructurada de forma jerárquica y lineal:
- **Pantalla de Inicio / Login:** Validación de credenciales de acceso.
- **Menú Principal (Dashboard):** Acceso directo a los módulos de Productos, Movimientos y Stock.
- **Módulo de Productos (CRUD):** Listado en tarjeta (`RecyclerView`) con botón flotante para agregar y menú de opciones contextual al tocar cada elemento.

### 3.3. Breve explicación de las herramientas y tecnologías empleadas en el desarrollo
- **Lenguaje:** Java (estándar robusto y estructurado para Android).
- **Interfaz Gráfica:** XML Layouts (`ConstraintLayout`, `LinearLayout`, `CardView`, `RecyclerView`, `Spinner`).
- **Arquitectura y Patrones:** Patrón Repository (Singleton) para la gestión centralizada de datos en memoria y ViewBinding para la vinculación segura de vistas.
- **Entorno:** Android Studio (SDK 30 a 35).

---

## 4. Explicación de las nuevas funcionalidades que la aplicación contiene

### 4.1. Nuevas características añadidas durante el desarrollo
- **Diálogo Modal de Registro y Edición:** Un formulario completo con validación de campos obligatorios para ingresar o modificar los atributos de los insumos.
- **Menú de Acciones Contextuales por Producto:** Al seleccionar cualquier tarjeta de producto, se despliega un menú interactivo con opciones para *Editar información*, *Actualizar stock* y *Deshabilitar / Habilitar (Baja lógica)*.
- **Filtro Dinámico por Categoría:** Selector desplegable (`Spinner`) para filtrar instantáneamente el inventario según el rubro del insumo.

### 4.2. Cómo estas funcionalidades mejoran la experiencia del usuario
Reducen la fricción operativa: el bodeguero no necesita navegar por múltiples pantallas complejas; con un solo toque sobre el producto accede a todas las operaciones de mantenimiento (CRUD), optimizando su tiempo durante las jornadas de alta demanda en el local.

---

## 5. Demostración de operaciones y persistencia (CRUD)

### 5.1. Mostrar cómo la aplicación gestiona los datos
Para el alcance del prototipo académico, los datos se administran mediante el **`ProductRepository`** (implementado bajo el patrón Singleton), el cual mantiene una lista en memoria sincronizada en tiempo real. Esto permite que cualquier cambio realizado (creación, actualización o baja lógica) se refleje inmediatamente en el catálogo y en las demás vistas del sistema sin latencia de red.

### 5.2. Ejemplos de operaciones CRUD
- **Create (Crear - RF-03):** Se añade el producto *"Jarabe de Vainilla"* (Código: `P005`, Categoría: *Toppings*, Stock inicial: `10` botellas).
- **Read (Leer - RF-04):** Se consulta el catálogo general y se aplica el filtro para mostrar únicamente los insumos de la categoría *"Repostería"*.
- **Update (Actualizar - RF-15):** Se modifica el stock del producto *"Frutilla"* de `12` kg a `15` kg tras una nueva recepción en bodega.
- **Delete / Deshabilitar (Baja lógica - RF-08):** Se desmarca el *"Helado de vainilla"* como disponible, ocultándolo de la operación activa pero manteniendo su registro histórico.

### 5.3. Descripción de cómo se manejaron posibles errores de validación
Se incorporaron validaciones lógicas en los formularios de entrada:
- Campos vacíos obligatorios bloquean el guardado y muestran alertas visuales (`Toast`).
- Control de tipos de datos en stock (conversión segura de texto a números enteros).
- Inmutabilidad del código de producto durante la edición para evitar conflictos de identificación en el inventario.

---

## 6. Elementos finales para que la aplicación quede terminada

### 6.1. Descripción de las pruebas que aún deben realizarse
- Pruebas de estrés de interfaz con catálogos superiores a 100 productos en el `RecyclerView`.
- Pruebas de integración entre el módulo de inventario (CRUD) y el módulo de registro de movimientos (`MovementsActivity`).

### 6.2. Aspectos por optimizar o corregir antes de la finalización
- Migración opcional de la persistencia en memoria hacia una base de datos local SQLite / Room para almacenamiento permanente en disco.
- Incorporación de animaciones fluidas al agregar o eliminar elementos de la lista.

### 6.3. Planificación de las últimas etapas del desarrollo
1. Revisión final de diseño y tipografías corporativas de *Volcán Waffles & Café*.
2. Ensayo general de la presentación con la simulación completa de los flujos de usuario.
3. Despliegue del archivo APK de demostración en el dispositivo físico del equipo.

---

## 7. Conclusión y cierre

### 7.1. Reflexión sobre el proceso de desarrollo y los aprendizajes obtenidos
El desarrollo de este módulo CRUD permitió consolidar los conocimientos clave en desarrollo móvil nativo con Java y Android, comprendiendo la importancia de separar responsabilidades (Modelos, Repositorios, Adaptadores y Actividades) para lograr un código limpio, mantenible y escalable.

### 7.2. Reconocimientos y agradecimientos
Agradecemos al equipo docente por la guía técnica y a los integrantes del equipo (**Diego Godoy, Johan Cofre, Adrian Anzoategui y Jeremy Rodriguez**) por la colaboración en el diseño y estructuración del prototipo para **Restaurante Volcán SpA**.
