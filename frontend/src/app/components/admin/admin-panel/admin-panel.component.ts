import {Component, OnInit} from '@angular/core';
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {httpTokenInterceptor} from "../../../interceptors/http-token.interceptor";
import {KeycloakService} from "../../../services/keycloak/keycloak.service";
import {NgIf} from "@angular/common";
import {AdminProductTableComponent} from "../admin-product-table/admin-product-table.component";

@Component({
  selector: 'app-admin-panel',
  standalone: true,
  imports: [
    NgIf,
    AdminProductTableComponent
  ],
  templateUrl: './admin-panel.component.html',
  styleUrl: './admin-panel.component.css',
  providers: [
    // { provide: HTTP_INTERCEPTORS, useValue: httpTokenInterceptor, multi: true }
  ]
})

export class AdminPanelComponent implements OnInit{

  isAdminUser: boolean = false;

  constructor(private keycloakService: KeycloakService) {}

  async ngOnInit() {
    await this.keycloakService.init();  // Initialize Keycloak
    this.isAdminUser = this.keycloakService.hasRole('admin');  // Check if the user has the 'admin' role
  }

  isAdmin() {
    return this.isAdminUser;
  }
}
