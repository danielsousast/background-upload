#import <ExpoModulesCore/ExpoModulesCore.h>
#import "BackgroundUploader.h"

@implementation BackgroundUploader

EX_EXPORT_MODULE(BackgroundUploader);

EX_EXPORT_METHOD_AS(uploadFile,
                    uploadFile:(NSDictionary *)config
                    resolver:(EXPromiseResolveBlock)resolve
                    rejecter:(EXPromiseRejectBlock)reject)
{
  NSString *filePath = config[@"filePath"];
  if (!filePath) {
    reject(@"INVALID_PARAMS", @"File path is required", nil);
    return;
  }
  
  // TODO: Implement actual upload logic
  NSString *uploadId = [[NSUUID UUID] UUIDString];
  resolve(@{
    @"id": uploadId,
    @"status": @"queued"
  });
}

EX_EXPORT_METHOD_AS(cancelUpload,
                    cancelUpload:(NSString *)uploadId
                    resolver:(EXPromiseResolveBlock)resolve
                    rejecter:(EXPromiseRejectBlock)reject)
{
  // TODO: Implement cancel logic
  resolve(@YES);
}

@end
