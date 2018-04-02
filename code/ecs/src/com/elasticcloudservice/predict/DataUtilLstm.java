package com.elasticcloudservice.predict;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilLstm {
    public static int[][] allTypesOfFlavors = {{1,1024},{1,2048},{1,4096},{2,2048},{2,4096},
            {2,8192},{4,4096},{4,8192},{4,16384},{8,8192},{8,16384},{8,32768},{16,16384},
            {16,32768},{16,65536}};
    /**
     *get datalist from data array
     * param dataArray:the String data array,every line like (id,flavori,date)
     * return ArrayList<double[]>:ArrayList.get(i)[j],shows the number of day(i+1) of flavor(j)
     * ArrayList.get(i)[0] show number of flavor not in the flavor list
     */
    public static double[][] loadDataFromStringArray(String[] dataArray) {
        String date = null;
        int day = 0;
        int flag = 0;
        int cpu = 0;
        int memory = 0;
        int weekday = 0;
        ArrayList<double[]> dataList = new ArrayList<>();
        for (String tempString : dataArray) {
            String[] tempData = tempString.split("\t");
            if (flag == 0) {
                dataList.add(new double[24]);
                date = tempData[2];
                flag++;
                weekday = getDayOfWeek(date);
            }
            int daysBtn = calDaysBetween(date,tempData[2]);
            //same day
            if (daysBtn==0) {
                int flavorNumber = getNumberOfFlavor(tempData[1]);
                if (flavorNumber!=0){
                    ((dataList.get(day))[flavorNumber-1])++;
                    cpu = cpu + allTypesOfFlavors[flavorNumber-1][0];
                    memory = memory + allTypesOfFlavors[flavorNumber-1][1];
                }
            }
            //new day
            else if(daysBtn==1) {
                dataList.get(day)[15] = cpu;
                dataList.get(day)[16] = memory;
                dataList.get(day)[weekday] = 1;
                date = tempData[2];
                cpu = 0;
                memory = 0;
                weekday = getDayOfWeek(date);
                day++;
                dataList.add(new double[24]);
                int flavorNumber = getNumberOfFlavor(tempData[1]);
                if (flavorNumber!=0){
                    ((dataList.get(day))[flavorNumber-1])++;
                    cpu = cpu + allTypesOfFlavors[flavorNumber-1][0];
                    memory = memory + allTypesOfFlavors[flavorNumber-1][1];
                }
            }
            //miss days
            else {
                dataList.get(day)[15] = cpu;
                dataList.get(day)[16] = memory;
                dataList.get(day)[weekday] = 1;
                date = tempData[2];
                cpu = 0;
                memory = 0;
                for (int i=0;i<daysBtn-1;i++) {
                    day++;
                    dataList.add(new double[24]);
                    dataList.get(day)[weekday+1+i>23?(16+(weekday+i-22)%7):weekday+1+i] = 1;
                }
                weekday = getDayOfWeek(date);
                day++;
                dataList.add(new double[24]);
                int flavorNumber = getNumberOfFlavor(tempData[1]);
                if (flavorNumber!=0){
                    ((dataList.get(day))[flavorNumber-1])++;
                    cpu = cpu + allTypesOfFlavors[flavorNumber-1][0];
                    memory = memory + allTypesOfFlavors[flavorNumber-1][1];
                }
            }
        }
        dataList.get(day)[15] = cpu;
        dataList.get(day)[16] = memory;
        dataList.get(day)[weekday] = 1;
        //data pre deal
//        for (int i = 0; i < dataList.size(); i++)
//            for (int j=1;j<dataList.get(i).length;j++){
//                dataList.get(i)[j] = Math.pow(Math.E, dataList.get(i)[j]);
//            }
        return listArrayToArrays(dataList);
    }

    public static double[][] loadDataFromStringArrayWithNoWeek(String[] dataArray) {
        String date = null;
        int day = 0;
        int flag = 0;
        ArrayList<double[]> dataList = new ArrayList<>();
        for (String tempString : dataArray) {
            String[] tempData = tempString.split("\t");
            if (flag == 0) {
                dataList.add(new double[15]);
                date = tempData[2];
                flag++;
            }
            int daysBtn = calDaysBetween(date,tempData[2]);
            //same day
            if (daysBtn==0) {
                int flavorNumber = getNumberOfFlavor(tempData[1]);
                if (flavorNumber!=0){
                    ((dataList.get(day))[flavorNumber-1])++;
                }
            }
            //new day
            else if(daysBtn==1) {
                date = tempData[2];
                day++;
                dataList.add(new double[15]);
                int flavorNumber = getNumberOfFlavor(tempData[1]);
                if (flavorNumber!=0){
                    ((dataList.get(day))[flavorNumber-1])++;
                }
            }
            //miss days
            else {
                date = tempData[2];
                for (int i=0;i<daysBtn-1;i++) {
                    day++;
                    dataList.add(new double[15]);
                }
                day++;
                dataList.add(new double[15]);
                int flavorNumber = getNumberOfFlavor(tempData[1]);
                if (flavorNumber!=0){
                    ((dataList.get(day))[flavorNumber-1])++;

                }
            }
        }
        //data pre deal
//        for (int i = 0; i < dataList.size(); i++)
//            for (int j=1;j<dataList.get(i).length;j++){
//                dataList.get(i)[j] = Math.pow(Math.E, dataList.get(i)[j]);
//            }
        return listArrayToArrays(dataList);
    }


    //get avg and std from double array,result[0] for avg,result[1] for std
    public static double[][] getAvgAndStdFromArray(double[][] array) {
        double[][] result = new double[2][array[0].length-7];
        double avg = 0;
        double std = 0;
        double sum = 0;
        for (int i=0;i<array[0].length-7;i++) {
            sum = 0;
            for (int j=0;j<array.length;j++) {
                sum = sum + array[j][i];
            }
            avg = sum / array.length;
            sum = 0;
            for (int j=0;j<array.length;j++){
                sum =sum + Math.pow((array[j][i] - avg),2);
            }
            std = Math.sqrt(sum);
            result[0][i] = avg;
            result[1][i] = std;
        }
        return result;
    }

    //calculate days between two date
    public static int calDaysBetween(String date1, String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        try {
            calendar1.setTime(sdf.parse(date1));
            calendar2.setTime(sdf.parse(date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar2.get(Calendar.DAY_OF_YEAR) - calendar1.get(Calendar.DAY_OF_YEAR);
    }

    //get flavor number
    public static int getNumberOfFlavor(String s) {
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher m = pattern.matcher(s);
        if (m.find()) {
            if (Integer.parseInt(m.group(1))>15)
                return 0;
            else
                return Integer.parseInt(m.group(1));
        }
        else
            return 0;
    }

    //get day of week,return 17 for sunday
    public static int getDayOfWeek(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 16+calendar.get(Calendar.DAY_OF_WEEK);
    }

    //change listArray to arrays
    public static double[][] listArrayToArrays(List<double[]> listArray) {
        double[][] array = new double[listArray.size()][];
        int i = 0;
        for (double[] row : listArray) {
            array[i++] = row;
        }
        return array;
    }
}
