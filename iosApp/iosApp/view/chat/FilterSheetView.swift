import SwiftUI
import Shared

struct FilterSheetView: View {
    private let component: ChatComponentBottomSheetChildFilterChild
    

    @State private var niche: String
    @State private var type: String
    @State private var budget: String
    @State private var timeEstimate: String
    
    init(component: ChatComponentBottomSheetChildFilterChild) {
        self.component = component
        

        let initialFilters = component.currentFilters
        _niche = State(initialValue: initialFilters.niche)
        _type = State(initialValue: initialFilters.type)

        _budget = State(initialValue: initialFilters.budget?.description ?? "")

        _timeEstimate = State(initialValue: initialFilters.timeEstimate ?? "")
    }
    
    var body: some View {
        NavigationStack {
            Form {
                Section("Niche") {
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
                    let budgetKotlinInt = Int(budget).map { KotlinInt(value: Int32($0)) }

                    Button("Apply") {
                        let updatedFilters = IdeaFilters(
                            niche: niche,
                            type: type,
                            budget: budgetKotlinInt,
                            timeEstimate: timeEstimate.isEmpty ? nil : timeEstimate
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
