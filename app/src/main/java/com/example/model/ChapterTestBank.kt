package com.example.model

data class TestQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val marks: Int = 4 // 20 questions * 4 marks = 80 Marks total
)

object ChapterTestBank {

    const val QUESTIONS_COUNT = 20
    const val MARKS_PER_QUESTION = 4
    const val TOTAL_MARKS = 80
    const val PASSING_MARKS = 60

    fun generateTestForChapter(
        classInfo: ClassInfo,
        subjectInfo: SubjectInfo,
        chapterInfo: ChapterInfo
    ): List<TestQuestion> {
        val questions = mutableListOf<TestQuestion>()
        val highlights = chapterInfo.highlights

        // 1. Generate questions based on the chapter's custom highlights
        highlights.forEachIndexed { index, highlight ->
            if (questions.size < 8) {
                val qNumber = questions.size + 1
                questions.add(
                    TestQuestion(
                        id = qNumber,
                        question = "According to the chapter core concept on '${highlight.title}', which of the following is accurate?",
                        options = listOf(
                            highlight.content.take(75) + "...",
                            "It operates inversely to the stated fundamental principles.",
                            "It is valid only under idealized conditions with zero friction or losses.",
                            "It has been replaced by classical approximation methods."
                        ),
                        correctIndex = 0,
                        explanation = "Key definition from '${highlight.title}': ${highlight.content}"
                    )
                )
            }
        }

        // 2. Generate specialized domain questions based on subject category
        val subjectQuestions = getSubjectDomainQuestions(subjectInfo.iconCategory, chapterInfo.title)
        for (sq in subjectQuestions) {
            if (questions.size < QUESTIONS_COUNT) {
                questions.add(sq.copy(id = questions.size + 1))
            }
        }

        // 3. Fill remaining questions up to 20 with general analytical & problem solving questions
        var extraIndex = 1
        while (questions.size < QUESTIONS_COUNT) {
            val qNum = questions.size + 1
            questions.add(
                TestQuestion(
                    id = qNum,
                    question = "Analysis Problem $extraIndex for ${chapterInfo.title}: In a standard assessment scenario, what primary deduction holds true?",
                    options = listOf(
                        "The primary conservation principle applies consistently throughout.",
                        "Total energy dissipation leads to unbounded acceleration.",
                        "The system reaches an indeterminate non-convergent state.",
                        "Parameters remain invariant regardless of boundary constraints."
                    ),
                    correctIndex = 0,
                    explanation = "In standard formulations of ${chapterInfo.title}, governing physical and logical laws guarantee balance."
                )
            )
            extraIndex++
        }

        return questions.take(QUESTIONS_COUNT)
    }

