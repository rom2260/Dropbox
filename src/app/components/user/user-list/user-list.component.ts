import {
  ChangeDetectionStrategy,
  Component,
  inject,
  OnInit,
  signal,
} from "@angular/core";
import { Router } from "@angular/router";
import { UserService } from "../user.service";
import { MatListModule } from "@angular/material/list";
import { MatIconModule } from "@angular/material/icon";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatDialog } from "@angular/material/dialog";
import { User } from "../user.model";
import { ApiInProgressState, ApiState } from "src/app/models/loader";
import { MatButtonModule } from "@angular/material/button";
import { AddUserDialogComponent } from "../add-user-dialog/add-user-dialog.component";
import { NgClass } from "@angular/common";
import { MatTooltip } from "@angular/material/tooltip";

@Component({
  selector: "app-user-list",
  standalone: true,
  imports: [
    MatListModule,
    MatIconModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    NgClass,
    MatTooltip,
  ],
  templateUrl: "./user-list.component.html",
  styleUrls: ["./user-list.component.scss"],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UserListComponent implements OnInit {
  private userService = inject(UserService);
  private router = inject(Router);
  private dialog = inject(MatDialog);

  protected readonly getUsersApiState = signal<ApiState<User[]>>({
    progress: null,
    data: null,
    error: null,
  });
  protected readonly deleteUserApiStateMap = signal<{
    [userId: string]: ApiState<void>;
  }>({});

  protected readonly API_IN_PROGRESS_STATE = ApiInProgressState;

  ngOnInit() {
    this.loadUsers();
  }

  private loadUsers() {
    this.getUsersApiState.update((state) => ({
      ...state,
      progress: ApiInProgressState.LOADING,
    }));
    this.userService.getUsers().subscribe({
      next: (data) => {
        this.getUsersApiState.update((state) => ({
          ...state,
          progress: ApiInProgressState.SUCCESS,
          data,
        }));
      },
      error: (error) => {
        this.getUsersApiState.update((state) => ({
          ...state,
          progress: ApiInProgressState.ERROR,
          error: error.message,
        }));
      },
    });
  }

  protected viewUserDetails(userId: number) {
    this.router.navigate(["/users", userId]);
  }

  protected addUser() {
    const dialogRef = this.dialog.open(AddUserDialogComponent, {
      width: "400px",
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadUsers();
      }
    });
  }

  protected deleteUser(userId: User["id"]) {
    this.addToDeleteApiMap(userId, ApiInProgressState.LOADING);
    this.userService.deleteUser(userId).subscribe({
      next: () => {
        this.addToDeleteApiMap(userId, ApiInProgressState.SUCCESS);
        this.loadUsers();
      },
      error: (error) => {
        this.addToDeleteApiMap(userId, ApiInProgressState.ERROR, error.message);
      },
    });
  }

  private addToDeleteApiMap(
    userId: User["id"],
    progress: ApiInProgressState,
    error = ""
  ) {
    this.deleteUserApiStateMap.update((deleteMap) => {
      deleteMap[userId] = { progress, error, data: null };
      return { ...deleteMap };
    });
  }
}
