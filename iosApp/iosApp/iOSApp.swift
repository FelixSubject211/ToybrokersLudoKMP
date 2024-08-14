import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    
    init() {
        Koin().doInit()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
