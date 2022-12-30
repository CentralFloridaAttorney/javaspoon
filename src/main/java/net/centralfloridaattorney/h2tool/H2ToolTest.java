package net.centralfloridaattorney.h2tool;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class H2ToolTest {
    String JDBC_URL = "jdbc:h2:file:~/h2/h2ToolTest";
    H2Tool h2Tool;

    @BeforeEach
    void setUp() {
        h2Tool = H2Tool.getInstance();
        h2Tool.init_destructive();
    }

    @Test
    void getIndex(){
        h2Tool.put(0, "remoteaddress", "00000");
        h2Tool.put(1, "remoteaddress", "11111");
        h2Tool.put(2, "remoteaddress", "22222");
        h2Tool.put(3, "remoteaddress", "33333");
        String[][] dataArray = h2Tool.getValues(false);
        int index = h2Tool.getIndex("remoteAddress", "22222");
        assertEquals(2, index);
        h2Tool.deleteIndex(index);
        index = h2Tool.getIndex("remoteAddress", "22222");
        assertEquals(0, index);
    }

    @Test
    void deleteIndex(){
        h2Tool.put(0, "remoteaddress", "00000");
        h2Tool.put(1, "remoteaddress", "11111");
        h2Tool.put(2, "remoteaddress", "22222");
        h2Tool.put(3, "remoteaddress", "33333");
        String[][] dataArray = h2Tool.getValues(false);
        assertEquals("22222", dataArray[1][1]);
        dataArray = h2Tool.getValues(true);
        assertEquals("11111", dataArray[1][1]);
        h2Tool.deleteIndex(1);
        dataArray = h2Tool.getValues(true);
        assertEquals("22222", dataArray[1][1]);
    }

    @Test
    void put() {
        //Test pu with 0 index
        h2Tool.put(0, "remoteaddress", "00000");
        assertEquals("remoteaddress:default", h2Tool.get(0, "remoteaddress"));
        //Test valid put indexes
        h2Tool.put(1, "remoteaddress", "11111");
        h2Tool.put(2, "remoteaddress", "22222");
        h2Tool.put(3, "remoteaddress", "33333");
        assertEquals("11111", h2Tool.get(1, "remoteaddress"));
        assertEquals("22222", h2Tool.get(2, "remoteaddress"));
        assertEquals("33333", h2Tool.get(3, "remoteaddress"));
        assertEquals("remoteaddress:default", h2Tool.get(4, "remoteaddress"));

        //Test put new columnName
        h2Tool.put(1, "newColumn", "newValue");
        assertEquals("newValue", h2Tool.get(1, "newColumn"));

        String[][] dataArray = {
                {"message", "phonenumber"},
                {"john", "8503213875"},
                {"melanie", "4074462689"}
        };

        h2Tool.put(dataArray);
        String phoneNumber = h2Tool.get(1, "phonenumber");
        assertEquals("8503213875", phoneNumber);
    }

    @Test
    void get() {
        h2Tool.put(1, "remoteaddress", "11111");
        h2Tool.put(2, "remoteaddress", "22222");
        h2Tool.put(3, "remoteaddress", "33333");
        assertEquals("22222", h2Tool.get(2, "remoteaddress"));
    }

    @Test
    void getValues() {
        h2Tool.put(1, "remoteaddress", "11111");
        h2Tool.put(2, "remoteaddress", "22222");
        h2Tool.put(3, "remoteaddress", "33333");
        String[][] dataArray = h2Tool.getValues(false);
        assertEquals("22222", dataArray[1][1]);
        dataArray = h2Tool.getValues(true);
        assertEquals("11111", dataArray[1][1]);

    }

    @Test
    void getKeys() {
        String[] keys = h2Tool.getKeys();
        String[] expected = {"INDEX"};
        assertEquals(keys[0], expected[0]);
    }
}