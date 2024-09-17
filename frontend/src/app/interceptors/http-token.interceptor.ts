import {HttpHeaders, HttpInterceptorFn} from '@angular/common/http';
import {inject} from "@angular/core";
import {KeycloakService} from "../services/keycloak/keycloak.service";

export const httpTokenInterceptor: HttpInterceptorFn =
  (request, next) => {
    const keycloakService = inject(KeycloakService)
    const token = keycloakService?.keycloak?.token;
    if (token) {
      const authReq = request?.clone({
        headers: new HttpHeaders({
          Authorization: `Bearer ${token}`
        })
      });
      return next(authReq);
    }
    return next(request);
};
