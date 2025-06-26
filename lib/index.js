"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const react_native_1 = require("react-native");
const LINKING_ERROR = `The package 'expo-background-uploader' doesn't seem to be linked. Make sure:
\n\n` +
    react_native_1.Platform.select({
        ios: "\u2022 You have run 'pod install'\n",
        default: "",
    }) +
    "\u2022 You rebuilt the app after installing the package\n" +
    "\u2022 You are not using Expo Go\n";
const Uploader = react_native_1.NativeModules.BackgroundUploader
    ? react_native_1.NativeModules.BackgroundUploader
    : new Proxy({}, {
        get() {
            throw new Error(LINKING_ERROR);
        },
    });
const eventEmitter = new react_native_1.NativeEventEmitter(Uploader);
exports.default = {
    configure(config) {
        var _a;
        return (_a = Uploader.configure) === null || _a === void 0 ? void 0 : _a.call(Uploader, config);
    },
    async startUpload(filePath, config) {
        const result = await Uploader.uploadFile({ ...config, filePath });
        return result.id;
    },
    async cancelUpload(uploadId) {
        return Uploader.cancelUpload(uploadId);
    },
    addProgressListener(listener) {
        return eventEmitter.addListener("UploadProgress", listener);
    },
    addCompletionListener(listener) {
        return eventEmitter.addListener("UploadComplete", listener);
    },
    addErrorListener(listener) {
        return eventEmitter.addListener("UploadError", listener);
    },
};
