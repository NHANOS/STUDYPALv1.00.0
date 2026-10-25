package com.example.model.curriculum

import com.example.model.ChapterHighlight
import com.example.model.ChapterInfo
import com.example.model.ClassInfo
import com.example.model.SubjectInfo

object Class11Curriculum {

    fun create(): ClassInfo {
        return ClassInfo(
            id = "class_11",
            name = "Class 11",
            subtitle = "Official CBSE Senior Secondary Curriculum",
            gradeNumber = 11,
            subjects = listOf(
                createMathematics(),
                createPhysics(),
                createChemistry(),
                createBiology(),
                createComputerScience(),
                createEconomics()
            )
        )
    }

    private fun createMathematics(): SubjectInfo {
        return SubjectInfo(
            id = "c11_math",
            name = "Mathematics",
            code = "MATH-11",
            description = "Complete CBSE Class 11 Mathematics syllabus covering calculus, algebra, and geometry",
            iconCategory = "math",
            chapters = listOf(
                ChapterInfo("c11_m_ch1", 1, "Sets", 50, "Sets and representations, empty set, subsets, power sets, universal set, Venn diagrams, union and intersection, complement of a set, De Morgan's laws.", listOf(
                    ChapterHighlight("De Morgan's Laws", "Set Theory", "(A ∪ B)' = A' ∩ B'  and  (A ∩ B)' = A' ∪ B'.")
                )),
                ChapterInfo("c11_m_ch2", 2, "Relations and Functions", 55, "Cartesian product, relations, domain, codomain, range, functions, types of functions (identity, constant, polynomial, rational, modulus, signum, greatest integer).", listOf(
                    ChapterHighlight("Function Definition", "Definition", "A relation f from A to B is a function if every element of A has one and only one image in B.")
                )),
                ChapterInfo("c11_m_ch3", 3, "Trigonometric Functions", 60, "Radian and degree measures, trigonometric identities, sign of trigonometric functions, sum and difference formulas, double and triple angle identities.", listOf(
                    ChapterHighlight("Sum & Difference", "Formula", "sin(x ± y) = sin x cos y ± cos x sin y ; cos(x ± y) = cos x cos y ∓ sin x sin y.")
                )),
                ChapterInfo("c11_m_ch4", 4, "Complex Numbers and Quadratic Equations", 50, "Imaginary unit i, complex numbers in algebraic form z = a + ib, modulus, conjugate, Argand plane, quadratic equations with complex roots.", listOf(
                    ChapterHighlight("Modulus & Conjugate", "Formula", "|z| = √(a² + b²) ; z̄ = a - ib ; z · z̄ = |z|².")
                )),
                ChapterInfo("c11_m_ch5", 5, "Linear Inequalities", 45, "Algebraic solutions of linear inequalities in one variable, graphical representation on number line and Cartesian plane.", listOf(
                    ChapterHighlight("Sign Reversal Rule", "Inequality", "Multiplying or dividing both sides by a negative number reverses the inequality sign: -x < y ⟹ x > -y.")
                )),
                ChapterInfo("c11_m_ch6", 6, "Permutations and Combinations", 55, "Fundamental principle of counting, factorial n!, permutations nPr, combinations nCr, circular permutations and word problems.", listOf(
                    ChapterHighlight("Permutation & Combination", "Formula", "nPr = n! / (n - r)! ; nCr = n! / [r!(n - r)!] ; nCr = nC(n - r).")
                )),
                ChapterInfo("c11_m_ch7", 7, "Binomial Theorem", 50, "Statement and proof of the Binomial Theorem for positive integral indices, Pascal's triangle, general and middle terms.", listOf(
                    ChapterHighlight("Binomial Expansion", "Formula", "(a + b)ⁿ = Σ [nCr · a^(n - r) · b^r] from r = 0 to n.")
                )),
                ChapterInfo("c11_m_ch8", 8, "Sequences and Series", 55, "Arithmetic and Geometric Progressions (AP and GP), general term of GP (ar^(n-1)), sum of first n terms of GP, sum of infinite GP, arithmetic and geometric means.", listOf(
                    ChapterHighlight("Geometric Progression", "Formula", "Sum S_n = a(rⁿ - 1)/(r - 1) ; Infinite GP S_∞ = a / (1 - r) for |r| < 1 ; AM ≥ GM.")
                )),
                ChapterInfo("c11_m_ch9", 9, "Straight Lines", 55, "Slope of a line, angle between two lines, collinearity, various forms of line equations (slope-intercept, point-slope, two-point, intercept, normal), distance of a point from a line.", listOf(
                    ChapterHighlight("Perpendicular Distance", "Formula", "d = |Ax₁ + By₁ + C| / √(A² + B²) for line Ax + By + C = 0.")
                )),
                ChapterInfo("c11_m_ch10", 10, "Conic Sections", 60, "Sections of a cone: circle, ellipse, parabola, hyperbola, standard equations and properties (focus, directrix, eccentricity, latus rectum).", listOf(
                    ChapterHighlight("Standard Parabola & Ellipse", "Formula", "Parabola: y² = 4ax (latus rectum = 4a) ; Ellipse: x²/a² + y²/b² = 1 (c² = a² - b², e = c/a).")
                )),
                ChapterInfo("c11_m_ch11", 11, "Introduction to Three Dimensional Geometry", 45, "Coordinate axes and coordinate planes in 3D, coordinates of a point, distance between two points, section formula.", listOf(
                    ChapterHighlight("3D Distance Formula", "Formula", "d = √[(x₂ - x₁)² + (y₂ - y₁)² + (z₂ - z₁)²].")
                )),
                ChapterInfo("c11_m_ch12", 12, "Limits and Derivatives", 65, "Intuitive idea of limit, limits of polynomials, rational and trigonometric functions, derivative as rate of change, product rule, quotient rule, first principle.", listOf(
                    ChapterHighlight("First Principle of Derivative", "Calculus", "f'(x) = lim(h→0) [f(x + h) - f(x)] / h ; Product Rule: (uv)' = u'v + uv'.")
                )),
                ChapterInfo("c11_m_ch13", 13, "Statistics", 45, "Measures of dispersion: range, mean deviation, variance and standard deviation of ungrouped and grouped data.", listOf(
                    ChapterHighlight("Variance & Standard Deviation", "Formula", "Variance σ² = (1/N) Σ fᵢ(xᵢ - x̄)² ; Standard Deviation σ = √(Variance).")
                )),
                ChapterInfo("c11_m_ch14", 14, "Probability", 45, "Random experiments, outcomes, sample spaces, events (exhaustive, mutually exclusive), axiomatic approach to probability, addition theorem.", listOf(
                    ChapterHighlight("Addition Theorem", "Probability", "P(A ∪ B) = P(A) + P(B) - P(A ∩ B) ; If mutually exclusive: P(A ∪ B) = P(A) + P(B).")
                ))
            )
        )
    }

