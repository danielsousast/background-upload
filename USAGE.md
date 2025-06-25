# Expo Background Uploader

A native module for Expo/React Native that enables robust background file uploads on both Android and iOS, supporting progress, retries, notifications, cancellation, and custom headers.

---

## Features
- Background uploads (even when app is minimized or closed)
- Multiple concurrent uploads
- Progress & completion events
- Automatic retry on network failure
- Cancel individual or batch uploads
- Persistent queue (resume after restart)
- Custom headers & authentication
- Notifications (Android foreground service, iOS local notifications)
- Supports images, videos, documents, audio, etc.

---

## Installation

```bash
npm install expo-background-uploader
# or
yarn add expo-background-uploader
```

### Android Setup
- Add required permissions to `AndroidManifest.xml`:
  ```xml
  <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
  <uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
  ```
- If using ProGuard, add rules for WorkManager and OkHttp.
- No manual linking required for Expo modules.

### iOS Setup
- Add permissions to `Info.plist`:
  ```xml
  <key>NSPhotoLibraryUsageDescription</key>
  <string>Allow access to upload files</string>
  <key>UIBackgroundModes</key>
  <array>
    <string>fetch</string>
    <string>processing</string>
    <string>remote-notification</string>
  </array>
  ```
- Enable Background Modes > Background fetch & Remote notifications in Xcode Capabilities.
- Run `npx pod-install` after install.

---

## Basic Usage

```javascript
import BackgroundUploader from 'expo-background-uploader';

const uploadConfig = {
  url: 'https://api.example.com/upload',
  method: 'POST',
  headers: {
    Authorization: 'Bearer <token>',
    'Content-Type': 'multipart/form-data',
  },
  fieldName: 'file',
  maxRetries: 3,
  retryDelay: 5000,
};

// Start upload
const uploadId = await BackgroundUploader.startUpload(filePath, uploadConfig);

// Listen to progress
const progressSub = BackgroundUploader.addProgressListener(progress => {
  console.log(`Upload ${progress.id}: ${progress.percent}%`);
});

// Cancel upload
await BackgroundUploader.cancelUpload(uploadId);
```

---

## Advanced Usage

### Configure Global Options
```javascript
BackgroundUploader.configure({
  maxConcurrentUploads: 3,
  defaultTimeout: 60000,
  enableNotifications: true,
  retryPolicy: {
    maxRetries: 3,
    retryDelay: 5000,
    backoffMultiplier: 2,
  },
});
```

### Upload with Callbacks
```javascript
const uploadId = await BackgroundUploader.startUpload(filePath, {
  url: 'https://api.example.com/upload',
  headers: { Authorization: `Bearer ${token}` },
  fields: { customMeta: 'value' },
  onProgress: progress => setUploadProgress(progress.percent),
  onComplete: result => console.log('Upload finished:', result),
  onError: error => console.error('Upload error:', error),
});
```

---

## API Reference

### Methods
- `configure(options)` — Set global uploader options
- `startUpload(filePath, config)` — Start a new upload, returns uploadId
- `cancelUpload(uploadId)` — Cancel a specific upload
- `addProgressListener(callback)` — Listen for upload progress events
- `addCompletionListener(callback)` — Listen for upload completion events
- `addErrorListener(callback)` — Listen for upload error events

### UploadConfig Options
- `url` (string, required): Upload endpoint
- `method` (string, default: POST): HTTP method
- `headers` (object): Custom headers
- `fieldName` (string): Form field name for file
- `fields` (object): Additional form fields
- `maxRetries` (number): Number of retry attempts
- `retryDelay` (number): Milliseconds between retries
- `timeout` (number): Request timeout
- `chunkSize` (number): For large files (future)
- `onProgress` (function): Progress callback
- `onComplete` (function): Completion callback
- `onError` (function): Error callback

---

## Troubleshooting
- **Uploads not running in background:**
  - Ensure background modes/permissions are set on both platforms.
  - On Android, use a real device (emulators may kill background tasks).
  - On iOS, test with TestFlight/AdHoc for full background support.
- **Large files:**
  - May require chunked upload (future feature).
- **Authentication errors:**
  - Check if your token is valid and sent in headers.
- **Expo Go:**
  - Native modules do not work in Expo Go; use a development build.

---

## License
MIT

---

## Roadmap
- Queue prioritization
- Chunked upload
- Persistent retry queue
- Analytics hooks
- File compression
- More examples

---

## Contributing
Pull requests and suggestions are welcome!
