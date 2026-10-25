package com.example.model.curriculum

import com.example.model.ChapterHighlight
import com.example.model.ChapterInfo
import com.example.model.ClassInfo
import com.example.model.SubjectInfo

object Class10Curriculum {

    fun create(): ClassInfo {
        return ClassInfo(
            id = "class_10",
            name = "Class 10",
            subtitle = "Official CBSE Board Examination Curriculum",
            gradeNumber = 10,
            subjects = listOf(
                createMathematics(),
                createPhysics(),
                createChemistry(),
                createBiology(),
                createEnglish(),
                createHistory(),
                createGeography(),
                createPoliticalScience(),
                createEconomics(),
                createComputerScience()
            )
        )
    }

    private fun createMathematics(): SubjectInfo {
        return SubjectInfo(
            id = "c10_math",
            name = "Mathematics",
            code = "MATH-10",
            description = "Complete CBSE Class 10 Board Mathematics syllabus with 14 full chapters",
            iconCategory = "math",
            chapters = listOf(
                ChapterInfo("c10_m_ch1", 1, "Real Numbers", 45, "Fundamental Theorem of Arithmetic, prime factorization, proving irrationality of √2, √3, √5.", listOf(
                    ChapterHighlight("Fundamental Theorem of Arithmetic", "Theorem", "Every composite number can be uniquely expressed as a product of primes: HCF(a, b) × LCM(a, b) = a × b.")
                )),
                ChapterInfo("c10_m_ch2", 2, "Polynomials", 50, "Zeroes of a polynomial, relationship between zeroes and coefficients of quadratic polynomials.", listOf(
                    ChapterHighlight("Zeroes & Coefficients", "Formula", "For ax² + bx + c = 0: Sum α + β = -b/a ; Product αβ = c/a.")
                )),
                ChapterInfo("c10_m_ch3", 3, "Pair of Linear Equations in Two Variables", 55, "Graphical method, substitution method, elimination method, consistency conditions.", listOf(
                    ChapterHighlight("Consistency Conditions", "Condition", "Unique solution: a₁/a₂ ≠ b₁/b₂ ; Infinitely many: a₁/a₂ = b₁/b₂ = c₁/c₂ ; No solution: a₁/a₂ = b₁/b₂ ≠ c₁/c₂.")
                )),
                ChapterInfo("c10_m_ch4", 4, "Quadratic Equations", 55, "Standard form ax² + bx + c = 0, quadratic formula, factorization, nature of roots.", listOf(
                    ChapterHighlight("Quadratic Formula", "Formula", "x = [-b ± √(b² - 4ac)] / (2a) ; D = b² - 4ac dictates real, equal, or distinct roots.")
                )),
                ChapterInfo("c10_m_ch5", 5, "Arithmetic Progressions", 50, "nth term of an AP (a_n), sum of first n terms (S_n), practical daily life problems.", listOf(
                    ChapterHighlight("AP Formulas", "Formula", "a_n = a + (n - 1)d ; S_n = (n/2)[2a + (n - 1)d] = (n/2)[a + l].")
                )),
                ChapterInfo("c10_m_ch6", 6, "Triangles", 60, "Basic Proportionality Theorem (Thales), criteria for similarity of triangles (AAA, SSS, SAS).", listOf(
                    ChapterHighlight("Basic Proportionality Theorem", "Theorem", "If a line is drawn parallel to one side of a triangle intersecting the other two sides, it divides them in the same ratio.")
                )),
                ChapterInfo("c10_m_ch7", 7, "Coordinate Geometry", 45, "Distance formula, Section formula, midpoint coordinates.", listOf(
                    ChapterHighlight("Section Formula", "Formula", "P(x, y) = [(m₁x₂ + m₂x₁) / (m₁ + m₂), (m₁y₂ + m₂y₁) / (m₁ + m₂)].")
                )),
                ChapterInfo("c10_m_ch8", 8, "Introduction to Trigonometry", 55, "Trigonometric ratios of acute angles, standard angles (0°, 30°, 45°, 60°, 90°), trigonometric identities.", listOf(
                    ChapterHighlight("Trigonometric Identity", "Formula", "sin²θ + cos²θ = 1 ; 1 + tan²θ = sec²θ ; 1 + cot²θ = cosec²θ.")
                )),
                ChapterInfo("c10_m_ch9", 9, "Some Applications of Trigonometry", 50, "Heights and distances, angle of elevation, angle of depression, real-world multi-step problems.", listOf(
                    ChapterHighlight("Angle of Elevation", "Concept", "Angle formed by the line of sight with the horizontal when the observer looks upward at an object.")
                )),
                ChapterInfo("c10_m_ch10", 10, "Circles", 50, "Tangent to a circle, tangent perpendicular to radius at point of contact, lengths of tangents from external point.", listOf(
                    ChapterHighlight("Tangent Theorem", "Theorem", "The lengths of tangents drawn from an external point to a circle are equal (PA = PB).")
                )),
                ChapterInfo("c10_m_ch11", 11, "Areas Related to Circles", 45, "Area of sector and segment of a circle, arc length of a sector.", listOf(
                    ChapterHighlight("Sector Area", "Formula", "Area of Sector = (θ / 360°) × πr² ; Arc Length = (θ / 360°) × 2πr.")
                )),
                ChapterInfo("c10_m_ch12", 12, "Surface Areas and Volumes", 60, "Surface areas and volumes of combinations of solids (cubes, cuboids, spheres, hemispheres, cylinders, cones).", listOf(
                    ChapterHighlight("Combined Solids", "Formula", "Cylinder: V = πr²h, TSA = 2πr(r + h) ; Cone: V = (1/3)πr²h, CSA = πrl (where l = √(r² + h²)).")
                )),
                ChapterInfo("c10_m_ch13", 13, "Statistics", 50, "Mean, median, and mode of grouped data, empirical relationship between the three measures of central tendency.", listOf(
                    ChapterHighlight("Empirical Relationship", "Formula", "3 Median = Mode + 2 Mean.")
                )),
                ChapterInfo("c10_m_ch14", 14, "Probability", 40, "Classical definition of probability, elementary events, complementary events, P(E) + P(not E) = 1.", listOf(
                    ChapterHighlight("Probability Range", "Law", "0 ≤ P(E) ≤ 1 ; P(Sure Event) = 1, P(Impossible Event) = 0.")
                ))
            )
        )
    }

