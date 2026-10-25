package com.example.model.curriculum

import com.example.model.ChapterHighlight
import com.example.model.ChapterInfo
import com.example.model.ClassInfo
import com.example.model.SubjectInfo

object Class12Curriculum {

    fun create(): ClassInfo {
        return ClassInfo(
            id = "class_12",
            name = "Class 12",
            subtitle = "Official CBSE Senior Secondary Board Syllabus",
            gradeNumber = 12,
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
            id = "c12_math",
            name = "Mathematics",
            code = "MATH-12",
            description = "Complete CBSE Class 12 Higher Secondary Mathematics including Advanced Calculus, Vectors, 3D Geometry and Probability",
            iconCategory = "math",
            chapters = listOf(
                ChapterInfo("c12_m_ch1", 1, "Relations and Functions", 55, "Types of relations: reflexive, symmetric, transitive and equivalence relations. One-to-one (injective) and onto (bijective) functions, composite functions and invertible functions.", listOf(
                    ChapterHighlight("Equivalence Relation", "Core Concept", "A relation R in set A is an equivalence relation if R is reflexive, symmetric, and transitive.")
                )),
                ChapterInfo("c12_m_ch2", 2, "Inverse Trigonometric Functions", 50, "Definition, range, domain, principal value branch, graphs of inverse trigonometric functions, and fundamental properties.", listOf(
                    ChapterHighlight("Principal Value Range", "Definition", "sin⁻¹(x) ∈ [-π/2, π/2], cos⁻¹(x) ∈ [0, π], tan⁻¹(x) ∈ (-π/2, π/2).")
                )),
                ChapterInfo("c12_m_ch3", 3, "Matrices", 55, "Concept, notation, order, equality, types of matrices, zero and identity matrix, operations (addition, scalar multiplication, matrix multiplication), transpose of a matrix, symmetric and skew-symmetric matrices.", listOf(
                    ChapterHighlight("Matrix Multiplication", "Properties", "AB ≠ BA in general (non-commutative); (AB)ᵀ = BᵀAᵀ.")
                )),
                ChapterInfo("c12_m_ch4", 4, "Determinants", 60, "Determinant of square matrix (up to 3x3), minors, cofactors, adjoint of a matrix, inverse of a matrix, consistency of linear equations, solving systems using matrix method.", listOf(
                    ChapterHighlight("Matrix Inversion", "Formula", "A⁻¹ = adj(A) / |A| where |A| ≠ 0; System AX = B has unique solution X = A⁻¹B.")
                )),
                ChapterInfo("c12_m_ch5", 5, "Continuity and Differentiability", 65, "Continuity and differentiability, chain rule, derivative of inverse trigonometric, implicit, exponential and logarithmic functions. Logarithmic differentiation, parametric differentiation, second order derivatives.", listOf(
                    ChapterHighlight("Logarithmic Differentiation", "Technique", "For y = [f(x)]^(g(x)), take ln y = g(x) ln(f(x)) and differentiate implicitly: (1/y)(dy/dx) = ...")
                )),
                ChapterInfo("c12_m_ch6", 6, "Application of Derivatives", 60, "Rate of change of quantities, increasing and decreasing functions, tangents and normals, maxima and minima (first and second derivative tests, practical optimization problems).", listOf(
                    ChapterHighlight("Second Derivative Test", "Theorem", "If f'(c) = 0 and f''(c) < 0 then c is local maximum; if f''(c) > 0 then c is local minimum.")
                )),
                ChapterInfo("c12_m_ch7", 7, "Integrals", 70, "Integration as inverse process of differentiation. Integration by substitution, partial fractions, and parts. Definite integrals as limit of a sum, Fundamental Theorem of Calculus, properties of definite integrals.", listOf(
                    ChapterHighlight("Integration by Parts", "Formula", "∫ u v dx = u ∫ v dx - ∫ [u' (∫ v dx)] dx; ILATE priority rule.")
                )),
                ChapterInfo("c12_m_ch8", 8, "Application of Integrals", 50, "Applications in finding the area under simple curves, especially lines, circles, parabolas, and ellipses in standard form, area bounded between two curves.", listOf(
                    ChapterHighlight("Area Bounded", "Formula", "Area A = ∫ₐᵇ |y₁ - y₂| dx from intersection points x = a to x = b.")
                )),
                ChapterInfo("c12_m_ch9", 9, "Differential Equations", 60, "Definition, order and degree, general and particular solutions, formation of differential equations. Solution by separation of variables, homogeneous differential equations, and first-order linear differential equations.", listOf(
                    ChapterHighlight("Integrating Factor", "Formula", "For dy/dx + Py = Q: I.F. = e^(∫ P dx); General solution is y · (I.F.) = ∫ [Q · (I.F.)] dx + C.")
                )),
                ChapterInfo("c12_m_ch10", 10, "Vector Algebra", 55, "Vectors and scalars, magnitude and direction of a vector, direction cosines and ratios, types of vectors, position vector, dot (scalar) product and cross (vector) product, projection of a vector.", listOf(
                    ChapterHighlight("Scalar & Vector Product", "Formula", "a⃗ · b⃗ = |a⃗||b⃗| cos θ; a⃗ × b⃗ = |a⃗||b⃗| sin θ n̂; Projection of a⃗ on b⃗ = (a⃗ · b⃗) / |b⃗|.")
                )),
                ChapterInfo("c12_m_ch11", 11, "Three Dimensional Geometry", 60, "Direction cosines and direction ratios of a line joining two points. Vector and Cartesian equation of a line, coplanar and skew lines, shortest distance between two lines.", listOf(
                    ChapterHighlight("Shortest Distance", "Formula", "d = |(b⃗₁ × b⃗₂) · (a⃗₂ - a⃗₁)| / |b⃗₁ × b⃗₂| for skew lines r⃗ = a⃗₁ + λb⃗₁ and r⃗ = a⃗₂ + μb⃗₂.")
                )),
                ChapterInfo("c12_m_ch12", 12, "Linear Programming", 45, "Introduction, related terminology such as constraints, objective function, optimization, mathematical formulation of L.P. problems, graphical method of solution for two variables, feasible and infeasible regions, corner point method.", listOf(
                    ChapterHighlight("Corner Point Theorem", "Optimization", "The optimal value (maximum or minimum) of an objective function Z = ax + by occurs at one of the corner points (vertices) of the bounded feasible region.")
                )),
                ChapterInfo("c12_m_ch13", 13, "Probability", 55, "Conditional probability, multiplication theorem on probability, independent events, total probability, Bayes' theorem, random variable and its probability distribution, mean of a random variable.", listOf(
                    ChapterHighlight("Bayes' Theorem", "Formula", "P(Aᵢ|B) = [P(Aᵢ) P(B|Aᵢ)] / [Σ P(Aⱼ) P(B|Aⱼ)] for a partition of sample space.")
                ))
            )
        )
    }

