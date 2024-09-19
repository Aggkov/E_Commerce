import { Component } from '@angular/core';
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {httpTokenInterceptor} from "../../../interceptors/http-token.interceptor";

@Component({
  selector: 'app-admin-panel',
  standalone: true,
  imports: [],
  templateUrl: './admin-panel.component.html',
  styleUrl: './admin-panel.component.css',
  providers: [
    // { provide: HTTP_INTERCEPTORS, useValue: httpTokenInterceptor, multi: true }
  ]
})

export class AdminPanelComponent {

  constructor() {
    console.log('in admin panel~');
  }
}
