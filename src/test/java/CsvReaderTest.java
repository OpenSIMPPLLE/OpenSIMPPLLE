import org.junit.Test;
import simpplle.comcode.CsvReader;

import java.io.File;
import java.io.FileWriter;

import static org.junit.Assert.*;

public class CsvReaderTest {

  @Test
  public void nextRecord() throws Exception {

    File file = File.createTempFile("temp",".csv");
    FileWriter writer = new FileWriter(file);
    writer.write("field\n");
    writer.write("value");
    writer.close();

    CsvReader reader = new CsvReader(file,",");
    assertTrue(reader.nextRecord());
    assertFalse(reader.nextRecord());

  }

  @Test
  public void hasField() throws Exception {

    File file = File.createTempFile("temp",".csv");
    FileWriter writer = new FileWriter(file);
    writer.write("a,b");
    writer.close();

    CsvReader reader = new CsvReader(file,",");
    assertTrue(reader.hasField("a"));
    assertTrue(reader.hasField("b"));
    assertFalse(reader.hasField("c"));

  }

  @Test
  public void getInteger() throws Exception {

    File file = File.createTempFile("temp",".csv");
    FileWriter writer = new FileWriter(file);
    writer.write("field\n");
    writer.write(Integer.MAX_VALUE + "\n");
    writer.write(Integer.MIN_VALUE + "\n");
    writer.write("\n");
    writer.close();

    CsvReader reader = new CsvReader(file,",");

    assertTrue(reader.nextRecord());
    assertTrue(reader.getInteger("field") == Integer.MAX_VALUE);

    assertTrue(reader.nextRecord());
    assertTrue(reader.getInteger("field") == Integer.MIN_VALUE);

    assertTrue(reader.nextRecord());
    assertNull(reader.getInteger("field"));

  }

  @Test
  public void getLong() throws Exception {

    File file = File.createTempFile("temp",".csv");
    FileWriter writer = new FileWriter(file);
    writer.write("field\n");
    writer.write(Long.MAX_VALUE + "\n");
    writer.write(Long.MIN_VALUE + "\n");
    writer.write("\n");
    writer.close();

    CsvReader reader = new CsvReader(file,",");

    assertTrue(reader.nextRecord());
    assertTrue(reader.getLong("field") == Long.MAX_VALUE);

    assertTrue(reader.nextRecord());
    assertTrue(reader.getLong("field") == Long.MIN_VALUE);

    assertTrue(reader.nextRecord());
    assertNull(reader.getLong("field"));

  }

  @Test
  public void getFloat() throws Exception {

    File file = File.createTempFile("temp",".csv");
    FileWriter writer = new FileWriter(file);
    writer.write("field\n");
    writer.write(Float.MAX_VALUE + "\n");
    writer.write(Float.MIN_VALUE + "\n");
    writer.write("\n");
    writer.close();

    CsvReader reader = new CsvReader(file,",");

    assertTrue(reader.nextRecord());
    assertTrue(reader.getFloat("field") == Float.MAX_VALUE);

    assertTrue(reader.nextRecord());
    assertTrue(reader.getFloat("field") == Float.MIN_VALUE);

    assertTrue(reader.nextRecord());
    assertNull(reader.getFloat("field"));

  }

  @Test
  public void getDouble() throws Exception {

    File file = File.createTempFile("temp",".csv");
    FileWriter writer = new FileWriter(file);
    writer.write("field\n");
    writer.write(Double.MAX_VALUE + "\n");
    writer.write(Double.MIN_VALUE + "\n");
    writer.write("\n");
    writer.close();

    CsvReader reader = new CsvReader(file,",");

    assertTrue(reader.nextRecord());
    assertTrue(reader.getDouble("field") == Double.MAX_VALUE);

    assertTrue(reader.nextRecord());
    assertTrue(reader.getDouble("field") == Double.MIN_VALUE);

    assertTrue(reader.nextRecord());
    assertNull(reader.getDouble("field"));

  }

