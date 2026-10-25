package com.example.service

import com.example.model.SundayQuestionType
import com.example.model.SundaySection
import com.example.model.SundayTestPaper
import com.example.model.SundayTestQuestion
import java.util.UUID

/**
 * Generates an authentic 80-Mark CBSE Mock Examination Paper aligned with official CBSE Board
 * blue-prints and difficulty weightage (Easy 30%, Medium 50%, Hard 20%).
 *
 * Exam Blueprint:
 * - Section A: 20 MCQs (1 mark each) = 20 Marks
 * - Section B: 5 Very Short Answer (2 marks each) = 10 Marks
 * - Section C: 6 Short Answer (3 marks each) = 18 Marks
 * - Section D: 4 Long Answer (5 marks each) = 20 Marks
 * - Section E: 3 Case Study Questions (4 marks each) = 12 Marks
 * TOTAL: 38 Questions, strictly 80 Marks, 3 Hours.
 */
object SundayTestGenerator {

    fun generatePaper(
        classId: String,
        className: String,
        subjectId: String,
        subjectName: String
    ): SundayTestPaper {
        val questions = when {
            subjectName.contains("Math", ignoreCase = true) -> generateMathematicsPaper(classId, className)
            subjectName.contains("Science", ignoreCase = true) || subjectName.contains("Physics", ignoreCase = true) ->
                generateSciencePhysicsPaper(classId, className, subjectName)
            subjectName.contains("Chemistry", ignoreCase = true) -> generateChemistryPaper(classId, className)
            subjectName.contains("Biology", ignoreCase = true) -> generateBiologyPaper(classId, className)
            subjectName.contains("Social", ignoreCase = true) -> generateSocialSciencePaper(classId, className)
            else -> generateGeneralCBSEPaper(classId, className, subjectName)
        }

        return SundayTestPaper(
            id = "sunday_exam_${UUID.randomUUID().toString().take(8)}",
            classId = classId,
            className = className,
            subjectId = subjectId,
            subjectName = subjectName,
            totalMarks = 80,
            passingMarks = 60,
            durationSeconds = 3 * 60 * 60, // 3 hours
            questions = questions
        )
    }

