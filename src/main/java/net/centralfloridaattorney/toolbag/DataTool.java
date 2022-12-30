package net.centralfloridaattorney.toolbag;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.time.LocalDateTime;
import java.util.*;

public class DataTool {
    public static final String REGEX_ADDRESS = "\\s*([0-9]*)\\s((NW|SW|SE|NE|S|N|E|W))?(.*)" +
            "((NW|SW|SE|NE|S|N|E|W))?((#|APT|BSMT|BLDG|DEPT|FL|FRNT|HNGR|KEY|LBBY|LOT|LOWR|OFC|" +
            "PH|PIER|REAR|RM|SIDE|SLIP|SPC|STOP|STE|TRLR|UNIT|UPPR|\\,)[^,]*)(\\,)([\\s\\w]*)\\n";
    //
    private static final String filePath = "resources\\documentData.csv";
    private static final int parcelIdLength = 22;
    private static final String stringToParse = "1150 CARMEL UNIT 401 CIR CASSELBERRY; FL 32707";
    private static final String[][] testStringArray = {
            {"1", "2", "5"},
            {"2", "1", "4"},
            {"3", "1", "3"}
    };
    public DataTool() {
    }

    public static String getValueFromKey(String[][] stringArray, String key) {
        for (int i = 0; i < stringArray.length; i++) {
            if (stringArray[i][0].equals(key)) return stringArray[i][1];
        }
        return "default";
    }

    public static String[][] getKeyValues(String dataElements) {
        dataElements = dataElements.replace("{", "").replace("}", "");
        String[] tabElements = dataElements.split("\n");
        String[][] keyValueArray = new String[tabElements.length][2];
        for (int i = 0; i < tabElements.length; i++) {
            String[] keyValues = tabElements[i].split("\t");
            keyValueArray[i][0] = keyValues[0];
            keyValueArray[i][1] = keyValues[1];
        }
        return keyValueArray;
    }

    public static byte[] getByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String[][] appendRowToArray(String[][] stringArray, String[] arrayToAppend) {
        String[][] appendedArray = Arrays.copyOf(stringArray, stringArray.length + 1);
        appendedArray[appendedArray.length - 1] = arrayToAppend;
        return appendedArray;
    }

