#!/bin/bash

echo "=========================================="
echo "  DEMOSTRACION API PEDIDOS"
echo "=========================================="
echo ""

BASE_URL="http://localhost:8080"

echo ">>> 1. Esperando a que la API este lista..."
until curl -s "$BASE_URL/pagos" > /dev/null 2>&1; do
    sleep 1
done
echo ">>> API lista!"
echo ""

echo "=========================================="
echo "  REGISTRO DE PAGOS"
echo "=========================================="

echo ""
echo ">>> 2. Registrar pago #1 (Juan Perez)"
curl -s -X POST "$BASE_URL/pagos" \
  -H "Content-Type: application/json" \
  -d '{
    "idPago": "PAG001",
    "precio": 500.00,
    "metodoPago": "tarjeta_credito"
  }' | jq .

echo ""
echo ">>> 3. Registrar pago #2 (Maria Gomez)"
curl -s -X POST "$BASE_URL/pagos" \
  -H "Content-Type: application/json" \
  -d '{
    "idPago": "PAG002",
    "precio": 250.00,
    "metodoPago": "efectivo"
  }' | jq .

echo ""
echo ">>> 4. Registrar pago #3 (Carlos Lopez) - Monto invalido"
curl -s -X POST "$BASE_URL/pagos" \
  -H "Content-Type: application/json" \
  -d '{
    "idPago": "PAG003",
    "precio": -50.00,
    "metodoPago": "tarjeta_credito"
  }' | jq .

echo ""
echo "=========================================="
echo "  CONSULTAS DE PAGOS"
echo "=========================================="

echo ""
echo ">>> 5. Listar todos los pagos"
curl -s "$BASE_URL/pagos" | jq .

echo ""
echo ">>> 6. Buscar pago por ID (PAG001)"
curl -s "$BASE_URL/pagos/PAG001" | jq .

echo ""
echo ">>> 7. Buscar pago inexistente"
curl -s "$BASE_URL/pagos/PAG999" | jq .

echo ""
echo "=========================================="
echo "  GESTION DE PEDIDOS"
echo "=========================================="

echo ""
echo ">>> 8. Crear pedido #1"
curl -s -X POST "$BASE_URL/api/pedidos" \
  -H "Content-Type: application/json" \
  -d '{
    "idPedido": "PED001",
    "precio": 500.00,
    "categoria": "electronica",
    "lugarEntrega": "Calle 123, Bogota"
  }' | jq .

echo ""
echo ">>> 9. Crear pedido #2"
curl -s -X POST "$BASE_URL/api/pedidos" \
  -H "Content-Type: application/json" \
  -d '{
    "idPedido": "PED002",
    "precio": 250.00,
    "categoria": "ropa",
    "lugarEntrega": "Carrera 45, Medellin"
  }' | jq .

echo ""
echo ">>> 10. Listar todos los pedidos"
curl -s "$BASE_URL/api/pedidos" | jq .

echo ""
echo ">>> 11. Obtener pedido por ID (PED001)"
curl -s "$BASE_URL/api/pedidos/PED001" | jq .

echo ""
echo ">>> 12. Intentar obtener pedido inexistente"
curl -s "$BASE_URL/api/pedidos/PED999" | jq .

echo ""
echo "=========================================="
echo "  ELIMINAR PEDIDOS"
echo "=========================================="

echo ""
echo ">>> 13. Eliminar pedido (PED002)"
curl -s -X DELETE "$BASE_URL/api/pedidos/PED002"
echo "Eliminado"

echo ""
echo ">>> 14. Lista de pedidos despues de eliminar"
curl -s "$BASE_URL/api/pedidos" | jq .

echo ""
echo "=========================================="
echo "  RESUMEN"
echo "=========================================="
echo ""
echo "Total pagos registrados:"
curl -s "$BASE_URL/pagos" | jq 'length'

echo ""
echo "Total pedidos registrados:"
curl -s "$BASE_URL/api/pedidos" | jq 'length'

echo ""
echo "=========================================="
echo "  FIN DE LA DEMOSTRACION"
echo "=========================================="
