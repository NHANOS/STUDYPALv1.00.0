package com.example.model.curriculum

import com.example.model.ChapterHighlight
import com.example.model.ChapterInfo
import com.example.model.ClassInfo
import com.example.model.SubjectInfo

object Class9Curriculum {

    fun create(): ClassInfo {
        return ClassInfo(
            id = "class_9",
            name = "Class 9",
            subtitle = "Official CBSE Secondary Curriculum",
            gradeNumber = 9,
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
            id = "c9_math",
            name = "Mathematics",
            code = "MATH-09",
            description = "Complete CBSE Class 9 Mathematics syllabus with key theorems and formulas",
            iconCategory = "math",
            chapters = listOf(
                ChapterInfo("c9_m_ch1", 1, "Number Systems", 50, "Rational and irrational numbers, decimal expansions, laws of exponents, representing numbers on the number line.", listOf(
                    ChapterHighlight("Irrational Numbers", "Definition", "A number that cannot be written in the form p/q where p, q are integers and q ≠ 0."),
                    ChapterHighlight("Law of Exponents", "Formula", "a^p · a^q = a^(p+q) ; (a^p)^q = a^(pq) ; a^p / a^q = a^(p-q)")
                )),
                ChapterInfo("c9_m_ch2", 2, "Polynomials", 55, "Zeroes of a polynomial, Factor Theorem, Remainder Theorem, and standard algebraic identities.", listOf(
                    ChapterHighlight("Factor Theorem", "Theorem", "If p(x) is a polynomial and p(a) = 0, then (x - a) is a factor of p(x)."),
                    ChapterHighlight("Identity", "Formula", "(x + y + z)² = x² + y² + z² + 2xy + 2yz + 2zx")
                )),
                ChapterInfo("c9_m_ch3", 3, "Coordinate Geometry", 40, "Cartesian plane, coordinates of a point, quadrants and axes.", listOf(
                    ChapterHighlight("Cartesian Coordinates", "Concept", "The horizontal line is the X-axis (abscissa) and the vertical line is the Y-axis (ordinate). Point is (x, y).")
                )),
                ChapterInfo("c9_m_ch4", 4, "Linear Equations in Two Variables", 45, "Standard form ax + by + c = 0, solutions of linear equations and graphical representations.", listOf(
                    ChapterHighlight("Standard Form", "Formula", "ax + by + c = 0, where a, b, c are real numbers and a, b ≠ 0. It has infinitely many solutions.")
                )),
                ChapterInfo("c9_m_ch5", 5, "Introduction to Euclid's Geometry", 35, "Euclid's definitions, axioms, and five historical postulates.", listOf(
                    ChapterHighlight("Euclid's Postulate 5", "Postulate", "If a straight line falling on two straight lines makes interior angles less than two right angles, lines intersect on that side.")
                )),
                ChapterInfo("c9_m_ch6", 6, "Lines and Angles", 50, "Linear pair axiom, vertically opposite angles, parallel lines and transversals.", listOf(
                    ChapterHighlight("Linear Pair Axiom", "Axiom", "If a ray stands on a line, then the sum of two adjacent angles formed is 180°.")
                )),
                ChapterInfo("c9_m_ch7", 7, "Triangles", 60, "Congruence criteria (SAS, ASA, AAS, SSS, RHS) and inequalities in a triangle.", listOf(
                    ChapterHighlight("Congruence Rule", "Rule", "Two triangles are congruent if corresponding sides and angles are equal (e.g. SAS, ASA, SSS, RHS).")
                )),
                ChapterInfo("c9_m_ch8", 8, "Quadrilaterals", 50, "Angle sum property of quadrilaterals, types of quadrilaterals, Mid-point Theorem.", listOf(
                    ChapterHighlight("Mid-Point Theorem", "Theorem", "The line segment joining the mid-points of two sides of a triangle is parallel to the third side and half of it.")
                )),
                ChapterInfo("c9_m_ch9", 9, "Circles", 55, "Chords, perpendiculars from center, angles subtended by arcs, cyclic quadrilaterals.", listOf(
                    ChapterHighlight("Cyclic Quadrilateral", "Property", "The sum of either pair of opposite angles of a cyclic quadrilateral is 180°.")
                )),
                ChapterInfo("c9_m_ch10", 10, "Heron's Formula", 40, "Area of a triangle using semi-perimeter s = (a + b + c)/2.", listOf(
                    ChapterHighlight("Heron's Formula", "Formula", "Area = √[s(s - a)(s - b)(s - c)], where s = (a + b + c) / 2.")
                )),
                ChapterInfo("c9_m_ch11", 11, "Surface Areas and Volumes", 60, "Surface areas and volumes of right circular cones, spheres, and hemispheres.", listOf(
                    ChapterHighlight("Sphere Volume & Area", "Formula", "Surface Area = 4πr² ; Volume = (4/3)πr³ ; Cone Volume = (1/3)πr²h.")
                )),
                ChapterInfo("c9_m_ch12", 12, "Statistics", 45, "Graphical representation of data: bar graphs, histograms, and frequency polygons.", listOf(
                    ChapterHighlight("Data Representation", "Concept", "Histograms display continuous grouped frequency distributions with widths proportional to class intervals.")
                ))
            )
        )
    }

