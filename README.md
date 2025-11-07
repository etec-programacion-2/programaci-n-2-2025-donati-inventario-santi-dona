Gestor de inventario
Para que el proyecto funcione en terminal colocar:
  git clone git@github.com:etec-programacion-2/programaci-n-2-2025-donati-inventario-santi-dona.git
  cd programaci-n-2-2025-donati-inventario-santi-dona
  gradle run --no-configuration-cache
Las versiones que hay que tener son:
  Java 21
  Kotlin 2.2.0
  Gradle 9.0.0
  org.apache.poi:poi 5.2.5
  org.apache.poi:poi-ooxml 5.2.5
Instrucciones de uso de usuario comun:
1. Inicio de sesión
  Ejecuta el programa: Aparece la ventana de inicio de sesión.
  Ingresa usuario y contraseña Presiona "Iniciar sesión" o Enter para acceder.
2. Navegación en la ventana principal
  Usa la barra lateral azul para cambiar entre paneles.
  Paneles disponibles: Inventario, Historial, Buscar.
3. Panel Inventario (Agregar y gestionar productos)
  Haz clic en "Agregar": Ingresa nombre, stock inicial, precio de compra/reventa, tipo
(Tecnología, Comida, Limpieza, Otro) y fecha de vencimiento si es comida.
  Haga clic en "Entrada": Seleccione el producto y la cantidad a ingresar.
  Haga clic en "Salida": Seleccione el producto y la cantidad a retirar.
  Haga clic en "Marcar Vencido": Seleccione los productos de comida vencidos.
  Haga clic en "Importar excel": Para importar un stock anterior.
5. Panel Histórico (Ver Movimientos)
  Vea la tabla de movimientos (entradas, salidas, etc.) con fecha y usuario.
  Haga clic en "Borrar historial" para limpiar todo (se requiere confirmación).
6. Panel Buscar (Buscar Productos)
  Ingrese el nombre del producto en el campo de texto.
  Haga clic en "Buscar" o presione Enter: Muestra detalles (stock, tipo, precios, fecha
de vencimiento, historial).
