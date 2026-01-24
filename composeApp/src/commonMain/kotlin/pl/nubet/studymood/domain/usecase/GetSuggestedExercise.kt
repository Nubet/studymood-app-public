package pl.nubet.studymood.domain.usecase

import pl.nubet.studymood.domain.model.MindToolCategory
import pl.nubet.studymood.domain.model.MindToolExercise

class GetSuggestedExercise(private val getExercisesByCategory: GetExercisesByCategory) {
    operator fun invoke(): Pair<MindToolCategory, MindToolExercise> {
        val category = MindToolCategory.Reframing
        val exercise = getExercisesByCategory(category.id).first()
        return category to exercise
    }
}
