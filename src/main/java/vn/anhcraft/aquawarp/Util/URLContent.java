package vn.anhcraft.aquawarp.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Copyright (c) by Anh Craft. All rights reserved.
 * Licensed under the apache license v2.0.
 */
public class URLContent {
    public static BufferedReader get(String u) {
        URL url;

        try {
            url = new URL(u);
            URLConnection conn = url.openConnection();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            conn.getInputStream()
                    )
            );

            return br;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
