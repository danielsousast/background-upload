import BackgroundUploader from "expo-background-uploader";
import * as DocumentPicker from "expo-document-picker";
import React, { useState } from "react";
import {
  ActivityIndicator,
  Button,
  StyleSheet,
  Text,
  View,
} from "react-native";

export default function ExampleUploadScreen() {
  const [file, setFile] = useState<any>(null);
  const [uploading, setUploading] = useState(false);
  const [progress, setProgress] = useState(0);
  const [status, setStatus] = useState("");

  const pickFile = async () => {
    const result = await DocumentPicker.getDocumentAsync({});
    if (result.type === "success") {
      setFile(result);
      setStatus("File selected: " + result.name);
    } else {
      setStatus("No file selected");
    }
  };

  const uploadFile = async () => {
    if (!file) return;
    setUploading(true);
    setStatus("Uploading...");
    setProgress(0);

    try {
      const uploadId = BackgroundUploader.startUpload(file.uri, {
        url: "https://httpbin.org/post", // replace with your upload endpoint
        method: "POST",
        headers: {
          "content-type": "application/octet-stream",
        },
        fields: {
          file: file.name,
        },
        type: "raw",
        // parameters: {},
      });

      BackgroundUploader.addProgressListener(({ uploadId, progress }) => {
        if (uploadId === uploadId) {
          setProgress(progress);
        }
      });
      BackgroundUploader.addCompletionListener(
        ({ uploadId, responseCode, responseBody }) => {
          if (uploadId === uploadId) {
            setStatus("Upload completed! Response: " + responseCode);
            setUploading(false);
          }
        }
      );
      BackgroundUploader.addErrorListener(({ uploadId, error }) => {
        if (uploadId === uploadId) {
          setStatus("Upload error: " + error);
          setUploading(false);
        }
      });
    } catch (err) {
      setStatus("Upload failed: " + err.message);
      setUploading(false);
    }
  };

  return (
    <View style={styles.container}>
      <Button title="Escolher arquivo" onPress={pickFile} />
      {file && <Text style={styles.fileName}>{file.name}</Text>}
      <Button
        title="Fazer upload"
        onPress={uploadFile}
        disabled={!file || uploading}
      />
      {uploading && (
        <View style={styles.progressContainer}>
          <ActivityIndicator size="small" color="#007AFF" />
          <Text>Progresso: {(progress * 100).toFixed(2)}%</Text>
        </View>
      )}
      <Text style={styles.status}>{status}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
    padding: 16,
    backgroundColor: "#fff",
  },
  fileName: {
    marginVertical: 8,
    fontWeight: "bold",
  },
  progressContainer: {
    flexDirection: "row",
    alignItems: "center",
    marginVertical: 8,
    gap: 8,
  },
  status: {
    marginTop: 12,
    color: "#333",
  },
});
