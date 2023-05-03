package com.nuzhnov.workcontrol.core.model

import com.soywiz.klock.TimeSpan

data class Participant(
    val student: Student,
    val lesson: Lesson,
    val totalVisitDuration: TimeSpan = TimeSpan.ZERO,
    val isMarked: Boolean = false,
    val theoryAssessment: Byte? = null,
    val practiceAssessment: Byte? = null,
    val activityAssessment: Byte? = null,
    val prudenceAssessment: Byte? = null,
    val creativityAssessment: Byte? = null,
    val preparationAssessment: Byte? = null,
    val note: String? = null
) {

    init {
        checkConstraints()
    }

    private fun checkConstraints() {
        checkTheoryAssessmentConstraints()
        checkPracticeAssessmentConstraints()
        checkActivityAssessmentConstraints()
        checkPrudenceAssessmentConstraints()
        checkCreativityAssessmentConstraints()
        checkPreparationAssessmentConstraints()
        checkNoteConstraints()
    }

    private fun checkTheoryAssessmentConstraints() {
        if (theoryAssessment != null && theoryAssessment !in THEORY_ASSESSMENT_RANGE) {
            throw IllegalArgumentException(
                "the theory assessment isn't in the range " +
                "[${THEORY_ASSESSMENT_RANGE.first}:${THEORY_ASSESSMENT_RANGE.last}]"
            )
        }
    }

    private fun checkPracticeAssessmentConstraints() {
        if (practiceAssessment != null && practiceAssessment !in PRACTICE_ASSESSMENT_RANGE) {
            throw IllegalArgumentException(
                "the practice assessment isn't in the range " +
                "[${PRACTICE_ASSESSMENT_RANGE.first}:${PRACTICE_ASSESSMENT_RANGE.last}]"
            )
        }
    }

    private fun checkActivityAssessmentConstraints() {
        if (activityAssessment != null && activityAssessment !in ACTIVITY_ASSESSMENT_RANGE) {
            throw IllegalArgumentException(
                "the activity assessment isn't in the range " +
                "[${ACTIVITY_ASSESSMENT_RANGE.first}:${ACTIVITY_ASSESSMENT_RANGE.last}]"
            )
        }
    }

    private fun checkPrudenceAssessmentConstraints() {
        if (prudenceAssessment != null && prudenceAssessment !in PRUDENCE_ASSESSMENT_RANGE) {
            throw IllegalArgumentException(
                "the prudence assessment isn't in the range " +
                "[${PRUDENCE_ASSESSMENT_RANGE.first}:${PRUDENCE_ASSESSMENT_RANGE.last}]"
            )
        }
    }

    private fun checkCreativityAssessmentConstraints() {
        if (creativityAssessment != null &&
            creativityAssessment !in CREATIVITY_ASSESSMENT_RANGE
        ) {
            throw IllegalArgumentException(
                "the creativity assessment isn't in the range " +
                "[${CREATIVITY_ASSESSMENT_RANGE.first}:${CREATIVITY_ASSESSMENT_RANGE.last}]"
            )
        }
    }

    private fun checkPreparationAssessmentConstraints() {
        if (preparationAssessment != null &&
            preparationAssessment !in PREPARATION_ASSESSMENT_RANGE
        ) {
            throw IllegalArgumentException(
                "the preparation assessment isn't in the range " +
                "[${PREPARATION_ASSESSMENT_RANGE.first}:${PREPARATION_ASSESSMENT_RANGE.last}]"
            )
        }
    }

    private fun checkNoteConstraints() {
        if (note != null && note.length !in NOTE_LENGTH_RANGE) {
            throw IllegalArgumentException(
                "the length of the note isn't in the range " +
                "[${NOTE_LENGTH_RANGE.first}:${NOTE_LENGTH_RANGE.last}]"
            )
        }
    }


    companion object {
        val THEORY_ASSESSMENT_RANGE = 1..10
        val PRACTICE_ASSESSMENT_RANGE = 1..10
        val ACTIVITY_ASSESSMENT_RANGE = 1..5
        val PRUDENCE_ASSESSMENT_RANGE = 1..5
        val CREATIVITY_ASSESSMENT_RANGE = 1..5
        val PREPARATION_ASSESSMENT_RANGE = 1..5
        val NOTE_LENGTH_RANGE = 1..5
    }
}