    private fun createPhysics(): SubjectInfo {
        return SubjectInfo(
            id = "c10_phys",
            name = "Physics",
            code = "PHYS-10",
            description = "Optics, electricity, and electromagnetic effects for Class 10 Board examinations",
            iconCategory = "physics",
            chapters = listOf(
                ChapterInfo("c10_p_ch1", 1, "Light — Reflection and Refraction", 55, "Spherical mirrors, mirror formula, magnification, refraction of light, Snell's law, spherical lenses, lens formula, power of a lens.", listOf(
                    ChapterHighlight("Mirror & Lens Formulas", "Formula", "Mirror: 1/f = 1/v + 1/u, m = -v/u ; Lens: 1/f = 1/v - 1/u, m = v/u ; Power P = 1/f (in meters, Dioptres).")
                )),
                ChapterInfo("c10_p_ch2", 2, "The Human Eye and the Colourful World", 50, "Structure of eye, power of accommodation, defects of vision (myopia, hypermetropia, presbyopia) and corrections, prism dispersion, atmospheric refraction, scattering of light (Tyndall effect).", listOf(
                    ChapterHighlight("Defects of Vision", "Concept", "Myopia (nearsightedness) corrected by concave lens; Hypermetropia (farsightedness) corrected by convex lens.")
                )),
                ChapterInfo("c10_p_ch3", 3, "Electricity", 60, "Electric current and circuit, electric potential and potential difference, Ohm's law, resistance, resistivity, series & parallel combinations, Joule's heating law, electric power.", listOf(
                    ChapterHighlight("Ohm's & Joule's Laws", "Fundamental Law", "V = I · R ; Resistance R = ρ(l / A) ; Heating H = I² · R · t = V · I · t ; Power P = V · I = I²R.")
                )),
                ChapterInfo("c10_p_ch4", 4, "Magnetic Effects of Electric Current", 55, "Magnetic field and field lines, Right-Hand Thumb Rule, field due to current-carrying straight conductor, circular loop and solenoid, Fleming's Left-Hand Rule, electric motor, electromagnetic induction, domestic electric circuits.", listOf(
                    ChapterHighlight("Fleming's Left-Hand Rule", "Rule", "Thumb: Motion/Force; Forefinger: Magnetic Field; Middle Finger: Current.")
                ))
            )
        )
    }

