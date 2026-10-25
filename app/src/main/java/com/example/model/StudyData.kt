package com.example.model

import com.example.model.curriculum.Class10Curriculum
import com.example.model.curriculum.Class11Curriculum
import com.example.model.curriculum.Class12Curriculum
import com.example.model.curriculum.Class9Curriculum

data class ClassInfo(
    val id: String,
    val name: String,
    val subtitle: String,
    val gradeNumber: Int,
    val subjects: List<SubjectInfo>
)

data class SubjectInfo(
    val id: String,
    val name: String,
    val code: String,
    val description: String,
    val iconCategory: String, // math, physics, chemistry, biology, english, history, geography, civics, economics, cs
    val chapters: List<ChapterInfo>
)

data class ChapterInfo(
    val id: String,
    val number: Int,
    val title: String,
    val estimatedMinutes: Int,
    val summary: String,
    val highlights: List<ChapterHighlight>
)

data class ChapterHighlight(
    val title: String,
    val category: String, // "Key Formula", "Fundamental Law", "Core Definition", "Crucial Concept"
    val content: String
)

object StudyRepository {

    val classes: List<ClassInfo> by lazy {
        listOf(
            Class9Curriculum.create(),
            Class10Curriculum.create(),
            Class11Curriculum.create(),
            Class12Curriculum.create()
        )
    }

    fun getClass(classId: String): ClassInfo? = classes.find { it.id == classId }

    fun getSubject(classId: String, subjectId: String): SubjectInfo? {
        return getClass(classId)?.subjects?.find { it.id == subjectId }
    }

    fun getChapter(classId: String, subjectId: String, chapterId: String): ChapterInfo? {
        return getSubject(classId, subjectId)?.chapters?.find { it.id == chapterId }
    }

    fun findChapter(chapterId: String): Triple<ClassInfo, SubjectInfo, ChapterInfo>? {
        for (c in classes) {
            for (s in c.subjects) {
                for (ch in s.chapters) {
                    if (ch.id == chapterId) {
                        return Triple(c, s, ch)
                    }
                }
            }
        }
        return null
    }

    fun getTotalChaptersCount(classId: String): Int {
        val c = getClass(classId) ?: return 0
        return c.subjects.sumOf { it.chapters.size }
    }

    fun getAllChapters(classId: String): List<Pair<SubjectInfo, ChapterInfo>> {
        val c = getClass(classId) ?: return emptyList()
        val list = mutableListOf<Pair<SubjectInfo, ChapterInfo>>()
        for (s in c.subjects) {
            for (ch in s.chapters) {
                list.add(Pair(s, ch))
            }
        }
        return list
    }
}
