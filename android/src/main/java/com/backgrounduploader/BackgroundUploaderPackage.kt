package com.backgrounduploader

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class BackgroundUploaderPackage : expo.modules.core.interfaces.Package {
  override fun createExpoModules(): List<Module> {
    return listOf(BackgroundUploaderModule())
  }
}