package simpplle.comcode;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A CSV reader reads tabular data in plain text. The first line of the file defines field names.
 * Each subsequent line is a record. Fields are delimited by a single character, which must not
 * appear within a field. An array field uses a different delimiter character, which follows the
 * same rules. Escape characters are not processed by the reader.
 *
 * Records are accessed one at a time in sequential order. This reduces memory usage so arbitrarily
 * large tables can be read. Fields within the current record are accessed by name. Empty non-array
 * fields return null. Empty array fields return an array with a single null value.
 */
public class CsvReader implements Closeable {

  private BufferedReader reader;
  private String delimiter;
  private String[] fields;
  private Map<String,String> values;

  /**
   * Creates a CSV reader. Upon creation, field names are read from the first line of the file. The
   * user must call nextRecord() to access the first record. This design choice allows a reader to
   * use a while loop, rather than a do-while loop to step through all records in a file.
   *
   * @param file a CSV-formatted file
   * @param delimiter a character that separates fields
   * @throws IOException if the file can't be opened for reading
   */
  public CsvReader(File file, String delimiter) throws IOException {
    this.delimiter = delimiter;
    this.reader = new BufferedReader(new FileReader(file));
    this.fields = reader.readLine().split(delimiter);
    this.values = new HashMap<>();
  }

  /**
   * Reads the next record from the file.
   *
   * @return true if there is a next record
   * @throws IOException if an I/O error occurs
   */
  public boolean nextRecord() throws IOException {
    String line = reader.readLine();
    if (line != null) {
      String[] split = line.split(delimiter);
      for (int i = 0; i < fields.length; i++) {
        values.put(fields[i],split[i]);
      }
    }
    return line != null;
  }

  /**
   * Checks if a field is defined.
   *
   * @param column the name of a field
   * @return true if the field exists
   */
  public boolean hasField(String column) {
    for (String entry : fields) {
      if (entry.equals(column)) {
        return true;
      }
    }
    return false;
  }

  public Integer getInteger(String column) throws NumberFormatException {
    return parseInteger(values.get(column));
  }

  public Long getLong(String column) throws NumberFormatException {
    return parseLong(values.get(column));
  }

  public Float getFloat(String column) throws NumberFormatException {
    return parseFloat(values.get(column));
  }

  public Double getDouble(String column) throws NumberFormatException {
    return parseDouble(values.get(column));
  }

  public Boolean getBoolean(String column) {
    return parseBoolean(values.get(column));
  }

  public String getString(String column) {
    return parseString(values.get(column));
  }

  public Integer[] getIntegerArray(String column, String delimiter) throws NumberFormatException {
    String[] tokens = values.get(column).split(delimiter, -1);
    Integer[] array = new Integer[tokens.length];
    for (int i = 0; i < tokens.length; i++) {
      array[i] = parseInteger(tokens[i]);
    }
    return array;
  }

  public Long[] getLongArray(String column, String delimiter) throws NumberFormatException {
    String[] tokens = values.get(column).split(delimiter, -1);
    Long[] array = new Long[tokens.length];
    for (int i = 0; i < tokens.length; i++) {
      array[i] = parseLong(tokens[i]);
    }
    return array;
  }

  public Float[] getFloatArray(String column, String delimiter) throws NumberFormatException {
    String[] tokens = values.get(column).split(delimiter, -1);
    Float[] array = new Float[tokens.length];
    for (int i = 0; i < tokens.length; i++) {
      array[i] = parseFloat(tokens[i]);
    }
    return array;
  }

  public Double[] getDoubleArray(String column, String delimiter) throws NumberFormatException {
    String[] tokens = values.get(column).split(delimiter, -1);
    Double[] array = new Double[tokens.length];
    for (int i = 0; i < tokens.length; i++) {
      array[i] = parseDouble(tokens[i]);
    }
    return array;
  }

  public Boolean[] getBooleanArray(String column, String delimiter) {
    String[] tokens = values.get(column).split(delimiter, -1);
    Boolean[] array = new Boolean[tokens.length];
    for (int i = 0; i < tokens.length; i++) {
      array[i] = parseBoolean(tokens[i]);
    }
    return array;
  }

  public String[] getStringArray(String column, String delimiter) {
    String[] tokens = values.get(column).split(delimiter, -1);
    String[] array = new String[tokens.length];
    for (int i = 0; i < tokens.length; i++) {
      array[i] = parseString(tokens[i]);
    }
    return array;
  }

  private Integer parseInteger(String token) throws NumberFormatException {
    if (token.isEmpty()) {
      return null;
    } else {
      return Integer.parseInt(token);
    }
  }

  private Long parseLong(String token) throws NumberFormatException {
    if (token.isEmpty()) {
      return null;
    } else {
      return Long.parseLong(token);
    }
  }

  private Float parseFloat(String token) throws NumberFormatException {
    if (token.isEmpty()) {
      return null;
    } else {
      return Float.parseFloat(token);
    }
  }

  private Double parseDouble(String token) throws NumberFormatException {
    if (token.isEmpty()) {
      return null;
    } else {
      return Double.parseDouble(token);
    }
  }

  private Boolean parseBoolean(String token) {
    if (token.isEmpty()) {
      return null;
    } else {
      return Boolean.parseBoolean(token);
    }
  }

  private String parseString(String token) {
    if (token.isEmpty()) {
      return null;
    } else {
      return token;
    }
  }

  @Override
  public void close() throws IOException {
    reader.close();
  }
}
