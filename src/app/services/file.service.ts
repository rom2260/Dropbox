import { inject, Injectable } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "../../environments/environment";
import { Observable, of, catchError, throwError } from "rxjs";
import { User } from "../components/user/user.model";
import { FileDTO, FileUploadUrlResponse } from "../models/file"

@Injectable({
  providedIn: "root",
})
export class FileService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/api/files`;

  public getUserFiles(
    userId: User["id"],
    params: { page: number; size: number }
  ): Observable<FileDTO[]> {
    return this.http.get<FileDTO[]>(`${this.apiUrl}/user/${userId}`, {
      params,
    });
  }

  public getUploadUrl(fileName: string): Observable<FileUploadUrlResponse> {
    return this.http.post<FileUploadUrlResponse>(
      `${this.apiUrl}/generate-upload-url`,
      { fileName }
    );
  }

  public getDownloadUrl(s3Path: string): Observable<{ url: string }> {
    return this.http.get<{ url: string }>(`${this.apiUrl}/download-url`, {
      params: { s3Path },
    });
  }


  public uploadFileToPresignedUrl(url: string, file: File): Observable<any> {
    return this.http.put(url, file, {
      headers: {
        "Content-Type": file.type,
      },
      withCredentials: false,
      observe: "response",
    });
  }


  public saveFileMetadata(metadata: Pick<FileDTO, 'contentType' | 's3Path' | 'fileName' | 'size'> & {userId: User['id']}): Observable<FileDTO> {
    return this.http.post<FileDTO>(`${this.apiUrl}/metadata`, metadata);
  }
}
