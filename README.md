# Level-Up Gamer - App de eCommerce para Android

Bienvenido al repositorio de la aplicación móvil oficial de Level-Up Gamer, una tienda de eCommerce de videojuegos y accesorios construida con tecnologías nativas de Android.

## Descripción

Esta aplicación permite a los usuarios explorar un catálogo de productos, gestionar su carrito de compras y participar en un ecosistema de recompensas y comunidad. Además, cuenta con un panel de administración integrado para gestionar el contenido de la tienda directamente desde la app.

## Funcionalidades Implementadas

La aplicación cuenta con dos flujos de usuario diferenciados:

### 🎮 Para Clientes

*   **Autenticación:** Registro y Login de usuarios.
*   **Catálogo de Productos:** Visualización de productos en una cuadrícula con imágenes, precios y ratings.
*   **Carrito de Compras:** Añadir, eliminar y modificar la cantidad de productos con actualización en tiempo real.
*   **Flujo de Checkout Completo:**
    *   Selección de dirección de envío entre múltiples direcciones guardadas.
    *   Opción para añadir nuevas direcciones durante el proceso de pago.
    *   Cálculo de costos de envío dinámico según la región.
    *   Simulación de selección de método de pago.
*   **Sistema de Recompensas y Lealtad:**
    *   Acumulación de **Puntos Level-Up** con cada compra.
    *   Sistema de **Niveles de Usuario** (Bronze, Silver, Gold, V.I.P.) que otorgan descuentos automáticos.
    *   Descuento especial para usuarios con correo `@duocuc.cl`.
    *   **Tienda de Canje** para usar los puntos y obtener recompensas (descuentos, envío gratis, etc.).
*   **Sistema de Referidos:**
    *   Cada usuario tiene un código de referido único.
    *   Los nuevos usuarios pueden usar un código al registrarse para ganar puntos de bienvenida.
*   **Comunidad y Eventos:**
    *   Sección para ver los próximos eventos "gamer".
    *   Pantalla de detalle para cada evento con mapa y opción de "Cómo Llegar" (integra Google Maps).
    *   Inscripción a eventos para ganar puntos.
*   **Gestión de Perfil:**
    *   Visualización de datos, puntos, nivel y código de referido.
    *   Edición de perfil y gestión completa de direcciones de envío (Añadir/Editar/Eliminar).

### ⚙️ Para Administradores

*   **Login Diferenciado:** Acceso a un panel de control con credenciales de administrador.
*   **Navegación Aislada:** Panel de administración con su propio flujo de navegación para una gestión segura.
*   **CRUD de Productos:**
    *   **Ver** todos los productos de la tienda en una lista.
    *   **Añadir** nuevos productos a través de un formulario completo.
    *   **Editar** los detalles de productos existentes (nombre, precio, stock, descripción, etc.).
    *   **Eliminar** productos del catálogo.
    *   **Carga de Imágenes:** Permite seleccionar una imagen desde el almacenamiento local del dispositivo para asignarla a un producto.

## Tecnologías Utilizadas

*   **Lenguaje:** Kotlin
*   **Interfaz de Usuario:** Jetpack Compose
*   **Navegación:** Jetpack Navigation for Compose
*   **Arquitectura:** Single-Activity, State Hoisting para la gestión de estado.
*   **Carga de Imágenes:** Coil

## Instrucciones de Prueba

Para facilitar la prueba de la aplicación, puedes usar los siguientes atajos en la pantalla de Login:

*   **Cliente Normal:** `cliente@ejemplo.com` (con dos direcciones de prueba en Santiago).
*   **Cliente Duoc:** `cliente@duocuc.cl` (con dos direcciones de prueba en Biobío y 20% de descuento automático).
*   **Administrador:** `admin@levelupgamer.com` / `admin123`

---
*Este proyecto fue desarrollado con la asistencia de un modelo de lenguaje de Google.*