    private fun createPhysics(): SubjectInfo {
        return SubjectInfo(
            id = "c9_physics",
            name = "Physics",
            code = "PHYS-09",
            description = "Motion, forces, gravitation, work, energy, and acoustic waves",
            iconCategory = "physics",
            chapters = listOf(
                ChapterInfo("c9_p_ch1", 1, "Motion", 55, "Distance, displacement, uniform and non-uniform motion, velocity, acceleration, and equations of motion.", listOf(
                    ChapterHighlight("Equations of Motion", "Formula", "1) v = u + at \n2) s = ut + ½at² \n3) v² - u² = 2as")
                )),
                ChapterInfo("c9_p_ch2", 2, "Force and Laws of Motion", 55, "Inertia, momentum, Newton's three laws of motion, conservation of momentum.", listOf(
                    ChapterHighlight("Newton's 2nd Law", "Fundamental Law", "F = m · a (Force is rate of change of momentum)."),
                    ChapterHighlight("Conservation of Momentum", "Law", "Total momentum remains constant in isolated interactions: m₁u₁ + m₂u₂ = m₁v₁ + m₂v₂.")
                )),
                ChapterInfo("c9_p_ch3", 3, "Gravitation", 50, "Universal law of gravitation, free fall, acceleration due to gravity (g), mass vs weight, thrust & pressure, Archimedes' Principle.", listOf(
                    ChapterHighlight("Universal Law of Gravitation", "Formula", "F = G · (m₁ · m₂) / r², where G = 6.67 × 10⁻¹¹ N·m²/kg²."),
                    ChapterHighlight("Archimedes' Principle", "Principle", "When a body is immersed in a fluid, it experiences an upward buoyant force equal to the weight of fluid displaced.")
                )),
                ChapterInfo("c9_p_ch4", 4, "Work and Energy", 50, "Work done by a constant force, kinetic and potential energy, Law of Conservation of Energy, power.", listOf(
                    ChapterHighlight("Work & Kinetic Energy", "Formula", "W = F · s · cosθ ; KE = ½mv² ; PE = mgh ; Power = Work / Time (Watts).")
                )),
                ChapterInfo("c9_p_ch5", 5, "Sound", 50, "Production and propagation of sound, longitudinal waves, speed of sound, reflection of sound, echo, reverberation, ultrasound.", listOf(
                    ChapterHighlight("Wave Equation", "Formula", "v = λ · ν (Speed of wave = wavelength × frequency)."),
                    ChapterHighlight("Echo Condition", "Concept", "The minimum distance between source and reflector for hearing an echo is ~17.2 m in air at 22°C.")
                ))
            )
        )
    }