    private fun createPhysics(): SubjectInfo {
        return SubjectInfo(
            id = "c11_phys",
            name = "Physics",
            code = "PHYS-11",
            description = "Complete CBSE Class 11 Physics syllabus covering mechanics, gravitation, thermodynamics, and waves",
            iconCategory = "physics",
            chapters = listOf(
                ChapterInfo("c11_p_ch1", 1, "Units and Measurements", 45, "SI units, fundamental and derived units, errors in measurement, significant figures, dimensional analysis and applications.", listOf(
                    ChapterHighlight("Dimensional Homogeneity", "Principle", "Dimensions of every term in a physical equation must be identical on both sides.")
                )),
                ChapterInfo("c11_p_ch2", 2, "Motion in a Straight Line", 50, "Position, displacement, average velocity, instantaneous velocity, uniform acceleration, kinematics equations via calculus.", listOf(
                    ChapterHighlight("Calculus Kinematics", "Formula", "v = dx/dt ; a = dv/dt = v(dv/dx) ; v = u + at ; s = ut + ½at² ; v² - u² = 2as.")
                )),
                ChapterInfo("c11_p_ch3", 3, "Motion in a Plane", 55, "Scalars and vectors, vector addition & resolution, dot and cross products, projectile motion (trajectory, time of flight, range, maximum height), uniform circular motion.", listOf(
                    ChapterHighlight("Projectile Motion", "Formula", "T = (2u sinθ)/g ; H = (u² sin²θ)/(2g) ; R = (u² sin 2θ)/g (Max range at 45°).")
                )),
                ChapterInfo("c11_p_ch4", 4, "Laws of Motion", 60, "Newton's laws of motion, inertia, momentum, impulse, law of conservation of linear momentum, friction (static, kinetic, rolling), banking of circular roads.", listOf(
                    ChapterHighlight("Banking of Roads", "Formula", "v_max = √[rg (tanθ + μ) / (1 - μ tanθ)] ; Optimum speed v = √(rg tanθ).")
                )),
                ChapterInfo("c11_p_ch5", 5, "Work, Energy and Power", 55, "Work-energy theorem, conservative and non-conservative forces, potential energy of a spring, elastic and inelastic collisions in 1D and 2D.", listOf(
                    ChapterHighlight("Work-Energy Theorem", "Theorem", "Net work done on a body equals the change in its kinetic energy: W_net = ΔK = ½mv² - ½mu².")
                )),
                ChapterInfo("c11_p_ch6", 6, "System of Particles and Rotational Motion", 65, "Center of mass, torque, angular momentum, conservation of angular momentum, moment of inertia, radius of gyration, parallel and perpendicular axes theorems.", listOf(
                    ChapterHighlight("Angular Momentum Conservation", "Law", "L = I · ω = constant when net external torque Στ = 0 (e.g. spinning figure skater).")
                )),
                ChapterInfo("c11_p_ch7", 7, "Gravitation", 50, "Kepler's laws of planetary motion, universal gravitation, variation of g with altitude and depth, gravitational potential energy, escape velocity, orbital velocity of satellites.", listOf(
                    ChapterHighlight("Escape Velocity", "Formula", "v_e = √(2GM / R) = √(2gR) ≈ 11.2 km/s on Earth's surface.")
                )),
                ChapterInfo("c11_p_ch8", 8, "Mechanical Properties of Solids", 45, "Stress, strain, Hooke's law, stress-strain curve, Young's modulus, shear modulus, bulk modulus, Poisson's ratio, elastic potential energy.", listOf(
                    ChapterHighlight("Hooke's Law", "Law", "Stress = Modulus of Elasticity × Strain (valid within proportional limit).")
                )),
                ChapterInfo("c11_p_ch9", 9, "Mechanical Properties of Fluids", 60, "Pressure in a fluid, Pascal's law, hydraulic lift, surface tension, surface energy, angle of contact, viscosity, Stokes' law, terminal velocity, streamline flow, Bernoulli's principle.", listOf(
                    ChapterHighlight("Bernoulli's Equation", "Fluid Dynamics", "P + ½ρv² + ρgh = constant along a streamline for non-viscous incompressible fluid.")
                )),
                ChapterInfo("c11_p_ch10", 10, "Thermal Properties of Matter", 50, "Heat and temperature, thermal expansion, specific heat capacity, calorimetry, latent heat, heat transfer (conduction, convection, radiation), Newton's law of cooling, Stefan's law.", listOf(
                    ChapterHighlight("Stefan-Boltzmann Law", "Radiation", "Radiated power E = eσAT⁴ (where σ = 5.67 × 10⁻⁸ W/m²K⁴).")
                )),
                ChapterInfo("c11_p_ch11", 11, "Thermodynamics", 60, "Thermal equilibrium, Zeroth law, First law of thermodynamics (ΔQ = ΔU + ΔW), isothermal and adiabatic processes, Second law of thermodynamics, heat engines and refrigerators.", listOf(
                    ChapterHighlight("First Law of Thermodynamics", "Fundamental Law", "ΔQ = ΔU + ΔW (Conservation of energy in thermodynamic transformations).")
                )),
                ChapterInfo("c11_p_ch12", 12, "Kinetic Theory of Gases", 45, "Molecular nature of matter, ideal gas equation PV = nRT, kinetic pressure formula P = ⅓ρv_rms², degrees of freedom, law of equipartition of energy, mean free path.", listOf(
                    ChapterHighlight("Average Kinetic Energy", "Formula", "Average translational KE of gas molecule = (3/2)k_B T.")
                )),
                ChapterInfo("c11_p_ch13", 13, "Oscillations", 55, "Periodic and oscillatory motion, Simple Harmonic Motion (SHM), displacement, velocity, acceleration, energy in SHM, simple pendulum, springs in series and parallel.", listOf(
                    ChapterHighlight("Simple Pendulum Period", "Formula", "T = 2π √(L / g) (independent of bob mass and amplitude for small swings).")
                )),
                ChapterInfo("c11_p_ch14", 14, "Waves", 55, "Transverse and longitudinal waves, speed of wave motion, progressive wave equation, principle of superposition, standing waves in strings and organ pipes, beats.", listOf(
                    ChapterHighlight("Beats Frequency", "Acoustics", "f_beat = |f₁ - f₂| (Difference in frequencies between two interfering audio sources).")
                ))
            )
        )
    }

