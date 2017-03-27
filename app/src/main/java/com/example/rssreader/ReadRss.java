package com.example.rssreader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class ReadRss extends AsyncTask<Void, Void, Void> {
    Context context;
    String address = "http://www.tantestorie.it/feed";
    ProgressDialog progressDialog;
    ArrayList<FeedItem>feedItems;
    RecyclerView recyclerView;
    URL url;
    public ProgressBar mProgress;

    //
    public void setProgressBar(ProgressBar bar) {
        this.mProgress = bar;
    }


    public ReadRss(Context context,RecyclerView recyclerView) {
        this.recyclerView=recyclerView;
        this.context = context;
        //progressDialog = new ProgressDialog(context);
        //progressDialog.setProgressStyle(0);
        //progressDialog.setMessage("Download delle storie in corso...");
    }

    @Override
    protected void onPreExecute() {
        //progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mProgress.setVisibility(View.GONE);
        //progressDialog.dismiss();
        //mProgress.setVisibility(View.GONE);
        MyAdapter adapter=new MyAdapter(context,feedItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new VerticalSpace(50));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected Void doInBackground(Void... params) {
        ProcessXml(Getdata());

        return null;
    }

    private void ProcessXml(Document data) {
        if (data != null) {
            feedItems=new ArrayList<>();
            Element root = data.getDocumentElement();
            Node channel = root.getChildNodes().item(1);
            NodeList items = channel.getChildNodes();
            for (int i = 0; i < items.getLength(); i++) {
                Node cureentchild = items.item(i);
                if (cureentchild.getNodeName().equalsIgnoreCase("item")) {
                    FeedItem item=new FeedItem();
                    NodeList itemchilds = cureentchild.getChildNodes();
                    for (int j = 0; j < itemchilds.getLength(); j++) {
                        Node cureent = itemchilds.item(j);
                        if (cureent.getNodeName().equalsIgnoreCase("title")){
                            item.setTitle(cureent.getTextContent());
                            //String title=cureent.getTextContent();
                            //Log.d(title,"testTitle");
                        }/*else if (cureent.getNodeName().equalsIgnoreCase("description")){
                            //String descr = cureent.;
                            //Toast.makeText(this, descr, Toast.LENGTH_LONG).show();
                            item.setDescription(cureent.getTextContent());
                        }*/else if (cureent.getNodeName().equalsIgnoreCase("pubDate")){
                            String aux=cureent.getTextContent();
                            aux= aux.substring(0, aux.length() - 6);
                            item.setPubDate(aux);
                        }else if (cureent.getNodeName().equalsIgnoreCase("link")){
                            item.setLink(cureent.getTextContent());
                        }else if (cureent.getNodeName().equalsIgnoreCase("description")) {
                            String description = cureent.getTextContent();
                            //Log.d(description,"test");
                            item.setThumbnailUrl(extractImageUrl(description));
                            //item.setDescription(cureent.getTextContent());
                            item.setDescription(extractDescription(description));

                        }

                            /*else if (cureent.getNodeName().equalsIgnoreCase("media:thumbnail")){
                            //this will return us thumbnail url
                            String url=cureent.getAttributes().item(0).getTextContent();
                            item.setThumbnailUrl(url);
                        }*/
                    }
                    feedItems.add(item);





                }
            }
        }
    }

    public Document Getdata() {
        try {
            url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDoc = builder.parse(inputStream);
            return xmlDoc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private String extractImageUrl(String description) {
        org.jsoup.nodes.Document document = Jsoup.parse(description);
        Elements imgs = document.select("img");

        for (org.jsoup.nodes.Element img : imgs) {
            if (img.hasAttr("src")) {
                return img.attr("src");
            }
        }

        // no image URL
        return "";
    }
    private String extractDescription(String description) {
        org.jsoup.nodes.Document document = Jsoup.parse(description);
        Elements imgs = document.select("p");
        return imgs.text();
    }
}
