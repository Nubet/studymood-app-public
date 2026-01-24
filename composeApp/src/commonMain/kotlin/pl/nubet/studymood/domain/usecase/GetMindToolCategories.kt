package pl.nubet.studymood.domain.usecase

import pl.nubet.studymood.domain.model.MindToolCategory

class GetMindToolCategories {
    operator fun invoke(): List<MindToolCategory> {
        return MindToolCategory.all()
    }
}
