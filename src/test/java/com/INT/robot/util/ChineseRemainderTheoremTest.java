package com.INT.robot.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test ;

class ChineseRemainderTheoremTest
{
    @Test 
    void testSimpleRatio()
    {
        var crm = new ChineseRemainderTheorem(2., 3. ) ;

        System.out.println("Running non-looping test.") ;
        var frac= crm.getARotationNonLame(0.3,0.2) ;
        System.out.println("Rotation "+ String.valueOf(frac)) ;
        Assertions.assertTrue(
            frac == 0.3,
            "non-cyclic step angle");
    }
}