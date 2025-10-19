import SwiftUI
import Shared

struct RootView: View {
    private let root: RootComponent
    
    init(_ root: RootComponent) {
        self.root = root
    }
        
    var body: some View {
        StackView(
            stackValue: StateValue(root.childStack),
            getTitle: { _ in "Heh" },
            onBack: { _ in
                root.onBackClicked()
            }
        ) { child in
            childView(for: child)
        }
        
    }
    
}

@ViewBuilder
private func childView(for child: RootComponentChild) -> some View {
    switch child {
    case let child as ChatChild:
        ChatView(component: child.component)
    default:
        EmptyView()
    }
}

private typealias ChatChild = RootComponentChild.Chat

struct RootView_Previews: PreviewProvider {
    static var previews: some View {
        RootView(PreviewRootComponent())
    }
}