  @Test
  public void getBoolean() throws Exception {

    File file = File.createTempFile("temp",".csv");
    FileWriter writer = new FileWriter(file);
    writer.write("field\n");
    writer.write("true\n");
    writer.write("True\n");
    writer.write("false\n");
    writer.write("False\n");
    writer.write("\n");
    writer.close();

    CsvReader reader = new CsvReader(file,",");

    assertTrue(reader.nextRecord());
    assertTrue(reader.getBoolean("field"));

    assertTrue(reader.nextRecord());
    assertTrue(reader.getBoolean("field"));

    assertTrue(reader.nextRecord());
    assertFalse(reader.getBoolean("field"));

    assertTrue(reader.nextRecord());
    assertFalse(reader.getBoolean("field"));

    assertTrue(reader.nextRecord());
    assertNull(reader.getBoolean("field"));

  }

  @Test
  public void getString() throws Exception {

    File file = File.createTempFile("temp",".csv");
    FileWriter writer = new FileWriter(file);
    writer.write("field\n");
    writer.write("string\n");
    writer.write("\n");
    writer.close();

    CsvReader reader = new CsvReader(file,",");

    assertTrue(reader.nextRecord());
    assertEquals(reader.getString("field"),"string");

    assertTrue(reader.nextRecord());
    assertNull(reader.getString("field"));

  }

  @Test
  public void getIntegerArray() throws Exception {

    File file = File.createTempFile("temp",".csv");
    FileWriter writer = new FileWriter(file);
    writer.write("field\n");
    writer.write(Integer.MIN_VALUE + ":" + Integer.MAX_VALUE + "\n");
    writer.write(":\n");
    writer.write(Integer.MIN_VALUE + "\n");
    writer.write("\n");
    writer.close();

    CsvReader reader = new CsvReader(file,",");

    assertTrue(reader.nextRecord());
    Integer[] populated = reader.getIntegerArray("field",":");
    assertTrue(populated[0] == Integer.MIN_VALUE);
    assertTrue(populated[1] == Integer.MAX_VALUE);

    assertTrue(reader.nextRecord());
    Integer[] unpopulated = reader.getIntegerArray("field",":");
    assertNull(unpopulated[0]);
    assertNull(unpopulated[1]);

    assertTrue(reader.nextRecord());
    Integer[] one = reader.getIntegerArray("field",":");
    assertTrue(one[0] == Integer.MIN_VALUE);

    assertTrue(reader.nextRecord());
    Integer[] empty = reader.getIntegerArray("field",":");
    assertNull(empty[0]);

  }

  @Test
  public void getLongArray() throws Exception {

    File file = File.createTempFile("temp",".csv");
    FileWriter writer = new FileWriter(file);
    writer.write("field\n");
    writer.write(Long.MIN_VALUE + ":" + Long.MAX_VALUE + "\n");
    writer.write(":\n");
    writer.write(Long.MIN_VALUE + "\n");
    writer.write("\n");
    writer.close();

    CsvReader reader = new CsvReader(file,",");

    assertTrue(reader.nextRecord());
    Long[] populated = reader.getLongArray("field",":");
    assertTrue(populated[0] == Long.MIN_VALUE);
    assertTrue(populated[1] == Long.MAX_VALUE);

    assertTrue(reader.nextRecord());
    Long[] unpopulated = reader.getLongArray("field",":");
    assertNull(unpopulated[0]);
    assertNull(unpopulated[1]);

    assertTrue(reader.nextRecord());
    Long[] one = reader.getLongArray("field",":");
    assertTrue(one[0] == Long.MIN_VALUE);

    assertTrue(reader.nextRecord());
    Long[] empty = reader.getLongArray("field",":");
    assertNull(empty[0]);

  }

