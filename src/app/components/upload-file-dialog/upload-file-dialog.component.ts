import { Component, inject, signal, Inject } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { FileService } from "../../services/file.service";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatProgressBarModule } from "@angular/material/progress-bar";
import { MatDialogModule } from "@angular/material/dialog";
import { ApiInProgressState, ApiState } from "src/app/models/loader";
import { MatTooltip } from "@angular/material/tooltip";
import { User } from "../user/user.model";
import { switchMap, map, Observable } from "rxjs";
import { FileDTO, FileUploadUrlResponse } from "src/app/models/file";
import { FileSizePipe } from "src/app/pipes/file-size.pipe";

@Component({
  selector: "app-upload-file-dialog",
  standalone: true,
  imports: [
    MatButtonModule,
    MatIconModule,
    MatProgressBarModule,
    MatDialogModule,
    MatTooltip,
    FileSizePipe
  ],
  templateUrl: "./upload-file-dialog.component.html",
  styleUrls: ["./upload-file-dialog.component.scss"],
})
export class UploadFileDialogComponent {
  private dialogRef = inject(MatDialogRef<UploadFileDialogComponent>);
  private fileService = inject(FileService);
  protected data: { userId: User['id'] } = inject(MAT_DIALOG_DATA);

  protected readonly API_IN_PROGRESS_STATE = ApiInProgressState;
  protected readonly selectedFile = signal<File | null>(null);
  protected readonly uploadFileApiState = signal<ApiState<void>>({
    progress: null,
    data: null,
    error: null,
  });

  protected onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile.set(input.files[0]);
      this.uploadFileApiState.set({ progress: null, data: null, error: null });
    }
  }

  protected removeFile(): void {
    this.selectedFile.set(null);
    this.uploadFileApiState.set({ progress: null, data: null, error: null });
  }

  protected upload(): void {
    if (!this.selectedFile()) return;
  
    this.uploadFileApiState.update((state) => ({
      ...state,
      progress: ApiInProgressState.LOADING,
    }));
  
    this.getUploadUrl$()
      .pipe(
        switchMap((uploadUrlResponse) =>
          this.uploadFile$(uploadUrlResponse).pipe(
            map(() => uploadUrlResponse)
          )
        ),
        switchMap((uploadUrlResponse) => this.saveMetadata$(uploadUrlResponse))
      )
      .subscribe({
        next: () => {
          this.uploadFileApiState.update((state) => ({
            ...state,
            progress: ApiInProgressState.SUCCESS,
          }));
          this.dialogRef.close(true);
        },
        error: (error) => {
          this.uploadFileApiState.update((state) => ({
            ...state,
            progress: ApiInProgressState.ERROR,
            error: error.message,
          }));
        },
      });
  }
  
  private getUploadUrl$(): Observable<FileUploadUrlResponse> {
    return this.fileService.getUploadUrl(this.selectedFile()!.name);
  }
  
  private uploadFile$(uploadUrlResponse: FileUploadUrlResponse): Observable<any> {
    return this.fileService.uploadFileToPresignedUrl(
      uploadUrlResponse.presignedUrl,
      this.selectedFile()!
    );
  }
  
  private saveMetadata$(uploadUrlResponse: FileUploadUrlResponse): Observable<FileDTO> {
    const metadata = {
      fileName: this.selectedFile()!.name,
      s3Path: uploadUrlResponse.path,
      userId: this.data.userId,
      size: this.selectedFile()!.size,
      contentType: this.selectedFile()!.type,
    };
    return this.fileService.saveFileMetadata(metadata);
  }
  

  protected onCancel(): void {
    this.dialogRef.close();
  }
}
