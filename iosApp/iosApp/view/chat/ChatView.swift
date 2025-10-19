import SwiftUI
import Shared

struct ChatView: View {
    private let component: ChatComponent

    @StateValue
    private var model: ChatComponentModel

    @StateValue
    private var bottomSheetSlot: ChildSlot<AnyObject, ChatComponentBottomSheetChild>

    @FocusState private var isInputFocused: Bool

    init(component: ChatComponent) {
        self.component = component
        _model = StateValue(component.model)
        _bottomSheetSlot = StateValue(component.childBottomSheetNavigation)
    }

    var body: some View {
        ZStack {
            AnimatedAuroraBackground()
                .ignoresSafeArea()

            // Messages or Empty State
            if model.messages.isEmpty && !model.isLoading {
                EmptyStateView(
                    selectedComplexity: Binding(
                        get: { model.selectedComplexity },
                        set: component.onComplexitySelected
                    ),
                    onGenerateRandom: component.onGenerateRandom
                )
                    .transition(.opacity.animation(.easeInOut(duration: 0.5)))
            } else {
                MessagesListView(
                    messages: model.messages,
                    isLoading: model.isLoading
                )
                    .transition(.opacity.animation(.easeInOut(duration: 0.4)))
            }

            VStack(spacing: 0) {
                Spacer()
                // Error Banner
                if let error = model.error {
                    ErrorBannerView(error: error, onDismiss: component.onClearError)
                        .padding(.horizontal)
                        .padding(.bottom, 4)
                        .transition(.move(edge: .bottom).combined(with: .opacity))
                }

                // Input Area
                InputAreaView(
                    inputText: Binding(
                        get: { model.inputText },
                        set: component.onInputTextChanged
                    ),
                    isLoading: model.isLoading,
                    onSend: component.onSendMessage,
                    onFilterTap: component.onFilterClick
                )
                    .focused($isInputFocused)
            }
        }
        .navigationTitle("Idea Generator")
        .navigationBarTitleDisplayMode(.large)
        .sheet(item: Binding<SheetItem?>(
            get: {
                if let child = bottomSheetSlot.child?.instance {
                    return SheetItem(child: child)
                } else {
                    return nil
                }
            },
            set: { item in
                if item == nil {
                    component.onDismissBottomSheet()
                }
            }
        )) { sheetItem in
            SheetContainerView(child: sheetItem.child)
        }
    }
}

// MARK: - Empty State View
struct EmptyStateView: View {
    @Binding var selectedComplexity: ComplexityLevel
    let onGenerateRandom: () -> Void

    var body: some View {
        VStack(spacing: 32) {
            Spacer()

            VStack(spacing: 16) {
                Image(systemName: "sparkles")
                    .font(.system(size: 60))
                    .foregroundStyle(.white)
                    .shadow(color: .purple.opacity(0.8), radius: 20)

                Text("Let's find a new idea")
                    .font(.title2.bold())
                    .foregroundStyle(.white)
            }

            VStack(spacing: 16) {
                Text("Select complexity")
                    .font(.headline)
                    .foregroundStyle(.white.opacity(0.7))

                Picker("Complexity", selection: $selectedComplexity) {
                    ForEach(ComplexityLevel.entries, id: \.name) { level in
                        Text(level.displayName).tag(level)
                    }
                }
                .pickerStyle(.segmented)
                .background(Color.white.opacity(0.1))
                .cornerRadius(8)
            }
            .padding(.horizontal, 40)

            GlassButton(
                title: "Generate Random Idea",
                icon: "lightbulb.fill",
                action: onGenerateRandom
            )
                .padding(.horizontal, 40)

            Spacer()
            Spacer()
        }
    }
}


// MARK: - Messages List
struct MessagesListView: View {
    let messages: [ChatMessage]
    let isLoading: Bool

    var body: some View {
        ScrollViewReader { proxy in
            ScrollView {
                LazyVStack(spacing: 20) {
                    ForEach(messages, id: \.id) { message in
                        MessageBubbleView(message: message)
                            .id(message.id)
                    }

                    if isLoading {
                        LoadingIndicatorView()
                    }
                }
                .padding(.horizontal)
                .padding(.bottom, 100)
            }
            .ignoresSafeArea(.container, edges: .bottom)
            .onChange(of: messages.count) {
                if let lastMessageId = messages.last?.id {
                    withAnimation(.spring()) {
                        proxy.scrollTo(lastMessageId, anchor: .bottom)
                    }
                }
            }
        }
    }
}

