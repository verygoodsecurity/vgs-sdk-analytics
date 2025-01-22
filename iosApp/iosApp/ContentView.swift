import SwiftUI
import analytics

struct ContentView: View {
  
  let analytics = VGSSharedAnalyticsManager(source: "analyticsIosDemo", sourceVersion: "1.0.0", dependencyManager: "demoDependancyManager")
  
  var body: some View {
    Button("Capture", action: capture)
  }
  
  func capture() {
    analytics.capture(vault: "<TENANT>", environment: "sandbox", formId: "demoId", event: VGSAnalyticsEvent.FieldAttach(fieldType: "test", contentPath: "test", ui: "demoUI"));
  }
}

struct ContentView_Previews: PreviewProvider {
  static var previews: some View {
    ContentView()
  }
}
