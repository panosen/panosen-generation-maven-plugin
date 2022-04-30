package com.panosen;

import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathTest {

    @Test
    public void test() {
        Path path = Paths.get("a", "b", "1.txt");

        String fileSeparator = System.getProperty("file.separator");

        String actual = path.toString();
        String expected = "a" + fileSeparator + "b" + fileSeparator + "1.txt";

        Assert.assertEquals(expected, actual);
    }

}