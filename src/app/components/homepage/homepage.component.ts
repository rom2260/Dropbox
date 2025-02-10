import { ChangeDetectionStrategy, Component, inject } from "@angular/core";
import { Router } from "@angular/router";
import { MatButton } from "@angular/material/button";

@Component({
  selector: "app-homepage",
  standalone: true,
  imports: [MatButton],
  templateUrl: "./homepage.component.html",
  styleUrls: ["./homepage.component.scss"],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class HomepageComponent {
  private router = inject(Router);

  navigateToUsers() {
    this.router.navigate(["/users"]);
  }
}