    private fun createPhysics(): SubjectInfo {
        return SubjectInfo(
            id = "c12_phys",
            name = "Physics",
            code = "PHYS-12",
            description = "Complete CBSE Class 12 Senior Secondary Physics covering Electromagnetism, Optics, Quantum Physics and Semiconductors",
            iconCategory = "physics",
            chapters = listOf(
                ChapterInfo("c12_p_ch1", 1, "Electric Charges and Fields", 60, "Electric charges, conservation, Coulomb's law, electric field, field lines, electric dipole, electric flux, Gauss's law and its applications to infinitely long wire, uniformly charged infinite plane sheet and spherical shell.", listOf(
                    ChapterHighlight("Gauss's Law", "Electromagnetism", "Φ = ∮ E⃗ · dA⃗ = q_enclosed / ε₀; Field due to infinite line charge E = λ / (2πε₀r).")
                )),
                ChapterInfo("c12_p_ch2", 2, "Electrostatic Potential and Capacitance", 60, "Electric potential, potential difference, equipotential surfaces, potential energy of a system of charges, conductors, dielectrics and electric polarization, capacitors and capacitance, parallel plate capacitor with dielectric, combination of capacitors, energy stored in a capacitor.", listOf(
                    ChapterHighlight("Capacitor Energy", "Formula", "U = ½ CV² = ½ Q²/C = ½ QV; Energy density u = ½ ε₀ E².")
                )),
                ChapterInfo("c12_p_ch3", 3, "Current Electricity", 65, "Electric current, drift velocity, Ohm's law, electrical resistance, V-I characteristics, electrical resistivity and conductivity, temperature dependence, internal resistance, EMF and potential difference, Kirchhoff's laws, Wheatstone bridge, meter bridge.", listOf(
                    ChapterHighlight("Kirchhoff's Laws", "Circuit Law", "Current Law (KCL): Σ I = 0 at any node. Voltage Law (KVL): Σ ΔV = 0 around any closed loop.")
                )),
                ChapterInfo("c12_p_ch4", 4, "Moving Charges and Magnetism", 65, "Biot-Savart law and application to current loop, Ampere's circuital law and applications, force on moving charge in magnetic field, Lorentz force, cyclotron, force between two parallel current-carrying conductors (definition of ampere), torque on current loop, moving coil galvanometer and conversion to ammeter/voltmeter.", listOf(
                    ChapterHighlight("Lorentz Force", "Formula", "F⃗ = q(E⃗ + v⃗ × B⃗); Force on wire F⃗ = I(L⃗ × B⃗).")
                )),
                ChapterInfo("c12_p_ch5", 5, "Magnetism and Matter", 50, "Bar magnet as an equivalent solenoid, magnetic field lines, Earth's magnetic field and magnetic elements, magnetic properties of materials (diamagnetic, paramagnetic, ferromagnetic substances), hysteresis curve, permanent magnets and electromagnets.", listOf(
                    ChapterHighlight("Curie's Law", "Property", "Magnetic susceptibility of paramagnetic substance χ ∝ 1/T; χ = C / T.")
                )),
                ChapterInfo("c12_p_ch6", 6, "Electromagnetic Induction", 55, "Electromagnetic induction, Faraday's laws of induction, induced EMF and current, Lenz's law, eddy currents, self and mutual induction, AC generator principle.", listOf(
                    ChapterHighlight("Faraday's & Lenz's Law", "Law", "ε = -dΦ_B / dt; The negative sign shows that the induced EMF opposes the change in magnetic flux causing it.")
                )),
                ChapterInfo("c12_p_ch7", 7, "Alternating Current", 60, "Alternating currents, peak and RMS value of AC, reactance and impedance, LCR series circuit, resonance, power in AC circuits, power factor, wattless current, AC transformer.", listOf(
                    ChapterHighlight("LCR Resonance", "Formula", "Resonant frequency ω₀ = 1 / √(LC); Impedance at resonance Z = R (minimum), Current I_max = V / R.")
                )),
                ChapterInfo("c12_p_ch8", 8, "Electromagnetic Waves", 45, "Displacement current, characteristics and transverse nature of electromagnetic waves, speed of EM waves, electromagnetic spectrum (radio, micro, infrared, visible, UV, X-rays, gamma rays) and their practical applications.", listOf(
                    ChapterHighlight("Wave Speed", "Formula", "c = 1 / √(μ₀ ε₀) ≈ 3 × 10⁸ m/s; c = E₀ / B₀.")
                )),
                ChapterInfo("c12_p_ch9", 9, "Ray Optics and Optical Instruments", 65, "Reflection and refraction of light, total internal reflection and optical fibers, refraction at spherical surfaces, lenses, thin lens formula, lensmaker's formula, magnification, power of lens, refraction through a prism, optical instruments (microscopes, astronomical telescopes).", listOf(
                    ChapterHighlight("Lensmaker's Formula", "Optics", "1/f = (μ - 1) [1/R₁ - 1/R₂]; Magnifying power of compound microscope m = -(L/f_o)(D/f_e).")
                )),
                ChapterInfo("c12_p_ch10", 10, "Wave Optics", 60, "Wavefront and Huygens' principle, reflection and refraction using Huygens' principle, interference, Young's double slit experiment, fringe width expression, coherent sources, diffraction due to a single slit, width of central maximum.", listOf(
                    ChapterHighlight("YDSE Fringe Width", "Formula", "β = λD / d; Central bright maximum has constructive interference when path difference Δx = nλ.")
                )),
                ChapterInfo("c12_p_ch11", 11, "Dual Nature of Radiation and Matter", 50, "Photoelectric effect, Hertz and Lenard's observations, Einstein's photoelectric equation, threshold frequency, work function, stopping potential, matter waves, de Broglie relation, Davisson-Germer experiment.", listOf(
                    ChapterHighlight("Photoelectric Equation", "Quantum Physics", "K_max = hν - Φ₀ = eV_s; de Broglie wavelength λ = h / p = h / √(2mE).")
                )),
                ChapterInfo("c12_p_ch12", 12, "Atoms", 45, "Alpha-particle scattering experiment, Rutherford's nuclear model of atom, Bohr's model of hydrogen atom, energy levels, hydrogen line spectrum (Lyman, Balmer, Paschen, Brackett, Pfund series).", listOf(
                    ChapterHighlight("Bohr Orbit Energy", "Formula", "E_n = -13.6 / n² eV; Angular momentum quantization mvr = nh / (2π).")
                )),
                ChapterInfo("c12_p_ch13", 13, "Nuclei", 45, "Composition and size of nucleus, atomic masses, isotopes, isobars, isotones, mass-energy relation, mass defect, binding energy per nucleon and its variation with mass number, nuclear fission and nuclear fusion.", listOf(
                    ChapterHighlight("Mass Defect & Binding Energy", "Nuclear Physics", "Δm = [Z m_p + (A - Z) m_n] - M_nucleus; Binding Energy BE = Δm · c² (≈ Δm × 931.5 MeV).")
                )),
                ChapterInfo("c12_p_ch14", 14, "Semiconductor Electronics: Materials, Devices and Simple Circuits", 55, "Energy bands in conductors, semiconductors, and insulators; intrinsic and extrinsic semiconductors (n-type and p-type); p-n junction diode: I-V characteristics in forward and reverse bias; diode as a rectifier (half-wave and full-wave).", listOf(
                    ChapterHighlight("Rectifier Action", "Solid State", "A p-n junction diode conducts during forward bias and blocks during reverse bias, converting AC into pulsating DC.")
                ))
            )
        )
    }

