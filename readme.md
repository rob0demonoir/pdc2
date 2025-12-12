\# 🧼 Príncipe de Colores - App Móvil



Aplicación nativa de E-commerce desarrollada en \*\*Kotlin\*\* para la venta de jabones artesanales. Este proyecto implementa una arquitectura moderna \*\*MVVM\*\*, consumo de \*\*API REST\*\* y gestión de estado en tiempo real.



\## ✨ Características Principales



\* \*\*🔐 Autenticación y Seguridad:\*\*

&nbsp;   \* Login y Registro de Clientes con validaciones robustas (Regex para contraseñas seguras).

&nbsp;   \* Gestión de sesiones mediante Singleton (`SessionManager`).

&nbsp;   \* Acceso oculto para Administradores.

\* \*\*🛒 Carrito de Compras Inteligente:\*\*

&nbsp;   \* Carrito persistente vinculado al usuario en el servidor.

&nbsp;   \* Validación de stock en tiempo real (impide agregar más items de los disponibles).

&nbsp;   \* Cálculo automático de totales.

\* \*\*📦 Catálogo Dinámico:\*\*

&nbsp;   \* Visualización de productos con imágenes (vía Coil).

&nbsp;   \* Indicadores visuales de "AGOTADO".

&nbsp;   \* Feedback visual (Toasts) y actualización automática de stock.

\* \*\*🔮 Módulo Esotérico (API Externa):\*\*

&nbsp;   \* Integración con API pública de Horóscopo.

&nbsp;   \* Cliente Retrofit independiente para no interferir con los microservicios principales.

\* \*\*💸 Proceso de Checkout:\*\*

&nbsp;   \* Simulación de pago y descuento automático de inventario en base de datos.



\## 🛠️ Stack Tecnológico



\* \*\*Lenguaje:\*\* Kotlin.

\* \*\*UI Toolkit:\*\* Jetpack Compose (Material Design 3).

\* \*\*Arquitectura:\*\* MVVM (Model-View-ViewModel) + Repository Pattern.

\* \*\*Red (Networking):\*\* Retrofit 2 + Gson Converter.

\* \*\*Asincronía:\*\* Kotlin Coroutines + StateFlow / SharedFlow.

\* \*\*Carga de Imágenes:\*\* Coil.

\* \*\*Navegación:\*\* Jetpack Navigation Compose.

\* \*\*Testing:\*\* JUnit + Mockito.



\## 📂 Estructura del Proyecto





com.duoc.principedecolores

├── data

│   ├── api          # Interfaces Retrofit y DTOs (Request/Response)

│   ├── model        # Modelos de datos de la UI (Clean Architecture)

│   └── repository   # Lógica de conexión y manejo de datos

├── ui

│   ├── navigation   # Grafo de navegación y rutas

│   ├── screen       # Pantallas Composable (Vistas)

│   ├── theme        # Tema y colores de la app

│   └── viewmodel    # Gestión del estado de la UI

└── utils            # SessionManager y utilidades

