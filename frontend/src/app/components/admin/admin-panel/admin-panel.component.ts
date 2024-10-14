import {Component, OnInit} from '@angular/core';
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {httpTokenInterceptor} from "../../../interceptors/http-token.interceptor";
import {KeycloakService} from "../../../services/keycloak/keycloak.service";
import {NgForOf, NgIf} from "@angular/common";
import {AdminProductTableComponent} from "../admin-product-table/admin-product-table.component";
import {ExportService} from "../../../services/export/export.service";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-admin-panel',
  standalone: true,
  imports: [
    NgIf,
    AdminProductTableComponent,
    NgForOf,
    RouterLink
  ],
  templateUrl: './admin-panel.component.html',
  styleUrl: './admin-panel.component.css',
  providers: [
    // { provide: HTTP_INTERCEPTORS, useValue: httpTokenInterceptor, multi: true }
  ]
})

export class AdminPanelComponent implements OnInit{

  isAdminUser: boolean = false;
  options = [
    { label: 'Excel', value: 'excel' },
    { label: 'CSV', value: 'csv' },
    { label: 'JSON', value: 'json' },
    { label: 'YAML', value: 'yaml' }
  ];

  constructor(private keycloakService: KeycloakService,
              private exportService: ExportService) {}

  async ngOnInit() {
    await this.keycloakService.init();  // Initialize Keycloak
    this.isAdminUser = this.keycloakService.hasRole('admin');  // Check if the user has the 'admin' role
  }

  isAdmin() {
    return this.isAdminUser;
  }

  onExportFormatSelected(event: Event): void {
    const selectedValue = (event.target as HTMLSelectElement).value;
    this.exportFile(selectedValue);
  }

  private exportFile(format: string) {
    switch (format) {
      case 'excel':
        this.exportService.export(format);
        break;
      case 'csv':
        this.exportService.export(format);
        break;
      case 'json':
        this.exportService.export(format);
        break;
      case 'yaml':
        this.exportService.export(format);
        break;
      default:
        console.log('Unknown format');
        break;
    }
  }
}
