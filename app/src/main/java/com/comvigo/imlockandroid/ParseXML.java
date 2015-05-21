package com.comvigo.imlockandroid;

import android.os.Environment;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Dmitry on 20.05.2015.
 */
public class ParseXML {

    public List<String> getBlockList() {
        return parseXML("black");
    }

    public List<String> getWhiteList() {
        return parseXML("white");
    }

    public List<String> blockAllOthers() {
        return parseXML("other");
    }

    private String getXML() {
        String sCurrentLine;
        String res = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "IMLockData.txt"));
            while ((sCurrentLine = br.readLine()) != null) {
                res += sCurrentLine;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    private List<String> parseXML(String command) {
        String xmlString = getXML();
        List<String> list = new ArrayList<>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            byte[] bytes = Charset.forName("UTF-16LE").encode(xmlString).array();
            InputStream inputStream = new ByteArrayInputStream(bytes);
            Document doc = db.parse(inputStream);
            switch (command) {
                case "black":
                    list = getBlock(doc);
                    break;
                case "white":
                    list = getWhite(doc);
                    break;
                case "other":
                    list = blockAllOthers(doc);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i =0; i<list.size(); i++){
            Log.d("!!", list.get(i));
        }
        return list;
    }

    private List<String> getBlock(Document doc) {
        List<String> blockList = new ArrayList<>();
        List<String> categories = new ArrayList<>();
        categories.add("Blocked");
        categories.add("PornSites");
        categories.add("Programs");
        for (int i = 0; i < categories.size(); i++) {
            NodeList entries = doc.getElementsByTagName(categories.get(i));
            Element node1 = (Element) entries.item(0);
            NodeList name = node1.getElementsByTagName("Site");
            int num = name.getLength();
            for (int j = 0; j < num; j++) {
                Element node = (Element) name.item(j);
                NodeList description = node.getElementsByTagName("description");
                String keyword = description.item(0).getFirstChild().getTextContent();
                blockList.add(keyword);
            }
        }
        return blockList;
    }

    private List<String> getWhite(Document doc) {
        List<String> whiteList = new ArrayList<>();
        NodeList entries = doc.getElementsByTagName("WhiteList");
        Element node1 = (Element) entries.item(0);
        NodeList name = node1.getElementsByTagName("Site");
        int num = name.getLength();
        for (int j = 0; j < num; j++) {
            Element node = (Element) name.item(j);
            NodeList description = node.getElementsByTagName("description");
            String keyword = description.item(0).getFirstChild().getTextContent();
            whiteList.add(keyword);
        }
        return whiteList;
    }

    private List<String> blockAllOthers(Document doc) {
        List<String> param = new ArrayList<>();
        NodeList entries = doc.getElementsByTagName("IsBlockAllOthers");
        param.add(entries.item(0).getFirstChild().getTextContent());
        return param;
    }

}