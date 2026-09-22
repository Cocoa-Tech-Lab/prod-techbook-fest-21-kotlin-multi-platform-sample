// Common API envelope types matching backend

export type ApiSuccess<T> = {
  data: T;
};

export type ApiFailure = {
  errorInfo: {
    code: number;
    message: string;
  };
};

export type ApiResponse<T> = ApiSuccess<T> | ApiFailure;

export type Empty = Record<string, never>; // Kotlin data object likely serializes as {}