    private fun createChemistry(): SubjectInfo {
        return SubjectInfo(
            id = "c10_chem",
            name = "Chemistry",
            code = "CHEM-10",
            description = "Chemical reactions, acids, bases, salts, metals, metallurgy, and carbon compounds",
            iconCategory = "chemistry",
            chapters = listOf(
                ChapterInfo("c10_c_ch1", 1, "Chemical Reactions and Equations", 50, "Balancing equations, types of reactions: combination, decomposition, displacement, double displacement, precipitation, neutralization, oxidation, reduction, corrosion, and rancidity.", listOf(
                    ChapterHighlight("Redox Reaction", "Concept", "Oxidation is gain of oxygen/loss of electrons; Reduction is loss of oxygen/gain of electrons occurring simultaneously.")
                )),
                ChapterInfo("c10_c_ch2", 2, "Acids, Bases and Salts", 55, "Indicators, chemical properties of acids and bases, pH scale, importance of pH in everyday life, salts (bleaching powder, baking soda, washing soda, plaster of Paris).", listOf(
                    ChapterHighlight("Plaster of Paris", "Reaction", "CaSO₄·½H₂O formed by heating gypsum (CaSO₄·2H₂O) at 373 K; sets hard on rehydration.")
                )),
                ChapterInfo("c10_c_ch3", 3, "Metals and Non-Metals", 55, "Physical and chemical properties, reactivity series, formation and properties of ionic compounds, basic metallurgical processes, corrosion prevention.", listOf(
                    ChapterHighlight("Reactivity Series", "Concept", "K > Na > Ca > Mg > Al > Zn > Fe > Pb > [H] > Cu > Hg > Ag > Au.")
                )),
                ChapterInfo("c10_c_ch4", 4, "Carbon and its Compounds", 60, "Covalent bonding in carbon, versatile nature (catenation and tetravalency), homologous series, nomenclature of functional groups, chemical properties (combustion, oxidation, addition, substitution), ethanol and ethanoic acid, soaps and detergents.", listOf(
                    ChapterHighlight("Catenation & Tetravalency", "Core Concept", "Carbon's self-linking property and 4 valence electrons enable millions of diverse organic structures.")
                ))
            )
        )
    }

    private fun createBiology(): SubjectInfo {
        return SubjectInfo(
            id = "c10_bio",
            name = "Biology",
            code = "BIO-10",
            description = "Life processes, neural and endocrine control, reproduction, heredity, and environment",
            iconCategory = "biology",
            chapters = listOf(
                ChapterInfo("c10_b_ch1", 1, "Life Processes", 60, "Autotrophic & heterotrophic nutrition, human digestive system, respiration (aerobic & anaerobic), circulatory system (human heart, double circulation), excretion (nephron function).", listOf(
                    ChapterHighlight("Double Circulation", "Mechanism", "Blood travels through the human heart twice during each complete cycle (pulmonary and systemic circulation).")
                )),
                ChapterInfo("c10_b_ch2", 2, "Control and Coordination", 55, "Nervous system, reflex arc, human brain structure, plant hormones (auxin, gibberellin, cytokinin, abscisic acid), plant movements (tropic & nastic), endocrine glands and hormones in humans.", listOf(
                    ChapterHighlight("Plant Hormones", "Biochemistry", "Auxin promotes cell elongation; Cytokinin promotes cell division; Abscisic Acid inhibits growth and causes wilting.")
                )),
                ChapterInfo("c10_b_ch3", 3, "How do Organisms Reproduce?", 55, "Asexual reproduction (fission, fragmentation, regeneration, budding, vegetative propagation, spore formation), sexual reproduction in flowering plants, human male and female reproductive systems, reproductive health and contraception.", listOf(
                    ChapterHighlight("Double Fertilization", "Botany", "In angiosperms: one sperm fertilizes egg forming zygote; second sperm fertilizes secondary nucleus forming triploid endosperm.")
                )),
                ChapterInfo("c10_b_ch4", 4, "Heredity and Evolution", 50, "Mendel's laws of inheritance, monohybrid and dihybrid crosses, sex determination in human beings (XX and XY chromosomes).", listOf(
                    ChapterHighlight("Sex Determination", "Genetics", "Father determines the sex of the child: egg carries X; sperm carrying X produces female (XX), Y produces male (XY).")
                )),
                ChapterInfo("c10_b_ch5", 5, "Our Environment", 40, "Ecosystem components, food chains and food webs, 10% energy transfer law, biomagnification, ozone layer depletion, waste management.", listOf(
                    ChapterHighlight("Lindeman's 10% Law", "Ecological Law", "Only 10% of energy entering a trophic level is stored as biomass and available to the next trophic level.")
                ))
            )
        )
    }

