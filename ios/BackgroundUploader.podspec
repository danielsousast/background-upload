Pod::Spec.new do |s|
  s.name             = 'BackgroundUploader'
  s.version          = '0.1.0'
  s.summary          = 'Background file uploader for React Native/Expo.'
  s.description      = <<-DESC
                       Background uploads with progress, retries, and notifications for iOS.
                       DESC
  s.homepage         = 'https://github.com/seuprojeto/expo-background-uploader'
  s.license          = { :type => 'MIT', :file => '../LICENSE' }
  s.author           = { 'Seu Nome' => 'email@exemplo.com' }
  s.source           = { :git => 'https://github.com/seuprojeto/expo-background-uploader.git', :tag => s.version.to_s }
  s.source_files     = 'BackgroundUploader.{h,m}', 'UploadManager.swift'
  s.requires_arc     = true
  s.platform     = :ios, '13.0'
  s.dependency 'React-Core'
end
