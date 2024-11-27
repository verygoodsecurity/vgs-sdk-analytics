import SwiftUI
import analytics

struct ContentView: View {
    
    let analytics = AnalyticsManager(vault: "<TENANT>", environment: "sandbox", source: "iosSDK", sourceVersion: "1.0.0")
    
	var body: some View {
        Button("Capture", action: capture)
	}
    
    func capture() {
        analytics.capture(event: Event.FieldInit(fieldType: "test", contentPath: "test"));
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
