import type { UploadConfig, UploaderConfig, UploadProgress, UploadResult } from "./types";
declare const _default: {
    configure(config: UploaderConfig): any;
    startUpload(filePath: string, config: UploadConfig): Promise<string>;
    cancelUpload(uploadId: string): Promise<void>;
    addProgressListener(listener: (progress: UploadProgress) => void): import("react-native").EmitterSubscription;
    addCompletionListener(listener: (result: UploadResult) => void): import("react-native").EmitterSubscription;
    addErrorListener(listener: (error: any) => void): import("react-native").EmitterSubscription;
};
export default _default;