    private fun createChemistry(): SubjectInfo {
        return SubjectInfo(
            id = "c12_chem",
            name = "Chemistry",
            code = "CHEM-12",
            description = "Complete CBSE Class 12 Chemistry syllabus including Physical Chemistry, Inorganic Coordination Chemistry and Organic Functional Groups",
            iconCategory = "chemistry",
            chapters = listOf(
                ChapterInfo("c12_c_ch1", 1, "Solutions", 55, "Types of solutions, expression of concentration of solutions of solids in liquids, solubility of gases in liquids (Henry's law), solid solutions, Raoult's law, ideal and non-ideal solutions, colligative properties (relative lowering of vapour pressure, elevation of boiling point, depression of freezing point, osmotic pressure), determination of molecular masses using colligative properties, abnormal molecular mass, van 't Hoff factor.", listOf(
                    ChapterHighlight("Colligative Properties", "Physical Chemistry", "ΔT_b = i K_b m ; ΔT_f = i K_f m ; Π = i CRT ; van 't Hoff factor i = normal molar mass / abnormal molar mass.")
                )),
                ChapterInfo("c12_c_ch2", 2, "Electrochemistry", 60, "Redox reactions, EMF of a cell, standard electrode potential, Nernst equation and its application to chemical cells, relation between Gibbs energy change and EMF of a cell, conductance in electrolytic solutions, specific and molar conductivity, Kohlrausch's Law, electrolysis and laws of electrolysis (Faraday's laws), dry cell, lead accumulator, fuel cells, corrosion.", listOf(
                    ChapterHighlight("Nernst Equation", "Formula", "E_cell = E°_cell - (0.0591 / n) log₁₀ Q  at 298 K; ΔG° = -n F E°_cell.")
                )),
                ChapterInfo("c12_c_ch3", 3, "Chemical Kinetics", 55, "Rate of a reaction (average and instantaneous), factors affecting rates of reaction: concentration, temperature, catalyst; order and molecularity of a reaction; rate law and specific rate constant, integrated rate equations and half-life (only for zero and first order reactions); collision theory of elementary chemical reactions, Arrhenius equation and activation energy.", listOf(
                    ChapterHighlight("First Order Half-Life", "Kinetics", "t_½ = 0.693 / k ; Integrated rate law k = (2.303 / t) log₁₀([A]₀ / [A]) ; Arrhenius eqn k = A e^(-Ea / RT).")
                )),
                ChapterInfo("c12_c_ch4", 4, "The d- and f-Block Elements", 55, "General introduction, electronic configuration, occurrence, characteristics of transition metals, general trends in properties (metallic character, ionization enthalpy, oxidation states, ionic radii, colour, catalytic property, magnetic properties, interstitial compounds, alloy formation). Preparation and properties of K₂Cr₂O₇ and KMnO₄. Lanthanoids: electronic configuration, oxidation states, lanthanoid contraction. Actinoids: general comparison with lanthanoids.", listOf(
                    ChapterHighlight("Lanthanoid Contraction", "Inorganic", "The steady decrease in atomic and ionic radii of lanthanoid elements with increasing atomic number due to poor shielding by 4f electrons.")
                )),
                ChapterInfo("c12_c_ch5", 5, "Coordination Compounds", 60, "Coordination compounds: Introduction, ligands, coordination number, colour, magnetic properties and shapes, IUPAC nomenclature of mononuclear coordination compounds. Bonding: Werner's theory, VBT (hybridization and geometry), and CFT (crystal field splitting in octahedral and tetrahedral complexes). Isomerism (structural and stereo). Importance of coordination compounds.", listOf(
                    ChapterHighlight("Crystal Field Splitting", "Coordination", "In an octahedral field, d-orbitals split into lower t₂g (triply degenerate) and higher e_g (doubly degenerate) with energy difference Δ_o.")
                )),
                ChapterInfo("c12_c_ch6", 6, "Haloalkanes and Haloarenes", 55, "Haloalkanes: Nomenclature, nature of C-X bond, physical and chemical properties, mechanism of substitution reactions (SN1 and SN2), optical rotation. Haloarenes: Nature of C-X bond, substitution reactions (directive influence of halogen for monosubstituted compounds only). Uses and environmental effects of dichloromethane, trichloromethane, tetrachloromethane, iodoform, freons, DDT.", listOf(
                    ChapterHighlight("SN1 vs SN2 Mechanisms", "Organic", "SN1: Two steps, carbocation intermediate, racemization, tertiary > secondary > primary. SN2: Single step, backside attack, Walden inversion, primary > secondary > tertiary.")
                )),
                ChapterInfo("c12_c_ch7", 7, "Alcohols, Phenols and Ethers", 60, "Alcohols: Nomenclature, methods of preparation, physical and chemical properties (primary, secondary and tertiary alcohols); identification (Lucas test); mechanism of dehydration. Phenols: Nomenclature, methods of preparation, physical and chemical properties, acidic nature of phenol, electrophilic aromatic substitution (halogenation, nitration, Kolbe's reaction, Reimer-Tiemann reaction). Ethers: Nomenclature, methods of preparation (Williamson synthesis), physical and chemical properties.", listOf(
                    ChapterHighlight("Reimer-Tiemann Reaction", "Name Reaction", "Phenol + CHCl₃ + aqueous NaOH ⟶ Salicylaldehyde (ortho-hydroxybenzaldehyde) via dichlorocarbene intermediate.")
                )),
                ChapterInfo("c12_c_ch8", 8, "Aldehydes, Ketones and Carboxylic Acids", 65, "Aldehydes and Ketones: Nomenclature, nature of carbonyl group, methods of preparation, physical and chemical properties, mechanism of nucleophilic addition, reactivity of alpha hydrogen in aldehydes: aldol condensation, Cannizzaro reaction. Carboxylic Acids: Nomenclature, acidic nature, methods of preparation, physical and chemical properties; HVZ reaction.", listOf(
                    ChapterHighlight("Aldol vs Cannizzaro", "Organic Synthesis", "Aldol condensation occurs in aldehydes/ketones having α-hydrogen; Cannizzaro occurs in aldehydes lacking α-hydrogen (e.g., HCHO, C₆H₅CHO) with concentrated alkali.")
                )),
                ChapterInfo("c12_c_ch9", 9, "Amines", 55, "Amines: Nomenclature, classification, structure, methods of preparation, physical and chemical properties, basicity of amines, identification of primary, secondary and tertiary amines (Hinsberg test). Diazonium salts: Preparation, chemical reactions (Sandmeyer, Gattermann, coupling reactions) and importance in synthetic organic chemistry.", listOf(
                    ChapterHighlight("Basicity Order & Diazonium", "Organic", "In aqueous medium, basicity of methyl-substituted amines: (CH₃)₂NH > CH₃NH₂ > (CH₃)₃N > NH₃. Diazonium salt coupling yields vibrant azo dyes.")
                )),
                ChapterInfo("c12_c_ch10", 10, "Biomolecules", 50, "Carbohydrates: Classification (aldoses and ketoses), monosaccharides (glucose and fructose), D-L configuration, oligosaccharides (sucrose, lactose, maltose), polysaccharides (starch, cellulose, glycogen). Proteins: Amino acids, peptide bond, polypeptides, proteins, structure of proteins (primary, secondary, tertiary, quaternary), denaturation of proteins; enzymes. Vitamins: Classification and functions. Nucleic Acids: DNA and RNA.", listOf(
                    ChapterHighlight("DNA Double Helix", "Biomolecule", "DNA consists of two antiparallel polynucleotide chains held by hydrogen bonds between complementary bases: Adenine = Thymine (2 H-bonds) and Guanine ≡ Cytosine (3 H-bonds).")
                ))
            )
        )
    }

