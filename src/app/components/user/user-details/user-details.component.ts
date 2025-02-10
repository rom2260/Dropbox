import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { FileService } from "../../../services/file.service";
import { MatCardModule } from "@angular/material/card";
import { MatListModule } from "@angular/material/list";
import { MatIconModule } from "@angular/material/icon";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatButtonModule } from "@angular/material/button";
import { MatProgressBarModule } from "@angular/material/progress-bar";
import { MatTooltipModule } from "@angular/material/tooltip";
import { MatPaginatorModule, PageEvent } from "@angular/material/paginator";
import { User } from "../user.model";
import { FileDTO } from "src/app/models/file";
import { ApiInProgressState, ApiState } from "src/app/models/loader";
import { MatTableModule } from "@angular/material/table";
import { MatDialog } from "@angular/material/dialog";
import { UploadFileDialogComponent } from "../../upload-file-dialog/upload-file-dialog.component";
import { FileSizePipe } from "../../../pipes/file-size.pipe";
import { DatePipe } from "@angular/common";

@Component({
  selector: "app-user-details",
  standalone: true,
  imports: [
    MatCardModule,
    MatListModule,
    MatIconModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatProgressBarModule,
    MatTooltipModule,
    MatPaginatorModule,
    MatTableModule,
    FileSizePipe,
    DatePipe
  ],
  templateUrl: "./user-details.component.html",
  styleUrls: ["./user-details.component.scss"],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UserDetailsComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private fileService = inject(FileService);
  private dialog = inject(MatDialog);

  protected readonly API_IN_PROGRESS_STATE = ApiInProgressState;

  protected readonly getUserFilesApiState = signal<ApiState<FileDTO[]>>({
    progress: null,
    data: null,
    error: null,
  });
  protected readonly downloadFileApiStateMap = signal<{
    [id: FileDTO["id"]]: ApiState<{url: string}>;
  }>({});
  protected readonly currentPage = signal(0);
  protected readonly pageSize = signal(10);
  protected readonly totalElements = signal(0);
  protected readonly totalPages = signal(0);

  protected readonly DISPLAYED_COLUMNS = [
    "fileName",
    "uploadedDate",
    "size",
    "contentType",
    "actions",
  ];

  ngOnInit() {
    const userId = this.route.snapshot.paramMap.get("id");
    if (userId) {
      this.loadUserFiles(+userId);
    }
  }

  private loadUserFiles(userId: User["id"]) {
    this.getUserFilesApiState.update((state) => ({
      ...state,
      progress: ApiInProgressState.LOADING,
    }));
    this.fileService
      .getUserFiles(userId, { page: this.currentPage(), size: this.pageSize() })
      .subscribe({
        next: (data) => {
          this.getUserFilesApiState.update((state) => ({
            ...state,
            data: data || [],
            progress: ApiInProgressState.SUCCESS,
          }));
        },
        error: (error) => {
          this.getUserFilesApiState.update((state) => ({
            ...state,
            progress: ApiInProgressState.ERROR,
            error: error.message,
          }));
        },
      });
  }

  protected openFileUploadDialog() {
    const userId = this.route.snapshot.paramMap.get("id");
    if (userId) {
      const dialogRef = this.dialog.open(UploadFileDialogComponent, {
        width: "500px",
        disableClose: true,
        data: { userId: +userId },
      });

      dialogRef.afterClosed().subscribe((result) => {
        if (result) {
          this.loadUserFiles(+userId);
        }
      });
    }
  }

  protected onPageChange(event: PageEvent) {
    this.currentPage.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    const userId = this.route.snapshot.paramMap.get("id");
    if (userId) {
      this.loadUserFiles(+userId);
    }
  }

  protected downloadFile(file: FileDTO) {
    this.addToDownloadFileApiMap(file.id, ApiInProgressState.LOADING);
    this.fileService.getDownloadUrl(file.s3Path).subscribe({
      next: (res: any) => {
        this.addToDownloadFileApiMap(file.id, ApiInProgressState.SUCCESS);
        window.open(res.url, "_blank");
      },
      error: (error) => {
        this.addToDownloadFileApiMap(file.id, ApiInProgressState.ERROR, error.message);
      },
    });
  }

  private addToDownloadFileApiMap(
    id: FileDTO["id"],
    progress: ApiInProgressState,
    error = ""
  ) {
    this.downloadFileApiStateMap.update((downloadFileMap) => {
      downloadFileMap[id] = { progress, error, data: null };
      return { ...downloadFileMap };
    });
  }
}
