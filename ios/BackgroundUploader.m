#import "BackgroundUploader.h"

@implementation BackgroundUploader

RCT_EXPORT_MODULE();

// Example stub method
RCT_EXPORT_METHOD(startUpload:(NSString *)filePath config:(NSDictionary *)config resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
  // TODO: Implement background upload using URLSession
  resolve(@"mock-upload-id");
}

@end