    private fun createEnglish(): SubjectInfo {
        return SubjectInfo(
            id = "c10_eng",
            name = "English Language & Literature",
            code = "ENG-10",
            description = "CBSE NCERT First Flight and Footprints Without Feet complete literature syllabus",
            iconCategory = "english",
            chapters = listOf(
                ChapterInfo("c10_e_ch1", 1, "A Letter to God & Dust of Snow", 40, "Lencho's absolute faith in God; Robert Frost's poem on unexpected natural moments changing mood.", listOf(
                    ChapterHighlight("Irony", "Literary Device", "Lencho calls the compassionate postmaster and staff a bunch of crooks.")
                )),
                ChapterInfo("c10_e_ch2", 2, "Nelson Mandela: Long Walk to Freedom & Fire and Ice", 45, "Mandela's inauguration speech on apartheid, human dignity, and twin obligations; Robert Frost on desire vs hatred.", listOf(
                    ChapterHighlight("Twin Obligations", "Theme", "Obligations to family and obligations to one's nation and community.")
                )),
                ChapterInfo("c10_e_ch3", 3, "Two Stories about Flying & A Tiger in the Zoo", 40, "The young seagull conquering fear of flying; mystery of the Black Aeroplane; Leslie Norris contrasting wild vs captive tiger.", listOf(
                    ChapterHighlight("Theme", "Literature", "Courage conquers self-doubt; natural freedom vs captivity.")
                )),
                ChapterInfo("c10_e_ch4", 4, "From the Diary of Anne Frank & How to Tell Wild Animals", 40, "Anne Frank's reflections in hiding in Amsterdam; Carolyn Wells's humorous guide to wild beasts.", listOf(
                    ChapterHighlight("Historical Value", "Memoir", "Paper has more patience than people: adolescent resilience under wartime tyranny.")
                )),
                ChapterInfo("c10_e_ch5", 5, "Glimpses of India & The Trees", 45, "A Baker from Goa, Coorg coffee hills, Tea from Assam; Adrienne Rich's poem on women's liberation.", listOf(
                    ChapterHighlight("Cultural Heritage", "Travelogue", "Rich traditions of Goan Paders, Kodavu warriors, and vast emerald Assam tea gardens.")
                )),
                ChapterInfo("c10_e_ch6", 6, "Mijbil the Otter & Fog", 35, "Gavin Maxwell's endearing pet otter in London; Carl Sandburg's haiku-like poem on silent mist.", listOf(
                    ChapterHighlight("Literary Imagery", "Poetry", "The fog comes on little cat feet, looking over harbor and city.")
                )),
                ChapterInfo("c10_e_ch7", 7, "Madam Rides the Bus & The Tale of Custard the Dragon", 40, "Valli's determined first bus journey to town; Ogden Nash's humorous mock-heroic ballad on bravery.", listOf(
                    ChapterHighlight("Characterization", "Literature", "Valli's fierce independence and the quiet courage of Custard when danger struck.")
                )),
                ChapterInfo("c10_e_ch8", 8, "The Sermon at Benares & For Anne Gregory", 40, "Lord Buddha's wisdom to Kisa Gotami on mortality; W.B. Yeats on true inner spiritual beauty.", listOf(
                    ChapterHighlight("Spiritual Insight", "Philosophy", "Death is common to all; wisdom lies in shedding selfish grief and accepting universal change.")
                )),
                ChapterInfo("c10_e_ch9", 9, "The Proposal", 40, "Anton Chekhov's farcical one-act play on Lomov, Natalya, and Chubukov arguing over Oxen Meadows and dogs.", listOf(
                    ChapterHighlight("Satire", "Drama", "Chekhov satirizes Victorian-era social marriages driven by economic greed rather than romance.")
                ))
            )
        )
    }

