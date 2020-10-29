package com.zbiyikli.sgetter.Domains;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;
import com.zbiyikli.sgetter.Utils.Utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Vlare {

    public static void fetch(String url, final SourceGetter.OnTaskCompleted onTaskCompleted){
        AndroidNetworking.get(url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<XModel> xModels = parseVideo(response);
                        if (xModels.isEmpty()){
                            onTaskCompleted.onError();
                        }else onTaskCompleted.onTaskCompleted(Utils.sortMe(xModels), true);
                    }

                    @Override
                    public void onError(ANError anError) {
                        onTaskCompleted.onError();
                    }
                });
    }

    private static ArrayList<XModel> parseVideo(String html){
        final String regex = "\"file\":\"(.*?)\",\"label\":\"(.*?)\"";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(html);
        ArrayList<XModel> xModels = new ArrayList<>();
        while (matcher.find()) {
            XModel xModel = new XModel();
            xModel.setQuality(matcher.group(2));
            xModel.setUrl(matcher.group(1));
            xModels.add(xModel);
        }
        return xModels;
    }
}