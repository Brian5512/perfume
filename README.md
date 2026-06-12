Microservicio Inventario

API REST en Spring Boot para gestionar inventario segun el diagrama de clases:
Producto, Inventario, MovimientoInventario, AlertaStock, Sucursal y UsuarioInventario.

## Configuracion

El servicio corre en el puerto `8092` y usa MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventario_bd
spring.datasource.username=root
spring.datasource.password=
```

Antes de levantarlo crea la base de datos:

```sql
CREATE DATABASE inventario_bd;
```

## Ejecutar

```bash
./mvnw spring-boot:run
```

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

## Endpoints principales

- `POST /api/v1/productos`
- `GET /api/v1/productos`
- `GET /api/v1/productos/{id}/disponibilidad`
- `POST /api/v1/sucursales`
- `GET /api/v1/sucursales/{id}/inventario`
- `POST /api/v1/inventarios`
- `PUT /api/v1/inventarios/{id}/ajustar`
- `POST /api/v1/inventarios/ventas/descontar`
- `POST /api/v1/movimientos-inventario`
- `GET /api/v1/movimientos-inventario/inventarios/{idInventario}`
- `GET /api/v1/alertas-stock`
- `PATCH /api/v1/alertas-stock/{id}/cerrar`
- `POST /api/v1/usuarios-inventario`

## Ejemplo rapido

Crear producto:

```json
{
  "nombre": "Perfume Citrus",
  "descripcion": "Fragancia fresca 100ml",
  "precio": 19990,
  "estado": "ACTIVO"
}
```

Crear sucursal:

```json
{
  "nombre": "Sucursal Centro",
  "direccion": "Av. Principal 123",
  "estado": "ACTIVA"
}
```

Crear inventario:

```json
{
  "productoId": 1,
  "sucursalId": 1,
  "stockActual": 20,
  "stockMinimo": 5,
  "stockMaximo": 100,
  "ubicacion": "Bodega A"
}
```

Ajustar stock:

```json
{
  "usuarioId": 1,
  "tipoMovimiento": "SALIDA",
  "cantidad": 3,
  "motivo": "Venta"
}
```

Tipos de movimiento soportados: `ENTRADA`, `SALIDA`, `AJUSTE`.

Descontar por venta desde el microservicio Ventas:

```json
{
  "productoId": 1,
  "sucursalId": 1,
  "usuarioId": 1,
  "ventaId": 1001,
  "cantidad": 2
}
```

Este endpoint registra una `SALIDA` en Inventario y luego llama por `RestTemplate` al microservicio Bodega. La URL de Bodega se configura con:

```properties
bodega.api.descuento-venta-url=http://localhost:8093/api/v1/despachos-producto/ventas/descontar
```

## Pruebas

Los tests usan H2 en memoria para no depender de MySQL local.

```bash
./mvnw test
```