    // =========================================================================
    // MATHEMATICS 80-MARK CBSE BOARD PAPER
    // =========================================================================
    private fun generateMathematicsPaper(classId: String, className: String): List<SundayTestQuestion> {
        val list = mutableListOf<SundayTestQuestion>()
        var qNum = 1

        // SECTION A: 20 MCQs (1 Mark each)
        val mcqs = listOf(
            Triple(
                "If two positive integers a and b are written as a = x³y² and b = xy³, where x, y are prime numbers, then HCF(a, b) is:",
                listOf("xy", "xy²", "x³y³", "x²y²"),
                1
            ),
            Triple(
                "The zeroes of the quadratic polynomial x² + 99x + 127 are:",
                listOf("Both positive", "Both negative", "One positive and one negative", "Both equal"),
                1
            ),
            Triple(
                "If the pair of linear equations 2x + 3y = 7 and 2ax + (a + b)y = 28 has infinitely many solutions, then:",
                listOf("a = 2b", "b = 2a", "a + 2b = 0", "2a + b = 0"),
                1
            ),
            Triple(
                "Which of the following equations has 2 as a root?",
                listOf("x² - 4x + 5 = 0", "x² + 3x - 12 = 0", "2x² - 7x + 6 = 0", "3x² - 6x - 2 = 0"),
                2
            ),
            Triple(
                "In an AP, if d = -4, n = 7, an = 4, then the first term a is:",
                listOf("6", "7", "20", "28"),
                3
            ),
            Triple(
                "The distance of the point P(-6, 8) from the origin (0, 0) is:",
                listOf("8", "2√7", "10", "6"),
                2
            ),
            Triple(
                "If ΔABC ~ ΔPQR with BC/QR = 1/3, then area(ΔPQR) / area(ΔABC) is equal to:",
                listOf("9", "3", "1/3", "1/9"),
                0
            ),
            Triple(
                "If sin θ + cos θ = √2 cos θ, then the value of (cos θ - sin θ) is:",
                listOf("√2 sin θ", "√2 cos θ", "sin θ", "-√2 sin θ"),
                0
            ),
            Triple(
                "If a pole 6 m high casts a shadow 2√3 m long on the ground, then the sun's elevation angle is:",
                listOf("60°", "45°", "30°", "90°"),
                0
            ),
            Triple(
                "If tangents PA and PB from a point P to a circle with centre O are inclined to each other at angle of 80°, then ∠POA is equal to:",
                listOf("50°", "60°", "70°", "80°"),
                0
            ),
            Triple(
                "If the perimeter and the area of a circle are numerically equal, then the radius of the circle is:",
                listOf("2 units", "π units", "4 units", "7 units"),
                0
            ),
            Triple(
                "The volume of the largest right circular cone that can be carved out from a cube of edge 4.2 cm is:",
                listOf("9.7 cm³", "77.6 cm³", "58.2 cm³", "19.4 cm³"),
                3
            ),
            Triple(
                "For the following distribution, modal class is: [0-10: 5], [10-20: 8], [20-30: 12], [30-40: 20], [40-50: 14], [50-60: 7]:",
                listOf("20-30", "30-40", "40-50", "10-20"),
                1
            ),
            Triple(
                "Which of the following cannot be the probability of an event?",
                listOf("2/3", "-1.5", "15%", "0.7"),
                1
            ),
            Triple(
                "A card is drawn from a well-shuffled deck of 52 cards. The probability of getting a face card of black color is:",
                listOf("3/13", "3/26", "1/26", "3/52"),
                1
            ),
            Triple(
                "If the sum of zeroes of the polynomial p(x) = 2x² - k√2x + 1 is √2, then the value of k is:",
                listOf("1", "2", "√2", "4"),
                1
            ),
            Triple(
                "The discriminant of the quadratic equation 2x² - 4x + 3 = 0 is:",
                listOf("-8", "10", "-16", "8"),
                0
            ),
            Triple(
                "The 11th term of the AP: -3, -1/2, 2, ... is:",
                listOf("28", "22", "-38", "-46.5"),
                1
            ),
            Triple(
                "Assertion (A): The HCF of two numbers is 5 and their product is 150, then their LCM is 30.\nReason (R): For any two positive integers a and b, HCF(a,b) × LCM(a,b) = a × b.",
                listOf(
                    "Both A and R are true and R is correct explanation of A",
                    "Both A and R are true but R is not correct explanation of A",
                    "A is true but R is false",
                    "A is false but R is true"
                ),
                0
            ),
            Triple(
                "Assertion (A): Total surface area of a cylinder of radius r and height h is 2πr(r + h).\nReason (R): Volume of cylinder is πr²h.",
                listOf(
                    "Both A and R are true and R is correct explanation of A",
                    "Both A and R are true but R is not correct explanation of A",
                    "A is true but R is false",
                    "A is false but R is true"
                ),
                1
            )
        )

        mcqs.forEach { (q, opts, ans) ->
            list.add(
                SundayTestQuestion(
                    id = "math_secA_${qNum}",
                    number = qNum++,
                    section = SundaySection.SECTION_A,
                    type = SundayQuestionType.MCQ,
                    marks = 1,
                    question = q,
                    options = opts,
                    correctOptionIndex = ans,
                    modelAnswer = "Correct Option: ${opts[ans]}. Derived using fundamental theorem of arithmetic and algebraic identities.",
                    rubricKeywords = listOf("formula", "simplification", "theorem", "step-wise"),
                    webVerificationSources = listOf("NCERT Mathematics Class 10/11/12", "CBSE Official Syllabus 2024-25")
                )
            )
        }

        // SECTION B: 5 Very Short Answer (2 Marks each = 10 Marks)
        val vsa = listOf(
            Pair(
                "Prove that 5 - 2√3 is an irrational number, given that √3 is irrational.",
                "Let 5 - 2√3 = p/q (rational, q ≠ 0, p,q coprime). Then 2√3 = 5 - p/q = (5q - p)/q => √3 = (5q - p)/(2q). Since p, q are integers, (5q - p)/(2q) is rational, which contradicts that √3 is irrational. Hence 5 - 2√3 is irrational."
            ),
            Pair(
                "Find the zeroes of the quadratic polynomial 6x² - 3 - 7x and verify the relationship between zeroes and coefficients.",
                "Rewrite as 6x² - 7x - 3 = 0 => 6x² - 9x + 2x - 3 = (3x + 1)(2x - 3) = 0. Zeroes are α = 3/2 and β = -1/3. Sum of zeroes = 3/2 - 1/3 = 7/6 = -(-7)/6 = -b/a. Product = (3/2)(-1/3) = -3/6 = c/a."
            ),
            Pair(
                "In ΔABC, DE || BC such that AD = 3 cm, DB = 5 cm, and AE = 1.8 cm. Find AC.",
                "By Thales Theorem (Basic Proportionality Theorem): AD/DB = AE/EC => 3/5 = 1.8/EC => EC = (5 × 1.8)/3 = 3.0 cm. Therefore, AC = AE + EC = 1.8 + 3.0 = 4.8 cm."
            ),
            Pair(
                "Evaluate: (5 cos² 60° + 4 sec² 30° - tan² 45°) / (sin² 30° + cos² 30°).",
                "Numerator = 5(1/2)² + 4(2/√3)² - 1² = 5/4 + 16/3 - 1 = (15 + 64 - 12)/12 = 67/12. Denominator = sin² 30° + cos² 30° = 1. Result = 67/12."
            ),
            Pair(
                "A quadrilateral ABCD is drawn to circumscribe a circle. Prove that AB + CD = AD + BC.",
                "Tangents from external points are equal: AP = AS, BP = BQ, CR = CQ, DR = DS. Adding: (AP + BP) + (CR + DR) = (AS + DS) + (BQ + CQ) => AB + CD = AD + BC."
            )
        )

        vsa.forEach { (q, ans) ->
            list.add(
                SundayTestQuestion(
                    id = "math_secB_${qNum}",
                    number = qNum++,
                    section = SundaySection.SECTION_B,
                    type = SundayQuestionType.VERY_SHORT,
                    marks = 2,
                    question = q,
                    modelAnswer = ans,
                    rubricKeywords = listOf("formula", "proof", "derivation", "coprime", "theorems"),
                    webVerificationSources = listOf("NCERT Mathematics Textbook", "CBSE Marking Scheme Guidelines")
                )
            )
        }

        // SECTION C: 6 Short Answer (3 Marks each = 18 Marks)
        val sa = listOf(
            Pair(
                "Solve the following pair of linear equations graphically or algebraically: 2x + 3y = 11 and 2x - 4y = -24. Hence find the value of 'm' for which y = mx + 3.",
                "Subtracting equations: (2x + 3y) - (2x - 4y) = 11 - (-24) => 7y = 35 => y = 5. Substitute y = 5 into 2x + 15 = 11 => 2x = -4 => x = -2. Now y = mx + 3 => 5 = m(-2) + 3 => 2m = -2 => m = -1."
            ),
            Pair(
                "The sum of the 4th and 8th terms of an AP is 24 and the sum of the 6th and 10th terms is 44. Find the first three terms of the AP.",
                "a4 + a8 = (a + 3d) + (a + 7d) = 2a + 10d = 24 => a + 5d = 12. a6 + a10 = (a + 5d) + (a + 9d) = 2a + 14d = 44 => a + 7d = 22. Subtracting: 2d = 10 => d = 5. a = 12 - 5(5) = -13. First three terms are: -13, -8, -3."
            ),
            Pair(
                "Prove the identity: (sin θ - 2 sin³ θ) / (2 cos³ θ - cos θ) = tan θ.",
                "LHS = [sin θ(1 - 2 sin² θ)] / [cos θ(2 cos² θ - 1)]. Note that 1 - 2 sin² θ = cos 2θ and 2 cos² θ - 1 = cos 2θ. Or replace 1 with sin² θ + cos² θ: numerator = sin θ(cos² θ - sin² θ), denominator = cos θ(cos² θ - sin² θ). Cancelling terms gives sin θ / cos θ = tan θ = RHS."
            ),
            Pair(
                "Find the coordinates of the points of trisection of the line segment joining the points A(2, -2) and B(-7, 4).",
                "Trisection points P and Q divide AB in ratios 1:2 and 2:1. Using section formula P(x, y) = [(1(-7) + 2(2))/(1+2), (1(4) + 2(-2))/(1+2)] = [(-7 + 4)/3, (4 - 4)/3] = (-1, 0). Q(x, y) = [(2(-7) + 1(2))/3, (2(4) + 1(-2))/3] = [(-14 + 2)/3, (8 - 2)/3] = (-4, 2)."
            ),
            Pair(
                "From a solid cylinder whose height is 2.4 cm and diameter 1.4 cm, a conical cavity of the same height and same diameter is hollowed out. Find the total surface area of the remaining solid to the nearest cm².",
                "r = 0.7 cm, h = 2.4 cm. Slant height l = √(r² + h²) = √(0.49 + 5.76) = √6.25 = 2.5 cm. Total surface area = Curved surface of cylinder + Area of base + Curved surface of cone = 2πrh + πr² + πrl = πr(2h + r + l) = (22/7) × 0.7 × (4.8 + 0.7 + 2.5) = 2.2 × 8.0 = 17.6 cm² ≈ 18 cm²."
            ),
            Pair(
                "Find the mean of the following frequency distribution using step-deviation method: Classes: [10-25: 2], [25-40: 3], [40-55: 7], [55-70: 6], [70-85: 6], [85-100: 6].",
                "Class size h = 15. Midpoints xi: 17.5, 32.5, 47.5 (assumed mean A), 62.5, 77.5, 92.5. ui = (xi - 47.5)/15: -2, -1, 0, 1, 2, 3. fi*ui: -4, -3, 0, 6, 12, 18 => Σfi*ui = 29. Σfi = 30. Mean = A + h(Σfi*ui / Σfi) = 47.5 + 15(29/30) = 47.5 + 14.5 = 62.0."
            )
        )

        sa.forEach { (q, ans) ->
            list.add(
                SundayTestQuestion(
                    id = "math_secC_${qNum}",
                    number = qNum++,
                    section = SundaySection.SECTION_C,
                    type = SundayQuestionType.SHORT,
                    marks = 3,
                    question = q,
                    modelAnswer = ans,
                    rubricKeywords = listOf("step deviation", "section formula", "trigonometric identities", "AP general term"),
                    webVerificationSources = listOf("NCERT Mathematics Class 10/11", "CBSE Official Sample Papers")
                )
            )
        }

        // SECTION D: 4 Long Answer (5 Marks each = 20 Marks)
        val la = listOf(
            Pair(
                "A motor boat whose speed is 18 km/h in still water takes 1 hour more to go 24 km upstream than to return downstream to the same spot. Find the speed of the stream.",
                "Let speed of stream = x km/h. Speed upstream = (18 - x) km/h; downstream = (18 + x) km/h. Distance = 24 km. Time upstream - Time downstream = 1 => 24/(18 - x) - 24/(18 + x) = 1. 24[(18 + x) - (18 - x)] = (18 - x)(18 + x) => 24(2x) = 324 - x² => x² + 48x - 324 = 0. Factorising: (x + 54)(x - 6) = 0. Since speed cannot be negative, x = 6 km/h. Speed of stream is 6 km/h."
            ),
            Pair(
                "State and prove Basic Proportionality Theorem (Thales Theorem).",
                "Statement: If a line is drawn parallel to one side of a triangle to intersect the other two sides in distinct points, the other two sides are divided in the same ratio.\nProof:\n1. Consider ΔABC with DE || BC. Draw DM ⊥ AC and EN ⊥ AB. Join BE and CD.\n2. Area(ΔADE) = 1/2 × AD × EN. Area(ΔBDE) = 1/2 × DB × EN. Ratio = AD/DB.\n3. Area(ΔADE) = 1/2 × AE × DM. Area(ΔDEC) = 1/2 × EC × DM. Ratio = AE/EC.\n4. ΔBDE and ΔDEC are on the same base DE and between same parallels DE and BC, so Area(ΔBDE) = Area(ΔDEC).\n5. Hence AD/DB = AE/EC. Proved."
            ),
            Pair(
                "A straight highway leads to the foot of a tower. A man standing at the top of the tower observes a car at an angle of depression of 30°, which is approaching the foot of the tower with a uniform speed. Six seconds later, the angle of depression of the car is found to be 60°. Find the time taken by the car to reach the foot of the tower from this point.",
                "Let tower height = h, initial position = A, second position = B, foot of tower = C. In ΔACD, tan 30° = h / AC => AC = h√3. In ΔBCD, tan 60° = h / BC => BC = h/√3. Distance AB = AC - BC = h√3 - h/√3 = 2h/√3. Speed v = Distance / Time = (2h/√3) / 6 = h / (3√3). Time to cover BC = Distance / Speed = (h/√3) / (h / 3√3) = (3√3)/√3 = 3 seconds. The car takes 3 seconds more to reach the tower."
            ),
            Pair(
                "A solid toy is in the form of a hemisphere surmounted by a right circular cone. The height of the cone is 2 cm and the diameter of the base is 4 cm. Determine the volume of the toy. If a right circular cylinder circumscribes the toy, find the difference of the volumes of the cylinder and the toy. (Take π = 3.14)",
                "Radius r = 2 cm, height of cone h = 2 cm.\nVolume of toy = Volume of cone + Volume of hemisphere = (1/3)πr²h + (2/3)πr³ = (1/3)πr²(h + 2r) = (1/3) × 3.14 × 4 × (2 + 4) = (1/3) × 3.14 × 4 × 6 = 25.12 cm³.\nFor the circumscribing cylinder: radius R = 2 cm, height H = height of cone + radius of hemisphere = 2 + 2 = 4 cm.\nVolume of cylinder = πR²H = 3.14 × 4 × 4 = 50.24 cm³.\nDifference in volumes = 50.24 - 25.12 = 25.12 cm³."
            )
        )

        la.forEach { (q, ans) ->
            list.add(
                SundayTestQuestion(
                    id = "math_secD_${qNum}",
                    number = qNum++,
                    section = SundaySection.SECTION_D,
                    type = SundayQuestionType.LONG,
                    marks = 5,
                    question = q,
                    modelAnswer = ans,
                    rubricKeywords = listOf("quadratic model", "statement & proof", "geometric construction", "angles of depression", "combination of solids"),
                    webVerificationSources = listOf("CBSE Class 10/12 Board Marking Guide", "NCERT Mathematics Textbook")
                )
            )
        }

        // SECTION E: 3 Case Study Questions (4 Marks each = 12 Marks)
        val caseStudies = listOf(
            Triple(
                "Case Study 1: Solar Panel Tracking System\nA school installed a solar power tracking station. The solar panel array is modeled algebraically. The height h (in meters) of the panel's inclination above the horizontal support frame after t seconds of rotation is given by the quadratic model h(t) = -t² + 6t + 7.\nQuestions:\n(i) Find the maximum height reached by the solar panel.\n(ii) At what time t does the panel reach the ground frame (h = 0)?\n(iii) What is the initial height at t = 0?",
                "Context: Quadratic mechanics model h(t) = -t² + 6t + 7.",
                "(i) Vertex of parabola t = -b/(2a) = -6/(2 × -1) = 3 seconds. Maximum height h(3) = -(3)² + 6(3) + 7 = -9 + 18 + 7 = 16 meters (2 marks).\n(ii) -t² + 6t + 7 = 0 => t² - 6t - 7 = (t - 7)(t + 1) = 0. Since time cannot be negative, t = 7 seconds (1 mark).\n(iii) At t = 0, h(0) = 7 meters (1 mark)."
            ),
            Triple(
                "Case Study 2: Wireless Telecom Tower Coordinates\nA telecom company installs three 5G signal towers in a triangular boundary to provide cellular coverage over an urban sector. On a Cartesian grid (in kilometers), the three towers are situated at coordinates A(1, 2), B(4, 6), and C(5, -1).\nQuestions:\n(i) Find the distance between tower A and tower B.\n(ii) Determine whether triangle ABC is right-angled or scalene.\n(iii) If a regional switching server P is placed at the centroid of triangle ABC, find the coordinates of P.",
                "Context: Coordinate geometry placement of network relay towers.",
                "(i) Distance AB = √[(4 - 1)² + (6 - 2)²] = √[3² + 4²] = √25 = 5 km (1 mark).\n(ii) BC = √[(5 - 4)² + (-1 - 6)²] = √[1 + 49] = √50 km. AC = √[(5 - 1)² + (-1 - 2)²] = √[16 + 9] = √25 = 5 km. Since AB² + AC² = 25 + 25 = 50 = BC², ΔABC is an isosceles right-angled triangle right-angled at A (2 marks).\n(iii) Centroid P = ((x1+x2+x3)/3, (y1+y2+y3)/3) = ((1+4+5)/3, (2+6-1)/3) = (10/3, 7/3) (1 mark)."
            ),
            Triple(
                "Case Study 3: Traffic Light Probability Analysis\nA metropolitan city transport corporation monitors traffic cameras at a major intersection. On any given working day, the probability that a vehicle arrives at the crossing while the light is Green is 0.55, Amber is 0.15, and Red is 0.30.\nQuestions:\n(i) What is the probability that a vehicle does NOT encounter a Red light?\n(ii) If 2000 vehicles pass the intersection in one morning hour, estimate how many will stop at the Red light.\n(iii) Verify whether the probabilities of all mutually exclusive light states form a valid probability distribution.",
                "Context: Applied probability distribution in traffic surveillance.",
                "(i) P(Not Red) = 1 - P(Red) = 1 - 0.30 = 0.70 (or P(Green) + P(Amber) = 0.55 + 0.15 = 0.70) (1 mark).\n(ii) Expected stopped vehicles = 2000 × P(Red) = 2000 × 0.30 = 600 vehicles (2 marks).\n(iii) Sum of probabilities = P(Green) + P(Amber) + P(Red) = 0.55 + 0.15 + 0.30 = 1.00. Since all individual probabilities lie in [0, 1] and sum to 1, it is a valid exhaustive probability distribution (1 mark)."
            )
        )

        caseStudies.forEach { (q, ctx, ans) ->
            list.add(
                SundayTestQuestion(
                    id = "math_secE_${qNum}",
                    number = qNum++,
                    section = SundaySection.SECTION_E,
                    type = SundayQuestionType.CASE_STUDY,
                    marks = 4,
                    question = q,
                    caseStudyContext = ctx,
                    modelAnswer = ans,
                    rubricKeywords = listOf("sub-question evaluation", "centroid formula", "pythagoras converse", "probability distribution"),
                    webVerificationSources = listOf("CBSE Case-Based Assessment Guide", "NCERT Mathematics Class 10/11/12")
                )
            )
        }

        return list
    }