    private fun createHistory(): SubjectInfo {
        return SubjectInfo(
            id = "c10_hist",
            name = "History",
            code = "HIST-10",
            description = "India and the Contemporary World II: Nationalism, global economy, and print culture",
            iconCategory = "history",
            chapters = listOf(
                ChapterInfo("c10_h_ch1", 1, "The Rise of Nationalism in Europe", 55, "French Revolution legacy, Napoleonic Code, liberal nationalism, Zollverein, revolutions of 1848, unification of Italy (Mazzini, Cavour, Garibaldi) and Germany (Bismarck), allegories (Marianne & Germania).", listOf(
                    ChapterHighlight("Civil Code of 1804", "History", "Napoleonic Code abolished privileges based on birth, established equality before law, and secured right to property.")
                )),
                ChapterInfo("c10_h_ch2", 2, "Nationalism in India", 60, "First World War impact, Satyagraha concept, Rowlatt Act, Jallianwala Bagh massacre, Khilafat & Non-Cooperation Movement, Simon Commission, Salt March & Civil Disobedience Movement, sense of collective belonging.", listOf(
                    ChapterHighlight("Salt March 1930", "Historic Event", "Mahatma Gandhi's 240-mile march from Sabarmati to Dandi ignited nationwide Civil Disobedience.")
                )),
                ChapterInfo("c10_h_ch3", 3, "The Making of a Global World", 50, "Pre-modern world trade, Silk Routes, conquest and diseases (smallpox in Americas), 19th-century indenture, Great Depression of 1929, post-war Bretton Woods institutions (IMF & World Bank).", listOf(
                    ChapterHighlight("Bretton Woods Twins", "Economics", "IMF and World Bank created in 1944 to maintain international financial stability and finance reconstruction.")
                )),
                ChapterInfo("c10_h_ch4", 4, "The Age of Industrialisation", 45, "Proto-industrialisation, steam power vs hand labour, factories in Britain, industrialisation in colonies, textile mills in India (Dwarkanath Tagore, Dinshaw Petit, Jamsetjee Tata), market for goods.", listOf(
                    ChapterHighlight("Proto-Industrialisation", "Economic History", "Phase of decentralized international production in rural cottages before factories emerged.")
                )),
                ChapterInfo("c10_h_ch5", 5, "Print Culture and the Modern World", 50, "First printed books in East Asia, Gutenberg printing press, print revolution, reading mania, religious debates (Martin Luther), French Revolution connection, print in 19th-century India.", listOf(
                    ChapterHighlight("Gutenberg Press (1448)", "Invention", "Movable type mechanical printing democratized knowledge and catalyzed the Protestant Reformation.")
                ))
            )
        )
    }

    private fun createGeography(): SubjectInfo {
        return SubjectInfo(
            id = "c10_geo",
            name = "Geography",
            code = "GEO-10",
            description = "Contemporary India II: Resources, agriculture, minerals, manufacturing, and lifelines",
            iconCategory = "geography",
            chapters = listOf(
                ChapterInfo("c10_g_ch1", 1, "Resources and Development", 45, "Resource classification, sustainable development, Rio Earth Summit 1992, land utilization, land degradation, soil classification (Alluvial, Black, Red/Yellow, Laterite, Arid, Forest).", listOf(
                    ChapterHighlight("Sustainable Development", "Principle", "Development that meets present needs without compromising the ability of future generations to meet theirs.")
                )),
                ChapterInfo("c10_g_ch2", 2, "Forest and Wildlife Resources", 40, "Biodiversity, IUCN species categories (endangered, vulnerable, rare, endemic), reserved & protected forests, community conservation (Chipko, Beej Bachao Andolan, Joint Forest Management).", listOf(
                    ChapterHighlight("Forest Classification", "Ecology", "Reserved forests are most valuable for timber and conservation; Protected forests prevent further depletion.")
                )),
                ChapterInfo("c10_g_ch3", 3, "Water Resources", 45, "Water scarcity, multi-purpose river projects and dams, environmental opposition, traditional rainwater harvesting techniques (Khadin, Johad, Guls, Kuls, rooftop harvesting).", listOf(
                    ChapterHighlight("Rainwater Harvesting", "Conservation", "Rooftop harvesting is mandatory in Tamil Nadu to replenish falling groundwater tables.")
                )),
                ChapterInfo("c10_g_ch4", 4, "Agriculture", 50, "Types of farming (primitive subsistence, intensive subsistence, commercial), cropping seasons (Rabi, Kharif, Zaid), major crops (rice, wheat, millets, pulses, sugarcane, tea, coffee, cotton), institutional & technological reforms.", listOf(
                    ChapterHighlight("Cropping Seasons", "Agronomy", "Kharif: June-October (Rice, Maize) ; Rabi: October-April (Wheat, Mustard) ; Zaid: Summer (Watermelon, Cucumber).")
                )),
                ChapterInfo("c10_g_ch5", 5, "Minerals and Energy Resources", 50, "Mode of occurrence of minerals, ferrous and non-ferrous minerals, non-metallic minerals, conventional energy (coal, petroleum, natural gas) and non-conventional energy (solar, wind, biogas, tidal).", listOf(
                    ChapterHighlight("Energy Transition", "Sustainability", "Non-conventional energy sources like solar and wind mitigate greenhouse emissions and resource depletion.")
                )),
                ChapterInfo("c10_g_ch6", 6, "Manufacturing Industries", 50, "Importance of manufacturing, industrial location factors, agro-based industries (textiles, sugar), mineral-based industries (iron & steel, aluminium smelting, chemical, fertilizer, automobile, IT), industrial pollution.", listOf(
                    ChapterHighlight("Economic Backbone", "Industry", "Manufacturing measures the economic strength of a country by transforming raw materials into high-value goods.")
                )),
                ChapterInfo("c10_g_ch7", 7, "Lifelines of National Economy", 45, "Road transport (Golden Quadrilateral, National Highways), railways, pipelines, waterways and major sea ports, airways, communication networks, international trade, tourism.", listOf(
                    ChapterHighlight("National Highways", "Infrastructure", "NHAI develops and maintains Golden Quadrilateral connecting Delhi, Mumbai, Chennai, and Kolkata.")
                ))
            )
        )
    }

