package com.example.finalyearproject;

import android.os.AsyncTask;

import com.example.finalyearproject.pager.TabbedInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class jSoup extends AsyncTask<Void, Void, Void> {

    private String url;

    public jSoup(String url) {
        this.url = url;
    }


    @Override
    protected Void doInBackground(Void... voids) {

        String[] array = {"Contents", "Notes[edit]", "References[edit]", "External links[edit]", "Navigation menu", "Further pictures[edit]"
                , "Gallery[edit]", "See also[edit]", "Further reading[edit]"};


        Document doc = null;
        try {
            String decodedURL = URLDecoder.decode(url, "UTF-8");
            doc = Jsoup.connect(decodedURL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element body = doc.body();


        Map<String, String> ElementArr = new HashMap<>();


        Elements elements1 = body.select("*");

        for (Element element : elements1) {
            if (element.tagName().equals("h2") == true && !Arrays.asList(array).contains(element.text())) {

                String x = "";

                Element node = element.nextElementSibling();

                if (node.tagName().equals("p") == true) {

                    x = x + node.text() + '\n' + '\n';
                }

                while (node != null && node.tagName().equals("h2") == false) {
                    node = node.nextElementSibling();

                    if (node != null) {
                        if (node.tagName().equals("p") == true) {

                            x = x + node.text() + '\n' + '\n';
                        }
                    }
                }

                String title = element.text();
                String regEx = "\\[(.)\\]";
                String updated = x.replaceAll(regEx, " ");
                ElementArr.put(title, updated);
            }

        }


        if (ElementArr.size() == 0) {
            Elements elements2 = body.select("p");
            TabbedInfo.setStringContent(elements2.text());
        } else {
            TabbedInfo.setMapContent(ElementArr);
        }

        return null;
    }

}