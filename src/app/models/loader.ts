export enum ApiInProgressState {
  LOADING = "LOADING",
  ERROR = "ERROR",
  SUCCESS = "SUCCESS",
}

export interface ApiState<T> {
  progress: ApiInProgressState | null;
  data: T | null;
  error: string | null;
}
