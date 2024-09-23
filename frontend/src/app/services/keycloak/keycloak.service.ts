import {Injectable} from '@angular/core';
import Keycloak from 'keycloak-js';
import {Router} from '@angular/router';
import {UserProfile} from './user-profile';

@Injectable({
  providedIn: 'root',
})
export class KeycloakService {
  private _keycloak: Keycloak | undefined;
  private _profile: UserProfile | undefined;
  private isInitialized = false;  // Track initialization state

  constructor(private router: Router) {
    // Initialize Keycloak instance
    this.keycloak;
  }

  get keycloak() {
    if (!this._keycloak) {
      this._keycloak = new Keycloak({
          url: 'http://localhost:9090',
          realm: 'E-Commerce',
          clientId: 'frontend',
      });

      // Log Keycloak configuration
      console.log('Keycloak instance created with configuration:', {
        url: this._keycloak.authServerUrl,
        realm: this._keycloak.realm,
        clientId: this._keycloak.clientId,
      });
    }
    return this._keycloak;
  }

  get profile(): UserProfile | undefined {
    return this._profile;
  }

  async init() {
    // Check if Keycloak has been initialized
    if (this.isInitialized) {
      console.log('Keycloak is already initialized.');
      return this.keycloak?.authenticated;  // Return the current authentication state
    }

    try {
    const authenticated = await this.keycloak?.init({
      onLoad: 'check-sso',
      // silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
      pkceMethod: 'S256',
    });

    // Log authentication status
    console.log('Keycloak initialization complete. Authenticated:', authenticated);

    if (authenticated) {
      this._profile = await this.keycloak.loadUserProfile() as UserProfile;
      this._profile.token = this.keycloak.token || '';

      // Log user profile and token details (be cautious with sensitive information)
      console.log('User Profile:', this._profile);
      console.log('Token:', this._profile.token);
    } else {
      console.warn('User is not authenticated.');
    }

    this.isInitialized = true;  // Mark as initialized
    return authenticated;

    }
    catch (error) {
      console.error('Keycloak initialization failed:', error);
      return false;
    }
  }

  async login() {
    if (!this.isInitialized) {
      await this.init();  // Ensure initialization before login
    }
    console.log('Keycloak status at service login', this.keycloak);
    return this.keycloak?.login({
        redirectUri: 'http://localhost:4200/products',
      }
    );
  }

  async logout() {
    if (!this.isInitialized) {
      await this.init();  // Ensure initialization before logout
    }
    console.log('Keycloak status at service logout', this.keycloak);
    return this.keycloak?.logout({
        redirectUri: 'http://localhost:4200/products',
      }
    );
  }

  hasRole(role: string): boolean {
    const roles = this.keycloak?.tokenParsed?.resource_access?.['frontend']?.roles || [];
    console.log('Roles from Keycloak:', roles); // Log roles
    return roles.includes(role);  }

}
