import { Component, inject, OnInit, signal } from "@angular/core";
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
} from "@angular/forms";
import { MatDialogRef } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatButtonModule } from "@angular/material/button";
import { UserService } from "../user.service";
import { ApiState, ApiInProgressState } from "src/app/models/loader";
import { User } from "../user.model";
import { MatTooltipModule } from "@angular/material/tooltip";

@Component({
  selector: "app-add-user-dialog",
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatTooltipModule,
  ],
  templateUrl: "./add-user-dialog.component.html",
  styleUrls: ["./add-user-dialog.component.scss"],
})
export class AddUserDialogComponent implements OnInit {
  private fb = inject(FormBuilder);
  private dialogRef = inject(MatDialogRef<AddUserDialogComponent>);
  private userService = inject(UserService);

  protected userForm: FormGroup = new FormGroup({});
  protected readonly addUserApiState = signal<ApiState<User>>({
    progress: null,
    data: null,
    error: null,
  });
  protected readonly API_IN_PROGRESS_STATE = ApiInProgressState;

  protected get username() {
    return this.userForm.get("username");
  }

  protected get email() {
    return this.userForm.get("email");
  }

  ngOnInit(): void {
    this.initializeForm();
  }

  private initializeForm(): void {
    this.userForm = this.fb.group({
      username: ["", Validators.required],
      email: ["", [Validators.required, Validators.email]],
    });
  }

  protected onSubmit(): void {
    if (this.userForm.valid) {
      this.addUserApiState.update((state) => ({
        ...state,
        progress: ApiInProgressState.LOADING,
      }));
      this.userService.createUser(this.userForm.value).subscribe({
        next: (user) => {
          this.addUserApiState.update((state) => ({
            ...state,
            progress: ApiInProgressState.SUCCESS,
            data: user,
          }));
          this.dialogRef.close(user);
        },
        error: (error) => {
          this.addUserApiState.update((state) => ({
            ...state,
            progress: ApiInProgressState.ERROR,
            error: error.message,
          }));
        },
      });
    }
  }

  protected onCancel(): void {
    this.dialogRef.close();
  }
}
