import { NativeEventEmitter, NativeModules, Platform } from "react-native";
import type {
  UploadConfig,
  UploaderConfig,
  UploadProgress,
  UploadResult,
} from "./types";

const LINKING_ERROR =
  `The package 'expo-background-uploader' doesn't seem to be linked. Make sure:
\n\n` +
  Platform.select({
    ios: "\u2022 You have run 'pod install'\n",
    default: "",
  }) +
  "\u2022 You rebuilt the app after installing the package\n" +
  "\u2022 You are not using Expo Go\n";

const Uploader = NativeModules.BackgroundUploader
  ? NativeModules.BackgroundUploader
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

const eventEmitter = new NativeEventEmitter(Uploader);

export default {
  configure(config: UploaderConfig) {
    return Uploader.configure?.(config);
  },
  async startUpload(filePath: string, config: UploadConfig): Promise<string> {
    return Uploader.startUpload(filePath, config);
  },
  async cancelUpload(uploadId: string): Promise<void> {
    return Uploader.cancelUpload(uploadId);
  },
  addProgressListener(listener: (progress: UploadProgress) => void) {
    return eventEmitter.addListener("UploadProgress", listener);
  },
  addCompletionListener(listener: (result: UploadResult) => void) {
    return eventEmitter.addListener("UploadComplete", listener);
  },
  addErrorListener(listener: (error: any) => void) {
    return eventEmitter.addListener("UploadError", listener);
  },
};