    private fun createChemistry(): SubjectInfo {
        return SubjectInfo(
            id = "c9_chem",
            name = "Chemistry",
            code = "CHEM-09",
            description = "States of matter, mixtures, solutions, atoms, molecules, and atomic structure",
            iconCategory = "chemistry",
            chapters = listOf(
                ChapterInfo("c9_c_ch1", 1, "Matter in Our Surroundings", 45, "States of matter (solid, liquid, gas), particulate nature, temperature conversion, evaporation and latent heat.", listOf(
                    ChapterHighlight("Latent Heat", "Definition", "Heat energy needed to change state of matter without change in temperature (vaporization and fusion).")
                )),
                ChapterInfo("c9_c_ch2", 2, "Is Matter Around Us Pure?", 50, "Elements, compounds, mixtures, solutions, suspensions, colloids, Tyndall effect, physical and chemical changes.", listOf(
                    ChapterHighlight("Colloids & Tyndall Effect", "Property", "Scattering of a beam of light by colloidal particles is called the Tyndall effect.")
                )),
                ChapterInfo("c9_c_ch3", 3, "Atoms and Molecules", 55, "Law of conservation of mass, law of constant proportions, Dalton's atomic theory, atomic mass, chemical formula, mole concept.", listOf(
                    ChapterHighlight("Avogadro's Constant", "Constant", "1 mole = 6.022 × 10²³ particles (atoms/molecules/ions).")
                )),
                ChapterInfo("c9_c_ch4", 4, "Structure of the Atom", 50, "Thomson, Rutherford, and Bohr models of atom, subatomic particles (electrons, protons, neutrons), valency, isotopes, isobars.", listOf(
                    ChapterHighlight("Bohr Model", "Principle", "Electrons revolve around nucleus in discrete orbits without radiating energy; valency depends on outermost electrons.")
                ))
            )
        )
    }

    private fun createBiology(): SubjectInfo {
        return SubjectInfo(
            id = "c9_bio",
            name = "Biology",
            code = "BIO-09",
            description = "Cell structure, organelles, plant and animal tissues, and food resource enhancement",
            iconCategory = "biology",
            chapters = listOf(
                ChapterInfo("c9_b_ch1", 1, "The Fundamental Unit of Life", 50, "Cell theory, prokaryotic and eukaryotic cells, cell membrane, osmosis, cell wall, nucleus, and organelles.", listOf(
                    ChapterHighlight("Cell Theory", "Principle", "All living things are composed of cells, and all cells arise from pre-existing cells (Schleiden, Schwann, Virchow).")
                )),
                ChapterInfo("c9_b_ch2", 2, "Tissues", 55, "Meristematic and permanent plant tissues (parenchyma, collenchyma, sclerenchyma, xylem, phloem), animal epithelial, connective, muscular, nervous tissues.", listOf(
                    ChapterHighlight("Complex Tissues", "Concept", "Xylem conducts water/minerals unidirectionally; Phloem translocates sucrose and amino acids bidirectionally.")
                )),
                ChapterInfo("c9_b_ch3", 3, "Improvement in Food Resources", 45, "Crop production management, crop variety improvement, manure & fertilizers, irrigation, animal husbandry, poultry, fish farming.", listOf(
                    ChapterHighlight("Crop Improvement", "Technique", "Hybridization and genetic modification aim for higher yield, improved quality, and biotic/abiotic resistance.")
                ))
            )
        )
    }

    private fun createEnglish(): SubjectInfo {
        return SubjectInfo(
            id = "c9_eng",
            name = "English Language & Literature",
            code = "ENG-09",
            description = "CBSE NCERT Beehive prose & poetry and Moments supplementary reader",
            iconCategory = "english",
            chapters = listOf(
                ChapterInfo("c9_e_ch1", 1, "The Fun They Had & The Road Not Taken", 40, "Future computerized schooling and Robert Frost's poem on choosing unique life paths.", listOf(
                    ChapterHighlight("Theme", "Literary Analysis", "Human interaction in education and the lasting weight of life choices.")
                )),
                ChapterInfo("c9_e_ch2", 2, "The Sound of Music & Wind", 40, "Evelyn Glennie's triumph over deafness, Bismillah Khan's Shehnai, and Subramania Bharati's poem on inner resilience.", listOf(
                    ChapterHighlight("Key Motif", "Literature", "Unwavering grit turns adversity into timeless melody and strength.")
                )),
                ChapterInfo("c9_e_ch3", 3, "The Little Girl & Rain on the Roof", 40, "Kezia discovering her father's unspoken love; Coates Kinney's nostalgic poem on motherly love.", listOf(
                    ChapterHighlight("Theme", "Literature", "Parental affection hidden beneath discipline and soothing childhood memories.")
                )),
                ChapterInfo("c9_e_ch4", 4, "A Truly Beautiful Mind & Lake Isle of Innisfree", 40, "Albert Einstein's life as a humanist and genius; W.B. Yeats longing for peaceful nature.", listOf(
                    ChapterHighlight("Core Concept", "Biography", "Intellectual brilliance paired with a passionate advocacy for global peace.")
                )),
                ChapterInfo("c9_e_ch5", 5, "The Snake and the Mirror & A Legend of the Northland", 35, "Humorous encounter of a vain doctor with a snake; ballad on generosity and greed.", listOf(
                    ChapterHighlight("Theme", "Literary Analysis", "Vanity dissolved by danger and moral lessons on kindness.")
                )),
                ChapterInfo("c9_e_ch6", 6, "My Childhood & No Men Are Foreign", 40, "APJ Abdul Kalam's formative upbringing in Rameswaram; James Kirkup's universal brotherhood poem.", listOf(
                    ChapterHighlight("Message", "Philosophy", "Communal harmony, modesty, and the oneness of humanity across borders.")
                )),
                ChapterInfo("c9_e_ch7", 7, "Reach for the Top & On Killing a Tree", 40, "Santosh Yadav scaling Everest, Maria Sharapova's tennis journey, Gieve Patel's conservation poem.", listOf(
                    ChapterHighlight("Theme", "Inspiration", "Unyielding ambition, mental toughness, and the deep tenacity of nature.")
                )),
                ChapterInfo("c9_e_ch8", 8, "Kathmandu & A Slumber Did My Spirit Seal", 35, "Vikram Seth's vivid travelogue of Pashupatinath and Baudhnath; Wordsworth's elegy.", listOf(
                    ChapterHighlight("Travelogue", "Literature", "Contrasting meditative silence with chaotic vibrancy in historic shrines.")
                )),
                ChapterInfo("c9_e_ch9", 9, "If I Were You", 35, "Gerrard outwitting an intruder with sharp intelligence and coolness in Douglas James's play.", listOf(
                    ChapterHighlight("Dramatic Technique", "Drama", "Presence of mind overcoming physical threat and crime.")
                ))
            )
        )
    }