    private fun createChemistry(): SubjectInfo {
        return SubjectInfo(
            id = "c11_chem",
            name = "Chemistry",
            code = "CHEM-11",
            description = "Complete CBSE Class 11 Chemistry syllabus covering physical, inorganic, and organic chemistry",
            iconCategory = "chemistry",
            chapters = listOf(
                ChapterInfo("c11_c_ch1", 1, "Some Basic Concepts of Chemistry", 50, "Mole concept, molar mass, empirical and molecular formulas, stoichiometry, limiting reagent, concentration terms (molarity, molality, mole fraction).", listOf(
                    ChapterHighlight("Molarity vs Molality", "Formula", "Molarity M = moles/V(L) ; Molality m = moles/mass of solvent(kg) (temperature independent).")
                )),
                ChapterInfo("c11_c_ch2", 2, "Structure of Atom", 60, "Bohr's model and limitations, dual nature of matter (de Broglie), Heisenberg's uncertainty principle, quantum numbers (n, l, m, s), orbital shapes, Aufbau principle, Pauli exclusion principle, Hund's rule.", listOf(
                    ChapterHighlight("Heisenberg's Uncertainty", "Quantum Mechanics", "Δx · Δp ≥ h / (4π). Position and momentum cannot both be precisely measured simultaneously.")
                )),
                ChapterInfo("c11_c_ch3", 3, "Classification of Elements and Periodicity", 50, "Modern periodic law, periodic trends in properties (atomic radii, ionic radii, ionization enthalpy, electron gain enthalpy, electronegativity, valence).", listOf(
                    ChapterHighlight("Ionization Enthalpy Trend", "Periodic Trend", "Increases across a period (greater effective nuclear charge) and decreases down a group (increased screening).")
                )),
                ChapterInfo("c11_c_ch4", 4, "Chemical Bonding and Molecular Structure", 65, "Ionic bond, lattice enthalpy, covalent bond, dipole moment, VSEPR theory, hybridization (sp, sp², sp³, dsp²), Molecular Orbital Theory (MOT) of homonuclear diatomic molecules, hydrogen bonding.", listOf(
                    ChapterHighlight("Bond Order in MOT", "Formula", "Bond Order = ½(N_b - N_a). Higher bond order indicates greater stability and shorter bond length.")
                )),
                ChapterInfo("c11_c_ch5", 5, "Chemical Thermodynamics", 60, "System and surroundings, state functions, first law of thermodynamics, enthalpy H = U + PV, Hess's law, spontaneity, entropy (S), Gibbs free energy (ΔG = ΔH - TΔS).", listOf(
                    ChapterHighlight("Gibbs Spontaneity Criterion", "Thermodynamics", "ΔG < 0 (spontaneous process at constant T and P) ; ΔG = 0 at equilibrium.")
                )),
                ChapterInfo("c11_c_ch6", 6, "Equilibrium", 65, "Dynamic equilibrium, law of mass action, equilibrium constants (Kc, Kp), Le Chatelier's principle, ionic equilibrium, acids and bases (Arrhenius, Bronsted-Lowry, Lewis), pH scale, common ion effect, buffer solutions, solubility product (Ksp).", listOf(
                    ChapterHighlight("Le Chatelier's Principle", "Principle", "If a dynamic equilibrium is subjected to change in T, P, or concentration, the system shifts to counteract the change.")
                )),
                ChapterInfo("c11_c_ch7", 7, "Redox Reactions", 45, "Concept of oxidation and reduction, oxidation number rules, balancing redox reactions by oxidation number and ion-electron methods, electrochemical cells.", listOf(
                    ChapterHighlight("Oxidation Number", "Concept", "Hypothetical charge an atom would carry if all bonds to other atoms were completely ionic.")
                )),
                ChapterInfo("c11_c_ch8", 8, "Organic Chemistry — Basic Principles & Techniques", 65, "IUPAC nomenclature of organic compounds, electronic displacements (inductive, electromeric, resonance, hyperconjugation), types of organic reactions, reactive intermediates (carbocations, carbanions, free radicals), purification techniques.", listOf(
                    ChapterHighlight("Hyperconjugation", "Electronic Effect", "Delocalization of σ electrons of C-H bond into adjacent unshared p-orbital stabilizes carbocations.")
                )),
                ChapterInfo("c11_c_ch9", 9, "Hydrocarbons", 60, "Classification of hydrocarbons: Alkanes (conformations of ethane, Wurtz reaction), Alkenes (Markovnikov's addition, ozonolysis), Alkynes (acidity of terminal alkynes), Aromatic hydrocarbons (benzene structure, electrophilic aromatic substitution, Huckel's rule).", listOf(
                    ChapterHighlight("Huckel's Rule of Aromaticity", "Rule", "A planar, cyclic, fully conjugated system with (4n + 2) π electrons is aromatic.")
                ))
            )
        )
    }