    private fun createBiology(): SubjectInfo {
        return SubjectInfo(
            id = "c12_bio",
            name = "Biology",
            code = "BIO-12",
            description = "Complete CBSE Class 12 Senior Secondary Biology covering Genetics, Biotechnology, Human Welfare and Ecology",
            iconCategory = "biology",
            chapters = listOf(
                ChapterInfo("c12_b_ch1", 1, "Sexual Reproduction in Flowering Plants", 55, "Flower structure; development of male and female gametophytes; pollination (types, agencies and examples); outbreeding devices; pollen-pistil interaction; double fertilization; post-fertilization events (development of endosperm and embryo, development of seed and formation of fruit); special modes (apomixis, parthenocarpy, polyembryony); significance of seed dispersal and fruit formation.", listOf(
                    ChapterHighlight("Double Fertilization", "Botany", "One male gamete fuses with the egg cell (syngamy ⟶ 2n zygote) and the other fuses with secondary nucleus (triple fusion ⟶ 3n primary endosperm nucleus PEN).")
                )),
                ChapterInfo("c12_b_ch2", 2, "Human Reproduction", 60, "Male and female reproductive systems; microscopic anatomy of testis and ovary; gametogenesis (spermatogenesis and oogenesis); menstrual cycle; fertilization, embryo development up to blastocyst formation, implantation; pregnancy and placenta formation; parturition; lactation.", listOf(
                    ChapterHighlight("Menstrual Cycle Hormones", "Human Physiology", "FSH stimulates follicular growth; LH surge induces ovulation on ~14th day; Corpus luteum secretes progesterone to maintain endometrium.")
                )),
                ChapterInfo("c12_b_ch3", 3, "Reproductive Health", 45, "Need for reproductive health and prevention of Sexually Transmitted Diseases (STDs); birth control methods, contraception and Medical Termination of Pregnancy (MTP); amniocentesis; infertility and assisted reproductive technologies (IVF, ZIFT, GIFT).", listOf(
                    ChapterHighlight("ART Techniques", "Biomedical", "IVF (In Vitro Fertilization): Embryo transferred at ≤8 blastomeres into fallopian tube (ZIFT) or >8 blastomeres into uterus (IUT).")
                )),
                ChapterInfo("c12_b_ch4", 4, "Principles of Inheritance and Variation", 65, "Mendelian inheritance; deviations from Mendelism (incomplete dominance, codominance, multiple alleles, pleiotropy); elementary idea of polygenic inheritance; chromosome theory of inheritance; chromosomes and genes; sex determination in humans, birds and honey bee; linkage and crossing over; sex linked inheritance (haemophilia, colour blindness); Mendelian disorders in humans (thalassemia, sickle cell anemia, phenylketonuria); chromosomal disorders in humans (Down's, Turner's and Klinefelter's syndromes).", listOf(
                    ChapterHighlight("Law of Segregation", "Genetics", "Alleles do not show any blending and both characters are recovered as such in F₂ generation; gametes receive only one of the two factors.")
                )),
                ChapterInfo("c12_b_ch5", 5, "Molecular Basis of Inheritance", 65, "Search for genetic material and DNA as genetic material; Structure of DNA and RNA; DNA packaging; DNA replication; Central Dogma; transcription, genetic code, translation; gene expression and regulation (lac operon); Genome projects: Human Genome Project (HGP) and DNA fingerprinting.", listOf(
                    ChapterHighlight("The Central Dogma", "Molecular Biology", "DNA ⟶ (transcription) ⟶ mRNA ⟶ (translation) ⟶ Protein. Genetic code is universal, degenerate, unambiguous and triplet.")
                )),
                ChapterInfo("c12_b_ch6", 6, "Evolution", 55, "Origin of life; biological evolution and evidences for biological evolution (paleontological, comparative anatomical, embryological and molecular evidences); Darwin's contribution, modern synthetic theory of evolution; mechanism of evolution (variation and mutation); gene flow and genetic drift; Hardy-Weinberg's principle; adaptive radiation; human evolution.", listOf(
                    ChapterHighlight("Hardy-Weinberg Equilibrium", "Evolution", "Gene frequencies in a population remain constant from generation to generation in the absence of evolutionary forces: p² + 2pq + q² = 1.")
                )),
                ChapterInfo("c12_b_ch7", 7, "Human Health and Disease", 55, "Pathogens; parasites causing human diseases (malaria, dengue, chikungunya, filariasis, ascariasis, typhoid, pneumonia, common cold, amoebiasis, ringworm) and their control; Basic concepts of immunology: vaccines; cancer, HIV and AIDS; Adolescence - drug and alcohol abuse.", listOf(
                    ChapterHighlight("Innate vs Acquired Immunity", "Immunology", "Innate: non-specific physical/chemical barriers present from birth. Acquired: pathogen-specific memory based on B-cells (humoral) and T-cells (cell-mediated).")
                )),
                ChapterInfo("c12_b_ch8", 8, "Microbes in Human Welfare", 45, "Microbes in food processing, industrial production, sewage treatment, energy generation (biogas), biocontrol agents and biofertilizers. Antibiotics; production and judicious use.", listOf(
                    ChapterHighlight("Biological Oxygen Demand (BOD)", "Ecology", "BOD refers to the amount of oxygen required by bacteria to oxidize all organic matter in 1L of water. Higher BOD indicates higher water pollution.")
                )),
                ChapterInfo("c12_b_ch9", 9, "Biotechnology: Principles and Processes", 55, "Genetic Engineering (Recombinant DNA Technology): Restriction enzymes, cloning vectors (plasmids, bacteriophages), processes of recombinant DNA technology (isolation of DNA, cutting at specific locations, amplification using PCR, insertion into host organism, bioreactors and downstream processing).", listOf(
                    ChapterHighlight("PCR (Polymerase Chain Reaction)", "Biotechnology", "Three steps: Denaturation (94°C), Annealing of primers (~54°C), and Extension by thermostable Taq DNA polymerase (72°C).")
                )),
                ChapterInfo("c12_b_ch10", 10, "Biotechnology and its Applications", 50, "Application of biotechnology in health and agriculture: Human insulin and vaccine production, stem cell technology, gene therapy; genetically modified organisms (Bt crops); transgenic animals; biosafety issues, biopiracy and patents.", listOf(
                    ChapterHighlight("Bt Cotton Mechanism", "Application", "Bacillus thuringiensis produces inactive protoxin Cry protein which converts to active toxin in alkaline gut of insects, creating pores and causing lysis.")
                )),
                ChapterInfo("c12_b_ch11", 11, "Organisms and Populations", 50, "Organisms and environment: Habitat and niche, abiotic factors (temperature, water, light, soil), adaptations; Populations: Population attributes (growth rate, birth and death rates, age distribution); Population growth models (exponential and logistic); Population interactions (mutualism, competition, predation, parasitism, commensalism, amensalism).", listOf(
                    ChapterHighlight("Logistic Growth", "Population Ecology", "dN/dt = rN [(K - N) / K] where r is intrinsic rate of natural increase and K is carrying capacity.")
                )),
                ChapterInfo("c12_b_ch12", 12, "Ecosystem", 45, "Ecosystems: Patterns, components; productivity and decomposition; energy flow; pyramids of number, biomass, energy; nutrient cycling (carbon and phosphorus); ecological succession; ecological services (carbon fixation, pollination, oxygen release).", listOf(
                    ChapterHighlight("10% Energy Law", "Ecology", "Only 10% of energy is transferred from one trophic level to the next; 90% is lost as heat. Hence the pyramid of energy is always upright.")
                )),
                ChapterInfo("c12_b_ch13", 13, "Biodiversity and Conservation", 45, "Concept of biodiversity; patterns of biodiversity; importance of biodiversity; loss of biodiversity; biodiversity conservation: hotspots, endangered organisms, extinction, Red Data Book, Sacred Groves, biosphere reserves, national parks, wildlife sanctuaries, Ramsar sites.", listOf(
                    ChapterHighlight("The Evil Quartet", "Conservation", "Four major causes of biodiversity loss: Habitat loss and fragmentation, Over-exploitation, Alien species invasions, and Co-extinctions.")
                ))
            )
        )
    }

