package com.comvigo.imlockandroid;

import android.os.Environment;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Dmitry on 20.05.2015.
 */
public class ParseXML {

    public List<String> getBlockList() {
        getXML();
        return parseXML("black");
    }

    public List<String> getWhiteList() {
        return parseXML("white");
    }

    public List<String> blockAllOthers() {
        return parseXML("other");
    }

    public void getXML(){
        String sCurrentLine;
        String res = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(
                    Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "IMLockData.txt"));
            while ((sCurrentLine = br.readLine()) != null) {
                res += sCurrentLine;
            }
            String utf8 = res.replace("utf-16", "utf-8");
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + "IMLockData.txt");
            FileOutputStream fop = new FileOutputStream(file);
            byte[] contentInBytes = utf8.getBytes();
            fop.write(contentInBytes);
            fop.flush();
            fop.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> parseXML(String command) {

        List<String> list = new ArrayList<>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + "IMLockData.txt"));
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