package mappeoppgave;

import mappeoppgave.domene.Innsjoe;
import mappeoppgave.domene.Innsjoer;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings({"WeakerAccess", "SpellCheckingInspection"})
public class Streamingoppgaver {

    private static List<Innsjoe> myInnsjoeList;

    /**
     * 1. Bruk av filtering med Predicates
     */

    public static List<Innsjoe> lakesWithNamesStartingWithC(List<Innsjoe> listOfLakes) {

         return listOfLakes.stream()
                .filter(i -> i.navn().startsWith("C"))
                .collect(Collectors.toList());
    }

    // TODO: Not sure about this one, check with someone
    public static List<Innsjoe> lakeNamecontainsMoreThanOneWord(List<Innsjoe> listOfLakes) {

        return listOfLakes.stream()
                .filter(i -> i.navn().matches("\\w*[\\s*-]\\w*.*"))
                .collect(Collectors.toList());
    }

    public static List<Innsjoe> listOfLakesBorderingMoreThanTwoCountries(List<Innsjoe> listOfLakes) {

        return listOfLakes.stream()
                .filter(i -> i.land().size() > 2)
                .collect(Collectors.toList());
    }

    public static List<Innsjoe> lakesInEuropeSizedOver10kkmSquaredOnlyBelongsToOneCountry(List<Innsjoe> listOfLakes) {

        return listOfLakes.stream()
                .filter(i -> i.kontinent().equalsIgnoreCase("Europe"))
                .filter(i -> i.land().size() == 1)
                .filter(i -> i.areal() > 10000.0)
                .collect(Collectors.toList());
    }

    public static Innsjoe firstLakeSizedOver5kSquared(List<Innsjoe> listOfLakes) {

        return listOfLakes.stream()
                .filter(i -> i.areal() >= 5000.0)
                .findFirst()
                .orElse(null);
    }

    /**
     * 2. Bruk av Map med Function
     */

    public static List<String> listOfAllLakeNames(List<Innsjoe> listOfLakes) {

        return listOfLakes.stream()
                .map(Innsjoe::navn)
                .collect(Collectors.toList());
    }

    public static List<String> listOfAllLakeNamesInCapitalLetters(List<Innsjoe> listOfLakes) {

        return listOfLakes.stream()
                .map(innsjoe -> innsjoe.navn().toUpperCase())
                .collect(Collectors.toList());
    }

    public static List<Innsjoe> listOfAllLakesContinentsChangedToAntarctica(List<Innsjoe> listOfLakes) {

        return listOfLakes.stream()
                .map(i -> new Innsjoe(
                        i.navn(), i.land(),
                        "Antarctica",
                        i.areal(), i.lengde(), i.maksDybde())
                ).collect(Collectors.toList());
    }

    public static String firstLakeSizedOver500kSquared(List<Innsjoe> listOfLakes) {

        return listOfLakes.stream()
                .filter(i -> i.areal() >= 500000.0)
                .map(Innsjoe::navn)
                .findFirst()
                .orElse("Nothing");
    }

    /**
     * 3. Bruk av Map, reduce og findAny
     */

    public static double findAverageArealForLakes(List<Innsjoe> listOfLakes) {

        return listOfLakes.stream()
                .mapToDouble(Innsjoe::areal)
                .average()
                .orElse(0d);
    }

    public static Innsjoe longestLake(List<Innsjoe> listOfLakes) {

        return listOfLakes.stream()
                .max((a,b) -> a.lengde().compareTo(b.lengde()))
                .orElse(null);
    }

    public static Innsjoe shortestLake(List<Innsjoe> listOfLakes) {

        return listOfLakes.stream()
                .min((a,b) -> a.lengde().compareTo(b.lengde()))
                .orElse(null);
    }

    public static Innsjoe lakeWithDepthBiggerThanTenthOfLength(List<Innsjoe> listOfLakes) {

        return listOfLakes.stream()
                .filter(i -> i.maksDybde() > i.lengde()/10)
                .findAny()
                .orElse(null);
    }

    public static double averageCountriesForLakes(List<Innsjoe> listOfLakes) {

        return listOfLakes.stream()
                .mapToDouble(i -> i.land().size())
                .average()
                .orElse(0d);
    }

    public static double productOfAllLakeDepths(List<Innsjoe> listOfLakes) {

        return listOfLakes.stream()
                .map(Innsjoe::maksDybde)
                .reduce((sum, i) -> sum * i)
                .orElse(0d);
    }

    /**
     * 4. Avanserte collectors
     */

    public static Map<String, List<Innsjoe>> groupLakesToContinents(List<Innsjoe> listOfLakes) {

        return listOfLakes.stream()
                .collect(Collectors.groupingBy(Innsjoe::kontinent));
    }

    public static Map<String, Long> countLakesForEachContinent(List<Innsjoe> listOfLakes) {

        return listOfLakes.stream()
                .collect(Collectors.groupingBy(Innsjoe::kontinent, Collectors.counting()));
    }

    public static String  stringOfLakeNamesWithSeparator(List<Innsjoe> listOfLakes) {

        return listOfLakes.stream()
                .map(Innsjoe::navn)
                .collect(Collectors.joining("|"));
    }

    public static double averageSurfaceAreaOfLakes(List<Innsjoe> listOfLakes) {

        return listOfLakes.stream()
                .collect(Collectors.averagingDouble(Innsjoe::areal));
    }

    // TODO: Not sure about this, check with someone
    public static Map<Boolean, List<Innsjoe>> separatingLakesBasedOn500mDepth(List<Innsjoe> listOfLakes) {

        return listOfLakes.stream()
                .collect(Collectors.partitioningBy(i -> i.maksDybde() > 500));
    }

    /**
     * 5. Flatmap
     */

    public static List<String> allCountriesInList(List<Innsjoe> listOfLakes) {

        return listOfLakes.stream()
                .map(Innsjoe::land)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static List<String> allCountriesInListNoDuplicates(List<Innsjoe> listOfLakes) {

        return listOfLakes.stream()
                .map(Innsjoe::land)
                .flatMap(Collection::stream)
                .distinct()
                .sorted() // <- for my own viewing pleasure
                .collect(Collectors.toList());
    }

    public static Map<String, Long> allCountriesAndHowManyTimesTheyShowUpInList(List<Innsjoe> listOfLakes) {

        return listOfLakes.stream()
                .map(Innsjoe::land)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public static void main(String[] args)
    {
        System.out.println(allCountriesAndHowManyTimesTheyShowUpInList(Innsjoer.innsjoer));
    }
}