    private fun createBiology(): SubjectInfo {
        return SubjectInfo(
            id = "c11_bio",
            name = "Biology",
            code = "BIO-11",
            description = "Complete CBSE Class 11 Biology syllabus covering botany, zoology, physiology, and cytology",
            iconCategory = "biology",
            chapters = listOf(
                ChapterInfo("c11_b_ch1", 1, "The Living World", 40, "Characteristics of living organisms, biodiversity, taxonomy, binomial nomenclature, taxonomic hierarchy.", listOf(
                    ChapterHighlight("Binomial Nomenclature", "Taxonomy", "Two-part Latinized scientific name: Genus capitalized, species in lowercase (Linnaeus).")
                )),
                ChapterInfo("c11_b_ch2", 2, "Biological Classification", 50, "Five kingdom classification (Whittaker): Monera, Protista, Fungi, Plantae, Animalia; viruses, viroids, lichens.", listOf(
                    ChapterHighlight("Five Kingdoms", "Classification", "Whittaker classified life based on cell structure, body organization, mode of nutrition, and reproduction.")
                )),
                ChapterInfo("c11_b_ch3", 3, "Plant Kingdom", 50, "Algae, Bryophytes, Pteridophytes, Gymnosperms, Angiosperms, alternation of generations.", listOf(
                    ChapterHighlight("Gymnosperms", "Botany", "Naked seed-bearing plants with dominant sporophytic generation (e.g. Pinus, Cycas).")
                )),
                ChapterInfo("c11_b_ch4", 4, "Animal Kingdom", 55, "Basis of classification (symmetry, coelom, germ layers, notochord), non-chordates and chordates (Phyla Porifera to Mammalia).", listOf(
                    ChapterHighlight("Chordate Hallmarks", "Zoology", "Notochord, dorsal hollow nerve cord, paired pharyngeal gill slits, and post-anal tail.")
                )),
                ChapterInfo("c11_b_ch5", 5, "Morphology of Flowering Plants", 50, "Root, stem, leaf modifications, inflorescence, flower structure, fruit, seed, semi-technical description of families.", listOf(
                    ChapterHighlight("Floral Formula", "Morphology", "Symbolic representation of flower symmetry, sexuality, calyx, corolla, androecium, and gynoecium.")
                )),
                ChapterInfo("c11_b_ch6", 6, "Anatomy of Flowering Plants", 50, "Meristematic and permanent tissues, tissue systems, internal anatomy of dicot and monocot roots, stems, and leaves.", listOf(
                    ChapterHighlight("Dicot vs Monocot Stem", "Anatomy", "Dicot stem has vascular bundles arranged in a ring with cambium; monocot has scattered closed bundles.")
                )),
                ChapterInfo("c11_b_ch7", 7, "Structural Organisation in Animals", 45, "Animal tissues (epithelial, connective, muscular, neural), morphology and anatomy of frog.", listOf(
                    ChapterHighlight("Connective Tissue", "Histology", "Connects and supports other tissues; includes loose (areolar, adipose), dense, and specialized (cartilage, bone, blood).")
                )),
                ChapterInfo("c11_b_ch8", 8, "Cell: The Unit of Life", 55, "Cell theory, prokaryotic and eukaryotic cells, plant and animal cells, endomembrane system, mitochondria, chloroplasts, ribosomes, cytoskeleton, nucleus.", listOf(
                    ChapterHighlight("Endomembrane System", "Cytology", "Coordinated organelles: Endoplasmic Reticulum, Golgi apparatus, Lysosomes, and Vacuoles.")
                )),
                ChapterInfo("c11_b_ch9", 9, "Biomolecules", 55, "Chemical constituents of living cells, carbohydrates, amino acids, lipids, proteins, nucleic acids (DNA/RNA structure), enzymes (nature, kinetics, activation energy).", listOf(
                    ChapterHighlight("Enzyme Kinetics", "Biochemistry", "Enzymes lower the activation energy barrier of biochemical reactions without altering equilibrium.")
                )),
                ChapterInfo("c11_b_ch10", 10, "Cell Cycle and Cell Division", 55, "Cell cycle phases (G1, S, G2, M), stages of mitosis, stages of meiosis (meiosis I & II), significance of crossing over.", listOf(
                    ChapterHighlight("Pachytene Crossing Over", "Cytogenetics", "Exchange of genetic material between non-sister chromatids of homologous chromosomes catalyzed by recombinase.")
                )),
                ChapterInfo("c11_b_ch11", 11, "Photosynthesis in Higher Plants", 60, "Chloroplast pigments, light reaction, photophosphorylation (cyclic and non-cyclic), Calvin cycle (C3 pathway), Hatch-Slack pathway (C4 pathway), photorespiration, factors affecting photosynthesis.", listOf(
                    ChapterHighlight("Rubisco", "Enzyme", "Most abundant enzyme on Earth; possesses both carboxylase and oxygenase catalytic activities.")
                )),
                ChapterInfo("c11_b_ch12", 12, "Respiration in Plants", 55, "Glycolysis (EMP pathway), fermentation, aerobic respiration (TCA / Krebs cycle), Electron Transport System (ETS), oxidative phosphorylation, respiratory quotient.", listOf(
                    ChapterHighlight("Krebs Cycle (TCA)", "Metabolism", "Matrix of mitochondria; complete oxidation of acetyl-CoA yielding NADH, FADH₂, and ATP.")
                )),
                ChapterInfo("c11_b_ch13", 13, "Plant Growth and Development", 50, "Growth phases, differentiation, dedifferentiation, redifferentiation, plant growth regulators (auxins, gibberellins, cytokinins, ethylene, ABA).", listOf(
                    ChapterHighlight("Ethylene", "Phytohormone", "Gaseous plant hormone promoting fruit ripening and abscission.")
                )),
                ChapterInfo("c11_b_ch14", 14, "Breathing and Exchange of Gases", 50, "Human respiratory system, mechanism of breathing, respiratory volumes and capacities (tidal volume, vital capacity), gas exchange, oxygen dissociation curve.", listOf(
                    ChapterHighlight("Oxygen Dissociation Curve", "Physiology", "Sigmoid curve; shift to the right with increased CO₂, H⁺ concentration (acidity), and temperature (Bohr effect).")
                )),
                ChapterInfo("c11_b_ch15", 15, "Body Fluids and Circulation", 55, "Blood composition and groups (ABO, Rh), lymph, human circulatory system, cardiac cycle, ECG, double circulation, regulation of cardiac activity, disorders.", listOf(
                    ChapterHighlight("Cardiac Output", "Cardiology", "Stroke Volume × Heart Rate ≈ 70 mL × 72 bpm ≈ 5 L/min.")
                )),
                ChapterInfo("c11_b_ch16", 16, "Excretory Products and Elimination", 55, "Ammonotelism, ureotelism, uricotelism, human excretory system, structure of nephron, urine formation (filtration, reabsorption, secretion), counter-current mechanism, regulation of kidney function (RAAS, ADH).", listOf(
                    ChapterHighlight("Counter-Current Mechanism", "Renal Physiology", "Henle's loop and vasa recta maintain hyperosmolarity in medullary interstitium to produce concentrated urine.")
                )),
                ChapterInfo("c11_b_ch17", 17, "Locomotion and Movement", 50, "Types of movement, skeletal muscle structure, sliding filament theory of muscle contraction, skeletal system (axial and appendicular), joints and disorders.", listOf(
                    ChapterHighlight("Sliding Filament Theory", "Muscular Mechanism", "Actin filaments slide past myosin cross-bridges powered by ATP hydrolysis during contraction.")
                )),
                ChapterInfo("c11_b_ch18", 18, "Neural Control and Coordination", 55, "Neuron structure, conduction and transmission of nerve impulses, synaptic transmission, central nervous system (forebrain, midbrain, hindbrain), reflex arc.", listOf(
                    ChapterHighlight("Action Potential", "Neurophysiology", "Rapid depolarization of axonal membrane caused by opening of voltage-gated Na⁺ channels.")
                )),
                ChapterInfo("c11_b_ch19", 19, "Chemical Coordination and Integration", 55, "Endocrine glands and hormones (hypothalamus, pituitary, pineal, thyroid, parathyroid, adrenal, pancreas, gonads), mechanisms of hormone action (peptide vs steroid hormones).", listOf(
                    ChapterHighlight("Hormone Action", "Endocrinology", "Peptide hormones bind membrane receptors generating second messengers (cAMP); steroid hormones enter nucleus regulating gene expression.")
                ))
            )
        )
    }