    private fun createHistory(): SubjectInfo {
        return SubjectInfo(
            id = "c9_hist",
            name = "History",
            code = "HIST-09",
            description = "India and the Contemporary World I: Revolutions, European socialism, and Nazism",
            iconCategory = "history",
            chapters = listOf(
                ChapterInfo("c9_h_ch1", 1, "The French Revolution", 55, "Old regime estates, storming of the Bastille, constitutional monarchy, reign of terror, abolition of slavery.", listOf(
                    ChapterHighlight("Storming of Bastille", "Milestone", "July 14, 1789: Symbolized the downfall of absolute autocratic despotism.")
                )),
                ChapterInfo("c9_h_ch2", 2, "Socialism in Europe and the Russian Revolution", 55, "Liberals, radicals and conservatives, industrial changes, 1905 Bloody Sunday, October Revolution of 1917, Stalin's collectivization.", listOf(
                    ChapterHighlight("Bolsheviks & Lenin", "Key Event", "April Theses: Peace, land to peasants, and banks nationalized under Bolshevik rule.")
                )),
                ChapterInfo("c9_h_ch3", 3, "Nazism and the Rise of Hitler", 55, "Weimar Republic birth, Great Depression, Hitler's propaganda, destruction of democracy, racial state and Holocaust.", listOf(
                    ChapterHighlight("Enabling Act", "Historical Act", "March 1933: Established dictatorship in Germany, sidelining parliament.")
                )),
                ChapterInfo("c9_h_ch4", 4, "Forest Society and Colonialism", 45, "Colonial forestry, commercial farming, scientific forestry, resistance in Bastar and Java.", listOf(
                    ChapterHighlight("Scientific Forestry", "Concept", "British replaced natural diverse forests with single-species timber plantations for railways and navy.")
                )),
                ChapterInfo("c9_h_ch5", 5, "Pastoralists in the Modern World", 45, "Nomadic pastoralists of India (Gaddis, Gujjar Bakarwals, Raikas) and African Maasai under colonial rule.", listOf(
                    ChapterHighlight("Pastoral Adaptability", "Social Concept", "Cyclical mobility allowed pastoral communities to survive climatic shocks and droughts.")
                ))
            )
        )
    }

