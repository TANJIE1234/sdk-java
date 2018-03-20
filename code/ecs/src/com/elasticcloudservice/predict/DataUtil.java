package com.elasticcloudservice.predict;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DataUtil{
    /**
     * get datalist from data array
     * param dataArray:the String data array,every line like (id,flavori,date)
     * return ArrayList<double[]>:ArrayList.get(i)[j],shows the number of day(i+1) of flavor(j)
     * ArrayList.get(i)[0] show number of flavor not in the flavor list
     */
    public static ArrayList<double[]> loadDataFromStringArray(String[] dataArray) {
        String date = null;
        int day = 0;
        int flag = 0;
        ArrayList<double[]> dataList = new ArrayList<>();
        for (String tempString : dataArray) {
            String[] tempData = tempString.split("\t");
            if (flag == 0) {
                dataList.add(new double[16]);
                date = tempData[2];
                flag++;
            }
            int daysBtn = calDaysBetween(date,tempData[2]);
            if (daysBtn==0) {
                ((dataList.get(day))[getNumberOfFlavor(tempData[1])])++;
            }
            else {
                    date = tempData[2];
                    day++;
                    dataList.add(new double[16]);
                    (dataList.get(day))[getNumberOfFlavor(tempData[1])]++;
            }
            //else if(daysBtn==1) {
            //    date = tempData[2];
            //    day++;
            //    dataList.add(new double[16]);
            //    (dataList.get(day))[getNumberOfFlavor(tempData[1])]++;
            //}
            //else {
            //    date = tempData[2];
            //    for (int i=0;i<daysBtn-1;i++) {
            //        day++;
            //        dataList.add(new double[16]);
            //    }
            //    day++;
            //    dataList.add(new double[16]);
            //    (dataList.get(day))[getNumberOfFlavor(tempData[1])]++;
            //}
        }

        return dataList;
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

    /**
     * get flavor array from datalist
     * param flavor:the flavor number
     * param key:the number of key for predict
     * param datalist:the return of loadDataFromStringArray
     * return:double[][]:the data used to train
     *   1    x1    x2    ...    x(key)    x(key+1)
     *             ....
     *   1    x(dataList.size()-key)  ...  x(datalist.size())
     */
    public static double[][] getFlavorArrayFromDataList(int flavor, int key, List<double[]> dataList) {
        int row = dataList.size()-key;
        double[][] flavorData = new double[row][key + 2];
        for (int i=0; i<row; i++)
            for (int j=1; j<key+2; j++){
                flavorData[i][j] = dataList.get(j-1+i)[flavor];
            }
        for (int i=0;i<row;i++) {
            flavorData[i][0]=1.0;
        }
        //change to sum
//		for (int i=0;i<flavorData.length;i++) {
//			for (int j=2;j<flavorData[0].length;j++) {
//				flavorData[i][j]=flavorData[i][j]+flavorData[i][j-1];
//			}
//		}
        //change to sum
        return flavorData;
    }

    //one row for arima
    public static double[] getFlavorArrayFromDataList(int flavor, List<double[]> dataList) {
        double[] flavorData = new double[dataList.size()];
        for (int i=0; i<flavorData.length; i++){
            flavorData[i]=dataList.get(i)[flavor];
        }
        return flavorData;
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

    //get numbers of flavors need to be predicted
    public static int[] getNumbersOfFlavors(String[] inputFlavors) {
        int[] numbersOfFlavors = new int[inputFlavors.length];
        for (int i=0;i<inputFlavors.length;i++){
            numbersOfFlavors[i]=getNumberOfFlavor(inputFlavors[i]);
        }
        return numbersOfFlavors;
    }

    //if a number in an Array
    public static boolean numberInArray(int number, int[] array) {
        for (int i : array) {
            if (i==number) return true;
        }
        return false;
    }

}
