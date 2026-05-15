import { Routes } from '@angular/router';
import { authGuard, tenantGuard } from '@core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () =>
      import('@features/auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'select-tenant',
    loadComponent: () =>
      import('@features/auth/select-tenant/select-tenant.component').then(m => m.SelectTenantComponent),
    canActivate: [tenantGuard]
  },
  {
    path: '',
    loadComponent: () =>
      import('./layout/layout.component').then(m => m.LayoutComponent),
    canActivate: [authGuard],
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        loadComponent: () =>
          import('@features/dashboard/dashboard.component').then(m => m.DashboardComponent),
        data: { breadcrumb: 'nav.dashboard', icon: 'pi pi-home' }
      },
      {
        path: 'inbound',
        data: { breadcrumb: 'nav.inbound', icon: 'pi pi-download' },
        children: [
          {
            path: '',
            redirectTo: 'orders',
            pathMatch: 'full'
          },
          {
            path: 'orders',
            loadComponent: () =>
              import('@features/inbound/orders/inbound-orders.component').then(m => m.InboundOrdersComponent),
            data: { breadcrumb: 'nav.inbound_orders' }
          },
          {
            path: 'orders/new',
            loadComponent: () =>
              import('@features/inbound/entry/inbound-order-entry.component').then(m => m.InboundOrderEntryComponent),
            data: { breadcrumb: 'nav.inbound_orders' }
          },
          {
            path: 'orders/:arrivalNo/edit',
            loadComponent: () =>
              import('@features/inbound/modify/inbound-order-modify.component').then(m => m.InboundOrderModifyComponent),
            data: { breadcrumb: 'nav.inbound_order_modify' }
          },
          {
            path: 'orders/:arrivalNo',
            loadComponent: () =>
              import('@features/inbound/detail/inbound-order-detail.component').then(m => m.InboundOrderDetailComponent),
            data: { breadcrumb: 'nav.inbound_order_detail' }
          },
          {
            path: 'receive',
            loadComponent: () =>
              import('@features/inbound/receive/inbound-receive.component').then(m => m.InboundReceiveComponent),
            data: { breadcrumb: 'nav.inbound_receive' }
          },
          {
            path: 'inspection',
            loadComponent: () =>
              import('@features/inbound/inspection/inbound-inspection.component').then(m => m.InboundInspectionComponent),
            data: { breadcrumb: 'nav.inbound_inspection' }
          },
          {
            path: 'confirm',
            loadComponent: () =>
              import('@features/inbound/confirm/inbound-confirm.component').then(m => m.InboundConfirmComponent),
            data: { breadcrumb: 'nav.inbound_confirm' }
          },
          {
            path: 'putaway',
            loadComponent: () =>
              import('@features/inbound/putaway/inbound-putaway.component').then(m => m.InboundPutawayComponent),
            data: { breadcrumb: 'nav.inbound_putaway' }
          }
        ]
      },
      {
        path: 'outbound',
        data: { breadcrumb: 'nav.outbound', icon: 'pi pi-upload' },
        children: [
          {
            path: '',
            redirectTo: 'orders',
            pathMatch: 'full'
          },
          {
            path: 'orders',
            loadComponent: () =>
              import('@features/outbound/orders/outbound-orders.component').then(m => m.OutboundOrdersComponent),
            data: { breadcrumb: 'nav.outbound_orders' }
          },
          {
            path: 'picking',
            loadComponent: () =>
              import('@features/outbound/picking/outbound-picking.component').then(m => m.OutboundPickingComponent),
            data: { breadcrumb: 'nav.outbound_picking' }
          },
          {
            path: 'packing',
            loadComponent: () =>
              import('@features/outbound/packing/outbound-packing.component').then(m => m.OutboundPackingComponent),
            data: { breadcrumb: 'nav.outbound_packing' }
          },
          {
            path: 'shipping',
            loadComponent: () =>
              import('@features/outbound/shipping/outbound-shipping.component').then(m => m.OutboundShippingComponent),
            data: { breadcrumb: 'nav.outbound_shipping' }
          }
        ]
      },
      {
        path: 'inventory',
        data: { breadcrumb: 'nav.inventory', icon: 'pi pi-box' },
        children: [
          {
            path: '',
            redirectTo: 'stock',
            pathMatch: 'full'
          },
          {
            path: 'stock',
            loadComponent: () =>
              import('@features/inventory/stock/inventory-stock.component').then(m => m.InventoryStockComponent),
            data: { breadcrumb: 'nav.inventory_stock' }
          },
          {
            path: 'adjustment',
            loadComponent: () =>
              import('@features/inventory/adjustment/inventory-adjustment.component').then(m => m.InventoryAdjustmentComponent),
            data: { breadcrumb: 'nav.inventory_adjustment' }
          },
          {
            path: 'transfer',
            loadComponent: () =>
              import('@features/inventory/transfer/inventory-transfer.component').then(m => m.InventoryTransferComponent),
            data: { breadcrumb: 'nav.inventory_transfer' }
          },
          {
            path: 'count',
            loadComponent: () =>
              import('@features/inventory/count/inventory-count.component').then(m => m.InventoryCountComponent),
            data: { breadcrumb: 'nav.inventory_count' }
          },
          {
            path: 'lpn',
            loadComponent: () =>
              import('@features/lpn/lpn-inquiry/lpn-inquiry.component').then(m => m.LpnInquiryComponent),
            data: { breadcrumb: 'nav.inventory_lpn' }
          },
          {
            path: 'lpn/:lpnNumber',
            loadComponent: () =>
              import('@features/lpn/lpn-detail/lpn-detail.component').then(m => m.LpnDetailComponent),
            data: { breadcrumb: 'nav.inventory_lpn_detail' }
          }
        ]
      },
      {
        path: 'master',
        data: { breadcrumb: 'nav.master', icon: 'pi pi-database' },
        children: [
          {
            path: '',
            redirectTo: 'products',
            pathMatch: 'full'
          },
          {
            path: 'products',
            loadComponent: () =>
              import('@features/master/products/master-products.component').then(m => m.MasterProductsComponent),
            data: { breadcrumb: 'nav.master_products' }
          },
          {
            path: 'locations',
            loadComponent: () =>
              import('@features/master/locations/master-locations.component').then(m => m.MasterLocationsComponent),
            data: { breadcrumb: 'nav.master_locations' }
          },
          {
            path: 'customers',
            loadComponent: () =>
              import('@features/master/customers/master-customers.component').then(m => m.MasterCustomersComponent),
            data: { breadcrumb: 'nav.master_customers' }
          },
          {
            path: 'warehouses',
            loadComponent: () =>
              import('@features/master/warehouses/master-warehouses.component').then(m => m.MasterWarehousesComponent),
            data: { breadcrumb: 'nav.master_warehouses' }
          },
          {
            path: 'users',
            loadComponent: () =>
              import('@features/master/users/master-users.component').then(m => m.MasterUsersComponent),
            data: { breadcrumb: 'nav.master_users' }
          },
          {
            path: 'lpn-config',
            loadComponent: () =>
              import('@features/master/lpn-config/master-lpn-config.component').then(m => m.MasterLpnConfigComponent),
            data: { breadcrumb: 'nav.master_lpn_config' }
          }
        ]
      },
      {
        path: 'reports',
        data: { breadcrumb: 'nav.reports', icon: 'pi pi-chart-bar' },
        loadComponent: () =>
          import('@features/reports/reports.component').then(m => m.ReportsComponent)
      },
      {
        path: 'warehouse-optimize',
        data: { breadcrumb: 'Warehouse Optimize', icon: 'pi pi-compass' },
        children: [
          {
            path: '',
            redirectTo: 'design',
            pathMatch: 'full'
          },
          {
            path: 'design',
            loadComponent: () =>
              import('@features/warehouse-optimize/warehouse-optimize-design.component').then(m => m.WarehouseOptimizeDesignComponent),
            data: { breadcrumb: 'Design' }
          },
          {
            path: 'view-2d',
            loadComponent: () =>
              import('@features/warehouse-optimize/warehouse-optimize-view2d.component').then(m => m.WarehouseOptimizeView2dComponent),
            data: { breadcrumb: '2D Result' }
          },
          {
            path: 'view-3d',
            loadComponent: () =>
              import('@features/warehouse-optimize/warehouse-optimize-view3d.component').then(m => m.WarehouseOptimizeView3dComponent),
            data: { breadcrumb: '3D Live' }
          },
          {
            path: 'slotting',
            loadComponent: () =>
              import('@features/warehouse-optimize/warehouse-optimize-slotting.component').then(m => m.WarehouseOptimizeSlottingComponent),
            data: { breadcrumb: 'Slotting' }
          },
          {
            path: 'routes',
            loadComponent: () =>
              import('@features/warehouse-optimize/warehouse-optimize-routes.component').then(m => m.WarehouseOptimizeRoutesComponent),
            data: { breadcrumb: 'Routes' }
          },
          {
            path: 'analytics',
            loadComponent: () =>
              import('@features/warehouse-optimize/warehouse-optimize-analytics.component').then(m => m.WarehouseOptimizeAnalyticsComponent),
            data: { breadcrumb: 'Analytics' }
          }
        ]
      }
    ]
  },
  {
    path: '**',
    redirectTo: ''
  }
];