    public static String getTSVFormat() {

        List<List<String>> matrix = null;
        String SEPARATOR = "\t";
        String END_OF_LINE = "\n";


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matrix.size(); i++) {
            for (int o = 0; o < matrix.get(i).size(); o++) {
                sb.append(matrix.get(i).get(o));
                if (o < (matrix.get(i).size() - 1))
                    sb.append(SEPARATOR);
                else
                    sb.append(END_OF_LINE);
            }
        }
        return sb.toString();

    }

    public static String[] appendValueToArray(String[] rowValue, String value) {
        String[] newStringArray = new String[rowValue.length + 1];
        int col = 0;
        for (String colValue : rowValue) {
            newStringArray[col++] = colValue;
        }
        newStringArray[col] = value;
        return newStringArray;
    }

    /**
     * public static double[][] get2INDArray(INDArray indArray, int width) {
     * //int rows = indArray.rows();
     * //int columns = indArray.columns();
     * int rows = 9;
     * int columns = 5;
     * double[][] doubleArray = new double[rows][columns];
     * INDArray matrix = indArray.tensorAlongDimension(0, 0);
     * <p>
     * for (int row = 0; row < rows; row++) {
     * for (int column = 0; column < columns; column++) {
     * //doubleArray[row][column] = indArray.getDouble(row, column);
     * DataTool.normalize(matrix.getFloat(column, row), 0.0d, Double.valueOf(width));
     * double value = matrix.getDouble(column, row);
     * double valueCap = DataTool.normalize(matrix.getFloat(column, row), 0.0d, Double.valueOf(width));
     * doubleArray[row][column] = valueCap;
     * }
     * }
     * return doubleArray;
     * }
     **/

    public static double normalize(float value, double min, double max) {
        double normal;
        normal = ((double) value) * (max - min);
        normal = normal - (min + 1.0d);
        normal = -normal + min;
        return normal;
    }

    public static String[][] appendValueToArray(String[][] stringArray, String[] newValues) {
        String[][] newArray = new String[stringArray.length][stringArray[0].length + 1];
        int row = 0;
        int col;
        for (String[] rowValues : stringArray) {
            col = 0;
            for (String value : rowValues) {
                newArray[row][col++] = value;
            }
            row++;
        }
        newArray = setKeyValue(newArray, newArray[0].length - 1, newValues);
        return newArray;
    }

    public static String convert1DArrayToString(String[] stringArray) {
        String stringValue = "";
        for (String value : stringArray) {
            stringValue = stringValue + "\r\n" + value;
        }
        int length = stringValue.length();
        return stringValue.substring(1, length);
    }

    public static String convert1DArrayToString(String[] stringArray, String separator) {
        String stringValue = "";
        if (stringArray != null) {
            for (String value : stringArray) {
                if (!" ".equals(value)) {
                    stringValue = stringValue + separator + value;
                }
            }
            stringValue = DataTool.snipAtBeforeChar(stringValue, separator.charAt(0));
        }
        return stringValue;
    }

    public static String convert2DArrayToString(String[][] array) {
        String arrayString = "";
        for (String[] rowString : array) {
            for (String columnString : rowString) {
                arrayString += columnString;
            }
        }

        arrayString = arrayString.replaceAll(",", ".");

        return arrayString;
    }

    public static String[][] convertToUpperCase(String[][] stringArray) {
        String[][] cleanString = new String[stringArray.length][stringArray[0].length];
        int row = 0;
        int col;
        for (String[] rowValue : stringArray) {
            col = 0;
            for (String value : rowValue) {
                cleanString[row][col++] = value.toUpperCase();
            }
            row++;
        }
        return cleanString;
    }

    public static String[][] deleteRowsWithValue(String[][] stringArray, String value) {
        String[][] cleanString = new String[stringArray.length][stringArray[0].length];
        int row = 0;
        for (String[] rowValue : stringArray) {
            if (!contains(rowValue, value)) {
                cleanString[row++] = rowValue;
            }
        }
        cleanString = Arrays.copyOf(cleanString, row);
        return cleanString;
    }

    public static String[][] get2DArray(String[] splitString, int numCols) {
        int numRows = (splitString.length - 1) / numCols;
        String[][] stringArray = new String[numRows][numCols];
        int count = 0;
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                stringArray[row][col] = splitString[count++];
            }
        }
        return stringArray;
    }

    public static String getAndroidFileName(String filePath) {
        int endPos = filePath.length();
        for (int currentPos = endPos - 1; currentPos > 0; currentPos--) {
            char character = filePath.charAt(currentPos);
            if (character == '/') {
                filePath = filePath.substring(currentPos, endPos);
                return filePath;
            }
        }
        return filePath;
    }

    public static String[] getColArray(String[][] tableData, int row) {
        String[] colArray = new String[tableData.length];
        int count = 0;
        for (String[] rowValue : tableData) {
            colArray[count++] = rowValue[row];
        }
        return colArray;
    }


    public static int getIndexRowWithValue(String[][] dataArray, String value) {
        int count = 0;
        for (String[] rowValue : dataArray) {
            if (contains(rowValue, value)) {
                return count;
            }
            count++;
        }
        return count;
    }

    public static int getIndexRowWithValue(String[][] stringArray, String value, int colNum) {
        int row = 0;
        for (String[] rowValue : stringArray) {
            if (rowValue[colNum].contains(value)) {
                return row;
            }
            row++;
        }
        return 0;
    }

    public static String[][] getIndexedArray(String[][] arrayString) {
        String[][] indexedArray = new String[arrayString.length][arrayString[0].length + 1];
        int row = 0;
        int col = 1;
        for (String[] rowValue : arrayString) {
            indexedArray[row][0] = String.valueOf(row);
            col = 1;
            for (String colValue : rowValue) {
                indexedArray[row][col++] = colValue;
            }
            row++;
        }
        return indexedArray;
    }

    public static String getModParcelId(String parcelId) {
        //TODO: This function was created because ocpafl.org reverses the first 3 sections of the parcelId
        String parcelIdSentence = parcelId.replace('-', ' ');
        String part1 = DataTool.snipAfterWordN(parcelIdSentence, 1);
        String part2 = DataTool.snipAfterWordN(parcelIdSentence, 2);
        part2 = DataTool.snipBeforeWordN(part2, 1);
        String part3 = DataTool.snipAfterWordN(parcelIdSentence, 3);
        part3 = DataTool.snipBeforeWordN(part3, 2);
        String part4 = DataTool.snipBeforeWordN(parcelIdSentence, 3);
        parcelIdSentence = part3 + part2 + part1 + part4;
        parcelIdSentence = parcelIdSentence.replace(" ", "");
        return parcelIdSentence;
    }

    public static ResultSet getResultSet(String[][] resultArray) {


        return null;
    }

    public static String[] getRowWithColIndex(String[][] documentData, int colIndex, String value) {
        String[] returnValue = new String[documentData[0].length];
        returnValue[0] = "default";
        for (String[] rowValue : documentData) {
            String colValue = rowValue[colIndex];
            if (colValue.equals(value)) {
                returnValue = Arrays.copyOf(rowValue, documentData[0].length);
                return returnValue;
            }
        }
        return returnValue;
    }

    public static String[] getRowWithValue(String[][] dataArray, String value) {
        String[] row = dataArray[0];
        for (String[] rowValue : dataArray) {
            if (contains(rowValue, value)) {
                return rowValue;
            }
        }
        return row;
    }

    public static String[] getRowWithValues(String[][] stringArray, String value, int colNum) {
        String[][] rowsWithValues = new String[stringArray.length][stringArray[0].length];
        int row = 0;
        for (String[] rowValue : stringArray) {
            if (rowValue[colNum].contains(value)) {
                return rowsWithValues[row];
            }
            row++;
        }
        return rowsWithValues[0];
    }

    public static String[][] getRowsWithValue(String[][] stringArray, String value, boolean contains) {
        String[][] rowsWithoutValues = new String[stringArray.length][stringArray[0].length];
        int row = 0;
        boolean rowContainsValue;
        for (String[] rowValues : stringArray) {
            rowContainsValue = !contains;
            for (String rowValue : rowValues) {
                if (rowValue.contains(value)) {
                    rowContainsValue = contains;
                }
            }
            if (rowContainsValue) {
                rowsWithoutValues[row++] = rowValues;
            }
        }

        rowsWithoutValues = Arrays.copyOf(rowsWithoutValues, row);

        return rowsWithoutValues;
    }

    public static String[][] getRowsWithValue(String[][] stringArray, int colNum, String value) {
        String[][] rowsWithValues = new String[stringArray.length][stringArray[0].length];
        int row = 0;
        for (String[] rowValue : stringArray) {
            if (rowValue[colNum].contains(value)) {
                rowsWithValues[row++] = rowValue;
            }
        }
        rowsWithValues = Arrays.copyOf(rowsWithValues, row);

        return rowsWithValues;
    }

    public static String[][] getRowsWithValue(String[][] stringArray, String value) {
        //Note this is case sensitive, adjustment is needed to make case insensitive
        String[][] rowsWithValues = new String[stringArray.length][stringArray[0].length];
        int row = 0;
        boolean rowContainsValue;
        for (String[] rowValues : stringArray) {
            rowContainsValue = false;
            for (String rowValue : rowValues) {
                if (rowValue.contains(value)) {
                    rowContainsValue = true;
                }
            }
            if (rowContainsValue) {
                rowsWithValues[row++] = rowValues;
            }
        }

        rowsWithValues = Arrays.copyOf(rowsWithValues, row);

        return rowsWithValues;
    }

    public static String[] getRowsWithValue(String[] stringArray, String value) {
        //Note this is case sensitive, adjustment is needed to make case insensitive
        String[] rowsWithValues = new String[stringArray.length];
        int row = 0;
        for (String rowValue : stringArray) {
            if (rowValue.contains(value)) {
                rowsWithValues[row++] = rowValue;
            }
        }
        rowsWithValues = Arrays.copyOf(rowsWithValues, row);
        return rowsWithValues;
    }

    public static String[][] getStringArray(ResultSet resultSet) {

        String[][] stringArray = null;
        try {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int numCols = rsmd.getColumnCount();
            stringArray = new String[0][numCols];
            String[] tempArray = new String[numCols];
            while (resultSet.next()) {
                for (int col = 0; col < numCols; col++) {
                    String value = resultSet.getString(col + 1);
                    tempArray[col] = value;
                }
                stringArray = DataTool.appendRowToArray(stringArray, tempArray);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stringArray;
    }

    public static String[][] getUniqueRows(String[][] documentData, String[][] newValues) {
        String[][] uniqueValues = new String[newValues.length][newValues[0].length];
        int row = 0;
        for (String[] rowValue : newValues) {
            if (!isWordIn2DArray(documentData, rowValue[0])) {
                uniqueValues[row] = rowValue;
            }
        }

        uniqueValues = Arrays.copyOf(uniqueValues, row);

        return uniqueValues;
    }

    public static boolean isDigitWord(String word) {
        char[] characters = word.toCharArray();
        for (char charValue : characters) {
            if (!Character.isDigit(charValue)) {
                return false;
            }
        }
        return true;
    }


    /**
     * public static boolean isWordIn2DArray(String filePath, int numCols, String word) {
     * //String filePath = "D:\\Projects\\tess4j-master\\src\\main\\resources\\state_abbreviations.csv";
     * String[][] array = FileTool.get2DStringArray(filePath, numCols);
     * for (String[] rowValues : array) {
     * for (String colValue : rowValues) {
     * if (colValue.contains(word)) {
     * return true;
     * }
     * }
     * }
     * return false;
     * }
     **/

    public static boolean isWordIn2DArray(String[][] stringArray, String word) {
        for (int i = 0; i < stringArray.length; i++) {
            String[] row = stringArray[i];
            for (int j = 0; j < row.length; j++) {
                if (stringArray[i][j].contains(word)) return true;
            }
        }


        //String filePath = "D:\\Projects\\tess4j-master\\src\\main\\resources\\state_abbreviations.csv";
        //String[][] array = FileTool.get2DStringArray(filePath, numCols);
        for (String[] rowValues : stringArray) {
            for (String colValue : rowValues) {
                if (colValue == null) {
                    return false;
                } else if (colValue.contains(word)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isWordInRow(String[] rowValue, String word) {
        word = word.toUpperCase();
        for (String value : rowValue) {
            value = value.toUpperCase();
            if (value.equals(word)) {
                return true;
            }
        }
        return false;
    }

    public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public static void main(String[] args) {
        DataTool dataTool = new DataTool();
        String parcelId = "32-20-28-1141-00-660";
        String dateString = "10/15/2018 8:27:42 AM";
        String modParcelId = DataTool.getModParcelId(parcelId);
        String cookieMessage = "event=onopen; event_time=1598911026115; command=login~1234; command_time=1598911026115; current_page=http://192.168.1.9:5000/index.html; current_page_time=1598911026115; onopen=192.168.1.9; onopen_time=1598911026115";
        String[][] cookieMessageArray = DataTool.getMessageStringArray(cookieMessage);
        String[] tabString = DataTool.getTabStringFromArray(cookieMessageArray);
        String[][] sorted = DataTool.sort(testStringArray, 2);
        //String[][] arrayString = FileTool.get2DStringArray(filePath, 11);

        //boolean isWordIn2DArray = isWordIn2DArray(arrayString, "2018117810");
        //arrayString = DataTool.removeRowWithWord(arrayString, "2018117810");

        //isWordIn2DArray = isWordIn2DArray(arrayString, "2018117810");

        //String[] rowHighestSimilarity = DataTool.getMostSimilarRow(arrayString, dateString, true);

        //


        //
        //arrayString = DataTool.replaceIn2DArray(arrayString, "$", "");
        //String[][] indexedArray = DataTool.getIndexedArray(arrayString);
        //FileTool.clearFile(filePath);
        //FileTool.append2DStringArray(filePath, indexedArray);
        //String cleanString = DataTool.getAddress(stringToParse);
        ////System.out.println(cleanString);
        ////System.out.println(indexedArray);
        //String documentPath = "D:\\Projects\\OfficialRecords\\src\\main\\resources\\officialRecords\\seminole\\june_2018\\documentData.csv";
        //int documentCols = 12;
        //String[][] documentData = FileTool.get2DStringArray(documentPath, documentCols);
        //String deedsPath = "D:\\Projects\\OfficialRecords\\src\\main\\resources\\officialRecords\\seminole\\june_2018\\Seminole_Deeds.csv";
        //int deedsCols = 28;
        //String[][] deedsData = FileTool.get2DStringArray(deedsPath, deedsCols);
        //String[][] missingRecords = DataTool.getRowsWithoutMatchingColValue(deedsData, 27, documentData, 11);
        //String filePath = "D:\\Projects\\OfficialRecords\\src\\main\\resources\\officialRecords\\seminole\\june_2018\\compiledData.csv";
        //String[][] compiledData = FileTool.get2DStringArray(filePath, 12);
        //compiledData = DataTool.replaceIn2DArray(compiledData, "\"", "");
        //FileTool.clearFile(filePath);
        //FileTool.append2DStringArray(filePath, compiledData);
        System.out.println("Finished DataTool.main()!");

    }

    public static String[] getMostSimilarRow(String[][] arrayString, String testString, boolean appendValue) {
        String[] similarRow = new String[arrayString[0].length];
        double highestScore = 0;
        int highestIndex = 0;
        int count = 0;
        double cosineSimilarity = 0;
        for (String[] row : arrayString) {
            for (String value : row) {

                /**
                 Cosine cosine = new Cosine();
                 if(cosine.similarity(testString, value)>cosineSimilarity){
                 cosineSimilarity = cosine.similarity(testString, value);
                 highestIndex = count;
                 }
                 **/
            }
            count++;
        }
        similarRow = arrayString[highestIndex];
        similarRow = DataTool.appendValueToArray(similarRow, String.valueOf(cosineSimilarity));
        return similarRow;
    }

    public static String[][] removeDuplicates(String[][] arrayString, int columnToCompare) {
        String[][] cleanString = new String[arrayString.length][arrayString[0].length];
        int row = 0;
        for (String[] rowValue : arrayString) {
            if (!isWordIn2DArray(cleanString, rowValue[columnToCompare])) {
                cleanString[row++] = rowValue;
            }
        }
        cleanString = Arrays.copyOf(cleanString, row);
        return cleanString;
    }

    public static String[] removeDuplicates(String[] stringArray) {
        int end = stringArray.length;

        Set<String> set = new HashSet<String>();

        for (int i = 0; i < end; i++) {
            set.add(stringArray[i]);
        }

        String[] cleanArray = new String[set.size()];
        Iterator it = set.iterator();
        int row = 0;
        while (it.hasNext()) {
            cleanArray[row++] = (String) it.next();
        }
        cleanArray = Arrays.copyOf(cleanArray, row);
        return cleanArray;
    }

    public static String removeLeadingSpaces(String sentence) {
        if (sentence == null) {
            return null;
        }

        if (sentence.isEmpty()) {
            return "";
        }

        int arrayIndex = 0;
        while (true) {
            if (!Character.isWhitespace(sentence.charAt(arrayIndex++))) {
                break;
            }
        }
        return sentence.substring(arrayIndex - 1);
    }

    public static String[][] removeRowWithWord(String[][] arrayString, String word) {
        String[][] cleanString = new String[arrayString.length][arrayString[0].length];
        int row = 0;
        for (String[] rowValue : arrayString) {
            if (!isWordInRow(rowValue, word)) {
                cleanString[row++] = rowValue;
            }
        }
        cleanString = Arrays.copyOf(cleanString, row);
        return cleanString;
    }

    public static String removeTrailingSpaces(String sentence) {
        if (sentence == null)
            return null;
        int len = sentence.length();
        for (; len > 0; len--) {
            if (!Character.isWhitespace(sentence.charAt(len - 1)))
                break;
        }
        return sentence.substring(0, len);
    }

    public static String[] replaceIn1DArray(String[] stringArray, char oldValue, char newValue) {
        String[] cleanString = new String[stringArray.length];
        int row = 0;
        for (String sentence : stringArray) {
            cleanString[row] = sentence.replace(oldValue, newValue);
            cleanString[row] = removeTrailingSpaces(cleanString[row]);
            cleanString[row] = removeLeadingSpaces(cleanString[row]);
            row++;
        }

        return cleanString;
    }

    public static String[][] setKeyValue(String[][] stringArray, String key, String value) {
        for (int i = 0; i < stringArray.length; i++) {
            if (stringArray[i][0].equals(key)) stringArray[i][1] = value;
        }
        return stringArray;
    }

    public static String[][] setKeyValue(String[][] stringArray, int colNum, String[] replacementValues) {
        String[][] cleanArray = new String[stringArray.length][stringArray[0].length];
        int row = 0;
        int col;
        for (String[] rowValues : stringArray) {
            col = 0;
            for (String value : rowValues) {
                cleanArray[row][col++] = value;
            }
            cleanArray[row][colNum] = replacementValues[row];
            row++;
        }

        return cleanArray;
    }


    public static String snipAfterWordN(String sentence, int numWords) {
        for (int i = 0; i < sentence.length(); i++) {
            // When a space is encountered, reduce words remaining by 1.
            if (sentence.charAt(i) == ' ') {
                numWords--;
            }
            // If no more words remaining, return a substring.
            if (numWords == 0) {
                return sentence.substring(0, i + 1);
            }
        }
        // Error case.
        return "";
    }

    public static String snipAtAfterChar(String sentence, char stopChar) {
        char[] characters = sentence.toCharArray();
        int count = 0;
        for (char character : characters) {
            if (character == stopChar) {
                return sentence.substring(0, count);
            }
            count++;
        }


        return sentence;
    }

    public static String[] snipAtAfterChar(String[] stringArrays, char stopChar) {
        int index = 0;
        for (String sentence : stringArrays) {
            char[] characters = sentence.toCharArray();
            int characterCount = 0;
            for (char character : characters) {
                if (character == stopChar) {
                    stringArrays[index++] = sentence.substring(0, characterCount);
                    break;
                } else {
                    characterCount++;
                }
            }
        }
        return stringArrays;
    }

    public static String snipAtBeforeChar(String sentence, char stopChar) {
        char[] characters = sentence.toCharArray();
        int count = 0;
        for (char character : characters) {
            if (character == stopChar) {
                return sentence.substring(count + 1, characters.length);
            }
            count++;
        }
        return sentence;
    }

    public static String[] snipAtBeforeChar(String[] rowValues, char stopChar) {
        String[] cleanString = new String[rowValues.length];
        int row = 0;
        for (String sentence : rowValues) {
            char[] characters = sentence.toCharArray();
            int count = 0;
            boolean foundStopChar = false;
            for (char character : characters) {

                if (character == stopChar) {
                    cleanString[row] = sentence.substring(count + 1, characters.length);
                    foundStopChar = true;
                }
                count++;
            }

            if (!foundStopChar) {
                cleanString[row] = sentence;
            }
            row++;
        }
        return cleanString;
    }

    public static String snipBeforeWordN(String sentence, int numWords) {
        for (int i = 0; i < sentence.length(); i++) {
            // When a space is encountered, reduce words remaining by 1.
            if (sentence.charAt(i) == ' ') {
                numWords--;
            }
            // If no more words remaining, return a substring.
            if (numWords == 0) {
                int sentLength = sentence.length();
                return sentence.substring(i + 1, sentLength);
            }
        }
        // Error case.
        return "";
    }

    public static String snipWordN(String sentence, int wordToTrim) {
        wordToTrim--;
        //Returns everything from the first letter to the first letter of the word to trim
        for (int i = 0; i < sentence.length(); i++) {
            // When a space is encountered, reduce words remaining by 1.
            if (sentence.charAt(i) == ' ') {
                wordToTrim--;
            }
            // If no more words remaining, return a substring.
            if (wordToTrim == 0) {
                return sentence.substring(0, i + 2);
            }
        }
        return sentence;
    }

    public static String trimAfterMiddleInitial(String notFoundSelectorText) {
        //notFoundSelectorText = getFirstNWords(notFoundSelectorText, 8);
        notFoundSelectorText = snipWordN(notFoundSelectorText, 8);
        notFoundSelectorText = snipBeforeWordN(notFoundSelectorText, 5);
        return notFoundSelectorText;
    }

    public static boolean isWordIn2DArray(String statesFilePath, int i, String value) {
        return false;
    }

    public static String getSeminolePID(String parcelId) {
        return null;
    }

    public static String[] appendArrayToArray(String[] array1, String[] array2) {
        int length = array1.length + array2.length;
        String[] newArray = new String[length];
        int count = 0;
        for (String value : array1) {
            newArray[count++] = value;
        }
        for (String value : array2) {
            newArray[count++] = value;
        }
        return newArray;
    }

    public static String[] getNotMatchedRows(String[] firstStringArray, String[][] secondStringArray) {
        String[] notMatchedRows = new String[firstStringArray.length];
        int count = 0;
        for (String value : firstStringArray) {
            if (!DataTool.isWordIn2DArray(secondStringArray, value)) {
                notMatchedRows[count++] = value;
            }
        }
        notMatchedRows = Arrays.copyOf(notMatchedRows, count);
        return notMatchedRows;
    }

    public static String[][] appendArrayToArray(String[][] firstArray, String[][] secondArray) {
        int largestRow = 0;
        for (String[] row : firstArray) {
            if (row.length > largestRow) {
                largestRow = row.length;
            }
        }
        for (String[] row : secondArray) {
            if (row.length > largestRow) {
                largestRow = row.length;
            }
        }

        String[][] mergedArrays = new String[firstArray.length + secondArray.length][largestRow];
        int rowNum = 0;
        int colNum;
        for (String[] row : firstArray) {
            colNum = 0;
            for (String value : row) {
                mergedArrays[rowNum][colNum++] = value;
            }
            rowNum++;
        }

        for (String[] row : secondArray) {
            colNum = 0;
            for (String value : row) {
                mergedArrays[rowNum][colNum++] = value;
            }
            rowNum++;
        }
        return mergedArrays;
    }

    public static String getCsvString(String[] keys, String[][] dataArray) {
        String csvString = DataTool.convert1DArrayToString(keys) + "\n";
        for (String[] row : dataArray) {
            csvString += DataTool.convert1DArrayToString(row) + "\n";
        }
        return csvString;
    }

    public static String[] getInitializedArray(int rows, String defaultValue) {
        String[] array = new String[rows];
        for (int i = 0; i < rows; i++) {
            array[i] = defaultValue;
        }
        return array;
    }

    public static String[][] getTabStringKeyValues(String[] tabString) {
        int length = tabString.length;
        String[][] tabStringArray = new String[length][2];
        for (int i = 0; i < length; i++) {
            String[] keyValue = tabString[i].split("\t");
            tabStringArray[i][0] = keyValue[0];
            tabStringArray[i][1] = keyValue[1];
        }
        return tabStringArray;
    }

    private static boolean contains(String[] stringArray, String text) {
        boolean containsText = false;
        for (String value : stringArray) {
            if (value.contains(text)) {
                containsText = true;
            }
            //test for all caps
            String valueAllCaps = value.toUpperCase();
            String textAllCaps = text.toUpperCase();
            if (valueAllCaps.equals(textAllCaps)) {
                containsText = true;
            }
        }
        return containsText;
    }

    private static String getAddress(String stringToParse) {
        return null;
    }

    private static String[] getArray(String stringToParse, String s) {
        String[] cleanString = stringToParse.split(s);


        return cleanString;
        //String fileString = FileTool.getFileString(filePath);
        ////System.out.println(fileString);

    }

    public static String snipAtBeforeWord(String stringValue, String snipBefore) {
        if (stringValue.contains(snipBefore)) {
            int lastIndex = stringValue.lastIndexOf(snipBefore);
            int lengthStringValue = stringValue.length();
            int lengthSnipBefore = snipBefore.length();
            int snipPoint = lastIndex + lengthSnipBefore;
            stringValue = stringValue.substring(snipPoint, lengthStringValue);
        }
        return stringValue;
    }

    public static String snipAtAfterWord(String stringValue, String snipAfter) {
        if (stringValue.contains(snipAfter)) {
            int totalLength = stringValue.length();
            int snipAfterLength = snipAfter.length();
            int snipPoint = totalLength - snipAfterLength;
            stringValue = stringValue.substring(0, snipPoint);
        }
        return stringValue;
    }

    public static String getHtmlString(String value) {
        //value = value.replace(" ", "&#160");
        //value = value.replace("\"", "&#34");
        //value = value.replace("\n", "&#10");
        //value = value.replace("\'", "&#39");
        //value = value.replace("<", "&#60");
        //value = value.replace(">", "&#62");
        //value = value.replace("~", "&#126;");
        return value;
    }

    public static String[][] getTabStringKeyValues(String tabString) {
        tabString = tabString.replace("{", "");
        tabString = tabString.replace("}", "");
        String[] tabStringArray = tabString.split("\n");
        return getTabStringKeyValues(tabStringArray);
    }

    public static String[][] getMessageStringArray(String cookieMessage) {
        String[] message = null;
        if (cookieMessage.startsWith("\"\\\"1 ")) {
            cookieMessage = cookieMessage.substring(5);
        }
        //message = cookieMessage.split(COOKIE_SEPARATOR);
        message = cookieMessage.split(";");

        String[][] messages = new String[message.length][2];
        for (int i = 0; i < messages.length; i++) {
            //String[] thisMessage = message[i].split(MESSAGE_SEPARATOR);
            String[] thisMessage = message[i].split("=");
            messages[i][0] = thisMessage[0].trim();
            messages[i][1] = thisMessage[1].trim();
        }
        return messages;
    }

    public static String getCurrentTime() {
        String SEPARATOR_TIME = "?";
        String REGEX_TIME = "[?][0-9]{1.2}[?][0-9]{1,2}[?][0-9]{4}[?]";
        LocalDateTime now = LocalDateTime.now();
        String month = String.valueOf(now.getMonthValue());
        String day = String.valueOf(now.getDayOfMonth());
        String year = String.valueOf(now.getYear());
        String hour = String.valueOf(now.getHour());
        String minute = String.valueOf(now.getMinute());
        String second = String.valueOf(now.getSecond());
        //String nano = String.valueOf(now.getNano());
        return SEPARATOR_TIME + month + SEPARATOR_TIME + day + SEPARATOR_TIME + year +
                SEPARATOR_TIME + hour + SEPARATOR_TIME + minute + SEPARATOR_TIME + second + SEPARATOR_TIME;
    }

    public static String getTabString(String[][] tabStringArray) {
        String tabString = "";
        for (int i = 0; i < tabStringArray.length; i++) {
            String[] tempRow = tabStringArray[i];
            for (int j = 0; j < tempRow.length; j++) {
                tabString += tabString + "\t" + tempRow[j];
            }
            if (i < tabStringArray.length - 1) {
                tabString = tabString + "\n";
            }
        }
        return tabString;
    }

    public static String[] getTabStringFromArray(String[][] tabStringArray) {
        int numRows = tabStringArray.length;
        String[] tabString = new String[numRows];
        for (int i = 0; i < numRows; i++) {
            tabString[i] = tabStringArray[i][0] + "\t" + tabStringArray[i][1];
        }
        return tabString;
    }

    public static String[][] parseContextElementKeyValues(String fileString) {
        String[] contextElementString = fileString.split("}");
        String keyValueString = contextElementString[0];
        return getStrings(keyValueString);
    }

    private static String[][] getStrings(String keyValueString) {
        String[] keyValueStringArray = keyValueString.replace("{", "").split("\n");
        String[][] keyValues = new String[keyValueStringArray.length][2];
        for (int i = 0; i < keyValueStringArray.length; i++) {
            String[] keyValue = keyValueStringArray[i].split("\t");
            keyValues[i][0] = keyValue[0];
            keyValues[i][1] = keyValue[1];
        }
        return keyValues;
    }

    public static String[][] parseContextElementValues(String fileString) {
        String[] contextElementString = fileString.split("}");
        String elementString = contextElementString[1];
        return getStrings(elementString);
    }

    public static String getRemoteFileName(String defaultRemoteAddress) {
        String cleanRemoteAddress = DataTool.getHtmlString(defaultRemoteAddress);
        return cleanRemoteAddress;
    }

    public static String getCleanFilePathString(String cleanString) {
        cleanString = cleanString.replace("<", "~");
        cleanString = cleanString.replace(">", "~");
        cleanString = cleanString.replace(":", "~");
        cleanString = cleanString.replace("\"", "~");
        cleanString = cleanString.replace("/", "~");
        cleanString = cleanString.replace("\\", "~");
        cleanString = cleanString.replace("|", "~");
        cleanString = cleanString.replace("?", "~");
        cleanString = cleanString.replace("*", "~");
        return cleanString;
    }

    public static String[][] removeKey(String[][] dataElementFilePathArray, String s) {

        return new String[0][];
    }

    public static String[] addRow(String[] stringArray, String row) {
        String[] newStringArray = Arrays.copyOf(stringArray, stringArray.length + 1);
        newStringArray[stringArray.length] = row;
        return newStringArray;
    }

    public static String[][] appendColumnToArray(String[][] stringArray, String[] columnToAppend, int colNum) {
        //This overwrites the values for the specified column
        String[][] appendedArray = stringArray;
        for (int i = 0; i < stringArray.length; i++) {
            appendedArray[i][colNum] = columnToAppend[i];
        }
        return appendedArray;
    }

    public static int getMaxCols(String[] rows) {
        int maxCols = 0;
        for (int i = 0; i < rows.length; i++) {
            if (rows[i].contains("\t")) {
                String[] cols = rows[i].split("\t");
                if (cols.length > maxCols) {
                    maxCols = cols.length;
                }
            } else if (rows[i].contains(",")) {
                String[] cols = rows[i].split(",");
                if (cols.length > maxCols) {
                    maxCols = cols.length;
                }
            }
        }
        return maxCols;
    }

    public static String[][] sort(String[][] tagArray, int colNum) {
        String[][] sortedArray = tagArray;

        for (String[] row : tagArray) {
            for (int i = 0; i < tagArray.length - 1; i++) {
                if (Integer.parseInt(sortedArray[i][colNum]) > Integer.parseInt(sortedArray[i + 1][colNum])) {
                    String[] tempRow = sortedArray[i];
                    sortedArray[i] = sortedArray[i + 1];
                    sortedArray[i + 1] = tempRow;
                }
            }
        }

        return sortedArray;
    }

    public static int getRowIndex(String[][] keyValueArray, String key) {
        int colNum = 0;
        for (int i = 0; i < keyValueArray[0].length; i++) {
            if (keyValueArray[0][i].equals(key)) {
                colNum = i;
            }
        }
        return colNum;
    }

    public static String[][] removeRow(String[][] fileArray, int removeRow) {
        int numRows = fileArray.length;
        int numCols = fileArray[0].length;
        String[][] cleanArray = new String[numRows-1][numCols];
        int row = 0;
        for(int i = 0; i<numRows;i++){
            if(i!=removeRow){
                cleanArray[row++] = fileArray[i];
            }
        }
        return cleanArray;
    }

    private int getHighestRow(double[] scores) {
        double highestScore = 0;
        int row = 0;
        int highestRow = 0;
        for (double score : scores) {
            if (score > highestScore) {
                highestScore = score;
                highestRow = row;
            }
            row++;
        }
        return highestRow;
    }
}