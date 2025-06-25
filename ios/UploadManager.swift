import Foundation
import UIKit

@objc(UploadManager)
class UploadManager: NSObject, URLSessionDelegate, URLSessionTaskDelegate, URLSessionDataDelegate {
    static let shared = UploadManager()
    private var session: URLSession!
    private var progressHandlers: [Int: (Double) -> Void] = [:]
    private var completionHandlers: [Int: (Bool, URLResponse?, Error?) -> Void] = [:]
    private override init() {
        super.init()
        let config = URLSessionConfiguration.background(withIdentifier: "com.backgrounduploader.session")
        config.isDiscretionary = false
        config.sessionSendsLaunchEvents = true
        session = URLSession(configuration: config, delegate: self, delegateQueue: nil)
    }

    @objc
    func startUpload(filePath: String, config: [String: Any], resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
        let fileUrl = URL(fileURLWithPath: filePath)
        guard let urlString = config["url"] as? String, let url = URL(string: urlString) else {
            reject("invalid_url", "URL inválida", nil)
            return
        }
        var request = URLRequest(url: url)
        request.httpMethod = config["method"] as? String ?? "POST"
        if let headers = config["headers"] as? [String: String] {
            for (k, v) in headers { request.setValue(v, forHTTPHeaderField: k) }
        }
        let task = session.uploadTask(with: request, fromFile: fileUrl)
        task.resume()
        resolve(task.taskIdentifier)
    }

    // MARK: - URLSession Delegate
    func urlSession(_ session: URLSession, task: URLSessionTask, didSendBodyData bytesSent: Int64, totalBytesSent: Int64, totalBytesExpectedToSend: Int64) {
        let progress = Double(totalBytesSent) / Double(totalBytesExpectedToSend)
        DispatchQueue.main.async {
            self.progressHandlers[task.taskIdentifier]?(progress)
        }
    }
    func urlSession(_ session: URLSession, task: URLSessionTask, didCompleteWithError error: Error?) {
        DispatchQueue.main.async {
            self.completionHandlers[task.taskIdentifier]?(error == nil, task.response, error)
        }
    }
}