    private fun createPoliticalScience(): SubjectInfo {
        return SubjectInfo(
            id = "c10_civ",
            name = "Political Science",
            code = "POL-10",
            description = "Democratic Politics II: Power sharing, federalism, political parties, and democracy outcomes",
            iconCategory = "civics",
            chapters = listOf(
                ChapterInfo("c10_pol_ch1", 1, "Power Sharing", 45, "Case studies of Belgium and Sri Lanka, majoritarianism in Sri Lanka, accommodation in Belgium, horizontal and vertical power sharing.", listOf(
                    ChapterHighlight("Horizontal Power Sharing", "Governance", "Separation of powers between Legislature, Executive, and Judiciary creating a system of checks and balances.")
                )),
                ChapterInfo("c10_pol_ch2", 2, "Federalism", 50, "What is federalism, key features, coming together vs holding together federations, Union, State, and Concurrent Lists, linguistic states, decentralization in India (73rd & 74th Amendments).", listOf(
                    ChapterHighlight("73rd Amendment (1992)", "Constitutional Law", "Mandated local self-government (Panchayati Raj) with reserved seats for women and scheduled castes.")
                )),
                ChapterInfo("c10_pol_ch3", 3, "Gender, Religion and Caste", 45, "Gender division, women's political representation, religion and communalism, secular state principles, caste inequalities and caste in Indian politics.", listOf(
                    ChapterHighlight("Secularism", "Constitutional Principle", "The Constitution prohibits discrimination on religious grounds; the state has no official religion.")
                )),
                ChapterInfo("c10_pol_ch4", 4, "Political Parties", 50, "Why we need political parties, functions, party systems (one-party, two-party, multi-party), national parties in India, regional state parties, challenges, and reforms.", listOf(
                    ChapterHighlight("Anti-Defection Law", "Political Reform", "Prevents elected MPs and MLAs from switching political parties after elections.")
                )),
                ChapterInfo("c10_pol_ch5", 5, "Outcomes of Democracy", 40, "Accountable, responsive and legitimate government, economic growth and development, reduction of inequality and poverty, accommodation of social diversity, dignity and freedom of citizens.", listOf(
                    ChapterHighlight("Legitimacy", "Democratic Value", "A democratic government may be slow, but it is legitimate because it is the people's own government.")
                ))
            )
        )
    }