// MARK: - Message Bubble with Glass Effect
struct MessageBubbleView: View {
    let message: ChatMessage

    var body: some View {
        HStack(alignment: .bottom, spacing: 10) {
            // Иконка для ассистента
            if !message.isUser {
                Image(systemName: "sparkles")
                    .font(.title2)
                    .foregroundStyle(LinearGradient(colors: [.purple, .blue], startPoint: .top, endPoint: .bottom))
                    .frame(width: 30, height: 30)
            }

            HStack {
                if message.isUser {
                    Spacer(minLength: 50)
                }

                VStack(alignment: message.isUser ? .trailing : .leading, spacing: 8) {
                    Text(message.text)
                    .font(.body)
                    .padding(16)
                    .background {
                        if message.isUser {
                            LinearGradient(
                                colors: [.blue, .purple.opacity(0.8)],
                                startPoint: .topLeading,
                                endPoint: .bottomTrailing
                            )
                        } else {
                            Color.white.opacity(0.15)
                        }
                    }
                    .foregroundStyle(.white)
                    .clipShape(RoundedRectangle(cornerRadius: 20, style: .continuous))

                    if let details = message.ideaDetails {
                        IdeaDetailsCard(details: details)
                            .frame(maxWidth: 300) // Ограничиваем ширину карточки
                    }
                }

                if !message.isUser {
                    Spacer(minLength: 50)
                }
            }
        }
    }
}

// MARK: - Input Area with Glass Effect
struct InputAreaView: View {
    @Binding var inputText: String
    let isLoading: Bool
    let onSend: () -> Void
    let onFilterTap: () -> Void

    var body: some View {
        HStack(alignment: .center, spacing: 12) {
            Button(action: onFilterTap) {
                Image(systemName: "paperclip")
                    .font(.system(size: 22, weight: .semibold))
                    .foregroundStyle(.gray)
            }
            TextField("Ask Anything...", text: $inputText, axis: .vertical)
                .tint(.blue)
                .foregroundStyle(.white)
                .lineLimit(1...5)
                .disabled(isLoading)

            Button(action: onSend) {
                Image(systemName: "arrow.up")
                    .font(.system(size: 17, weight: .bold))
                    .foregroundStyle(inputText.isEmpty ? .black : .white)
                    .frame(width: 32, height: 32)
                    .background(inputText.isEmpty ? Color(UIColor.systemGray4) : Color.blue)
                    .clipShape(Circle())
            }
            .disabled(inputText.isEmpty || isLoading)
            .animation(.easeInOut(duration: 0.2), value: inputText.isEmpty)

        }
        .padding(.horizontal, 12)
        .padding(.vertical, 8)
        .background(.ultraThinMaterial, in: Capsule())
        .padding()
    }
}


// MARK: - Loading Indicator
struct LoadingIndicatorView: View {
    var body: some View {
        HStack(spacing: 8) {
            ProgressView()
            Text("Generating...")
                .font(.subheadline)
                .foregroundStyle(.secondary)
        }
        .padding()
    }
}

// MARK: - Error Banner
struct ErrorBannerView: View {
    let error: String
    let onDismiss: () -> Void

    var body: some View {
        HStack(spacing: 12) {
            Image(systemName: "exclamationmark.triangle.fill")
                .foregroundStyle(.red)

            Text(error)
                .font(.subheadline)
                .foregroundStyle(.primary)
                .frame(maxWidth: .infinity, alignment: .leading)

            Button(action: onDismiss) {
                Image(systemName: "xmark.circle.fill")
                    .foregroundStyle(.secondary)
            }
        }
        .padding()
        .background(
            Color.red.opacity(0.1)
                .background(.ultraThinMaterial)
        )
        .clipShape(RoundedRectangle(cornerRadius: 12, style: .continuous))
        .padding()
    }
}

