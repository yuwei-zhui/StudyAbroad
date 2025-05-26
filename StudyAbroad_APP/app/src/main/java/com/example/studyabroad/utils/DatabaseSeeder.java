package com.example.studyabroad.utils;

import android.content.Context;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.util.Log;

import com.example.studyabroad.database.AppDatabase;
import com.example.studyabroad.database.entity.University;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Database seeding utility class
 * Used to add sample university and program data to Room database
 */
public class DatabaseSeeder {
    private static final String TAG = "DatabaseSeeder";
    
    /**
     * Initialize database with sample data
     * @param context Application context
     */
    public static void seedDatabase(Context context) {
        Log.d(TAG, "Starting database initialization with sample data");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            
            // Check if database already has data
            int universityCount = db.universityDao().getUniversityCount();
            Log.d(TAG, "Current university count in database: " + universityCount);
            
            if (universityCount > 0) {
                Log.d(TAG, "Database already initialized, skipping seeding process");
                return;
            }
            
            // Add university data
            List<University> universities = createSampleUniversities();
            Log.d(TAG, "Created " + universities.size() + " sample universities");
            
            List<Long> universityIds = db.universityDao().insertAll(universities);
            Log.d(TAG, "Successfully inserted university data, IDs: " + universityIds);
            
            // Add program data
            List<com.example.studyabroad.database.entity.Program> programs = createSamplePrograms(universityIds);
            Log.d(TAG, "Created " + programs.size() + " sample programs");
            
            List<Long> programIds = db.programDao().insertAll(programs);
            Log.d(TAG, "Successfully inserted program data, total: " + programIds.size());
            
            Log.d(TAG, "Database initialization successful");
        });
        executor.shutdown();
    }
    
    /**
     * Force reseed database (clears existing data and reseeds)
     * @param context Application context
     */
    public static void forceReseedDatabase(Context context) {
        Log.d(TAG, "Force reseeding database");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            
            // Clear existing data
            db.programDao().deleteAll();
            db.universityDao().deleteAll();
            Log.d(TAG, "Cleared existing university and program data");
            
            // Reseed data
            List<University> universities = createSampleUniversities();
            Log.d(TAG, "Created " + universities.size() + " sample universities");
            
            List<Long> universityIds = db.universityDao().insertAll(universities);
            Log.d(TAG, "Successfully inserted university data, IDs: " + universityIds);
            
            // Add program data
            List<com.example.studyabroad.database.entity.Program> programs = createSamplePrograms(universityIds);
            Log.d(TAG, "Created " + programs.size() + " sample programs");
            
            List<Long> programIds = db.programDao().insertAll(programs);
            Log.d(TAG, "Successfully inserted program data, total: " + programIds.size());
            
            Log.d(TAG, "Database force reseed completed successfully");
        });
        executor.shutdown();
    }
    
    /**
     * Create sample university data
     */
    private static List<University> createSampleUniversities() {
        List<University> universities = new ArrayList<>();
        
        // The University of Melbourne
        University melbourne = new University("The University of Melbourne", "Australia", 13);
        melbourne.setCity("Melbourne");
        melbourne.setRegion("Victoria");
        melbourne.setWebsite("unimelb.edu.au");
        melbourne.setLogoUrl("https://upload.wikimedia.org/wikipedia/en/1/10/University_of_Melbourne_logo.svg");
        melbourne.setDescription("The University of Melbourne is a public research university located in Melbourne, Australia. Founded in 1853, it is Australia's second oldest university and the oldest in Victoria.");
        melbourne.setMainImageUrl("https://live.staticflickr.com/997/42350121561_e618b30df1_b.jpg");
        melbourne.setFoundedYear("1853");
        melbourne.setTotalStudents(52000);
        melbourne.setInternationalStudents(19000);
        melbourne.setCampusSize("125 hectares");
        melbourne.setHasScholarships(true);
        universities.add(melbourne);
        
        // The University of Sydney
        University sydney = new University("The University of Sydney", "Australia", 19);
        sydney.setCity("Sydney");
        sydney.setRegion("New South Wales");
        sydney.setWebsite("sydney.edu.au");
        sydney.setLogoUrl("https://upload.wikimedia.org/wikipedia/en/0/0b/University_of_Sydney.svg");
        sydney.setDescription("The University of Sydney is a public research university located in Sydney, Australia. Founded in 1850, it is Australia's first university and is regarded as one of the world's leading universities.");
        sydney.setMainImageUrl("https://live.staticflickr.com/2917/14539062766_913e0d58eb_b.jpg");
        sydney.setFoundedYear("1850");
        sydney.setTotalStudents(73000);
        sydney.setInternationalStudents(24000);
        sydney.setCampusSize("72 hectares");
        sydney.setHasScholarships(true);
        universities.add(sydney);
        

        
        // University of Cambridge, UK
        University cambridge = new University("University of Cambridge", "United Kingdom", 2);
        cambridge.setCity("Cambridge");
        cambridge.setRegion("England");
        cambridge.setWebsite("cam.ac.uk");
        cambridge.setLogoUrl("https://upload.wikimedia.org/wikipedia/commons/7/7a/University_of_Cambridge_coat_of_arms.svg");
        cambridge.setDescription("The University of Cambridge is a collegiate research university in Cambridge, United Kingdom. Founded in 1209, it is the world's third-oldest university in continuous operation.");
        cambridge.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/2/29/Kings_College_Chapel_Cambridge.jpg");
        cambridge.setFoundedYear("1209");
        cambridge.setTotalStudents(24450);
        cambridge.setInternationalStudents(11820);
        cambridge.setCampusSize("288 hectares");
        cambridge.setHasScholarships(true);
        universities.add(cambridge);
        
        // Tsinghua University, China
        University tsinghua = new University("Tsinghua University", "China", 14);
        tsinghua.setCity("Beijing");
        tsinghua.setRegion("Beijing");
        tsinghua.setWebsite("tsinghua.edu.cn");
        tsinghua.setLogoUrl("https://upload.wikimedia.org/wikipedia/en/f/ff/Tsinghua_University_Logo.svg");
        tsinghua.setDescription("Tsinghua University is a major public research university in Beijing, China, and a member of the elite C9 League of Chinese universities. Since its establishment in 1911, it has produced many notable leaders in science, engineering, politics, business, academia, and culture.");
        tsinghua.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/b/bd/Tsinghua_University_20161115.jpg");
        tsinghua.setFoundedYear("1911");
        tsinghua.setTotalStudents(36300);
        tsinghua.setInternationalStudents(4000);
        tsinghua.setCampusSize("395 hectares");
        tsinghua.setHasScholarships(true);
        universities.add(tsinghua);
        
        // Imperial College London, UK
        University imperial = new University("Imperial College London", "United Kingdom", 8);
        imperial.setCity("London");
        imperial.setRegion("England");
        imperial.setWebsite("imperial.ac.uk");
        imperial.setLogoUrl("https://upload.wikimedia.org/wikipedia/en/f/f7/Imperial_College_London_Logo.svg");
        imperial.setDescription("Imperial College London is a public research university in London, United Kingdom. Its history began with Prince Albert's vision of an area for culture, including the Royal Albert Hall, Victoria & Albert Museum, Natural History Museum and several royal colleges.");
        imperial.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/c/c9/Imperial_College_London_Exhibition_Road_entrance.jpg");
        imperial.setFoundedYear("1907");
        imperial.setTotalStudents(19000);
        imperial.setInternationalStudents(9500);
        imperial.setCampusSize("5.3 hectares");
        imperial.setHasScholarships(true);
        universities.add(imperial);
        
        // Stanford University, USA
        University stanford = new University("Stanford University", "United States", 3);
        stanford.setCity("Stanford");
        stanford.setRegion("California");
        stanford.setWebsite("stanford.edu");
        stanford.setLogoUrl("https://upload.wikimedia.org/wikipedia/commons/b/b5/Seal_of_Leland_Stanford_Junior_University.svg");
        stanford.setDescription("Stanford University is a private research university in Stanford, California. Stanford was founded in 1885 by Leland and Jane Stanford in memory of their only child, Leland Stanford Jr., who had died of typhoid fever at age 15 the previous year.");
        stanford.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/f/f8/Hoover_Tower_Stanford_University.jpg");
        stanford.setFoundedYear("1885");
        stanford.setTotalStudents(17000);
        stanford.setInternationalStudents(3600);
        stanford.setCampusSize("8180 acres");
        stanford.setHasScholarships(true);
        universities.add(stanford);
        
        // University of Toronto, Canada
        University toronto = new University("University of Toronto", "Canada", 21);
        toronto.setCity("Toronto");
        toronto.setRegion("Ontario");
        toronto.setWebsite("utoronto.ca");
        toronto.setLogoUrl("https://upload.wikimedia.org/wikipedia/en/0/04/University_of_Toronto_coat_of_arms.svg");
        toronto.setDescription("The University of Toronto is a public research university in Toronto, Ontario, Canada, located on the grounds that surround Queen's Park. It was founded by royal charter in 1827 as King's College, the first institution of higher learning in Upper Canada.");
        toronto.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/2/2c/University_College%2C_University_of_Toronto.jpg");
        toronto.setFoundedYear("1827");
        toronto.setTotalStudents(97000);
        toronto.setInternationalStudents(23000);
        toronto.setCampusSize("71 hectares");
        toronto.setHasScholarships(true);
        universities.add(toronto);
        
        // National University of Singapore
        University nus = new University("National University of Singapore", "Singapore", 11);
        nus.setCity("Singapore");
        nus.setRegion("Singapore");
        nus.setWebsite("nus.edu.sg");
        nus.setLogoUrl("https://upload.wikimedia.org/wikipedia/en/b/b9/NUS_coat_of_arms.svg");
        nus.setDescription("The National University of Singapore is a national public research university in Singapore. Founded in 1905 as the Straits Settlements and Federated Malay States Government Medical School, NUS is the oldest higher education institution in Singapore.");
        nus.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/a/a6/NUS_University_Cultural_Centre.jpg");
        nus.setFoundedYear("1905");
        nus.setTotalStudents(40000);
        nus.setInternationalStudents(12000);
        nus.setCampusSize("150 hectares");
        nus.setHasScholarships(true);
        universities.add(nus);
        
        // The University of Hong Kong
        University hku = new University("The University of Hong Kong", "Hong Kong", 26);
        hku.setCity("Hong Kong");
        hku.setRegion("Hong Kong Island");
        hku.setWebsite("hku.hk");
        hku.setLogoUrl("https://upload.wikimedia.org/wikipedia/en/8/8a/HKU_Logo.svg");
        hku.setDescription("The University of Hong Kong is a public research university in Hong Kong. Founded in 1911, its origins trace back to the Hong Kong College of Medicine for Chinese, which was founded in 1887.");
        hku.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/1/1f/HKU_Main_Building.jpg");
        hku.setFoundedYear("1911");
        hku.setTotalStudents(31000);
        hku.setInternationalStudents(11000);
        hku.setCampusSize("16.3 hectares");
        hku.setHasScholarships(true);
        universities.add(hku);
        
        // Technical University of Munich, Germany
        University tum = new University("Technical University of Munich", "Germany", 37);
        tum.setCity("Munich");
        tum.setRegion("Bavaria");
        tum.setWebsite("tum.de");
        tum.setLogoUrl("https://upload.wikimedia.org/wikipedia/commons/c/c8/Logo_of_the_Technical_University_of_Munich.svg");
        tum.setDescription("The Technical University of Munich is a public research university in Munich, with additional campuses in Garching, and Freising-Weihenstephan. It specializes in engineering, technology, medicine, and applied and natural sciences.");
        tum.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/d/d2/TU_M%C3%BCnchen_Hauptgeb%C3%A4ude.jpg");
        tum.setFoundedYear("1868");
        tum.setTotalStudents(45000);
        tum.setInternationalStudents(11000);
        tum.setCampusSize("565 hectares");
        tum.setHasScholarships(true);
        universities.add(tum);
        
        // Harvard University, USA
        University harvard = new University("Harvard University", "United States", 4);
        harvard.setCity("Cambridge");
        harvard.setRegion("Massachusetts");
        harvard.setWebsite("harvard.edu");
        harvard.setLogoUrl("https://upload.wikimedia.org/wikipedia/en/2/29/Harvard_shield_wreath.svg");
        harvard.setDescription("Harvard University is a private Ivy League research university in Cambridge, Massachusetts. Founded in 1636, Harvard is the oldest institution of higher education in the United States.");
        harvard.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/c/cc/Harvard_University_campus.jpg");
        harvard.setFoundedYear("1636");
        harvard.setTotalStudents(23000);
        harvard.setInternationalStudents(5000);
        harvard.setCampusSize("209 acres");
        harvard.setHasScholarships(true);
        universities.add(harvard);
        
        // University of Oxford, UK
        University oxford = new University("University of Oxford", "United Kingdom", 5);
        oxford.setCity("Oxford");
        oxford.setRegion("England");
        oxford.setWebsite("ox.ac.uk");
        oxford.setLogoUrl("https://upload.wikimedia.org/wikipedia/commons/f/ff/Oxford-University-Circlet.svg");
        oxford.setDescription("The University of Oxford is a collegiate research university in Oxford, England. There is evidence of teaching as early as 1096, making it the oldest university in the English-speaking world.");
        oxford.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/9/9d/Oxford_High_Street.jpg");
        oxford.setFoundedYear("1096");
        oxford.setTotalStudents(24000);
        oxford.setInternationalStudents(12000);
        oxford.setCampusSize("Distributed across city");
        oxford.setHasScholarships(true);
        universities.add(oxford);
        
        // California Institute of Technology, USA
        University caltech = new University("California Institute of Technology", "United States", 6);
        caltech.setCity("Pasadena");
        caltech.setRegion("California");
        caltech.setWebsite("caltech.edu");
        caltech.setLogoUrl("https://upload.wikimedia.org/wikipedia/en/a/a4/Seal_of_the_California_Institute_of_Technology.svg");
        caltech.setDescription("The California Institute of Technology is a private research university in Pasadena, California. Known for its strength in science and engineering, Caltech is one of the most selective institutions in the world.");
        caltech.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/3/3b/Caltech_Beckman_Institute.jpg");
        caltech.setFoundedYear("1891");
        caltech.setTotalStudents(2400);
        caltech.setInternationalStudents(600);
        caltech.setCampusSize("124 acres");
        caltech.setHasScholarships(true);
        universities.add(caltech);
        
        // University College London, UK
        University ucl = new University("University College London", "United Kingdom", 9);
        ucl.setCity("London");
        ucl.setRegion("England");
        ucl.setWebsite("ucl.ac.uk");
        ucl.setLogoUrl("https://upload.wikimedia.org/wikipedia/en/5/5f/University_College_London_logo.svg");
        ucl.setDescription("University College London is a public research university in London, England. It is a member institution of the federal University of London, and is the second-largest university in the United Kingdom by total enrolment.");
        ucl.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/c/c1/UCL_Portico_Building.jpg");
        ucl.setFoundedYear("1826");
        ucl.setTotalStudents(42000);
        ucl.setInternationalStudents(19000);
        ucl.setCampusSize("16 hectares");
        ucl.setHasScholarships(true);
        universities.add(ucl);
        
        // ETH Zurich, Switzerland
        University ethz = new University("ETH Zurich", "Switzerland", 7);
        ethz.setCity("Zurich");
        ethz.setRegion("Zurich");
        ethz.setWebsite("ethz.ch");
        ethz.setLogoUrl("https://upload.wikimedia.org/wikipedia/commons/d/d4/ETH_Zurich_Logo.svg");
        ethz.setDescription("ETH Zurich is a public research university in the city of Zurich, Switzerland. Founded by the Swiss Federal Government in 1855, it was modeled on the École Polytechnique in Paris.");
        ethz.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/4/4f/ETH_Zurich_main_building.jpg");
        ethz.setFoundedYear("1855");
        ethz.setTotalStudents(22000);
        ethz.setInternationalStudents(8000);
        ethz.setCampusSize("33 hectares");
        ethz.setHasScholarships(true);
        universities.add(ethz);
        
        // University of Chicago, USA
        University chicago = new University("University of Chicago", "United States", 10);
        chicago.setCity("Chicago");
        chicago.setRegion("Illinois");
        chicago.setWebsite("uchicago.edu");
        chicago.setLogoUrl("https://upload.wikimedia.org/wikipedia/en/4/48/University_of_Chicago_shield.svg");
        chicago.setDescription("The University of Chicago is a private research university in Chicago, Illinois. Founded in 1890, its main campus is located in Chicago's Hyde Park neighborhood.");
        chicago.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/e/e1/University_of_Chicago_campus.jpg");
        chicago.setFoundedYear("1890");
        chicago.setTotalStudents(17000);
        chicago.setInternationalStudents(4000);
        chicago.setCampusSize("217 acres");
        chicago.setHasScholarships(true);
        universities.add(chicago);
        
        // University of Pennsylvania, USA
        University upenn = new University("University of Pennsylvania", "United States", 12);
        upenn.setCity("Philadelphia");
        upenn.setRegion("Pennsylvania");
        upenn.setWebsite("upenn.edu");
        upenn.setLogoUrl("https://upload.wikimedia.org/wikipedia/commons/9/92/UPenn_shield_with_banner.svg");
        upenn.setDescription("The University of Pennsylvania is a private Ivy League research university in Philadelphia, Pennsylvania. The university claims a founding date of 1740 and is one of the nine colonial colleges chartered prior to the U.S. Declaration of Independence.");
        upenn.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/6/6a/College_Hall_University_of_Pennsylvania.jpg");
        upenn.setFoundedYear("1740");
        upenn.setTotalStudents(25000);
        upenn.setInternationalStudents(5500);
        upenn.setCampusSize("299 acres");
        upenn.setHasScholarships(true);
        universities.add(upenn);
        
        // Yale University, USA
        University yale = new University("Yale University", "United States", 16);
        yale.setCity("New Haven");
        yale.setRegion("Connecticut");
        yale.setWebsite("yale.edu");
        yale.setLogoUrl("https://upload.wikimedia.org/wikipedia/commons/0/07/Yale_University_Shield_1.svg");
        yale.setDescription("Yale University is a private Ivy League research university in New Haven, Connecticut. Founded in 1701, Yale is the third-oldest institution of higher education in the United States.");
        yale.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/b/bd/Yale_University_Old_Campus.jpg");
        yale.setFoundedYear("1701");
        yale.setTotalStudents(13000);
        yale.setInternationalStudents(2800);
        yale.setCampusSize("310 acres");
        yale.setHasScholarships(true);
        universities.add(yale);
        
        // University of Edinburgh, UK
        University edinburgh = new University("University of Edinburgh", "United Kingdom", 22);
        edinburgh.setCity("Edinburgh");
        edinburgh.setRegion("Scotland");
        edinburgh.setWebsite("ed.ac.uk");
        edinburgh.setLogoUrl("https://upload.wikimedia.org/wikipedia/en/7/7c/University_of_Edinburgh_coat_of_arms.svg");
        edinburgh.setDescription("The University of Edinburgh is a public research university in Edinburgh, Scotland. Founded in 1583, it is the sixth-oldest university in the English-speaking world and one of Scotland's ancient universities.");
        edinburgh.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/f/f9/Old_College%2C_University_of_Edinburgh.jpg");
        edinburgh.setFoundedYear("1583");
        edinburgh.setTotalStudents(47000);
        edinburgh.setInternationalStudents(19000);
        edinburgh.setCampusSize("Distributed across city");
        edinburgh.setHasScholarships(true);
        universities.add(edinburgh);
        
        // Northwestern University, USA
        University northwestern = new University("Northwestern University", "United States", 47);
        northwestern.setCity("Evanston");
        northwestern.setRegion("Illinois");
        northwestern.setWebsite("northwestern.edu");
        northwestern.setLogoUrl("https://upload.wikimedia.org/wikipedia/commons/d/d9/Northwestern_University_seal.svg");
        northwestern.setDescription("Northwestern University is a private research university in Evanston, Illinois. Founded in 1851, Northwestern is known for its strong programs in journalism, business, engineering, and medicine.");
        northwestern.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/8/8a/Northwestern_University_campus.jpg");
        northwestern.setFoundedYear("1851");
        northwestern.setTotalStudents(22000);
        northwestern.setInternationalStudents(4500);
        northwestern.setCampusSize("240 acres");
        northwestern.setHasScholarships(true);
        universities.add(northwestern);
        
        // University of California, Los Angeles, USA
        University ucla = new University("University of California, Los Angeles", "United States", 29);
        ucla.setCity("Los Angeles");
        ucla.setRegion("California");
        ucla.setWebsite("ucla.edu");
        ucla.setLogoUrl("https://upload.wikimedia.org/wikipedia/commons/0/0d/The_University_of_California_UCLA.svg");
        ucla.setDescription("UCLA is a public land-grant research university in Los Angeles, California. Founded in 1919, UCLA is the largest university in the state of California and one of the most applied-to universities in the United States.");
        ucla.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/f/f8/UCLA_campus_aerial.jpg");
        ucla.setFoundedYear("1919");
        ucla.setTotalStudents(47000);
        ucla.setInternationalStudents(12000);
        ucla.setCampusSize("419 acres");
        ucla.setHasScholarships(true);
        universities.add(ucla);
        
        // McGill University, Canada
        University mcgill = new University("McGill University", "Canada", 30);
        mcgill.setCity("Montreal");
        mcgill.setRegion("Quebec");
        mcgill.setWebsite("mcgill.ca");
        mcgill.setLogoUrl("https://upload.wikimedia.org/wikipedia/en/thumb/2/29/McGill_University_CoA.svg/150px-McGill_University_CoA.svg.png");
        mcgill.setDescription("McGill University is a public research university located in Montreal, Quebec, Canada. Founded in 1821, McGill is one of Canada's most prestigious universities and is known for its strong research programs.");
        mcgill.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/c/c4/McGill_University_Arts_Building.jpg");
        mcgill.setFoundedYear("1821");
        mcgill.setTotalStudents(40000);
        mcgill.setInternationalStudents(12000);
        mcgill.setCampusSize("80 hectares");
        mcgill.setHasScholarships(true);
        universities.add(mcgill);
        
        // University of Manchester, UK
        University manchester = new University("University of Manchester", "United Kingdom", 32);
        manchester.setCity("Manchester");
        manchester.setRegion("England");
        manchester.setWebsite("manchester.ac.uk");
        manchester.setLogoUrl("https://upload.wikimedia.org/wikipedia/en/5/5e/University_of_Manchester_logo.svg");
        manchester.setDescription("The University of Manchester is a public research university in Manchester, England. Formed in 2004 by the merger of the University of Manchester Institute of Science and Technology and the Victoria University of Manchester, it is a red brick university and a founding member of the Russell Group.");
        manchester.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/a/a8/University_of_Manchester_campus.jpg");
        manchester.setFoundedYear("2004");
        manchester.setTotalStudents(40000);
        manchester.setInternationalStudents(12000);
        manchester.setCampusSize("270 hectares");
        manchester.setHasScholarships(true);
        universities.add(manchester);
        
        // Australian National University
        University anu = new University("Australian National University", "Australia", 34);
        anu.setCity("Canberra");
        anu.setRegion("Australian Capital Territory");
        anu.setWebsite("anu.edu.au");
        anu.setLogoUrl("https://upload.wikimedia.org/wikipedia/en/1/1c/Australian_National_University_logo.svg");
        anu.setDescription("The Australian National University is a public research university located in Canberra, the capital of Australia. Founded in 1946, ANU is the only university to have been created by the Parliament of Australia.");
        anu.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/8/8c/ANU_campus.jpg");
        anu.setFoundedYear("1946");
        anu.setTotalStudents(25000);
        anu.setInternationalStudents(11000);
        anu.setCampusSize("145 hectares");
        anu.setHasScholarships(true);
        universities.add(anu);
        
        // University of British Columbia, Canada
        University ubc = new University("University of British Columbia", "Canada", 34);
        ubc.setCity("Vancouver");
        ubc.setRegion("British Columbia");
        ubc.setWebsite("ubc.ca");
        ubc.setLogoUrl("https://upload.wikimedia.org/wikipedia/en/5/50/University_of_British_Columbia_coat_of_arms.svg");
        ubc.setDescription("The University of British Columbia is a public research university with campuses near Vancouver and in Kelowna, British Columbia. Established in 1908, UBC is British Columbia's oldest university.");
        ubc.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/f/f4/UBC_campus_aerial.jpg");
        ubc.setFoundedYear("1908");
        ubc.setTotalStudents(65000);
        ubc.setInternationalStudents(17000);
        ubc.setCampusSize("402 hectares");
        ubc.setHasScholarships(true);
        universities.add(ubc);
        
        // New York University, USA
        University nyu = new University("New York University", "United States", 38);
        nyu.setCity("New York");
        nyu.setRegion("New York");
        nyu.setWebsite("nyu.edu");
        nyu.setLogoUrl("https://upload.wikimedia.org/wikipedia/commons/0/0c/New_York_University_Logo.svg");
        nyu.setDescription("New York University is a private research university in New York City. Founded in 1831, NYU is one of the largest private universities in the United States by enrollment.");
        nyu.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/a/a5/NYU_Washington_Square_Park.jpg");
        nyu.setFoundedYear("1831");
        nyu.setTotalStudents(51000);
        nyu.setInternationalStudents(19000);
        nyu.setCampusSize("Urban campus");
        nyu.setHasScholarships(true);
        universities.add(nyu);
        
        // King's College London, UK
        University kcl = new University("King's College London", "United Kingdom", 40);
        kcl.setCity("London");
        kcl.setRegion("England");
        kcl.setWebsite("kcl.ac.uk");
        kcl.setLogoUrl("https://upload.wikimedia.org/wikipedia/en/4/4e/Kings_College_London_logo.svg");
        kcl.setDescription("King's College London is a public research university located in London, England. Founded in 1829, King's is one of the oldest universities in England and a founding college of the University of London.");
        kcl.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/d/d8/Kings_College_London_Strand_Campus.jpg");
        kcl.setFoundedYear("1829");
        kcl.setTotalStudents(35000);
        kcl.setInternationalStudents(14000);
        kcl.setCampusSize("Multiple campuses across London");
        kcl.setHasScholarships(true);
        universities.add(kcl);
        
        // Seoul National University, South Korea
        University snu = new University("Seoul National University", "South Korea", 41);
        snu.setCity("Seoul");
        snu.setRegion("Seoul");
        snu.setWebsite("snu.ac.kr");
        snu.setLogoUrl("https://upload.wikimedia.org/wikipedia/en/4/4e/Seoul_National_University_logo.svg");
        snu.setDescription("Seoul National University is a national public research university located in Seoul, South Korea. Founded in 1946, SNU is widely considered to be the most prestigious university in South Korea.");
        snu.setMainImageUrl("https://upload.wikimedia.org/wikipedia/commons/8/8a/Seoul_National_University_campus.jpg");
        snu.setFoundedYear("1946");
        snu.setTotalStudents(28000);
        snu.setInternationalStudents(4000);
        snu.setCampusSize("200 hectares");
        snu.setHasScholarships(true);
        universities.add(snu);
            
            return universities;
    }
    
    /**
     * Create sample program data
     */
    private static List<com.example.studyabroad.database.entity.Program> createSamplePrograms(List<Long> universityIds) {
        List<com.example.studyabroad.database.entity.Program> programs = new ArrayList<>();
        
        // The University of Melbourne programs (Real data from official website)
        long melbourneId = universityIds.get(0);
        
        // Melbourne Computer Science Master - Updated with real data
        com.example.studyabroad.database.entity.Program melbourneCS = new com.example.studyabroad.database.entity.Program();
        melbourneCS.setUniversityId(melbourneId);
        melbourneCS.setName("Master of Computer Science");
        melbourneCS.setCategory("Computer Science");
        melbourneCS.setDegreeLevel("Master");
        melbourneCS.setDuration("2 years full time / 4 years part time");
        melbourneCS.setMode("On campus");
        melbourneCS.setLocation("Parkville");
        melbourneCS.setIntakeDate("March");
        melbourneCS.setApplicationDeadline("31 August 2025");
        melbourneCS.setTuitionFee(62566); // Updated with real international fee
        melbourneCS.setCurrency("AUD");
        melbourneCS.setLanguage("English");
        melbourneCS.setDescription("The Master of Computer Science is designed for students with a bachelor degree in any area who want to develop specialised IT skills. This research training program gives students the opportunity to undertake a year-long research project in a field of choice.");
        melbourneCS.setMinGPA(4.0);
        melbourneCS.setEntryRequirements("Bachelor's degree in any area with a weighted average mark of at least 65% (or equivalent). Commonwealth Supported Places available for domestic students.");
        melbourneCS.setCareerProspects("Graduates will be prepared for roles as software architects, data scientists, software engineers, and IT consultants in high-level tech companies and R&D labs.");
        melbourneCS.setScholarshipAvailable(true);
        melbourneCS.setFacultyName("Faculty of Engineering and Information Technology");
        melbourneCS.setProgramUrl("study.unimelb.edu.au/find/courses/graduate/master-of-computer-science");
        programs.add(melbourneCS);
        
        // Melbourne Information Technology Master - Updated with real data
        com.example.studyabroad.database.entity.Program melbourneIT = new com.example.studyabroad.database.entity.Program();
        melbourneIT.setUniversityId(melbourneId);
        melbourneIT.setName("Master of Information Technology");
        melbourneIT.setCategory("Information Technology");
        melbourneIT.setDegreeLevel("Master");
        melbourneIT.setDuration("2 years full time / 4 years part time");
        melbourneIT.setMode("On campus");
        melbourneIT.setLocation("Parkville");
        melbourneIT.setIntakeDate("March");
        melbourneIT.setApplicationDeadline("31 August 2025");
        melbourneIT.setTuitionFee(90250); // Real international fee
        melbourneIT.setCurrency("AUD");
        melbourneIT.setLanguage("English");
        melbourneIT.setDescription("The Master of Information Technology is designed for students with a bachelor degree in any area who want to learn about information technology and develop practical IT skills.");
        melbourneIT.setMinGPA(4.0);
        melbourneIT.setEntryRequirements("Bachelor's degree in any area with a weighted average mark of at least 65% (or equivalent)");
        melbourneIT.setCareerProspects("Graduates will be prepared for roles as IT project managers, business analysts, and IT consultants.");
        melbourneIT.setScholarshipAvailable(true);
        melbourneIT.setFacultyName("Faculty of Engineering and Information Technology");
        melbourneIT.setProgramUrl("study.unimelb.edu.au/find/courses/graduate/master-of-information-technology");
        programs.add(melbourneIT);
        
        // Melbourne Business Analytics Master - Updated with real data
        com.example.studyabroad.database.entity.Program melbourneBA = new com.example.studyabroad.database.entity.Program();
        melbourneBA.setUniversityId(melbourneId);
        melbourneBA.setName("Master of Business Analytics");
        melbourneBA.setCategory("Business Analytics");
        melbourneBA.setDegreeLevel("Master");
        melbourneBA.setDuration("1.5 years full time");
        melbourneBA.setMode("On campus");
        melbourneBA.setLocation("Parkville");
        melbourneBA.setIntakeDate("March/July");
        melbourneBA.setApplicationDeadline("31 August 2025");
        melbourneBA.setTuitionFee(88000);
        melbourneBA.setCurrency("AUD");
        melbourneBA.setLanguage("English");
        melbourneBA.setDescription("The Master of Business Analytics combines business knowledge with analytical skills to solve complex business problems using data-driven approaches.");
        melbourneBA.setMinGPA(4.0);
        melbourneBA.setEntryRequirements("Bachelor's degree with strong quantitative background");
        melbourneBA.setCareerProspects("Graduates work as business analysts, data scientists, and analytics consultants in various industries.");
        melbourneBA.setScholarshipAvailable(true);
        melbourneBA.setFacultyName("Melbourne Business School");
        melbourneBA.setProgramUrl("study.unimelb.edu.au/find/courses/graduate/master-of-business-analytics");
        programs.add(melbourneBA);
        
        // University of Sydney programs - Updated with real data
        if (universityIds.size() > 1) {
            long sydneyId = universityIds.get(1);
            
            com.example.studyabroad.database.entity.Program sydneyCS = new com.example.studyabroad.database.entity.Program();
            sydneyCS.setUniversityId(sydneyId);
            sydneyCS.setName("Master of Computer Science");
            sydneyCS.setCategory("Computer Science");
            sydneyCS.setDegreeLevel("Master");
            sydneyCS.setDuration("1.5 years full time");
            sydneyCS.setMode("On campus");
            sydneyCS.setLocation("Camperdown/Darlington");
            sydneyCS.setIntakeDate("March/July");
            sydneyCS.setApplicationDeadline("30 November 2025");
            sydneyCS.setTuitionFee(88000); // Real international fee
            sydneyCS.setCurrency("AUD");
            sydneyCS.setLanguage("English");
            sydneyCS.setDescription("The Master of Computer Science provides you with the knowledge to design computer systems and applications, with specializations in AI, cybersecurity, and software engineering.");
            sydneyCS.setMinGPA(4.0);
            sydneyCS.setEntryRequirements("Bachelor's degree in Computer Science or related field with credit average (65%) or equivalent");
            sydneyCS.setCareerProspects("Graduates will be prepared for roles as software developers, system architects, and technical leads in leading technology companies.");
            sydneyCS.setScholarshipAvailable(true);
            sydneyCS.setFacultyName("Faculty of Engineering");
            sydneyCS.setProgramUrl("sydney.edu.au/courses/courses/pc/master-of-computer-science.html");
            programs.add(sydneyCS);
        }
        
        // University of Cambridge programs - Updated with real data
        if (universityIds.size() > 2) {
            long cambridgeId = universityIds.get(2);
            
            com.example.studyabroad.database.entity.Program cambridgeCS = new com.example.studyabroad.database.entity.Program();
            cambridgeCS.setUniversityId(cambridgeId);
            cambridgeCS.setName("MPhil in Advanced Computer Science");
            cambridgeCS.setCategory("Computer Science");
            cambridgeCS.setDegreeLevel("Master");
            cambridgeCS.setDuration("1 year full time");
            cambridgeCS.setMode("On campus");
            cambridgeCS.setLocation("Cambridge");
            cambridgeCS.setIntakeDate("October");
            cambridgeCS.setApplicationDeadline("2 January 2025");
            cambridgeCS.setTuitionFee(37000); // Real international fee
            cambridgeCS.setCurrency("GBP");
            cambridgeCS.setLanguage("English");
            cambridgeCS.setDescription("The MPhil in Advanced Computer Science is designed to give students with a first degree in Computer Science the opportunity to study advanced topics and undertake a research project.");
            cambridgeCS.setMinGPA(4.5);
            cambridgeCS.setEntryRequirements("First-class honours degree in Computer Science or related subject from a recognized university");
            cambridgeCS.setCareerProspects("Graduates pursue careers in research, leading technology companies, or continue to PhD studies at Cambridge or other top universities.");
            cambridgeCS.setScholarshipAvailable(true);
            cambridgeCS.setFacultyName("Department of Computer Science and Technology");
            cambridgeCS.setProgramUrl("cl.cam.ac.uk/admissions/mphil");
            programs.add(cambridgeCS);
            
            com.example.studyabroad.database.entity.Program cambridgeEng = new com.example.studyabroad.database.entity.Program();
            cambridgeEng.setUniversityId(cambridgeId);
            cambridgeEng.setName("MPhil in Engineering");
            cambridgeEng.setCategory("Engineering");
            cambridgeEng.setDegreeLevel("Master");
            cambridgeEng.setDuration("1 year full time");
            cambridgeEng.setMode("On campus");
            cambridgeEng.setLocation("Cambridge");
            cambridgeEng.setIntakeDate("October");
            cambridgeEng.setApplicationDeadline("2 January 2025");
            cambridgeEng.setTuitionFee(37000);
            cambridgeEng.setCurrency("GBP");
            cambridgeEng.setLanguage("English");
            cambridgeEng.setDescription("The MPhil in Engineering offers advanced study in various engineering disciplines with opportunities for research and innovation.");
            cambridgeEng.setMinGPA(4.5);
            cambridgeEng.setEntryRequirements("First-class honours degree in Engineering or related field");
            cambridgeEng.setCareerProspects("Graduates work in leading engineering firms, research institutions, or pursue PhD studies.");
            cambridgeEng.setScholarshipAvailable(true);
            cambridgeEng.setFacultyName("Department of Engineering");
            cambridgeEng.setProgramUrl("eng.cam.ac.uk/admissions/postgraduate");
            programs.add(cambridgeEng);
        }
        
        // Imperial College London programs - Updated with real data
        if (universityIds.size() > 4) {
            long imperialId = universityIds.get(4);
            
            com.example.studyabroad.database.entity.Program imperialCS = new com.example.studyabroad.database.entity.Program();
            imperialCS.setUniversityId(imperialId);
            imperialCS.setName("MSc Computing");
            imperialCS.setCategory("Computer Science");
            imperialCS.setDegreeLevel("Master");
            imperialCS.setDuration("1 year full time");
            imperialCS.setMode("On campus");
            imperialCS.setLocation("South Kensington, London");
            imperialCS.setIntakeDate("September");
            imperialCS.setApplicationDeadline("31 July 2025");
            imperialCS.setTuitionFee(43800); // Real overseas fee
            imperialCS.setCurrency("GBP");
            imperialCS.setLanguage("English");
            imperialCS.setDescription("Undertake intensive training in computer science and acquire the core computing skills needed for a career in the computer industry. Suitable for graduates of disciplines other than computing.");
            imperialCS.setMinGPA(4.0);
            imperialCS.setEntryRequirements("First-class degree in any subject outside computing or computer science, with sufficient quantitative or analytical elements");
            imperialCS.setCareerProspects("Graduates are sought after in roles such as application/web development, networking, AI, media, finance, robotics, and computer games.");
            imperialCS.setScholarshipAvailable(true);
            imperialCS.setFacultyName("Department of Computing");
            imperialCS.setProgramUrl("imperial.ac.uk/study/courses/postgraduate-taught/computing");
            programs.add(imperialCS);
            
            com.example.studyabroad.database.entity.Program imperialBioeng = new com.example.studyabroad.database.entity.Program();
            imperialBioeng.setUniversityId(imperialId);
            imperialBioeng.setName("MSc Bioengineering");
            imperialBioeng.setCategory("Bioengineering");
            imperialBioeng.setDegreeLevel("Master");
            imperialBioeng.setDuration("1 year full time");
            imperialBioeng.setMode("On campus");
            imperialBioeng.setLocation("South Kensington, London");
            imperialBioeng.setIntakeDate("September");
            imperialBioeng.setApplicationDeadline("31 March 2025");
            imperialBioeng.setTuitionFee(35900);
            imperialBioeng.setCurrency("GBP");
            imperialBioeng.setLanguage("English");
            imperialBioeng.setDescription("The MSc Bioengineering programme combines engineering principles with biological systems to develop solutions for healthcare and biotechnology.");
            imperialBioeng.setMinGPA(4.0);
            imperialBioeng.setEntryRequirements("First or upper second class honours degree in Engineering, Physics, Mathematics, or related field");
            imperialBioeng.setCareerProspects("Graduates work in medical device companies, pharmaceutical industry, or research institutions.");
            imperialBioeng.setScholarshipAvailable(true);
            imperialBioeng.setFacultyName("Department of Bioengineering");
            imperialBioeng.setProgramUrl("imperial.ac.uk/bioengineering/study/postgraduate");
            programs.add(imperialBioeng);
        }
        
        // Stanford University programs - Updated with real data
        if (universityIds.size() > 5) {
            long stanfordId = universityIds.get(5);
            
            com.example.studyabroad.database.entity.Program stanfordCS = new com.example.studyabroad.database.entity.Program();
            stanfordCS.setUniversityId(stanfordId);
            stanfordCS.setName("Master of Science in Computer Science");
            stanfordCS.setCategory("Computer Science");
            stanfordCS.setDegreeLevel("Master");
            stanfordCS.setDuration("1.5-2 years full time");
            stanfordCS.setMode("On campus");
            stanfordCS.setLocation("Stanford, California");
            stanfordCS.setIntakeDate("September");
            stanfordCS.setApplicationDeadline("TBD (typically December)");
            stanfordCS.setTuitionFee(65082); // Real 2024-25 Engineering tuition per year
            stanfordCS.setCurrency("USD");
            stanfordCS.setLanguage("English");
            stanfordCS.setDescription("Stanford's MS in Computer Science provides excellent preparation for a career as a computer professional or for future entry into a PhD program. Individual programs can be structured to consist entirely of coursework or to involve some research.");
            stanfordCS.setMinGPA(4.0);
            stanfordCS.setEntryRequirements("Bachelor's degree with strong quantitative and analytical skills. GRE not required. Typically GPAs are at least 3.5 on a 4.0 scale.");
            stanfordCS.setCareerProspects("Graduates join leading tech companies in Silicon Valley including Google, Apple, Meta, and innovative startups.");
            stanfordCS.setScholarshipAvailable(false); // Stanford does not offer financial support for MS students
            stanfordCS.setFacultyName("School of Engineering");
            stanfordCS.setProgramUrl("cs.stanford.edu/admissions/masters-admissions");
            programs.add(stanfordCS);
            
            com.example.studyabroad.database.entity.Program stanfordMBA = new com.example.studyabroad.database.entity.Program();
            stanfordMBA.setUniversityId(stanfordId);
            stanfordMBA.setName("Master of Business Administration");
            stanfordMBA.setCategory("Business Administration");
            stanfordMBA.setDegreeLevel("Master");
            stanfordMBA.setDuration("2 years full time");
            stanfordMBA.setMode("On campus");
            stanfordMBA.setLocation("Stanford, California");
            stanfordMBA.setIntakeDate("September");
            stanfordMBA.setApplicationDeadline("April 2025");
            stanfordMBA.setTuitionFee(109970); // Real 2024-25 GSB tuition
            stanfordMBA.setCurrency("USD");
            stanfordMBA.setLanguage("English");
            stanfordMBA.setDescription("Stanford MBA is a transformational journey that develops leaders who change the world. The program emphasizes entrepreneurship, innovation, and global perspective.");
            stanfordMBA.setMinGPA(4.0);
            stanfordMBA.setEntryRequirements("Bachelor's degree, GMAT/GRE scores, work experience, essays, and recommendations");
            stanfordMBA.setCareerProspects("Graduates work in consulting, technology, finance, and entrepreneurship with average starting salaries exceeding $180,000.");
            stanfordMBA.setScholarshipAvailable(true);
            stanfordMBA.setFacultyName("Graduate School of Business");
            stanfordMBA.setProgramUrl("gsb.stanford.edu/programs/mba");
            programs.add(stanfordMBA);
        }
        
        // Add more programs for other universities with real data...
        // University of Toronto programs
        if (universityIds.size() > 6) {
            long torontoId = universityIds.get(6);
            
            com.example.studyabroad.database.entity.Program torontoCS = new com.example.studyabroad.database.entity.Program();
            torontoCS.setUniversityId(torontoId);
            torontoCS.setName("Master of Science in Computer Science");
            torontoCS.setCategory("Computer Science");
            torontoCS.setDegreeLevel("Master");
            torontoCS.setDuration("2 years full time");
            torontoCS.setMode("On campus");
            torontoCS.setLocation("Toronto, Ontario");
            torontoCS.setIntakeDate("September");
            torontoCS.setApplicationDeadline("15 December 2025");
            torontoCS.setTuitionFee(58160); // Real international fee
            torontoCS.setCurrency("CAD");
            torontoCS.setLanguage("English");
            torontoCS.setDescription("The University of Toronto's computer science program is consistently ranked among the top in the world, with strengths in AI, machine learning, and systems.");
            torontoCS.setMinGPA(4.0);
            torontoCS.setEntryRequirements("Bachelor's degree in Computer Science or related field with B+ average or equivalent");
            torontoCS.setCareerProspects("Graduates work at leading tech companies including Google, Microsoft, and Amazon's Toronto offices, or pursue PhD studies.");
            torontoCS.setScholarshipAvailable(true);
            torontoCS.setFacultyName("Faculty of Arts & Science");
            torontoCS.setProgramUrl("web.cs.toronto.edu/graduate/msc");
            programs.add(torontoCS);
        }
        
        return programs;
    }
} 