package com.piyush.pictattendance.data.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ListView;

import com.piyush.pictattendance.model.Subject;
import com.piyush.pictattendance.model.UserWrapper;
import com.piyush.pictattendance.utils.Constants;
import com.piyush.pictattendance.utils.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class ResponseConvertor implements Converter<ResponseBody, UserWrapper> {

    public static class Factory extends Converter.Factory
    {
        @Nullable
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                                Annotation[] annotations,
                                                                Retrofit retrofit)
        {
        return new ResponseConvertor();
    }
    }


    @Override
    public UserWrapper convert(@NonNull ResponseBody value) throws IOException {

        Document document = Jsoup.parse(value.string());
        Elements rows = document
                .select("table[id=table10]")
                .get(0)
                .select("tr");
        if(rows==null)
            return null;
        Element element = document
                .select("table[id=table5]")
                .get(0);
        String name = element
                .select("tr")
                .get(4)
                .select("td")
                .get(1)
                .text();
        List<Subject> subjects = new ArrayList<>();
        String year = element
                .select("tr")
                .get(8)
                .select("td")
                .get(1)
                .text();
        double percent = 0f;
        for(int i = 2;i<rows.size()-1;i++)
        {
            Elements cols = (rows.get(i))
                    .select("td");
            Subject subject = parseSubject(cols);
                percent += subject.getAttendance();
                subjects.add(subject);


        }
        double average ;
        try {
             average = Double.parseDouble(rows.get(rows.size() - 1).select("td").get(2).text());
        }catch (NumberFormatException e)
        {
            average=percent;
        }
        if(Constants.DEBUG)
        Log.d("SIZEESUBJECT",String.valueOf(subjects.size()));
        percent = Utils.round(percent / subjects.size(), 2);
            if(subjects.isEmpty())
            return null;

        if(average==percent)
        return new UserWrapper(name,year,subjects, percent,false);
        else
        {
            Log.d("HIII", String.valueOf(percent));
            percent=0;
            //for (Subject subject: subjects)
            int size = subjects.size();
            List<Subject> temp = new ArrayList<>();

            for(int i=0;i<size;i++) {
             Subject subject = subjects.get(i);
                if (!(subject.getAttendance() == 0 && subject.getPresent() == 0 && subject.getTotal() == 0)) {
                    temp.add(subject);
                    percent+=subject.getAttendance();
                }
            }
            percent = Utils.round(percent / temp.size(), 2);


            return new UserWrapper(name,year,temp, percent,false);

        }
    }


    private Subject parseSubject(Elements element)
    {
        return Subject
                .create(element.get(0).text(),
                        element.get(1).text(),
                        element.get(2).text());

    }
}