private struct SheetItem: Identifiable {
    let child: ChatComponentBottomSheetChild
    var id: String {
        switch child {
        case is ChatComponentBottomSheetChildFilterChild
            : return "filters"
        default:
            return UUID().uuidString
        }
    }
}

private struct SheetContainerView: View {
    let child: ChatComponentBottomSheetChild

    var body: some View {
        switch child {
        case let filterChild as ChatComponentBottomSheetChildFilterChild:
            FilterSheetView(
                component: filterChild
            )
        default:
            EmptyView()
        }
    }
}


struct AnimatedAuroraBackground: View {
    @State private var startPoint = UnitPoint(x: 0, y: -0.5)
    @State private var endPoint = UnitPoint(x: 1, y: 1.5)

    var body: some View {
        LinearGradient(
            gradient: Gradient(colors: [.blue.opacity(0.6), .black, .purple.opacity(0.7), .black]),
            startPoint: startPoint,
            endPoint: endPoint
        )
            .onAppear {
                withAnimation(.easeInOut(duration: 8).repeatForever(autoreverses: true)) {
                    startPoint = UnitPoint(x: 1, y: 1.5)
                    endPoint = UnitPoint(x: 0, y: -0.5)
                }
            }
    }
}


struct GlassButton: View {
    let title: String
    let icon: String
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            HStack(spacing: 12) {
                Image(systemName: icon)
                Text(title).fontWeight(.bold)
            }
            .font(.headline)
            .foregroundStyle(.white)
            .padding()
            .frame(maxWidth: .infinity)
            .background(.white.opacity(0.1))
            .overlay(
                RoundedRectangle(cornerRadius: 24, style: .continuous)
                    .stroke(.white.opacity(0.3), lineWidth: 1)
            )
            .clipShape(RoundedRectangle(cornerRadius: 24, style: .continuous))
        }
    }
}


struct IdeaDetailsCard: View {
    let details: IdeaDetails

    var body: some View {
        VStack(alignment: .leading, spacing: 14) {
            // Верхняя строка: Тип и значок "Уникальный"
            HStack {
                Label(details.type.capitalized, systemImage: "info.circle.fill")
                    .font(.caption.weight(.medium))
                    .foregroundStyle(.white.opacity(0.7))

                Spacer()

                if details.unique {
                    Label("Unique", systemImage: "checkmark.seal.fill")
                        .font(.caption.weight(.bold))
                        .foregroundStyle(.green)
                }
            }

            // Средний блок: Сложность и время
            VStack(alignment: .leading, spacing: 6) {
                HStack {
                    Image(systemName: "brain.head.profile")
                    Text("Complexity: \(details.complexity.mental) mental, \(details.complexity.implementation) implementation")
                }

                HStack {
                    Image(systemName: "clock")
                    Text("Time Estimate: \(details.timeEstimate)")
                }
            }
            .font(.caption)
            .foregroundStyle(.white.opacity(0.9))

            // Теги
            if !details.tags.isEmpty {
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 8) {
                        ForEach(details.tags, id: \.self) { tag in
                            Text(tag)
                                .font(.caption2.weight(.medium))
                                .padding(.horizontal, 10)
                                .padding(.vertical, 5)
                                .background(Color.blue.opacity(0.3))
                                .foregroundStyle(.white)
                                .clipShape(Capsule())
                        }
                    }
                }
            }

            // Нижний блок: Схожесть
            VStack(alignment: .leading, spacing: 4) {
                HStack {
                    Text("Similarity Score")
                    Spacer()
                    Text("\(Int(details.similarityScore * 100))%")
                }
                .font(.caption2.weight(.medium))
                .foregroundStyle(.white.opacity(0.7))

                ProgressView(value: details.similarityScore)
                    .tint(
                        LinearGradient(
                            colors: [.blue, .purple],
                            startPoint: .leading,
                            endPoint: .trailing
                        )
                    )
            }
        }
        .padding(16)
        .background(.white.opacity(0.15))
        .clipShape(RoundedRectangle(cornerRadius: 16, style: .continuous))
        .overlay(
            RoundedRectangle(cornerRadius: 16, style: .continuous)
                .stroke(.white.opacity(0.2), lineWidth: 1)
        )
    }
}