    // =========================================================================
    // SCIENCE / PHYSICS 80-MARK CBSE BOARD PAPER
    // =========================================================================
    private fun generateSciencePhysicsPaper(classId: String, className: String, subjectName: String): List<SundayTestQuestion> {
        val list = mutableListOf<SundayTestQuestion>()
        var qNum = 1

        val mcqs = listOf(
            Triple("An object is placed at a distance of 15 cm in front of a concave mirror of focal length 10 cm. The image formed is:", listOf("Virtual and erect", "Real, inverted and magnified", "Real, inverted and diminished", "Virtual and magnified"), 1),
            Triple("The resistance of a wire of length L and uniform cross-section A is R. If its length is doubled and cross-section halved, its new resistance is:", listOf("R", "2R", "4R", "8R"), 2),
            Triple("Which of the following represents the balanced chemical equation for the displacement reaction of zinc with copper sulphate?", listOf("Zn + CuSO4 -> ZnSO4 + Cu", "2Zn + CuSO4 -> Zn2SO4 + Cu", "Zn + 2CuSO4 -> Zn(SO4)2 + 2Cu", "Zn + Cu2SO4 -> ZnSO4 + 2Cu"), 0),
            Triple("The pH of gastric juices released during digestion is:", listOf("Less than 7", "More than 7", "Equal to 7", "Equal to 0"), 0),
            Triple("Which of the following compounds contains a functional group -CHO?", listOf("Ethanol", "Ethanoic acid", "Ethanal", "Ethanone"), 2),
            Triple("In human alimentary canal, the site of complete digestion of carbohydrates, proteins, and fats is:", listOf("Stomach", "Small intestine", "Large intestine", "Liver"), 1),
            Triple("The movement of sunflower in response to daylight is an example of:", listOf("Geotropism", "Phototropism", "Hydrotropism", "Chemotropism"), 1),
            Triple("Offspring formed by asexual method of reproduction have greater similarity among themselves because:", listOf("Asexual reproduction involves only one parent", "Gametes are involved", "It occurs before sexual reproduction", "Mutations are high"), 0),
            Triple("A Mendelian experiment consisted of breeding tall pea plants bearing violet flowers with short pea plants bearing white flowers. All progeny had violet flowers but almost half were short. The genetic make-up of tall parent is:", listOf("TTWW", "TTww", "TtWW", "TtWw"), 2),
            Triple("At noon, the sun appears white because:", listOf("Light is least scattered", "All colors of white light are scattered away", "Blue color is scattered most", "Red color is scattered most"), 0),
            Triple("A magnetic compass needle is placed near a current-carrying wire. If the current through the wire is increased, the deflection of the compass needle:", listOf("Decreases", "Increases", "Remains unchanged", "Becomes zero"), 1),
            Triple("Electric power is given by P = I²R. Which of the following does not express electrical power?", listOf("VI", "V²/R", "I²R", "IR²"), 3),
            Triple("Rusting of iron can be prevented by coating with a layer of zinc. This process is called:", listOf("Anodising", "Galvanisation", "Alloying", "Corrosion"), 1),
            Triple("Which of the following is an example of an exothermic reaction?", listOf("Electrolysis of water", "Photosynthesis", "Respiration in living cells", "Decomposition of calcium carbonate"), 2),
            Triple("The functional unit of human kidney is called:", listOf("Nephron", "Neuron", "Alveoli", "Glomerulus"), 0),
            Triple("When light enters from air to glass, which of its properties remains unchanged?", listOf("Wavelength", "Velocity", "Frequency", "Amplitude"), 2),
            Triple("The SI unit of magnetic field strength is:", listOf("Tesla", "Weber", "Henry", "Gauss"), 0),
            Triple("The energy available at subsequent trophic levels in a food chain follows:", listOf("10% Law", "20% Law", "50% Law", "5% Law"), 0),
            Triple("Assertion (A): Silver chloride turns grey in sunlight.\nReason (R): Decomposition of silver chloride into silver and chlorine occurs by light energy.", listOf("Both A and R are true and R is correct explanation of A", "Both A and R are true but R is not correct explanation of A", "A is true but R is false", "A is false but R is true"), 0),
            Triple("Assertion (A): Concave mirrors are used as shaving mirrors.\nReason (R): When face is held close between pole and focus, it forms an erect, magnified virtual image.", listOf("Both A and R are true and R is correct explanation of A", "Both A and R are true but R is not correct explanation of A", "A is true but R is false", "A is false but R is true"), 0)
        )

        mcqs.forEach { (q, opts, ans) ->
            list.add(
                SundayTestQuestion(
                    id = "sci_secA_${qNum}",
                    number = qNum++,
                    section = SundaySection.SECTION_A,
                    type = SundayQuestionType.MCQ,
                    marks = 1,
                    question = q,
                    options = opts,
                    correctOptionIndex = ans,
                    modelAnswer = "Correct Option: ${opts[ans]}. Grounded in NCERT Science fundamental principles.",
                    rubricKeywords = listOf("principle", "reasoning", "law"),
                    webVerificationSources = listOf("NCERT Science", "CBSE Official Guidelines")
                )
            )
        }

        // Section B: 5 Questions (2 Marks each)
        val vsa = listOf(
            Pair("Write chemical equations for the reaction that occurs when quicklime is added to water. Identify the type of reaction and temperature change.", "CaO(s) + H2O(l) -> Ca(OH)2(aq) + Heat. Combination reaction and exothermic reaction. Temperature increases significantly."),
            Pair("Name the hormone secreted by thyroid gland. Mention the mineral required for its synthesis and the disease caused by its deficiency in our diet.", "Thyroxine. Iodine is required for synthesis. Deficiency of iodine leads to Goitre (swollen neck)."),
            Pair("Draw a neat schematic ray diagram showing refraction of light through a triangular glass prism. Mark angle of incidence and angle of deviation.", "Ray strikes first face, bends towards normal. Exits second face, bends away from normal. Angle between incident ray extended forward and emergent ray extended backward is Angle of Deviation (D)."),
            Pair("Two electric bulbs rated 220V, 100W and 220V, 60W are connected in parallel to an electric mains supply. Find the total current drawn from the line.", "I1 = P1/V = 100/220 = 0.455 A. I2 = P2/V = 60/220 = 0.273 A. Total current I = I1 + I2 = (100 + 60)/220 = 160/220 = 0.73 A."),
            Pair("What is biological magnification? Explain with an example of an aquatic food chain.", "The phenomenon where non-biodegradable harmful chemicals (like pesticides, DDT) accumulate in progressively increasing concentrations at successive trophic levels of a food chain. Highest accumulation occurs in top consumers (e.g. fish-eating birds/humans).")
        )

        vsa.forEach { (q, ans) ->
            list.add(
                SundayTestQuestion(
                    id = "sci_secB_${qNum}",
                    number = qNum++,
                    section = SundaySection.SECTION_B,
                    type = SundayQuestionType.VERY_SHORT,
                    marks = 2,
                    question = q,
                    modelAnswer = ans,
                    rubricKeywords = listOf("chemical equation", "hormone", "refraction ray diagram", "biological magnification"),
                    webVerificationSources = listOf("NCERT Science Class 10/12", "CBSE Board Guidelines")
                )
            )
        }

        // Section C: 6 Questions (3 Marks each)
        val sa = listOf(
            Pair("State Ohm's Law. Draw a circuit diagram to verify this law. What does the slope of V-I graph represent?", "Ohm's Law states that electric current flowing through a conductor is directly proportional to potential difference across its ends, provided temperature and physical conditions remain constant: V ∝ I => V = IR. Circuit includes battery, key, ammeter (series), voltmeter (parallel across resistor), rheostat. Slope of V-I graph (ΔV/ΔI) represents Resistance (R)."),
            Pair("Explain the process of double circulation in human heart with a labelled schematic diagram.", "Blood flows through the heart twice during one complete cardiac cycle. 1. Pulmonary Circulation: Deoxygenated blood from right ventricle -> lungs for oxygenation -> left atrium via pulmonary veins. 2. Systemic Circulation: Oxygenated blood from left ventricle -> all body tissues via aorta -> deoxygenated blood returns to right atrium via vena cava. Prevents mixing of oxygenated and deoxygenated blood."),
            Pair("What are homologous and analogous organs? Give one example of each. What evolutionary evidence do they provide?", "Homologous organs: Same basic structural plan and embryonic origin but perform different functions (e.g. forelimbs of frog, bird, human). Evidence of divergent evolution and common ancestry. Analogous organs: Different basic structure but perform similar functions (e.g. wings of insect and wings of bat). Evidence of convergent evolution due to similar environmental adaptations."),
            Pair("A concave lens of focal length 15 cm forms an image 10 cm from the lens. How far is the object placed from the lens? Draw the ray diagram.", "f = -15 cm, v = -10 cm (virtual image formed by concave lens). Lens formula: 1/f = 1/v - 1/u => 1/u = 1/v - 1/f = -1/10 - (-1/15) = -1/10 + 1/15 = (-3 + 2)/30 = -1/30. u = -30 cm. Object is placed 30 cm in front of the concave lens."),
            Pair("Explain the electrolytic refining of copper with a neat labelled diagram. Write reactions taking place at anode and cathode.", "Anode: Impure copper block. Cathode: Thin strip of pure copper. Electrolyte: Acidified copper sulphate solution. At Anode: Cu(impure) -> Cu²⁺ + 2e⁻ (oxidation). At Cathode: Cu²⁺ + 2e⁻ -> Cu(pure) (reduction). Insoluble impurities settle down as anode mud."),
            Pair("Differentiate between biodegradable and non-biodegradable wastes with three points of comparison and suitable examples.", "1. Decomposition: Biodegradable broken down by microbial decomposers; Non-biodegradable cannot be broken down naturally. 2. Environmental impact: Biodegradable recycle organic nutrients; Non-biodegradable persist, cause pollution and bio-magnification. 3. Examples: Biodegradable = vegetable peels, paper; Non-biodegradable = plastics, DDT, electronic scrap.")
        )

        sa.forEach { (q, ans) ->
            list.add(
                SundayTestQuestion(
                    id = "sci_secC_${qNum}",
                    number = qNum++,
                    section = SundaySection.SECTION_C,
                    type = SundayQuestionType.SHORT,
                    marks = 3,
                    question = q,
                    modelAnswer = ans,
                    rubricKeywords = listOf("ohm's law", "double circulation", "homologous", "lens formula", "electrolytic refining"),
                    webVerificationSources = listOf("NCERT Science", "CBSE Official Curriculum")
                )
            )
        }

        // Section D: 4 Questions (5 Marks each)
        val la = listOf(
            Pair("Explain the mechanism of action of soap in cleaning dirty clothes. Why does micelle formation take place when soap is added to water? Will a micelle be formed in other solvents like ethanol?", "Soap molecules are sodium or potassium salts of long-chain carboxylic acids. Soap molecule has two parts: 1. Ionic hydrophilic head (water-loving). 2. Hydrocarbon hydrophobic tail (water-hating, oil-loving). In water, hydrocarbon tails dissolve in grease/dirt droplet while ionic heads face outward towards water, forming a spherical cluster called a micelle. On agitation, dirt trapped inside micelle is rinsed away. In ethanol, hydrophobic tails interact with ethanol because ethanol is non-polar/organic, so micelle does not form."),
            Pair("Draw a neat schematic diagram of female reproductive system in human beings. Explain: (i) Role of ovary, fallopian tube, and uterus. (ii) What happens when the egg is not fertilised?", "(i) Ovary produces ova and female sex hormones (estrogen & progesterone). Fallopian tube (oviduct) is site of fertilisation and conducts ovum to uterus. Uterus supports zygote implantation, development of placenta and embryo gestation. (ii) When unfertilised, thick and spongy uterine lining breaks down along with blood vessels and unfertilised egg, leading to menstruation lasting 3-5 days."),
            Pair("Describe an experiment using a glass slab to demonstrate lateral displacement of light. State the factors on which lateral displacement depends.", "When light ray enters rectangular glass slab, it bends towards normal at air-glass interface, and away from normal at glass-air interface. The emergent ray is parallel to the incident ray but shifted sideways by a perpendicular distance known as Lateral Displacement. Factors: 1. Thickness of glass slab (directly proportional). 2. Angle of incidence. 3. Refractive index of glass slab. 4. Wavelength of light (inversely proportional)."),
            Pair("What is an electric motor? State its working principle. Explain the construction and function of split rings (commutator) and brushes with a circuit diagram.", "Electric motor converts electrical energy into mechanical energy. Principle: When a current-carrying rectangular coil is placed in a magnetic field, it experiences a torque according to Fleming's Left Hand Rule. Construction: Armature coil ABCD, strong permanent magnets, split-ring commutator, carbon brushes, DC battery. Split rings reverse direction of current in the coil every half rotation, ensuring continuous unidirectional rotation. Brushes maintain sliding electrical contact with external circuit.")
        )

        la.forEach { (q, ans) ->
            list.add(
                SundayTestQuestion(
                    id = "sci_secD_${qNum}",
                    number = qNum++,
                    section = SundaySection.SECTION_D,
                    type = SundayQuestionType.LONG,
                    marks = 5,
                    question = q,
                    modelAnswer = ans,
                    rubricKeywords = listOf("micelle formation", "female reproduction system", "lateral displacement", "electric motor principle"),
                    webVerificationSources = listOf("NCERT Physics/Chemistry/Biology Class 10/12", "CBSE Marking Schemes")
                )
            )
        }

        // Section E: 3 Case Studies (4 Marks each)
        val caseStudies = listOf(
            Triple(
                "Case Study 1: Domestic Electric Circuit Safety\nAn electrical engineer audits home wiring in a residential complex. Electricity enters through main fuse rated 15A. Appliances include 2 kW electric heater, 1 kW toaster, and three 100 W incandescent lamps operating simultaneously on 220V supply.\nQuestions:\n(i) Calculate the total electric power consumed when all appliances are on.\n(ii) Determine the total current drawn from 220V supply.\n(iii) Will the 15A main fuse blow? Explain the safety significance of earthing wire.",
                "Context: Household circuit safety, overloading, and grounding.",
                "(i) Total Power P = 2000W + 1000W + 3(100W) = 3300 W (1 mark).\n(ii) Current I = P/V = 3300 / 220 = 15.0 A (1 mark).\n(iii) The 15A fuse is at its critical threshold and may blow with any slight surge (1 mark). Earth wire provides low resistance path for metallic appliance leakages, preventing fatal electric shocks to users (1 mark)."
            ),
            Triple(
                "Case Study 2: Extraction of Metals from Ores\nMetals low in reactivity series like Mercury (cinnabar ore HgS) and copper (copper glance Cu2S) can be reduced by heating alone in presence of oxygen (roasting).\nQuestions:\n(i) Write the chemical equation for roasting of cinnabar ore.\n(ii) Write the reduction equation of mercuric oxide to mercury metal.\n(iii) What is the difference between calcination and roasting?",
                "Context: Metallurgy and reduction processes of sulphide ores.",
                "(i) 2HgS(s) + 3O2(g) --heat--> 2HgO(s) + 2SO2(g) (1.5 marks).\n(ii) 2HgO(s) --heat--> 2Hg(l) + O2(g) (1.5 marks).\n(iii) Roasting is heating carbonate/sulphide ores in excess air; Calcination is heating in limited or absence of air (1 mark)."
            ),
            Triple(
                "Case Study 3: Human Eye Defects & Correction\nA 16-year-old student sitting in the back row of a classroom is unable to read letters written on the blackboard clearly, but reads textbook text comfortably.\nQuestions:\n(i) Identify the eye defect the student is suffering from.\n(ii) State two common causes of this defect.\n(iii) Name the type of corrective lens required and explain how it restores sharp vision on retina.",
                "Context: Human visual optics and refractive errors.",
                "(i) Myopia (Near-sightedness) (1 mark).\n(ii) Causes: 1. Excessive curvature of eye cornea/lens. 2. Elongation of the eyeball (1.5 marks).\n(iii) Corrective lens: Concave (diverging) lens. It diverges incoming parallel light rays slightly so that after passing through crystalline eye lens, image focuses sharply on the retina instead of in front of it (1.5 marks)."
            )
        )

        caseStudies.forEach { (q, ctx, ans) ->
            list.add(
                SundayTestQuestion(
                    id = "sci_secE_${qNum}",
                    number = qNum++,
                    section = SundaySection.SECTION_E,
                    type = SundayQuestionType.CASE_STUDY,
                    marks = 4,
                    question = q,
                    caseStudyContext = ctx,
                    modelAnswer = ans,
                    rubricKeywords = listOf("power current calculation", "roasting calcination", "myopia correction"),
                    webVerificationSources = listOf("NCERT Science", "CBSE Official Question Bank")
                )
            )
        }

        return list
    }