    private fun createComputerScience(): SubjectInfo {
        return SubjectInfo(
            id = "c11_cs",
            name = "Computer Science",
            code = "CS-11",
            description = "Complete CBSE Class 11 CS syllabus with Python programming, data structures, and computer organization",
            iconCategory = "cs",
            chapters = listOf(
                ChapterInfo("c11_cs_ch1", 1, "Computer Systems and Organization", 45, "Basic computer architecture, CPU registers, bus architecture, primary and secondary memory, cache, operating system concepts, number systems (binary, octal, hexadecimal, conversions).", listOf(
                    ChapterHighlight("Number Base Conversion", "Computer Arithmetic", "Radix conversions between Decimal, Binary (base 2), Octal (base 8), and Hexadecimal (base 16).")
                )),
                ChapterInfo("c11_cs_ch2", 2, "Computational Thinking & Python Programming", 60, "Algorithm, flowchart, Python tokens, variables, data types, mutable vs immutable types, operators, operator precedence, type casting, expressions, if-else, loops.", listOf(
                    ChapterHighlight("Mutability in Python", "Language Feature", "Lists, dictionaries, and sets are mutable; integers, floats, strings, and tuples are immutable.")
                )),
                ChapterInfo("c11_cs_ch3", 3, "Strings, Lists, Tuples and Dictionaries", 65, "Traversing, slicing, built-in methods of strings, lists, tuples, and key-value mapping in dictionaries, list comprehensions.", listOf(
                    ChapterHighlight("List Comprehension", "Python Feature", "[x**2 for x in range(10) if x % 2 == 0] yields concise, expressive, and optimized list creations.")
                )),
                ChapterInfo("c11_cs_ch4", 4, "Functions and Modules", 55, "User defined functions, parameters and arguments, default arguments, return values, scope of variables (local vs global), built-in modules (math, random, statistics).", listOf(
                    ChapterHighlight("Variable Scope", "Programming Rule", "LEGB Rule: Local, Enclosing, Global, Built-in determines variable resolution in Python.")
                )),
                ChapterInfo("c11_cs_ch5", 5, "Cyber Safety, Society, Law and Ethics", 45, "Digital footprints, cyber bullying, phishing, malware, hacking vs cracking, intellectual property rights, open source licenses (GPL, Creative Commons), IT Act 2000.", listOf(
                    ChapterHighlight("Intellectual Property", "Legal Ethics", "Copyright, patents, and trademarks protect creators' legal rights in software and content.")
                ))
            )
        )
    }

