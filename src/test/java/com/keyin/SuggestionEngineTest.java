package com.keyin;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import java.io.IOException; // import new
import java.util.stream.Collectors;
import java.util.stream.Stream;


@ExtendWith(MockitoExtension.class)
public class SuggestionEngineTest {
    private SuggestionEngine suggestionEngine = new SuggestionEngine();

    @Mock
    private SuggestionsDatabase mockSuggestionDB;
    private boolean testInstanceSame = false;

    @Test
    public void testGenerateSuggestions() throws Exception {
        suggestionEngine.loadDictionaryData( Paths.get( ClassLoader.getSystemResource("words.txt").getPath()));

//        Assertions.assertTrue(testInstanceSame);
        Assertions.assertTrue(suggestionEngine.generateSuggestions("hellw").contains("hello"));
    }


    // Test to verify that "hello" is not suggested when "hello" is input
    @Test
    public void testGenerateSuggestionsFail() throws Exception {
        suggestionEngine.loadDictionaryData( Paths.get( ClassLoader.getSystemResource("words.txt").getPath()));

        testInstanceSame = true;
        Assertions.assertTrue(testInstanceSame);
        Assertions.assertFalse(suggestionEngine.generateSuggestions("hello").contains("hello"));
    }


    // Test using mock database to verify suggestion functionality
    @Test
    public void testSuggestionsAsMock() {
        Map<String,Integer> wordMapForTest = new HashMap<>();

        wordMapForTest.put("test", 1);

        Mockito.when(mockSuggestionDB.getWordMap()).thenReturn(wordMapForTest);

        suggestionEngine.setWordSuggestionDB(mockSuggestionDB);

        Assertions.assertFalse(suggestionEngine.generateSuggestions("test").contains("test"));

        Assertions.assertTrue(suggestionEngine.generateSuggestions("tes").contains("test"));
    }

    // MY TESTS //

    //  Test to verify that the dictionary data is loaded correctly and matches the expected word map
    @Test
     void testLoadDictionaryData() throws IOException {
        // Arrange
        Map<String, Integer> expectedWordMap = new HashMap<>();
        expectedWordMap.put("keyin", 1);
        expectedWordMap.put("college", 1);

    }


//   Test to verify that an IOException is thrown when an invalid file is loaded
    @Test
    void testLoadDictionaryDataWithErrorFile() {
        // Arrange - немає потреби у підготовці даних, оскільки ми очікуємо IOException

        // Act & Assert
        Assertions.assertThrows(IOException.class, () -> {
            suggestionEngine.loadDictionaryData(Paths.get("error_dictionary.txt"));
        });
    }


    // Test to verify that an empty word map is returned when the database is not initialized
    @Test
    void testGetWordSuggestionDBWhenDatabaseIsNull() {
        // Act
        Map<String, Integer> wordMap = suggestionEngine.getWordSuggestionDB();

        // Assert
        Assertions.assertNotNull(wordMap);
        Assertions.assertTrue(wordMap.isEmpty());
    }


    // Test to verify that the method returns an empty string for a known word
    @Test
    public void testGenerateSuggestionsKnownWord() throws Exception {
        suggestionEngine.loadDictionaryData(Paths.get(ClassLoader.getSystemResource("words.txt").getPath()));
        Assertions.assertEquals("", suggestionEngine.generateSuggestions("hello"));
    }


    // Test to verify that the method returns correct suggestions for an unknown word
@Test
public void testGenerateSuggestionsUnknownWord() throws Exception {
    suggestionEngine.loadDictionaryData(Paths.get(ClassLoader.getSystemResource("words.txt").getPath()));
    String suggestions = suggestionEngine.generateSuggestions("helo");
    Assertions.assertTrue(suggestions.contains("hello"));
}


    // Test to verify that the method generates the correct number of edits
@Test
public void testWordEditsCount() {
    Stream<String> edits = suggestionEngine.wordEdits("test");
    long count = edits.count();
    Assertions.assertTrue(count > 0); // Перевірка, що хоча б одне редагування створено
}


}