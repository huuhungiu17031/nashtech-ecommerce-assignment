package com.nashtech.cellphonesfake.enumeration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentMethodTest {

    @Test
    void testShipCodeValue() {
        assertEquals("SHIP_CODE", PaymentMethod.SHIP_CODE.name());
    }

    @Test
    void testVnPayValue() {
        assertEquals("VN_PAY", PaymentMethod.VN_PAY.name());
    }

    @Test
    void testShipCodeOrdinal() {
        assertEquals(0, PaymentMethod.SHIP_CODE.ordinal());
    }

    @Test
    void testVnPayOrdinal() {
        assertEquals(1, PaymentMethod.VN_PAY.ordinal());
    }

    @Test
    void testValueOf() {
        assertEquals(PaymentMethod.SHIP_CODE, PaymentMethod.valueOf("SHIP_CODE"));
        assertEquals(PaymentMethod.VN_PAY, PaymentMethod.valueOf("VN_PAY"));
    }
}