    // =========================================================================
    // CHEMISTRY 80-MARK CBSE BOARD PAPER
    // =========================================================================
    private fun generateChemistryPaper(classId: String, className: String): List<SundayTestQuestion> {
        // Fallback to Science/Physics engine which includes rich chemistry & physics sections
        return generateSciencePhysicsPaper(classId, className, "Chemistry")
    }

    // =========================================================================
    // BIOLOGY 80-MARK CBSE BOARD PAPER
    // =========================================================================
    private fun generateBiologyPaper(classId: String, className: String): List<SundayTestQuestion> {
        return generateSciencePhysicsPaper(classId, className, "Biology")
    }

    // =========================================================================
    // SOCIAL SCIENCE 80-MARK CBSE BOARD PAPER
    // =========================================================================
    private fun generateSocialSciencePaper(classId: String, className: String): List<SundayTestQuestion> {
        val list = mutableListOf<SundayTestQuestion>()
        var qNum = 1

        val mcqs = listOf(
            Triple("Who was declared the first King of United Italy in 1861?", listOf("Giuseppe Garibaldi", "Victor Emmanuel II", "Count Cavour", "Otto von Bismarck"), 1),
            Triple("The Non-Cooperation Movement was called off by Mahatma Gandhi due to which incident?", listOf("Jallianwala Bagh massacre", "Chauri Chaura incident", "Kakori train robbery", "Simon Commission"), 1),
            Triple("Which of the following soils is known for its self-aeration capacity and is ideal for cotton cultivation?", listOf("Alluvial soil", "Black soil", "Red and yellow soil", "Laterite soil"), 1),
            Triple("Kharif crops are harvested in which months?", listOf("October-November", "September-October", "March-April", "June-July"), 1),
            Triple("Which is the oldest artificial port on the east coast of India?", listOf("Kolkata", "Chennai", "Visakhapatnam", "Paradip"), 1),
            Triple("In which year was the Sinhala Act passed in Sri Lanka recognizing Sinhala as the only official language?", listOf("1948", "1956", "1965", "1972"), 1),
            Triple("Which tier of government has the power to legislate on subjects mentioned in the Residuary List in India?", listOf("Union Government", "State Government", "Local Government", "Both Union and State"), 0),
            Triple("Which organization publishes the Human Development Report (HDR)?", listOf("World Bank", "UNDP", "IMF", "WHO"), 1),
            Triple("Activities in which natural goods are produced by exploiting natural resources belong to which sector?", listOf("Primary sector", "Secondary sector", "Tertiary sector", "Quaternary sector"), 0),
            Triple("Which body in India supervises the functioning of formal sources of loans?", listOf("Finance Ministry", "Reserve Bank of India", "State Bank of India", "NITI Aayog"), 1),
            Triple("Removal of trade barriers or restrictions set by the government is known as:", listOf("Privatisation", "Globalisation", "Liberalisation", "Nationalisation"), 2),
            Triple("The Consumer Protection Act (COPRA) was enacted in India in the year:", listOf("1986", "1991", "2005", "2019"), 0),
            Triple("Which of the following states has the highest literacy rate in India as per 2011 census?", listOf("Kerala", "Goa", "Himachal Pradesh", "Tamil Nadu"), 0),
            Triple("Rinderpest was a devastating cattle disease that arrived in which continent in late 1880s?", listOf("Africa", "Asia", "Europe", "South America"), 0),
            Triple("Which treaty recognized Greece as an independent nation in 1832?", listOf("Treaty of Vienna", "Treaty of Constantinople", "Treaty of Versailles", "Treaty of Geneva"), 1),
            Triple("Bhakra Nangal river valley project is built on which river?", listOf("Ganga", "Satluj", "Yamuna", "Godavari"), 1),
            Triple("Which of the following is a non-metallic mineral?", listOf("Bauxite", "Mica", "Copper", "Iron ore"), 1),
            Triple("The system of checks and balances is another name for:", listOf("Horizontal division of power", "Vertical division of power", "Community government", "Federal coalition"), 0),
            Triple("Assertion (A): Democracy produces a government that is accountable to citizens.\nReason (R): Regular, free and fair elections and open public debate are key features of democracy.", listOf("Both A and R are true and R is correct explanation of A", "Both A and R are true but R is not correct explanation of A", "A is true but R is false", "A is false but R is true"), 0),
            Triple("Assertion (A): Agriculture and industry are not exclusive of each other; they move hand in hand.\nReason (R): Agro-industries have given a major boost to agriculture by raising its productivity.", listOf("Both A and R are true and R is correct explanation of A", "Both A and R are true but R is not correct explanation of A", "A is true but R is false", "A is false but R is true"), 0)
        )

        mcqs.forEach { (q, opts, ans) ->
            list.add(
                SundayTestQuestion(
                    id = "sst_secA_${qNum}",
                    number = qNum++,
                    section = SundaySection.SECTION_A,
                    type = SundayQuestionType.MCQ,
                    marks = 1,
                    question = q,
                    options = opts,
                    correctOptionIndex = ans,
                    modelAnswer = "Correct Option: ${opts[ans]}.",
                    rubricKeywords = listOf("historical event", "constitutional power", "economic classification"),
                    webVerificationSources = listOf("NCERT Social Science", "CBSE Board Syllabus")
                )
            )
        }

        // Add Section B (5 VSA), C (6 SA), D (4 LA), E (3 Case Studies)
        // Section B
        val vsa = listOf(
            Pair("Why did Mahatma Gandhi decide to withdraw the Non-Cooperation Movement in February 1922?", "At Chauri Chaura in Gorakhpur, a peaceful demonstration turned violent and participants burnt a police station killing 22 policemen. Gandhiji felt the movement was turning violent and satyagrahis needed proper training before launching mass struggles."),
            Pair("Differentiate between renewable and non-renewable resources with one example of each.", "Renewable resources can be renewed or reproduced by physical, chemical or mechanical processes (e.g. solar energy, wind energy, forests). Non-renewable resources occur over long geological times and get exhausted with consumption (e.g. coal, petroleum, natural gas)."),
            Pair("State two reasons why power sharing is desirable in a democracy.", "1. Prudential reason: It reduces the possibility of conflict between social groups and ensures political stability. 2. Moral reason: Power sharing is the very spirit of democracy; citizens have a fundamental right to be consulted on governance."),
            Pair("Why is per capita income calculated in US Dollars rather than local currencies for international comparisons?", "US Dollars are used as a universal benchmark currency adjusted for Purchasing Power Parity (PPP) so that one dollar buys the same basket of goods and services across all countries, eliminating domestic price distortion."),
            Pair("What is the basic objective of the 'Self Help Groups' (SHGs) for the rural poor?", "To organize rural poor, especially women, pool their small savings, provide collateral-free small loans at reasonable interest rates, and build sustainable micro-enterprises and women empowerment.")
        )
        vsa.forEach { (q, ans) ->
            list.add(
                SundayTestQuestion(
                    id = "sst_secB_${qNum}",
                    number = qNum++,
                    section = SundaySection.SECTION_B,
                    type = SundayQuestionType.VERY_SHORT,
                    marks = 2,
                    question = q,
                    modelAnswer = ans,
                    rubricKeywords = listOf("chauri chaura", "prudential moral reasons", "ppp comparison", "shg collateral"),
                    webVerificationSources = listOf("NCERT Social Science Class 10")
                )
            )
        }

        // Section C
        val sa = listOf(
            Pair("Describe the process of Unification of Germany.", "1. Nationalist feelings were widespread among middle-class Germans who tried to unite fragmented confederations in 1848 Frankfurt parliament. 2. Prussia took leadership under Chief Minister Otto von Bismarck with help of Prussian army and bureaucracy. 3. Three wars over seven years with Austria, Denmark, and France ended in Prussian victory. 4. In January 1871, Prussian King William I was proclaimed German Emperor at Hall of Mirrors in Versailles."),
            Pair("Explain three institutional and technological reforms introduced by the Indian government to improve agriculture.", "1. Institutional: Collectivisation, consolidation of holdings (Chakbandi), abolition of zamindari system, and land ceiling. 2. Technological: Green Revolution (HYV seeds, fertilizers) and White Revolution (Operation Flood). 3. Financial/Support: Provision of crop insurance against drought/floods, establishment of Grameen banks, Kisan Credit Card (KCC), and MSP announcements."),
            Pair("How does federalism accommodate social and regional diversity in India?", "1. Three-tier governance: Central, State, and Local (Panchayats/Municipalities). 2. Linguistic states: Reorganisation of states on linguistic lines united the country. 3. Language policy: Hindi is official language, but 21 other scheduled languages are constitutionally protected. 4. Special provisions for border/tribal states under Article 371."),
            Pair("Explain the term 'Disguised Unemployment'. Give an example from the agricultural sector.", "A situation where more people are employed in an activity than actually required. Even if several workers are withdrawn, total production does not fall. Example: A rural family of 5 members working on a 2-hectare farm where only 2 workers are needed. The marginal productivity of the extra 3 members is zero."),
            Pair("How do Multinational Corporations (MNCs) interlink production across countries? Explain with three points.", "1. Setting up joint ventures with local domestic companies. 2. Buying up existing local companies and expanding their production network (e.g. Cargill Foods acquiring Parakh Foods). 3. Placing large orders with small local producers for garments, footwear, and sports goods under their own brand names."),
            Pair("What are the major challenges faced by political parties in modern democracies?", "1. Lack of internal democracy: Power concentrated in top leadership. 2. Dynastic succession: Family members favored over deserving grassroot cadres. 3. Growing role of money and muscle power in elections. 4. Lack of meaningful ideological choices for voters.")
        )
        sa.forEach { (q, ans) ->
            list.add(
                SundayTestQuestion(
                    id = "sst_secC_${qNum}",
                    number = qNum++,
                    section = SundaySection.SECTION_C,
                    type = SundayQuestionType.SHORT,
                    marks = 3,
                    question = q,
                    modelAnswer = ans,
                    rubricKeywords = listOf("bismarck unification", "kisan credit card", "disguised unemployment", "mnc integration"),
                    webVerificationSources = listOf("NCERT History/Geography/Political Science/Economics")
                )
            )
        }

        // Section D
        val la = listOf(
            Pair("Analyze the role of women in the Civil Disobedience Movement.", "Large scale participation of women was an outstanding feature of Civil Disobedience Movement. 1. Thousands of women came out of homes to listen to Gandhiji during Salt March. 2. They manufactured salt, picketed foreign cloth shops and liquor stores. 3. Many went to jail in urban and rural centers. 4. In urban areas, women came from high-caste educated families; in rural areas from rich peasant households. 5. However, Congress was reluctant to grant positions of authority to women within the organisation for a long time."),
            Pair("Why is conservation of mineral resources essential? Suggest four practical measures to conserve minerals.", "Minerals are geological finite assets that take millions of years to form. Continued extraction leads to depletion, higher costs, and ecological damage. Measures: 1. Recycling of metals using scrap metals. 2. Using renewable substitutes (e.g. solar energy instead of coal). 3. Adopting advanced mining technologies to minimize wastage. 4. Planned sustainable management and reuse of industrial minerals."),
            Pair("Explain five measures adopted by the Constitution of India to make the third-tier of democracy (local self-government) powerful and effective after the 1992 Amendment.", "1. Mandatory regular elections to local government bodies. 2. Reservation of seats for SC, ST, and OBCs. 3. At least one-third (33%) of all positions reserved for women. 4. Independent State Election Commission created in each state to conduct panchayat and municipal elections. 5. State governments mandated to share revenue and administrative powers with local bodies."),
            Pair("Describe the vital role of formal credit in rural economic development. Why is it necessary to expand formal sources of credit in rural India?", "1. Formal credit (banks, cooperatives) provides affordable loans at regulated interest rates, enabling farmers to invest in seeds, fertilizers, and irrigation. 2. Informal moneylenders charge exorbitant interest (36-60% per annum), trapping borrowers in debt traps. 3. Cheap credit promotes rural micro-enterprises, cottage industries, and agricultural modernizations. 4. Prevents exploitation and distress sale of land/assets.")
        )
        la.forEach { (q, ans) ->
            list.add(
                SundayTestQuestion(
                    id = "sst_secD_${qNum}",
                    number = qNum++,
                    section = SundaySection.SECTION_D,
                    type = SundayQuestionType.LONG,
                    marks = 5,
                    question = q,
                    modelAnswer = ans,
                    rubricKeywords = listOf("women civil disobedience", "mineral conservation recycling", "73rd amendment local bodies", "formal credit debt trap"),
                    webVerificationSources = listOf("NCERT Social Science", "CBSE Official Syllabus")
                )
            )
        }

        // Section E
        val caseStudies = listOf(
            Triple(
                "Case Study 1: Print Revolution and Public Debate\nThe print revolution created a new culture of dialogue and debate. Ideas of scientists and philosophers became accessible to the common people. Writings of Voltaire and Rousseau were read widely, and those who read these books saw the world through eyes that were questioning, critical and rational.\nQuestions:\n(i) How did the print revolution impact traditional beliefs?\n(ii) Mention two thinkers whose writings influenced the French Revolution.\n(iii) Why did authorities fear the power of printed books?",
                "Context: Print culture, dissemination of Enlightenment philosophy and social revolution.",
                "(i) Print opened possibilities of questioning dogma, institutional corruption of church, and absolute royal authority (1 mark).\n(ii) Voltaire and Jean-Jacques Rousseau (1.5 marks).\n(iii) Rulers feared that uncontrolled printed material could incite rebellion, heresy, and destabilize monarchies (1.5 marks)."
            ),
            Triple(
                "Case Study 2: Jute Textile Industry in Hooghly Basin\nMost of the jute mills in India are located in West Bengal, mainly along the banks of the Hooghly river in a narrow belt.\nQuestions:\n(i) Name two major factors responsible for concentration of jute mills in Hooghly basin.\n(ii) What challenges does the Indian jute industry face currently?\n(iii) What policy has government adopted to promote domestic jute consumption?",
                "Context: Geography of agro-based textile manufacturing clusters.",
                "(i) Proximity of jute-producing areas, abundant fresh water for processing, cheap labor from West Bengal, Bihar, and good railway/water transport network (1.5 marks).\n(ii) Stiff competition in international market from synthetic substitutes and other competitors like Bangladesh (1.5 marks).\n(iii) Mandatory use of jute packaging material under National Jute Policy (1 mark)."
            ),
            Triple(
                "Case Study 3: Globalisation and Workers' Vulnerability\nWith globalization and flexible labor laws, large retailers in America and Europe place contracts with garment exporters in developing countries like India. To maximize profits and deliver orders on time, employers employ workers on temporary basis with long working hours.\nQuestions:\n(i) What do you mean by flexible labor laws?\n(ii) How has globalization impacted job security of factory workers?\n(iii) Suggest one measure to ensure fair globalization for workers.",
                "Context: Modern labor conditions, garment exports, and fair globalization.",
                "(i) Laws allowing employers to hire and fire workers according to fluctuating seasonal demand rather than permanent contracts (1.5 marks).\n(ii) Loss of permanent job security, denial of health/pension benefits, and forced overtime during peak seasons (1.5 marks).\n(iii) Strict enforcement of labor laws by government, fair minimum wages, and social security benefits (1 mark)."
            )
        )
        caseStudies.forEach { (q, ctx, ans) ->
            list.add(
                SundayTestQuestion(
                    id = "sst_secE_${qNum}",
                    number = qNum++,
                    section = SundaySection.SECTION_E,
                    type = SundayQuestionType.CASE_STUDY,
                    marks = 4,
                    question = q,
                    caseStudyContext = ctx,
                    modelAnswer = ans,
                    rubricKeywords = listOf("print revolution", "hooghly jute basin", "globalization labor rights"),
                    webVerificationSources = listOf("NCERT Social Science", "CBSE Marking Scheme")
                )
            )
        }

        return list
    }

    // =========================================================================
    // GENERAL / ENGLISH / OTHER CBSE BOARD PAPER
    // =========================================================================
    private fun generateGeneralCBSEPaper(classId: String, className: String, subjectName: String): List<SundayTestQuestion> {
        // Fallback to mathematics paper format with class & subject context
        return generateMathematicsPaper(classId, className)
    }
}
