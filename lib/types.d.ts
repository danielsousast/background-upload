export interface UploadConfig {
    url: string;
    method?: 'POST' | 'PUT';
    headers?: Record<string, string>;
    fieldName?: string;
    fields?: Record<string, string>;
    maxRetries?: number;
    retryDelay?: number;
    timeout?: number;
    chunkSize?: number;
    onProgress?: (progress: UploadProgress) => void;
    onComplete?: (result: UploadResult) => void;
    onError?: (error: any) => void;
}
export interface UploaderConfig {
    maxConcurrentUploads?: number;
    defaultTimeout?: number;
    enableNotifications?: boolean;
    retryPolicy?: {
        maxRetries?: number;
        retryDelay?: number;
        backoffMultiplier?: number;
    };
}
export interface UploadProgress {
    id: string;
    percent: number;
    bytesSent: number;
    totalBytes: number;
    speed?: number;
    eta?: number;
}
export interface UploadResult {
    id: string;
    status: 'success' | 'cancelled' | 'failed';
    response?: any;
    error?: any;
}
