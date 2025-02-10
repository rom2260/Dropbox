import { Routes } from "@angular/router";

export const routes: Routes = [
  {
    path: "",
    loadComponent: () =>
      import("./user-list/user-list.component").then(
        ({ UserListComponent }) => UserListComponent
      ),
  },
  {
    path: ":id",
    loadComponent: () =>
      import("./user-details/user-details.component").then(
        ({ UserDetailsComponent }) => UserDetailsComponent
      ),
  },
];
