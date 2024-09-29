import { Routes } from '@angular/router';
import {ProductListComponent} from "./components/product-list/product-list.component";
import {ProductDetailsComponent} from "./components/product-details/product-details.component";
import {CartDetailsComponent} from "./components/cart-details/cart-details.component";
import {CheckoutComponent} from "./components/checkout/checkout.component";
import {OrderSuccessComponent} from "./components/order-success/order-success.component";
import {AdminPanelComponent} from "./components/admin/admin-panel/admin-panel.component";
import {authGuard} from "./guard/auth.guard";
import {UserOrdersComponent} from "./components/user-orders/user-orders.component";

export const routes: Routes = [
  {path: 'admin-panel', component: AdminPanelComponent, canActivate: [authGuard]}, // auth-guard
  {path: 'user-orders', component: UserOrdersComponent, canActivate: [authGuard]}, // auth-guard
  {path: 'search/:query', component: ProductListComponent },
  {path: 'category/:id/:name', component: ProductListComponent},
  {path: 'category', component: ProductListComponent},
  {path: 'products/:id', component: ProductDetailsComponent},
  {path: 'products', component: ProductListComponent},
  {path: 'cart-details', component: CartDetailsComponent},
  {path: 'checkout', component: CheckoutComponent},
  {path: 'success', component: OrderSuccessComponent},
  {path: '', redirectTo: '/products', pathMatch: 'full'},
  {path: '**', redirectTo: '/products', pathMatch: 'full'},

];