    private fun createGeography(): SubjectInfo {
        return SubjectInfo(
            id = "c9_geo",
            name = "Geography",
            code = "GEO-09",
            description = "Contemporary India I: Physical features, climate, drainage, and population",
            iconCategory = "geography",
            chapters = listOf(
                ChapterInfo("c9_g_ch1", 1, "India — Size and Location", 40, "Latitudinal and longitudinal extent, tropic of cancer, standard meridian (82°30'E), neighboring countries.", listOf(
                    ChapterHighlight("Standard Meridian", "Fact", "82°30' E passing through Mirzapur in UP is taken as the Standard Time for all of India.")
                )),
                ChapterInfo("c9_g_ch2", 2, "Physical Features of India", 50, "Plate tectonics, Northern Mountains (Himalayas), Northern Plains, Peninsular Plateau, Indian Desert, Coastal Plains, Islands.", listOf(
                    ChapterHighlight("Himalayan Ranges", "Topography", "Himadri (Greater), Himachal (Lesser), and Shiwaliks (Outer) forming young fold mountains.")
                )),
                ChapterInfo("c9_g_ch3", 3, "Drainage", 45, "Himalayan river systems (Indus, Ganga, Brahmaputra), Peninsular rivers (Narmada, Tapi, Godavari, Krishna, Cauvery), lakes, economic role.", listOf(
                    ChapterHighlight("River Basins", "Hydrology", "Himalayan rivers are perennial fed by glaciers; Peninsular rivers are seasonal and rain-fed.")
                )),
                ChapterInfo("c9_g_ch4", 4, "Climate", 50, "Factors influencing India's climate, monsoon mechanism, seasons, rainfall distribution, monsoons as a unifying bond.", listOf(
                    ChapterHighlight("Monsoon Mechanism", "Meteorology", "Inter-Tropical Convergence Zone (ITCZ), differential heating, and Coriolis force driving SW monsoon winds.")
                )),
                ChapterInfo("c9_g_ch5", 5, "Natural Vegetation and Wildlife", 45, "Tropical evergreen, deciduous, thorn forests, montane vegetation, mangrove forests, wildlife sanctuaries, biosphere reserves.", listOf(
                    ChapterHighlight("Mangrove Forests", "Ecology", "Sundarbans in Ganga-Brahmaputra delta host the Royal Bengal Tiger and dense tidal vegetation.")
                )),
                ChapterInfo("c9_g_ch6", 6, "Population", 45, "Population size, distribution, density, population growth and processes of population change, literacy, occupational structure.", listOf(
                    ChapterHighlight("Population Dynamics", "Demography", "Birth rate, death rate, and migration are the three primary components of population growth.")
                ))
            )
        )
    }

    private fun createPoliticalScience(): SubjectInfo {
        return SubjectInfo(
            id = "c9_civ",
            name = "Political Science",
            code = "POL-09",
            description = "Democratic Politics I: Democracy, constitutional design, institutions, and rights",
            iconCategory = "civics",
            chapters = listOf(
                ChapterInfo("c9_pol_ch1", 1, "What is Democracy? Why Democracy?", 45, "Features of democracy, major decisions by elected leaders, free and fair electoral competition, rule of law, arguments for/against.", listOf(
                    ChapterHighlight("Core Definition", "Definition", "Democracy is a form of government in which the rulers are elected by the people.")
                )),
                ChapterInfo("c9_pol_ch2", 2, "Constitutional Design", 45, "South African constitution, need for a constitution, making of the Indian Constitution, Constituent Assembly, guiding values and Preamble.", listOf(
                    ChapterHighlight("Preamble Values", "Constitutional Law", "Sovereign, Socialist, Secular, Democratic, Republic, Justice, Liberty, Equality, Fraternity.")
                )),
                ChapterInfo("c9_pol_ch3", 3, "Electoral Politics", 45, "Why elections, system of elections in India, electoral constituencies, reserved constituencies, voters' list, Election Commission of India.", listOf(
                    ChapterHighlight("Universal Adult Suffrage", "Principle", "Every Indian citizen aged 18 or above has the right to vote with one vote, one value.")
                )),
                ChapterInfo("c9_pol_ch4", 4, "Working of Institutions", 50, "Decision-makers, Parliament (Lok Sabha & Rajya Sabha), Political & Permanent Executive, Prime Minister and Council of Ministers, Judiciary.", listOf(
                    ChapterHighlight("Judicial Independence", "Governance", "Supreme Court and High Courts have the power of Judicial Review to safeguard fundamental rights.")
                )),
                ChapterInfo("c9_pol_ch5", 5, "Democratic Rights", 45, "Life without rights, rights in a democracy, Six Fundamental Rights in Indian Constitution, Right to Constitutional Remedies, NHRC.", listOf(
                    ChapterHighlight("Article 32", "Fundamental Right", "Right to Constitutional Remedies empowers citizens to directly approach High Courts and the Supreme Court.")
                ))
            )
        )
    }

