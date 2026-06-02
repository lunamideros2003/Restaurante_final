import { HttpErrorResponse } from '@angular/common/http';

/** Extrae mensaje legible de respuestas Spring (400, 404, 409, 500). */
export function mensajeErrorApi(error: HttpErrorResponse, fallback: string): string {
  const body = error.error;
  if (typeof body === 'string' && body.trim()) {
    return body;
  }
  if (body && typeof body === 'object') {
    if (body.error) return String(body.error);
    if (body.message) return String(body.message);
  }
  if (error.status === 0) {
    return 'No hay conexión con el backend. ¿Está corriendo en http://localhost:8080?';
  }
  return fallback;
}
