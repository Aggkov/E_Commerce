import {CanActivateFn, Router} from '@angular/router';
import {KeycloakService} from "../services/keycloak/keycloak.service";
import {inject} from "@angular/core";

export const authGuard: CanActivateFn = async (route, state) => {
  const keycloakService = inject(KeycloakService);
  const router = inject(Router);

  // Wait until Keycloak is initialized
  // await keycloakService.init();

  console.log('Keycloak Authenticated:', keycloakService.keycloak?.authenticated);
  console.log('Keycloak Token Expired:', keycloakService.keycloak?.isTokenExpired());

  if (!keycloakService.keycloak?.authenticated || keycloakService.keycloak?.isTokenExpired()) {
    await keycloakService.login();
    return false;
  }
  return true;
};
