package com.academy.android.starwarsmovies;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class ResourcesUtil {

  public static String loadJson(Context context) {
    String jsonString = null;
    InputStream is = context.getResources().openRawResource(R.raw.starwars_data);
    Writer writer = new StringWriter();
    char[] buffer = new char[1024];
    try {
      try {
        Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        int n;
        while ((n = reader.read(buffer)) != -1) {
          writer.write(buffer, 0, n);
        }
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      } finally {
        is.close();
        jsonString = writer.toString();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return jsonString;
  }
}