    private fun getSubjectDomainQuestions(category: String, chapterTitle: String): List<TestQuestion> {
        return when (category) {
            "physics" -> listOf(
                TestQuestion(
                    id = 0,
                    question = "What is the SI unit of electric potential difference in electromagnetic theory?",
                    options = listOf("Volt (V)", "Ampere (A)", "Ohm (Ω)", "Coulomb (C)"),
                    correctIndex = 0,
                    explanation = "1 Volt is defined as 1 Joule of work done per Coulomb of charge (1 V = 1 J/C)."
                ),
                TestQuestion(
                    id = 0,
                    question = "According to Newton's Second Law of Motion, force is defined as:",
                    options = listOf("Rate of change of momentum", "Product of mass and velocity", "Derivative of work done", "Energy per unit time"),
                    correctIndex = 0,
                    explanation = "F = dp/dt. When mass is constant, F = m * a."
                ),
                TestQuestion(
                    id = 0,
                    question = "In wave mechanics, when light passes from an optically rarer medium into a denser medium, it bends:",
                    options = listOf("Towards the normal", "Away from the normal", "At 90 degrees to incident ray", "Without any deviation"),
                    correctIndex = 0,
                    explanation = "Light slows down in an optically denser medium, causing the wavefront to refract towards the normal."
                ),
                TestQuestion(
                    id = 0,
                    question = "The work-energy theorem states that work done by all forces on a body equals:",
                    options = listOf("Change in its kinetic energy", "Total potential energy stored", "Change in thermal velocity", "Internal mechanical equilibrium"),
                    correctIndex = 0,
                    explanation = "W_net = ΔKE = KE_final - KE_initial."
                ),
                TestQuestion(
                    id = 0,
                    question = "What determines the frequency of sound produced by a vibrating tuning fork?",
                    options = listOf("Dimensions and elasticity of the prongs", "Amplitude of the strike", "Surrounding atmospheric humidity", "Distance from observer"),
                    correctIndex = 0,
                    explanation = "Fundamental frequency depends on length, thickness, and material elasticity of the tuning fork."
                ),
                TestQuestion(
                    id = 0,
                    question = "In thermodynamics, an adiabatic process is characterized by:",
                    options = listOf("Zero heat transfer into or out of the system (Q = 0)", "Constant temperature (T = const)", "Constant pressure (P = const)", "Constant volume (V = const)"),
                    correctIndex = 0,
                    explanation = "An adiabatic boundary prevents heat exchange, so ΔQ = 0 and ΔU = -W."
                )
            )
            "math" -> listOf(
                TestQuestion(
                    id = 0,
                    question = "For a quadratic equation ax² + bx + c = 0, distinct real roots exist if and only if:",
                    options = listOf("b² - 4ac > 0", "b² - 4ac = 0", "b² - 4ac < 0", "b = 2ac"),
                    correctIndex = 0,
                    explanation = "Discriminant D = b² - 4ac must be strictly greater than 0 for two distinct real roots."
                ),
                TestQuestion(
                    id = 0,
                    question = "What is the derivative of f(x) = sin(2x) with respect to x?",
                    options = listOf("2 cos(2x)", "-2 cos(2x)", "cos(2x)", "2 sin(x)"),
                    correctIndex = 0,
                    explanation = "By chain rule: d/dx[sin(2x)] = cos(2x) * d/dx[2x] = 2 cos(2x)."
                ),
                TestQuestion(
                    id = 0,
                    question = "The sum of the interior angles of a convex polygon with n sides is given by:",
                    options = listOf("(n - 2) * 180°", "n * 180°", "(n + 2) * 180°", "(2n - 1) * 90°"),
                    correctIndex = 0,
                    explanation = "Dividing the polygon into (n - 2) triangles from a single vertex yields (n - 2) * 180°."
                ),
                TestQuestion(
                    id = 0,
                    question = "In coordinate geometry, two non-vertical lines with slopes m1 and m2 are perpendicular if:",
                    options = listOf("m1 * m2 = -1", "m1 = m2", "m1 + m2 = 0", "m1 - m2 = 1"),
                    correctIndex = 0,
                    explanation = "The product of perpendicular slopes in Euclidean plane is always -1."
                ),
                TestQuestion(
                    id = 0,
                    question = "Evaluate the definite integral ∫ (from 0 to 1) 3x² dx:",
                    options = listOf("1", "3", "0.5", "2"),
                    correctIndex = 0,
                    explanation = "[x³] from 0 to 1 = 1³ - 0³ = 1."
                )
            )
            "chemistry" -> listOf(
                TestQuestion(
                    id = 0,
                    question = "Which type of chemical bond is formed by the complete transfer of valence electrons?",
                    options = listOf("Ionic bond", "Covalent bond", "Coordinate bond", "Metallic bond"),
                    correctIndex = 0,
                    explanation = "Ionic bonds form through electrostatic attraction between oppositely charged ions created by electron transfer."
                ),
                TestQuestion(
                    id = 0,
                    question = "The pH of a neutral aqueous solution at standard 25°C is:",
                    options = listOf("7.0", "1.0", "14.0", "0.0"),
                    correctIndex = 0,
                    explanation = "At 25°C, [H+] = [OH-] = 10^-7 M, yielding -log(10^-7) = 7.0."
                ),
                TestQuestion(
                    id = 0,
                    question = "According to Le Chatelier's Principle, increasing pressure on an equilibrium gaseous system shifts it towards:",
                    options = listOf("Fewer moles of gas", "Greater moles of gas", "No change always", "Lower activation energy"),
                    correctIndex = 0,
                    explanation = "The system counteracts higher pressure by shifting towards the side with fewer gas moles."
                ),
                TestQuestion(
                    id = 0,
                    question = "Which gas is evolved when dilute hydrochloric acid reacts with zinc metal granules?",
                    options = listOf("Hydrogen (H₂)", "Oxygen (O₂)", "Chlorine (Cl₂)", "Carbon dioxide (CO₂)"),
                    correctIndex = 0,
                    explanation = "Zn + 2HCl → ZnCl₂ + H₂↑. Hydrogen gas is released with effervescence."
                )
            )
            "biology" -> listOf(
                TestQuestion(
                    id = 0,
                    question = "Which cellular organelle is recognized as the 'Powerhouse of the Cell' for ATP synthesis?",
                    options = listOf("Mitochondria", "Ribosome", "Endoplasmic Reticulum", "Golgi Apparatus"),
                    correctIndex = 0,
                    explanation = "Mitochondria generate cellular energy via oxidative phosphorylation in cellular respiration."
                ),
                TestQuestion(
                    id = 0,
                    question = "During photosynthesis in green plants, oxygen is liberated from the splitting of:",
                    options = listOf("Water (H₂O)", "Carbon dioxide (CO₂)", "Glucose", "Chlorophyll molecules"),
                    correctIndex = 0,
                    explanation = "Photolysis of water in the light-dependent reactions releases O₂, electrons, and protons."
                ),
                TestQuestion(
                    id = 0,
                    question = "DNA replication in eukaryotic cells occurs during which phase of the cell cycle?",
                    options = listOf("S phase (Synthesis)", "G1 phase", "M phase (Mitosis)", "G2 phase"),
                    correctIndex = 0,
                    explanation = "Synthesis (S) phase is specifically dedicated to duplicating nuclear chromosomes."
                )
            )
            else -> listOf(
                TestQuestion(
                    id = 0,
                    question = "What is the primary methodology for validating empirical hypothesis in academic study?",
                    options = listOf("Controlled repeatable experiments", "Intuitive assumptions", "Popular consensus", "Unchecked extrapolation"),
                    correctIndex = 0,
                    explanation = "Scientific and analytical rigor requires repeatable controlled observations."
                ),
                TestQuestion(
                    id = 0,
                    question = "In structured argumentation, a premise is defined as:",
                    options = listOf("A supporting statement leading to a conclusion", "An irrelevant digression", "A rhetorical flourish", "A summary index"),
                    correctIndex = 0,
                    explanation = "Premises form the logical basis and foundation for reasoned conclusions."
                )
            )
        }
    }
}
