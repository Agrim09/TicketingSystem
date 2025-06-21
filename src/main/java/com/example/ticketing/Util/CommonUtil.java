package com.example.ticketing.Util;

import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.Date;

public class CommonUtil {

     public static String generateTrxId(String transaction) {
        // Format tanggal saat ini (yyyyMMdd)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String datePart = sdf.format(new Date());

        // Generate UUID dan ambil sebagian untuk membuat lebih ringkas
        String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        // Gabungkan menjadi order ID dengan format ORDER-yyyyMMdd-UUID
        return transaction + "-" + datePart + "-" + uuidPart;
    }
    
}
