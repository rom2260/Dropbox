export interface FileDTO {
  contentType: string;
  fileName: string;
  id: number;
  s3Path: string;
  size: number;
  uploadedTime: string;
}

export interface FileUploadUrlResponse {
  path: string;
  presignedUrl: string;
}