    private fun createEconomics(): SubjectInfo {
        return SubjectInfo(
            id = "c10_eco",
            name = "Economics",
            code = "ECON-10",
            description = "Understanding Economic Development: Growth, sectors, money, credit, and globalization",
            iconCategory = "economics",
            chapters = listOf(
                ChapterInfo("c10_eco_ch1", 1, "Development", 45, "Developmental goals, national income, per capita income, World Bank criterion, Human Development Index (HDI) by UNDP, sustainable development.", listOf(
                    ChapterHighlight("HDI Components", "Economic Metric", "Life expectancy at birth, per capita gross national income, and mean years of schooling.")
                )),
                ChapterInfo("c10_eco_ch2", 2, "Sectors of the Indian Economy", 50, "Primary, secondary, and tertiary sectors, historical shift in sectors, rising importance of tertiary sector, organized vs unorganized sector, public vs private sectors.", listOf(
                    ChapterHighlight("Tertiary Sector", "Economic Shift", "Services (banking, IT, transport) contribute over 50% to Indian GDP but employment remains concentrated in agriculture.")
                )),
                ChapterInfo("c10_eco_ch3", 3, "Money and Credit", 50, "Barter system & double coincidence of wants, modern forms of money, loan activities of banks, terms of credit, formal vs informal credit sectors, Self-Help Groups (SHGs).", listOf(
                    ChapterHighlight("Terms of Credit", "Banking", "Interest rate, collateral requirement, documentation, and mode of repayment comprise the terms of credit.")
                )),
                ChapterInfo("c10_eco_ch4", 4, "Globalisation and the Indian Economy", 45, "Multinational Corporations (MNCs), foreign trade and market integration, liberalization, role of WTO, impact of globalization on India, fair globalization.", listOf(
                    ChapterHighlight("Liberalization (1991)", "Economic Reform", "Removal of trade barriers and government restrictions to facilitate foreign trade and investment.")
                )),
                ChapterInfo("c10_eco_ch5", 5, "Consumer Rights", 40, "Consumer exploitation in the marketplace, Consumer Protection Act (COPRA 1986 / 2019), consumer rights (safety, information, choice, heard, redressal, consumer education).", listOf(
                    ChapterHighlight("Right to Redressal", "Consumer Law", "Consumers have the right to seek remedies against unfair trade practices and receive compensation for damage.")
                ))
            )
        )
    }

    private fun createComputerScience(): SubjectInfo {
        return SubjectInfo(
            id = "c10_cs",
            name = "Computer Science & IT",
            code = "CS-10",
            description = "Web development, database systems, SQL queries, cyber security, and Python programming",
            iconCategory = "cs",
            chapters = listOf(
                ChapterInfo("c10_cs_ch1", 1, "Networking & Web Technologies", 45, "Internet, World Wide Web, web servers, HTTP/HTTPS protocols, IP addressing, DNS, email protocols (SMTP, POP3, IMAP), cloud computing.", listOf(
                    ChapterHighlight("DNS Function", "Networking", "Domain Name System translates human-readable domain names (e.g. google.com) into numerical IP addresses.")
                )),
                ChapterInfo("c10_cs_ch2", 2, "HTML & CSS Web Development", 50, "HTML document structure, semantic tags, tables, forms, input fields, CSS styling, inline/internal/external stylesheets, responsive box model.", listOf(
                    ChapterHighlight("CSS Box Model", "Web Design", "Margin, border, padding, and content define the physical footprint of elements in HTML.")
                )),
                ChapterInfo("c10_cs_ch3", 3, "Database Management Systems (RDBMS & SQL)", 55, "Relational concepts, primary key, foreign key, candidate key, SQL DDL & DML commands (CREATE, INSERT, SELECT, UPDATE, DELETE, WHERE, ORDER BY).", listOf(
                    ChapterHighlight("SQL Query", "Database Syntax", "SELECT name, marks FROM students WHERE marks >= 90 ORDER BY marks DESC;")
                )),
                ChapterInfo("c10_cs_ch4", 4, "Cybersecurity & Digital Laws", 40, "Cybercrimes, hacking vs ethical hacking, malware types (virus, worms, trojan, ransomware), intellectual property rights, IT Act 2000.", listOf(
                    ChapterHighlight("IT Act 2000", "Cyber Law", "Provides legal recognition for electronic transactions and defines penalties for cyber offenses in India.")
                )),
                ChapterInfo("c10_cs_ch5", 5, "Python Scripting & Problem Solving", 55, "Control flow, functions with arguments and return values, string manipulation, list operations, file handling basics.", listOf(
                    ChapterHighlight("Python Functions", "Programming", "def calculate_grade(marks): returns formatted output with modular, reusable code.")
                ))
            )
        )
    }
}
