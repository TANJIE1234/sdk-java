package com.elasticcloudservice.predict;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import com.elasticcloudservice.predict.DataUtil;
import com.elasticcloudservice.predict.arima.ARIMAPredict;

public class Predict {
	private static final double ALPHA = 0.0001;
	private static final int ITERATION = 300000;
	private  static  int KEY = 20;

	public static String[] predictVm(String[] ecsContent, String[] inputContent, int KEY) {
		Predict.KEY=KEY;
		final int SUM_KINDS_OF_FLAVORS = 15;							//sum kinds of flavors
		String physicalServer = inputContent[0];						//get input server String
		int typeOfECS = Integer.parseInt(inputContent[2]);				//get number of input flavor
		/** =========================================
		 * put inputFlavors into a String array
		 * array index start at 0
		 * */
		String[] inputFlavors = new String[typeOfECS];
		for (int i=3;i<typeOfECS+3;i++) {
			inputFlavors[i-3]=inputContent[i];
		}
		/** ====================================== **/

		String paramToOptimize = inputContent[inputContent.length-4];	//param to optimize
		String beginDate=inputContent[inputContent.length-2];			//begin date
		String endDate=inputContent[inputContent.length-1];				//end date

//		List<int[]> resultOfDaysOfFlavors = 							//get flavor of days,List.get(i)[j]=
//				predictAll(ecsContent,beginDate,endDate,inputFlavors);	//number of flavori at day j,j=0 shows the sum.

//		double[][] fds =  DataUtilLstm.loadDataFromStringArray(ecsContent);
//		double[][] dsf = DataUtilLstm.getAvgAndStdFromArray(fds);
//		for (int i=0;i<fds.length;i++){
//			for (int j=0;j<fds[0].length;j++){
//				System.out.printf("%8s",fds[i][j]);
//			}
//			System.out.println();
//		}
		List<int[]> resultOfDaysOfFlavors = 							//get flavor of days,List.get(i)[j]=
				ARIMAPredict.predict(ecsContent,beginDate,endDate,inputFlavors);	//number of flavori at day j,j=0 shows the sum.



		ArrayList<Integer> resultOfNumbersOfFlavors =					//get sum of flavors and number of every flavor,
				new ArrayList<>(SUM_KINDS_OF_FLAVORS+1);		//the first item show the sum,list.get(i) shows flavori.

		resultOfNumbersOfFlavors.add(0);								//first item to count

		int[] numberOfFlavors = DataUtil.getNumbersOfFlavors(inputFlavors);		//get the flavor number to be predicted
		/** ===================================
		 * get number of flavors
		 * if the flavor is not to be predicted,number=-1
		 * */
		for (int i=1;i<SUM_KINDS_OF_FLAVORS+1;i++) {
			if (DataUtil.numberInArray(i, numberOfFlavors)) {
				resultOfNumbersOfFlavors.add(resultOfDaysOfFlavors.get(i)[0]);
				resultOfNumbersOfFlavors.set(0, resultOfNumbersOfFlavors.get(0) + resultOfNumbersOfFlavors.get(i));
			} else {
				resultOfNumbersOfFlavors.add(-1);
			}
		}

		String[] physicalServerPara = physicalServer.split(" ");	//get param of physical server
		int serverTypeCPU = Integer.parseInt(physicalServerPara[0]);	//get number of CPU of physical server
		int serverTypeMemory = Integer.parseInt(physicalServerPara[1]);	//get number of memory of physical server
		/** ==================================
		 * get result of boxing
		 * **/
		String[] results =
				PutServer.putservermethod(serverTypeCPU,serverTypeMemory,resultOfNumbersOfFlavors,paramToOptimize);
		return results;
	}

	/**
	 * predict one flavor
	 * param trainDataStringArray:train data in format of String array
	 * param falvor:the number of flavor to be predicted
	 * param days:how many days need to be predicted
	 * return int[]:the first item store the sum,int[i] store dayi**/
	public static int[] predictOneFlavor(String[] trainDataStringArray, int flavor, int days) {
		List<double[]> dataList =
				DataUtil.loadDataFromStringArray(trainDataStringArray);			//get data list

		double[][] trainData =
				DataUtil.getFlavorArrayFromDataList(flavor, KEY, dataList);

		LinearRegression m = new LinearRegression(trainData, ALPHA, ITERATION);
		m.trainTheta();

		double[] history = new double[KEY + 1];
		double[] temp = new double[KEY + 1];
		double[] result_double = new double[days+1];
		int[] result_int = new int[days+1];
		System.arraycopy(trainData[trainData.length - 1], 2, history, 1, KEY);
		history[0] = 1.0;

		for (int i = 1; i < days+1; i++) {
			for (int j = 0; j < KEY + 1; j++) {
				result_double[i] = result_double[i] + m.getTheta()[j] * history[j];
			}
			if (result_double[i] < 0) {
				result_int[i] = 0;
			} else result_int[i] = (int) Math.round(result_double[i]);
			temp = history;
			System.arraycopy(temp, 2, history, 1, KEY - 1);
			history[KEY] = result_double[i];
		}
		//********new add************//
//		for (int i=result_int.length-1;i>0;i--) {
//			result_int[i]=result_int[i]-result_int[i-1];
//		}
		for (int r : result_int) {
			result_int[0]=result_int[0]+r;
		}
		return result_int;
	}

	/**
	 * predict all kinds of flavors
	 * param trainDataStringArray:train data in format of String array
	 * param beginDate:the begin date of prediction
	 * param endDate:the end date of prediction
	 * param inputFlavors:the flavors need to be predicted
	 * return:List<int[]>:**/
	public static List<int[]> predictAll(String[] trainDataStringArray,String beginDate, String endDate, String[] inputFlavors) {
		int days = DataUtil.calDaysBetween(beginDate, endDate);					//calculate days
		int[] result;															//create temp array for predictOneFlavor
		List<int[]> resultList = new ArrayList<>();								//create return list
		for (int i=0;i<16;i++){
			resultList.add(new int[days+1]);
		}
		for (String inputFlavor:inputFlavors) {
			int numberOfFlavor = DataUtil.getNumberOfFlavor(inputFlavor);
			result = predictOneFlavor(trainDataStringArray,numberOfFlavor,days);
			resultList.set(numberOfFlavor,result);
		}
		return resultList;
	}


}

