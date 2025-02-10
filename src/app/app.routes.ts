import {
  provideRouter,
  Routes,
  withInMemoryScrolling,
  withRouterConfig,
} from "@angular/router";

export const routes: Routes = [
  {
    path: "",
    loadComponent: () =>
      import("./components/homepage/homepage.component").then(
        ({ HomepageComponent }) => HomepageComponent
      ),
  },
  {
    path: "users",
    loadChildren: () =>
      import("./components/user/user.routes").then(({ routes }) => routes),
  },
  { path: "**", redirectTo: "" },
];

export const appRouteProvider = provideRouter(
  routes,
  withInMemoryScrolling({
    scrollPositionRestoration: "disabled",
  }),
  withRouterConfig({
    onSameUrlNavigation: "reload",
  })
);