    private fun createEconomics(): SubjectInfo {
        return SubjectInfo(
            id = "c9_eco",
            name = "Economics",
            code = "ECON-09",
            description = "Understanding economic activities, village economy, poverty, and food security",
            iconCategory = "economics",
            chapters = listOf(
                ChapterInfo("c9_eco_ch1", 1, "The Story of Village Palampur", 40, "Organization of production: land, labor, physical capital (fixed & working), human capital, farming and non-farming activities.", listOf(
                    ChapterHighlight("Factors of Production", "Economic Theory", "Land, Labor, Physical Capital, and Human Capital combine to produce goods and services.")
                )),
                ChapterInfo("c9_eco_ch2", 2, "People as Resource", 45, "Human capital formation, role of education and health in human resource development, unemployment (seasonal, disguised, educated).", listOf(
                    ChapterHighlight("Disguised Unemployment", "Concept", "Situation where more people are engaged in work than required, contributing zero marginal productivity.")
                )),
                ChapterInfo("c9_eco_ch3", 3, "Poverty as a Challenge", 45, "Poverty line estimation in India, vulnerable groups, interstate disparities, global poverty scenario, anti-poverty measures (MGNREGA).", listOf(
                    ChapterHighlight("MGNREGA 2005", "Government Scheme", "Guarantees 100 days of wage employment in a financial year to rural households.")
                )),
                ChapterInfo("c9_eco_ch4", 4, "Food Security in India", 45, "Dimensions of food security (availability, accessibility, affordability), buffer stock, Public Distribution System (PDS), role of cooperatives.", listOf(
                    ChapterHighlight("Buffer Stock & PDS", "Policy", "Government procures wheat and rice via FCI at Minimum Support Price (MSP) and distributes via ration shops.")
                ))
            )
        )
    }

    private fun createComputerScience(): SubjectInfo {
        return SubjectInfo(
            id = "c9_cs",
            name = "Computer Science & IT",
            code = "CS-09",
            description = "Information technology, digital documentation, spreadsheets, Python, and cyber ethics",
            iconCategory = "cs",
            chapters = listOf(
                ChapterInfo("c9_cs_ch1", 1, "Computer Systems & Architecture", 40, "Hardware components, memory hierarchy (RAM, ROM, Cache, Secondary), input/output units, operating system functions.", listOf(
                    ChapterHighlight("Von Neumann Architecture", "Architecture", "Central Processing Unit (ALU + Control Unit), primary memory, and I/O connected via system bus.")
                )),
                ChapterInfo("c9_cs_ch2", 2, "Communication & Networking Basics", 45, "LAN, MAN, WAN, network topologies, client-server model, internet, web browsers, search engines, cloud storage.", listOf(
                    ChapterHighlight("Network Topologies", "Networking", "Star, Bus, Ring, and Mesh topologies governing data transmission efficiency and redundancy.")
                )),
                ChapterInfo("c9_cs_ch3", 3, "Digital Documentation & Presentation", 40, "Word processing tools, paragraph styles, table creation, slide transitions, master slides, animations.", listOf(
                    ChapterHighlight("Master Slide", "Concept", "Specifies universal styling, typography, and layout across all slides in a presentation.")
                )),
                ChapterInfo("c9_cs_ch4", 4, "Electronic Spreadsheets", 45, "Cell referencing (relative, absolute, mixed), mathematical formulas, statistical functions (SUM, AVERAGE, IF), charts.", listOf(
                    ChapterHighlight("Absolute Referencing", "Spreadsheet Rule", "\$A\$1 locks column and row when formulas are copied across cells.")
                )),
                ChapterInfo("c9_cs_ch5", 5, "Introduction to Python Programming", 55, "Variables, data types (int, float, str, bool), operators, conditionals (if-elif-else), loops (for, while).", listOf(
                    ChapterHighlight("Python Loop", "Syntax", "for i in range(1, 10, 2): iterates with start, stop, and step parameters.")
                )),
                ChapterInfo("c9_cs_ch6", 6, "Cyber Safety & Digital Ethics", 40, "Safe browsing practices, identity theft, malware, phishing, strong passwords, netiquette, digital footprint.", listOf(
                    ChapterHighlight("Digital Footprint", "Cyber Safety", "The traceable trail of data created while using the internet and digital communications.")
                ))
            )
        )
    }
}
