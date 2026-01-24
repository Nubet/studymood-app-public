package pl.nubet.studymood.domain.usecase.study

import pl.nubet.studymood.data.repository.SubjectRepository
import pl.nubet.studymood.domain.model.Subject

class GetSubjects(private val subjectRepository: SubjectRepository) {
    suspend operator fun invoke(): List<Subject> = subjectRepository.list()
}