    private fun createComputerScience(): SubjectInfo {
        return SubjectInfo(
            id = "c12_cs",
            name = "Computer Science",
            code = "CS-12",
            description = "CBSE Class 12 Computer Science covering Computational Thinking, Python Data Structures, Networks, and Relational Databases",
            iconCategory = "cs",
            chapters = listOf(
                ChapterInfo("c12_cs_ch1", 1, "Python Revision and Functions", 50, "Review of Python fundamentals, mutable and immutable data types, strings, lists, tuples, dictionaries. Functions: scope of variables (global vs local), default arguments, keyword arguments, returning multiple values, recursion basics.", listOf(
                    ChapterHighlight("Scope & Mutability", "Programming", "Lists and dictionaries are mutable in-place. LEGB rule determines variable resolution: Local ⟶ Enclosing ⟶ Global ⟶ Built-in.")
                )),
                ChapterInfo("c12_cs_ch2", 2, "File Handling (Text, Binary, CSV)", 55, "Need for data persistence. Text files: opening modes (r, w, a), read(), readline(), readlines(), write(), writelines(). Binary files: pickle module (dump and load). CSV files: csv module (reader and writer), delimiter and newline handling.", listOf(
                    ChapterHighlight("Pickle & CSV Modules", "File I/O", "import pickle; pickle.dump(obj, file); obj = pickle.load(file). For CSV: csv.writer(file) / csv.reader(file).")
                )),
                ChapterInfo("c12_cs_ch3", 3, "Data Structures: Stacks", 50, "Linear data structures: Stack concept, LIFO (Last In First Out) principle, Stack operations using Python lists: Push (append) and Pop (pop), peek, underflow and overflow conditions, application of stacks.", listOf(
                    ChapterHighlight("Stack Implementation", "Data Structures", "stack = []; stack.append(item) # Push; if stack: val = stack.pop() else: print('Underflow') # Pop.")
                )),
                ChapterInfo("c12_cs_ch4", 4, "Computer Networks", 55, "Evolution of networking (ARPANET, NSFNET, Internet), network topologies (Star, Bus, Ring, Mesh, Tree), transmission media (twisted pair, coaxial, optical fiber, wireless), network devices (modem, hub, switch, repeater, bridge, gateway, router), protocols (TCP/IP, HTTP, HTTPS, FTP, SMTP, POP3, VoIP), web services, network security and cyber ethics.", listOf(
                    ChapterHighlight("Network Topologies", "Networking", "Star topology connects all devices to a central hub/switch (fault tolerant). Bus topology connects all devices along a single backbone cable.")
                )),
                ChapterInfo("c12_cs_ch5", 5, "Database Management & SQL", 60, "Database concepts, relational data model, candidate key, primary key, alternate key, foreign key. Structured Query Language (SQL): DDL commands (CREATE, DROP, ALTER), DML commands (INSERT, UPDATE, DELETE), DQL (SELECT with WHERE, ORDER BY, GROUP BY, HAVING, aggregate functions: COUNT, SUM, AVG, MIN, MAX), Cartesian product and Equi-Joins.", listOf(
                    ChapterHighlight("GROUP BY vs HAVING", "SQL", "GROUP BY groups rows sharing property for aggregate functions. HAVING filters aggregated groups (WHERE filters individual rows before grouping).")
                )),
                ChapterInfo("c12_cs_ch6", 6, "Interface Python with SQL Database", 50, "Connecting Python with MySQL using connector package (mysql.connector), establishing connection, cursor object, executing SQL queries from Python, fetching records (fetchone(), fetchall(), rowcount), parameterized queries, commit and rollback.", listOf(
                    ChapterHighlight("Python-MySQL Pipeline", "Integration", "conn = mysql.connector.connect(...); cur = conn.cursor(); cur.execute('SELECT * FROM tbl'); data = cur.fetchall(); conn.close().")
                ))
            )
        )
    }