  @Test
  public void getFloatArray() throws Exception {

    File file = File.createTempFile("temp",".csv");
    FileWriter writer = new FileWriter(file);
    writer.write("field\n");
    writer.write(Float.MIN_VALUE + ":" + Float.MAX_VALUE + "\n");
    writer.write(":\n");
    writer.write(Float.MIN_VALUE + "\n");
    writer.write("\n");
    writer.close();

    CsvReader reader = new CsvReader(file,",");

    assertTrue(reader.nextRecord());
    Float[] populated = reader.getFloatArray("field",":");
    assertTrue(populated[0] == Float.MIN_VALUE);
    assertTrue(populated[1] == Float.MAX_VALUE);

    assertTrue(reader.nextRecord());
    Float[] unpopulated = reader.getFloatArray("field",":");
    assertNull(unpopulated[0]);
    assertNull(unpopulated[1]);

    assertTrue(reader.nextRecord());
    Float[] one = reader.getFloatArray("field",":");
    assertTrue(one[0] == Float.MIN_VALUE);

    assertTrue(reader.nextRecord());
    Float[] empty = reader.getFloatArray("field",":");
    assertNull(empty[0]);

  }

  @Test
  public void getDoubleArray() throws Exception {

    File file = File.createTempFile("temp",".csv");
    FileWriter writer = new FileWriter(file);
    writer.write("field\n");
    writer.write(Double.MIN_VALUE + ":" + Double.MAX_VALUE + "\n");
    writer.write(":\n");
    writer.write(Double.MIN_VALUE + "\n");
    writer.write("\n");
    writer.close();

    CsvReader reader = new CsvReader(file,",");

    assertTrue(reader.nextRecord());
    Double[] populated = reader.getDoubleArray("field",":");
    assertTrue(populated[0] == Double.MIN_VALUE);
    assertTrue(populated[1] == Double.MAX_VALUE);

    assertTrue(reader.nextRecord());
    Double[] unpopulated = reader.getDoubleArray("field",":");
    assertNull(unpopulated[0]);
    assertNull(unpopulated[1]);

    assertTrue(reader.nextRecord());
    Double[] one = reader.getDoubleArray("field",":");
    assertTrue(one[0] == Double.MIN_VALUE);

    assertTrue(reader.nextRecord());
    Double[] empty = reader.getDoubleArray("field",":");
    assertNull(empty[0]);

  }

  @Test
  public void getBooleanArray() throws Exception {

    File file = File.createTempFile("temp",".csv");
    FileWriter writer = new FileWriter(file);
    writer.write("field\n");
    writer.write("true:True:false:False\n");
    writer.write(":::\n");
    writer.write("true\n");
    writer.write("\n");
    writer.close();

    CsvReader reader = new CsvReader(file,",");

    assertTrue(reader.nextRecord());
    Boolean[] populated = reader.getBooleanArray("field",":");
    assertTrue(populated[0]);
    assertTrue(populated[1]);
    assertFalse(populated[2]);
    assertFalse(populated[3]);

    assertTrue(reader.nextRecord());
    Boolean[] unpopulated = reader.getBooleanArray("field",":");
    assertNull(unpopulated[0]);
    assertNull(unpopulated[1]);
    assertNull(unpopulated[2]);
    assertNull(unpopulated[3]);

    assertTrue(reader.nextRecord());
    Boolean[] one = reader.getBooleanArray("field",":");
    assertTrue(one[0]);

    assertTrue(reader.nextRecord());
    Boolean[] empty = reader.getBooleanArray("field",":");
    assertNull(empty[0]);

  }

  @Test
  public void getStringArray() throws Exception {

    File file = File.createTempFile("temp",".csv");
    FileWriter writer = new FileWriter(file);
    writer.write("field\n");
    writer.write("a:b:c\n");
    writer.write("::\n");
    writer.write("a\n");
    writer.write("\n");
    writer.close();

    CsvReader reader = new CsvReader(file,",");

    assertTrue(reader.nextRecord());
    String[] populated = reader.getStringArray("field",":");
    assertEquals(populated[0],"a");
    assertEquals(populated[1],"b");
    assertEquals(populated[2],"c");

    assertTrue(reader.nextRecord());
    String[] unpopulated = reader.getStringArray("field",":");
    assertNull(unpopulated[0]);
    assertNull(unpopulated[1]);
    assertNull(unpopulated[2]);

    assertTrue(reader.nextRecord());
    String[] one = reader.getStringArray("field",":");
    assertEquals(one[0],"a");

    assertTrue(reader.nextRecord());
    String[] empty = reader.getStringArray("field",":");
    assertNull(empty[0]);

  }

}