package com.m2dl.helloandroid.helloandroid2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by root on 08/01/15.
 */
public class Reseau extends Activity {

    TextView tv;

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        tv = new TextView(this);
        setContentView(tv);

        try {
            URL url = new URL("http://www.meteo-toulouse.org");
            URLConnection conn = url.openConnection();

            // Get the response

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String currentline = "";

            while ((currentline = rd.readLine()) != null) {
                tv.append(currentline + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