    private fun createEconomics(): SubjectInfo {
        return SubjectInfo(
            id = "c11_eco",
            name = "Economics",
            code = "ECON-11",
            description = "Complete CBSE Class 11 Statistics for Economics and Introductory Microeconomics",
            iconCategory = "economics",
            chapters = listOf(
                ChapterInfo("c11_eco_ch1", 1, "Introduction to Statistics", 40, "What is economics, meaning and scope of statistics, functions of statistics, importance of statistics in economic decision making.", listOf(
                    ChapterHighlight("Role of Statistics", "Methodology", "Enables quantitative presentation of economic facts, formulating policies, and econometric modeling.")
                )),
                ChapterInfo("c11_eco_ch2", 2, "Collection, Organisation and Presentation of Data", 50, "Primary and secondary data, census vs sample method, random sampling, classification of data, tabular and diagrammatic presentation (bar, pie, histogram).", listOf(
                    ChapterHighlight("Sampling Techniques", "Data Science", "Simple random sampling gives every element in the target population an equal probability of selection.")
                )),
                ChapterInfo("c11_eco_ch3", 3, "Measures of Central Tendency", 55, "Arithmetic mean, median, mode for individual, discrete, and continuous series, mathematical properties.", listOf(
                    ChapterHighlight("Mean Property", "Formula", "The sum of deviations of observations from their arithmetic mean is always zero: Σ(xᵢ - x̄) = 0.")
                )),
                ChapterInfo("c11_eco_ch4", 4, "Correlation and Index Numbers", 50, "Karl Pearson's correlation coefficient, Spearman's rank correlation, Consumer Price Index (CPI), Wholesale Price Index (WPI), Laspeyres and Paasche index.", listOf(
                    ChapterHighlight("Karl Pearson's r", "Formula", "-1 ≤ r ≤ +1. Measures linear relationship strength between two variables.")
                )),
                ChapterInfo("c11_eco_ch5", 5, "Introduction to Microeconomics", 45, "Scarcity, central problems of an economy (what, how, for whom to produce), opportunity cost, Production Possibility Frontier (PPF).", listOf(
                    ChapterHighlight("Opportunity Cost", "Economic Principle", "Cost of the next best alternative foregone when making a choice.")
                )),
                ChapterInfo("c11_eco_ch6", 6, "Theory of Consumer Behaviour", 55, "Cardinal utility analysis, Law of Diminishing Marginal Utility, consumer equilibrium, ordinal utility analysis, indifference curves and properties, budget line.", listOf(
                    ChapterHighlight("Consumer Equilibrium", "Microeconomics", "Marginal Rate of Substitution (MRS_xy) = P_x / P_y (Slope of indifference curve equals price ratio).")
                )),
                ChapterInfo("c11_eco_ch7", 7, "Production and Costs", 55, "Short-run production function, Law of Variable Proportions (Total, Marginal, Average Product), short-run costs (TFC, TVC, TC, AFC, AVC, ATC, MC), shape of cost curves.", listOf(
                    ChapterHighlight("Law of Variable Proportions", "Production Theory", "As additional units of variable factor are combined with fixed factors, marginal product initially rises, then falls, and eventually becomes negative.")
                )),
                ChapterInfo("c11_eco_ch8", 8, "Theory of the Firm under Perfect Competition", 50, "Revenue concepts (Total, Average, Marginal Revenue), producer equilibrium under MR-MC approach, supply curve of a competitive firm.", listOf(
                    ChapterHighlight("Producer Equilibrium", "Condition", "1) MR = MC, 2) MC must be rising (cutting MR from below) at the point of equilibrium.")
                )),
                ChapterInfo("c11_eco_ch9", 9, "Market Equilibrium", 45, "Equilibrium price and quantity determination, excess demand and excess supply, shifts in demand and supply curves, price ceiling and price floor applications.", listOf(
                    ChapterHighlight("Price Ceiling & Floor", "Market Intervention", "Price Ceiling is maximum legal price set below equilibrium (rationing); Price Floor is minimum legal price set above (e.g. MSP).")
                ))
            )
        )
    }
}
