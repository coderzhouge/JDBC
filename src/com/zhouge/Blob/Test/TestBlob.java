package com.zhouge.Blob.Test;

import com.zhouge.Blob.BlobMethod;
import org.junit.Test;

public class TestBlob {

    @Test
    public void  testInsertBlob(){
        new BlobMethod().InsertBlob();
    }


    @Test
    public void testQueryBlob(){
        new BlobMethod().QueryBlob();
    }







}