    private fun createEconomics(): SubjectInfo {
        return SubjectInfo(
            id = "c12_eco",
            name = "Economics",
            code = "ECO-12",
            description = "CBSE Class 12 Economics covering Introductory Macroeconomics and Indian Economic Development",
            iconCategory = "economics",
            chapters = listOf(
                ChapterInfo("c12_ec_ch1", 1, "National Income and Related Aggregates", 65, "What is Macroeconomics? Basic concepts in macroeconomics: consumption goods, capital goods, final goods, intermediate goods; stocks and flows; gross investment and depreciation. Circular flow of income; Methods of calculating National Income: Value Added (Product) method, Expenditure method, Income method. Aggregates: GDP_MP, GNP_MP, NDP_FC, NNP_FC (National Income), Real vs Nominal GDP, GDP deflator.", listOf(
                    ChapterHighlight("National Income Identities", "Macroeconomics", "NNP_FC = GDP_MP - Depreciation + NFIA - Net Indirect Taxes (NIT). Real GDP = (Nominal GDP / Price Index) × 100.")
                )),
                ChapterInfo("c12_ec_ch2", 2, "Money and Banking", 55, "Money: Meaning, functions and supply of money (currency held by public and net demand deposits held by commercial banks: M1, M2, M3, M4). Money creation by commercial banking system (credit multiplier). Central Bank (RBI) and its functions: issuing currency, banker to government, banker's bank, controller of credit (Repo Rate, Reverse Repo Rate, CRR, SLR, Open Market Operations, Margin Requirements).", listOf(
                    ChapterHighlight("Credit Creation Multiplier", "Banking", "Money Multiplier = 1 / LRR (Legal Reserve Ratio). Total Credit Created = Initial Deposits × (1 / LRR).")
                )),
                ChapterInfo("c12_ec_ch3", 3, "Determination of Income and Employment", 65, "Aggregate demand, aggregate supply and their components. Propensity to consume and propensity to save (MPC, MPS, APC, APS). Short-run fixed price equilibrium; investment multiplier and its mechanism. Problems of excess demand and deficient demand; measures to correct them: changes in government spending, taxes and monetary policy tools.", listOf(
                    ChapterHighlight("Investment Multiplier", "Formula", "k = ΔY / ΔI = 1 / (1 - MPC) = 1 / MPS. MPC + MPS = 1.")
                )),
                ChapterInfo("c12_ec_ch4", 4, "Government Budget and the Economy", 50, "Government budget: Meaning, objectives and components. Classification of receipts: revenue receipts and capital receipts; Classification of expenditure: revenue expenditure and capital expenditure. Balanced, surplus and deficit budget; Measures of government deficit: Revenue Deficit, Fiscal Deficit, Primary Deficit and their implications.", listOf(
                    ChapterHighlight("Fiscal Deficit", "Public Finance", "Fiscal Deficit = Total Budget Expenditure - (Revenue Receipts + Non-debt Capital Receipts). Primary Deficit = Fiscal Deficit - Interest Payments.")
                )),
                ChapterInfo("c12_ec_ch5", 5, "Balance of Payments and Foreign Exchange", 50, "Balance of Payments (BoP) account: Meaning and components (Current Account and Capital Account); BoP surplus and deficit. Foreign exchange rate: Meaning of fixed and flexible exchange rates, managed floating; determination of exchange rate in a free market; appreciation and depreciation of domestic currency.", listOf(
                    ChapterHighlight("Current vs Capital Account", "International Trade", "Current Account records trade in goods (merchandise), trade in services (invisibles), and unilateral transfers. Capital Account records investments and borrowings.")
                )),
                ChapterInfo("c12_ec_ch6", 6, "Development Experience (1947-90) & Economic Reforms since 1991", 55, "A brief introduction of the state of Indian economy on the eve of independence. Common goals of Five Year Plans. Main features, problems and policies of agriculture (land reforms, Green Revolution), industry (IPR 1956, SSI), and foreign trade. Economic Reforms since 1991: Need and main features of Liberalization, Privatization and Globalization (LPG policies); appraisal of LPG policies.", listOf(
                    ChapterHighlight("LPG Reforms 1991", "Indian Economy", "New Economic Policy 1991 introduced de-licensing (Liberalization), disinvestment in PSUs (Privatization), and tariff reductions with FDI inflow (Globalization).")
                )),
                ChapterInfo("c12_ec_ch7", 7, "Current Challenges facing Indian Economy", 60, "Human Capital Formation: Education and health sectors. Rural Development: Key issues - credit and marketing, organic farming. Employment: Growth and changes in workforce participation rate in formal and informal sectors, problems and policies. Sustainable Economic Development: Meaning, effects of economic development on resources and environment, global warming.", listOf(
                    ChapterHighlight("Sustainable Development", "Development", "Development that meets the needs of the present without compromising the ability of future generations to meet their own needs (Brundtland Report).")
                )),
                ChapterInfo("c12_ec_ch8", 8, "Development Experience of India – A Comparison with Neighbours", 50, "A comparative study of India and its neighbouring countries (China and Pakistan): Issues of economic growth, population, sectoral development and other human development indicators (HDI).", listOf(
                    ChapterHighlight("Comparative Indicators", "Comparative Development", "China initiated reforms in 1978 and One Child Policy (1979); India initiated reforms in 1991. China leads in manufacturing and HDI rank.")
                ))
            )
        )
    }
}
