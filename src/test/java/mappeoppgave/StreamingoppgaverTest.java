package mappeoppgave;

import mappeoppgave.domene.Innsjoe;
import mappeoppgave.domene.Innsjoer;
import org.hamcrest.core.StringStartsWith;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertThat;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@SuppressWarnings({"SpellCheckingInspection", "ArraysAsListWithZeroOrOneArgument"})
public class StreamingoppgaverTest {

    private final List<Innsjoe> rawDataList = Innsjoer.innsjoer;
    private List<Innsjoe> innsjoeTestList;

    @Before
    public void setup()
    {
            innsjoeTestList = new ArrayList<>();
    }
    @After
    public void teardown()
    {
        innsjoeTestList = null;
    }


    /**
     * 1. Bruk av filtering med Predicates
     */

    @Test
    public void lakesWithNamesStartingWithC () throws Exception
    {
        innsjoeTestList = Streamingoppgaver.lakesWithNamesStartingWithC(rawDataList);

        int counter = 0;
        for (Innsjoe innsjoe : rawDataList)
            if (innsjoe.navn().startsWith("C"))
                counter++;

        assertEquals(counter, innsjoeTestList.size());
    }

    @Test
    public void lakeNamecontainsMoreThanOneWord () throws Exception
    {
        innsjoeTestList = Streamingoppgaver.lakeNamecontainsMoreThanOneWord(rawDataList);

        int count = 0;
        for (Innsjoe innsjoe : rawDataList)
            if (innsjoe.navn().matches("\\w*[\\s*-]\\w*.*"))
                count++;

        assertEquals(count, innsjoeTestList.size());
    }

    @Test
    public void listOfLakesBorderingMoreThanTwoCountries () throws Exception
    {
        innsjoeTestList = Streamingoppgaver.listOfLakesBorderingMoreThanTwoCountries(rawDataList);

        int count = 0;
        for (Innsjoe innsjoe : rawDataList)
            if (innsjoe.land().size() > 2)
                count++;

        assertEquals(count, innsjoeTestList.size());
    }

    @Test
    public void lakesInEuropeSizedOver10kkmSquaredOnlyBelongsToOneCountry () throws Exception
    {
        innsjoeTestList = Streamingoppgaver.lakesInEuropeSizedOver10kkmSquaredOnlyBelongsToOneCountry(rawDataList);

        boolean checkList = true;
        for (Innsjoe innsjoe : innsjoeTestList) {
            if (innsjoe.kontinent().equalsIgnoreCase("Europe") && innsjoe.areal() < 10000.0) {
                checkList = false;
                break;
            }
        }
        assertTrue("List contains elements that does not support the logic of the method tested", checkList);
    }

    @Test
    public void firstLakeSizedOver5kSquared () throws Exception
    {
        Innsjoe firstLake5k = Streamingoppgaver.firstLakeSizedOver5kSquared(rawDataList);

        Innsjoe testTemp = null;
        for (Innsjoe innsjoe : rawDataList)
            if (innsjoe.areal() > 5000.0) {
                testTemp = innsjoe;
                break;
            }
        assertEquals(testTemp, firstLake5k);
    }

    /**
     * 2. Bruk av Map med Function
     */

    @Test
    public void listOfAllLakeNames () throws Exception
    {
        List<String> allLakeNames = Streamingoppgaver.listOfAllLakeNames(rawDataList);
        assertEquals(rawDataList.size(), allLakeNames.size());
    }

    @Test
    public void listOfAllLakeNamesInCapitalLetters () throws Exception
    {
        List<String> lakeNamesCapitalized = Streamingoppgaver.listOfAllLakeNamesInCapitalLetters(rawDataList)
                .stream().filter(s -> s.matches("\\p{Lower}+"))
                .collect(Collectors.toList());
        assertEquals(0, lakeNamesCapitalized.size());
    }

    @Test
    public void listOfAllLakesContinentsChangedToAntarctica () throws Exception
    {
        innsjoeTestList = Streamingoppgaver.listOfAllLakesContinentsChangedToAntarctica(rawDataList).stream()
                .filter(i -> !i.kontinent().equalsIgnoreCase("Antarctica"))
                .collect(Collectors.toList());

        assertEquals(0, innsjoeTestList.size(), 0);
    }

    @Test
    public void firstLakeSizedOver500kSquared () throws Exception
    {
        String bigLake = Streamingoppgaver.firstLakeSizedOver500kSquared(rawDataList);
        assertEquals(bigLake, "Nothing");
    }

    /**
     * 3. Bruk av Map, reduce og findAny
     */

    @Test
    public void findAverageArealForLakes () throws Exception
    {
        double avgArealForLakes = Streamingoppgaver.findAverageArealForLakes(rawDataList);
        assertEquals(30353.88888888889, avgArealForLakes, 0.1);
    }

    @Test
    public void longestLake () throws Exception
    {
        Innsjoe longestLake = Streamingoppgaver.longestLake(rawDataList);
        assertEquals(13700.0, longestLake.lengde(), 0);
    }

