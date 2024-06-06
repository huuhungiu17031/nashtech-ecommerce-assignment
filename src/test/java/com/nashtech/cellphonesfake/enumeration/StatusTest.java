package com.nashtech.cellphonesfake.enumeration;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class StatusTypeTest {

    @Test
    void testPendingStatus() {
        assertEquals("PENDING", StatusType.PENDING.name());
    }

    @Test
    void testCompletedStatus() {
        assertEquals("COMPLETED", StatusType.COMPLETED.name());
    }

    @Test
    void testCancelledStatus() {
        assertEquals("CANCELLED", StatusType.CANCELLED.name());
    }

    @Test
    void testEnumValues() {
        StatusType[] statusTypes = StatusType.values();
        assertEquals(3, statusTypes.length);
        assertTrue(Arrays.asList(statusTypes).contains(StatusType.PENDING));
        assertTrue(Arrays.asList(statusTypes).contains(StatusType.COMPLETED));
        assertTrue(Arrays.asList(statusTypes).contains(StatusType.CANCELLED));
    }

    @Test
    void testEnumValueOf() {
        assertEquals(StatusType.PENDING, StatusType.valueOf("PENDING"));
        assertEquals(StatusType.COMPLETED, StatusType.valueOf("COMPLETED"));
        assertEquals(StatusType.CANCELLED, StatusType.valueOf("CANCELLED"));
    }
}