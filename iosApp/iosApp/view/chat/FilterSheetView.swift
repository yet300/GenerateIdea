import SwiftUI
import Shared

struct FilterSheetView: View {
    private let component: ChatComponentBottomSheetChildFilterChild
    
    // 1. Создаем локальные, изменяемые @State для каждого поля
    @State private var niche: String
    @State private var type: String
    @State private var budget: String
    @State private var timeEstimate: String
    
    init(component: ChatComponentBottomSheetChildFilterChild) {
        self.component = component
        
        // 2. Инициализируем локальные @State из начальных данных компонента
        let initialFilters = component.currentFilters
        _niche = State(initialValue: initialFilters.niche)
        _type = State(initialValue: initialFilters.type)
        // Преобразуем опциональный Int в String для TextField
        _budget = State(initialValue: initialFilters.budget?.description ?? "")
        // Используем пустую строку для "None"
        _timeEstimate = State(initialValue: initialFilters.timeEstimate ?? "")
    }
    
    var body: some View {
        NavigationStack {
            Form {
                Section("Niche") {
                    // 3. Привязываем UI к локальным @State переменным
                    TextField("e.g., AI in healthcare", text: $niche)
                }
                
                Section("Idea Type") {
                    Picker("Type", selection: $type) {
                        ForEach(ideaTypes, id: \.self) { ideaType in
                            Text(ideaType.capitalized).tag(ideaType)
                        }
                    }
                }
                
                Section {
                    TextField("e.g., 50000", text: $budget)
                        .keyboardType(.numberPad)
                } header: {
                    Text("Budget (Optional)")
                } footer: {
                    Text("Enter your budget in dollars")
                }
                
                Section("Time Estimate (Optional)") {
                    Picker("Time Estimate", selection: $timeEstimate) {
                        Text("None").tag("")
                        ForEach(timeEstimates, id: \.self) { time in
                            Text(time).tag(time)
                        }
                    }
                }
            }
            .navigationTitle("Filters")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel", action: component.onDismiss)
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button("Apply") {
                        // 4. Собираем новый объект IdeaFilters из локальных состояний
                        let updatedFilters = IdeaFilters(
                            niche: niche,
                            type: type,
                            budget: Int(budget) as! KotlinInt, // Пытаемся преобразовать String обратно в Int
                            timeEstimate: timeEstimate.isEmpty ? nil : timeEstimate // nil если строка пустая
                        )
                        
                        component.onFiltersChanged(updatedFilters)
                        component.onDismiss()
                    }
                    .fontWeight(.semibold)
                }
            }
        }
    }
    
    private let ideaTypes = [
        "startup",
        "hackathon",
        "pet_project",
        "research",
        "business",
        "app",
        "service"
    ]
    
    private let timeEstimates = [
        "1-2 weeks",
        "1 month",
        "3 months",
        "6 months",
        "1 year",
        "2+ years"
    ]
}
