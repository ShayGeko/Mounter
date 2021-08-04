package com.example.mounter.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MounterStringUtilsTest {

    @Test
    void isNullOrEmpty() {
        String str1 = "";
        String str2 = null;
        String str3 = " ";

        Boolean expected1 = true,
                expected2 = true,
                expected3 = false;

        Boolean actual1 = MounterStringUtils.isNullOrEmpty(str1);
        Boolean actual2 = MounterStringUtils.isNullOrEmpty(str2);
        Boolean actual3 = MounterStringUtils.isNullOrEmpty(str3);

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
        assertEquals(expected3, actual3);
    }

    @Test
    void containsData() {
        String str1 = "";
        String str2 = null;
        String str3 = " ";

        Boolean expected1 = false,
                expected2 = false,
                expected3 = true;

        Boolean actual1 = MounterStringUtils.containsData(str1);
        Boolean actual2 = MounterStringUtils.containsData(str2);
        Boolean actual3 = MounterStringUtils.containsData(str3);

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
        assertEquals(expected3, actual3);
    }
}