    @Test
    public void shortestLake () throws Exception
    {
        Innsjoe shortestLake = Streamingoppgaver.shortestLake(rawDataList);
        assertEquals(90.0, shortestLake.lengde(), 0);
    }

    @Test
    public void lakeWithDepthBiggerThanTenthOfLength () throws Exception
    {
        Innsjoe testLake = Streamingoppgaver.lakeWithDepthBiggerThanTenthOfLength(rawDataList);
        double testCondition = testLake.lengde()/10;
        assertTrue(testLake.maksDybde() > testCondition);
    }

    @Test
    public void averageCountriesForLakes () throws Exception
    {
        double avgCountriesForLakes = Streamingoppgaver.averageCountriesForLakes(rawDataList);
        assertEquals(1.5833333333333333, avgCountriesForLakes, 0.01);
    }

    @Test
    public void productOfAllLakeDepths () throws Exception
    {
        double productOfLakeDepths = Streamingoppgaver.productOfAllLakeDepths(rawDataList);
        assertEquals(1.0915134706186265E79, productOfLakeDepths, 0.1);
    }

    /**
     * 4. Avanserte Collectors
     */

    @Test
    public void groupLakesToContinents () throws Exception
    {
        Map<String, List<Innsjoe>> testMap = Streamingoppgaver.groupLakesToContinents(rawDataList);
        List<Innsjoe> lakesInEuropeTestList = rawDataList.stream()
                .filter(innsjoe -> innsjoe.kontinent().equalsIgnoreCase("Europe"))
                .collect(Collectors.toList());
        assertTrue(testMap.get("Europe").equals(lakesInEuropeTestList));
    }

    // TODO: This test is kinda rubbish imo, check with someone
    @Test
    public void countLakesForEachContinent () throws Exception
    {
        Map<String, Long> testMap = Streamingoppgaver.countLakesForEachContinent(rawDataList);

        assertTrue(testMap.get("Asia") == 8);
    }

    // TODO: Is this a good test? I have no idea, check with someone
    @Test
    public void stringOfLakeNamesWithSeparator () throws Exception
    {
        char[] lakeNamesWithSeparator =
                Streamingoppgaver.stringOfLakeNamesWithSeparator(rawDataList).toCharArray();

        int separatorCount = 0;
        for (char c : lakeNamesWithSeparator)
                    if(c == '|')
                        separatorCount++;

        assertNotNull(lakeNamesWithSeparator);
        assertEquals(rawDataList.size()-1, separatorCount);
    }

    @Test
    public void averageSurfaceAreaOfLakes () throws Exception
    {
        double averageSurfaceAreaOfLakes = Streamingoppgaver.averageSurfaceAreaOfLakes(rawDataList);

        assertEquals(30353.88888888889, averageSurfaceAreaOfLakes, 0.1);
    }

    // TODO: This test is kinda heavy imo, check with someone
    @Test
    public void separatingLakesBasedOn500mDepth () throws Exception
    {
        innsjoeTestList = Innsjoer.innsjoer;

        Map<Boolean, List<Innsjoe>> separatedLakesby500mDepth =
                Streamingoppgaver.separatingLakesBasedOn500mDepth(innsjoeTestList);
        List<Innsjoe> over500 = new ArrayList<>();
        List<Innsjoe> under500 = new ArrayList<>();

        for (Innsjoe i : innsjoeTestList)
        {
            if (i.maksDybde() < 500)
                under500.add(i);
            else
                over500.add(i);
        }
        assertEquals(separatedLakesby500mDepth.get(true), over500);
        assertEquals(separatedLakesby500mDepth.get(false), under500);
    }

    /**
     * 5. Flatmap
     *
     * POSSIBLE SOLUTIONS FOR BAD TESTS: use simpeList from this.Fields
     */

    @Test
    public void allCountriesInList () throws Exception
    {
        List<String> listFromMethod = Streamingoppgaver.allCountriesInList(rawDataList);
        List<String> listFromRaw = new ArrayList<>();

        for (Innsjoe innsjoe : rawDataList)
            listFromRaw.addAll(innsjoe.land());

        assertEquals(listFromMethod.size(), listFromRaw.size());

    }

    @Test
    public void allCountriesInListNoDuplicates () throws Exception
    {
        int allCountriesNoDuplicates = Streamingoppgaver.allCountriesInListNoDuplicates(rawDataList).size();

        assertEquals(25, allCountriesNoDuplicates, 0);
    }

    @Test
    public void allCountriesAndHowManyTimesTheyShowUpInList () throws Exception
    {
        Map<String, Long> allCountriesAndHowManyTimesTheyShowUp =
                Streamingoppgaver.allCountriesAndHowManyTimesTheyShowUpInList(rawDataList);

        assertEquals(allCountriesAndHowManyTimesTheyShowUp.get("Russia"), 6, 0);
    }
}