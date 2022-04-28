package com.zhouge.Batch;

import com.zhouge.Batch.method.TestInsert;
import org.junit.Test;

public class TestMethod {

    @Test
    public void testInsert1(){
        new TestInsert().testPrepareStatementForBatch();
    }

    @Test
    public void testInsert2(){
        new TestInsert().testPrepareStatementWithBatch();
    }

    @Test
    public void testInsert3(){
        new TestInsert().testPrepareStatementWithTransaction();
    }
}
