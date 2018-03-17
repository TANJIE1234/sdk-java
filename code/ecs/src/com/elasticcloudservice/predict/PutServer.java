package com.elasticcloudservice.predict;


import java.util.ArrayList;

public class PutServer {
    public static int judgeCPU(String s) {//·µ»ØÐéÄâ»úCPUµÄÏûºÄ
        if(s.equals("flavor1")||s.equals("flavor2")||s.equals("flavor3"))
            return 1;
        else if(s.equals("flavor4")||s.equals("flavor5")||s.equals("flavor6"))
            return 2;
        else if(s.equals("flavor7")||s.equals("flavor8")||s.equals("flavor9"))
            return 4;
        else if(s.equals("flavor10")||s.equals("flavor11")||s.equals("flavor12"))
            return 8;
        else if(s.equals("flavor13")||s.equals("flavor14")||s.equals("flavor15"))
            return 16;
        else
            return 0;
    }
    public static int judgeMemory(String s) {//·µ»ØÐéÄâ»úMemoryµÄÏûºÄ
        if(s.equals("flavor1"))
            return 1;
        else if(s.equals("flavor2")||s.equals("flavor4"))
            return 2;
        else if(s.equals("flavor3")||s.equals("flavor5"))
            return 4;
        else if(s.equals("flavor7")||s.equals("flavor8")||s.equals("flavor10")||s.equals("flavor6"))
            return 8;
        else if(s.equals("flavor9")||s.equals("flavor11")||s.equals("flavor13"))
            return 16;
        else if(s.equals("flavor12")||s.equals("flavor14"))
            return 32;
        else if(s.equals("flavor15"))
            return 64;
        else
            return 0;
    }
    public static int remainVirtual(ArrayList<Integer> input) {//·µ»ØÐéÄâ»úÁÐ±íÖÐÓàÏÂµÄÐéÄâ»úžöÊý
        return input.get(0);
    }
    public static int maxVirtualIndex(ArrayList<Integer> input) {//·µ»ØŽæÔÚµÄÏÂ±ê×îŽóµÄÐéÄâ»ú¡£Èç¹ûÒÑŸ­Ã»ÓÐÐéÄâ»úÁË£¬·µ»Ø0
        for(int i=15;i>=1;i--)
            if(input.get(i)!=0&&input.get(i)!=-1)
                return i;
        return 0;
    }
    public static void first_fit_putVirtualIntoServer(int ServerTypeCPU,int ServerTypeMemory,ArrayList<Server> ServerList,String Virtual){
        for(int i=0;i<ServerList.size();i++)//ŽÓµÚÒ»žö·þÎñÆ÷¿ªÊŒ±éÀú
            if(ServerList.get(i).RemainCPU>=judgeCPU(Virtual)&&ServerList.get(i).RemainMemory>=judgeMemory(Virtual)){//Èç¹ûµÚižö·þÎñÆ÷ÄÜ·ÅÏÂžÃÐéÄâ»ú
                ServerList.get(i).RemainCPU=ServerList.get(i).RemainCPU-judgeCPU(Virtual);
                ServerList.get(i).RemainMemory=ServerList.get(i).RemainMemory-judgeMemory(Virtual);
                int num=ServerList.get(i).VirtualList.get(Virtual);
                ServerList.get(i).VirtualList.put(Virtual, num+1);
                return;
            }
        ServerList.add(new Server(ServerTypeCPU,ServerTypeMemory));//Èç¹û·Å²»ÏÂ£¬ÄÇŸÍÒªÐÂœš·þÎñÆ÷£¬¶øÇÒÐÂœšµÄ·þÎñÆ÷Ò»¶šÄÜ·ÅÏÂ
        ServerList.get(ServerList.size()-1).RemainCPU=ServerList.get(ServerList.size()-1).RemainCPU-judgeCPU(Virtual);
        ServerList.get(ServerList.size()-1).RemainMemory=ServerList.get(ServerList.size()-1).RemainMemory-judgeMemory(Virtual);
        int num=ServerList.get(ServerList.size()-1).VirtualList.get(Virtual);
        ServerList.get(ServerList.size()-1).VirtualList.put(Virtual, num+1);
    }

    public static String[] putservermethod(int ServerTypeCPU,int ServerTypeMemory,ArrayList<Integer> VirtualList_int,String judgeType) {//·ÖÅä·œ·š¡£ÊäÈë²ÎÊý·Ö±ðÎªÐèÒªÊä³öÎÄŒþµÄŸø¶ÔµØÖ·£¬·þÎñÆ÷CPU£¬·þÎñÆ÷ÄÚŽæ£¬ÐéÄâ»úÁÐ±í
        //ÐéÄâ»úÁÐ±íÏÂ±ê0ÎªÐéÄâ»ú×ÜÊý£¬ÏÂ±ê1-15·Ö±ðÎªÐéÄâ»ú1¡ª15µÄÊýÁ¿¡£ÏÂ±êÎª-1±íÊŸ²»ÐèÒªÅÐ¶ÏµÄÐéÄâ»ú£¬ÏÂ±êŽóÓÚµÈÓÚ0ÎªÐèÒªÅÐ¶ÏµÄÐéÄâ»ú
        ArrayList<Integer> VirtualList_int_copy=new ArrayList<Integer>();
        VirtualList_int_copy.addAll(VirtualList_int);
        ArrayList<Server> ServerList=new ArrayList<Server>();//Êä³öµÄ·þÎñÆ÷×ÊÔŽ·ÖÅä
        ServerList.add(new Server(ServerTypeCPU,ServerTypeMemory));

        exchange_first_fit_putVirtualIntoServer(ServerTypeCPU,ServerTypeMemory,ServerList,VirtualList_int,judgeType);

        Integer ServerCount=ServerList.size();//·þÎñÆ÷µÄ×ÜÊý
        //Êä³öœá¹ûÎÄŒþ¡£Êä³öÒ»žöÎÄŒþ£¬ÎÄŒþžñÊœÎª
		/*
                        Ô€²âµÄÐéÄâ»ú×ÜÊý
		        ÐéÄâ»ú¹æžñÃû³Æ1 ÐéÄâ»úžöÊý
		        ÐéÄâ»ú¹æžñÃû³Æ2 ÐéÄâ»úžöÊý
		   ¡­¡­¡­¡­
                        ËùÐèÎïÀí·þÎñÆ÷×ÜÊý
                                ÎïÀí·þÎñÆ÷1 ÐéÄâ»ú¹æžñÃû³Æ1 ÄÜ·ÅÖÃžÃÀàÐÍÐéÄâ»úžöÊý ÐéÄâ»ú¹æžñÃû³Æ2 ÄÜ·ÅÖÃžÃÀàÐÍÐéÄâ»úžöÊý ¡­¡­
                                ÎïÀí·þÎñÆ÷2 ÐéÄâ»ú¹æžñÃû³Æ1 ÄÜ·ÅÖÃžÃÀàÐÍÐéÄâ»úžöÊý ÐéÄâ»ú¹æžñÃû³Æ2 ÄÜ·ÅÖÃžÃÀàÐÍÐéÄâ»úžöÊý ¡­¡­
           ¡­¡­¡­¡­
        */
        ArrayList<String> FinalResult=new ArrayList<String>();
        FinalResult.add(VirtualList_int_copy.get(0).toString()+"\r");
        for(int i=1;i<=15;i++)
            if(VirtualList_int_copy.get(i)!=-1)//Ö»ÒªÊÇÐèÒªÅÐ¶ÏµÄÐéÄâ»ú£¬ŸÍËãÊýÁ¿ÊÇ0Ò²ÒªÊä³ö
                FinalResult.add("flavor"+i+" "+VirtualList_int_copy.get(i)+"\r");
        FinalResult.add("\r");
        FinalResult.add(ServerCount.toString()+"\r");
        for(int i=0;i<ServerList.size();i++){
            String TempString=""+(i+1)+" ";
            int j=1;
            while(j<16){
                if(ServerList.get(i).VirtualList.get("flavor"+j)>0){
                    TempString=TempString+"flavor"+j+" "+ServerList.get(i).VirtualList.get("flavor"+j)+" ";
                    j++;
                }
                else
                    j++;
            }
            TempString=TempString+"\r";
            FinalResult.add(TempString);
        }
        String[] FinalResultshuzu=FinalResult.toArray(new String[0]);
        return FinalResultshuzu;
    }

    public static int orderMaxVirtualIndex(ArrayList<Integer> VirtualList,int[] Order){
        for(int i=14;i>=0;i--)
            if(VirtualList.get(Order[i])>0)
                return Order[i];
        return 0;
    }

    public static int findMinVirtual(String JudgeResource,Server CurrentServer,int CurrentFlavor){//·µ»ØÔÚžÃ·þÎñÆ÷ÖÐÅÐ¶Ï×ÊÔŽ×îÐ¡£¬ÁíÒ»žö×ÊÔŽÏàÍ¬µÄÐéÄâ»ú
        if(JudgeResource.equals("CPU")){//Èç¹ûÒªÅÐ¶ÏµÄÊÇCPU£¬Ò²ŸÍÊÇÒªÕÒCurrentSreverÖÐMemoryºÍCurrentFlavorÏàÍ¬£¬µ«ÊÇCPU×îÐ¡µÄÄÇžöflavorµÄ±àºÅ
            for(int i=1;i<=15;i++){
                if(CurrentServer.VirtualList.containsKey("flavor"+i)&&CurrentServer.VirtualList.get("flavor"+i)>0&&judgeCPU("flavor"+i)<judgeCPU("flavor"+CurrentFlavor)&&judgeMemory("flavor"+i)==judgeMemory("flavor"+CurrentFlavor)&&CurrentServer.RemainCPU+judgeCPU("flavor"+i)-judgeCPU("flavor"+CurrentFlavor)>=0)
                    //Òª·ûºÏ£º1¡¢žÃflavorŽæÔÚ£¬2¡¢žÃflavorµÄMemoryºÍcurrentflavorÏàÍ¬£¬3¡¢žÃFlavorµÄCPU±ÈcurrentflavorµÄÐ¡£¬4¡¢·þÎñÆ÷µÄ¿ÕŒäÔÊÐíœ»»»
                    return i;
            }
            return 0;//·µ»Ø0±íÊŸ²»ŽæÔÚ£¬²»œ»»»
        }
        else{//ÅÐ¶ÏµÄÊÇMemory
            int[] Order=new int[]{1,2,4,3,5,6,7,8,10,9,11,13,12,14,15};
            for(int i=0;i<=14;i++){
                if(CurrentServer.VirtualList.containsKey("flavor"+i)&&CurrentServer.VirtualList.get("flavor"+i)>0&&judgeMemory("flavor"+Order[i])<judgeMemory("flavor"+CurrentFlavor)&&judgeCPU("flavor"+Order[i])==judgeCPU("flavor"+CurrentFlavor)&&CurrentServer.RemainMemory+judgeMemory("flavor"+Order[i])-judgeMemory("flavor"+CurrentFlavor)>=0)
                    return Order[i];
            }
            return 0;//·µ»Ø0±íÊŸ²»ŽæÔÚ£¬²»œ»»»
        }
    }

    public static void exchange_first_fit_putVirtualIntoServer(int ServerTypeCPU,int ServerTypeMemory,ArrayList<Server> ServerList/*ÈÏÎªÀïÃæÖÁÉÙÒÑŸ­ÓÐÁËÒ»Ìš·þÎñÆ÷*/,ArrayList<Integer> VirtualList,String JudgeType){
        int[] Order=new int[15];
        if(JudgeType.equals("CPU")){//°ŽÕÕCPUŽóÐ¡ÀŽÅÅÐò
            for(int i=0;i<15;i++)
                Order[i]=i+1;
        }
        if(JudgeType.equals("MEM")){//°ŽÕÕMemoryŽóÐ¡ÀŽÅÅÐò
            Order[0]=1;Order[1]=2;Order[2]=4;Order[3]=3;Order[4]=5;Order[5]=6;Order[6]=7;Order[7]=8;Order[8]=10;Order[9]=9;
            Order[10]=11;Order[11]=13;Order[12]=12;Order[13]=14;Order[14]=15;
        }
        while(remainVirtual(VirtualList)!=0){//Ö»ÒªVirtual»¹ŽæÔÚ
            int StartIndex=orderMaxVirtualIndex(VirtualList,Order);
            for(int i=0;i<ServerList.size()+1;i++){//ŽÓµÚÒ»žö·þÎñÆ÷¿ªÊŒ±éÀú
                if(i!=ServerList.size()){
                    if(ServerList.get(i).RemainCPU>=judgeCPU("flavor"+StartIndex)&&ServerList.get(i).RemainMemory>=judgeMemory("flavor"+StartIndex)){//Èç¹ûÁœžö×ÊÔŽ¶ŒÄÜ×°ÏÂ
                        ServerList.get(i).RemainCPU=ServerList.get(i).RemainCPU-judgeCPU("flavor"+StartIndex);
                        ServerList.get(i).RemainMemory=ServerList.get(i).RemainMemory-judgeMemory("flavor"+StartIndex);
                        ServerList.get(i).VirtualList.put("flavor"+StartIndex, ServerList.get(i).VirtualList.get("flavor"+StartIndex)+1);
                        VirtualList.set(0, VirtualList.get(0)-1);
                        VirtualList.set(StartIndex, VirtualList.get(StartIndex)-1);
                        break;
                    }
                    else if(ServerList.get(i).RemainCPU<judgeCPU("flavor"+StartIndex)&&ServerList.get(i).RemainMemory>=judgeMemory("flavor"+StartIndex)){//CPU·Å²»ÏÂ£¬µ«ÊÇMemory·ÅµÃÏÂ
                        int MaybeExchangeIndex=findMinVirtual("CPU", ServerList.get(i), StartIndex);
                        if(MaybeExchangeIndex==0)
                            continue;
                        else{
                            VirtualList.set(MaybeExchangeIndex, VirtualList.get(MaybeExchangeIndex)+1);
                            VirtualList.set(StartIndex, VirtualList.get(StartIndex)-1);
                            ServerList.get(i).RemainCPU=ServerList.get(i).RemainCPU+judgeCPU("flavor"+MaybeExchangeIndex)-judgeCPU("flavor"+StartIndex);
                            ServerList.get(i).VirtualList.put("flavor"+MaybeExchangeIndex,ServerList.get(i).VirtualList.get("flavor"+MaybeExchangeIndex)-1);
                            ServerList.get(i).VirtualList.put("flavor"+StartIndex,ServerList.get(i).VirtualList.get("flavor"+StartIndex)+1);
                            break;
                        }
                    }
                    else if(ServerList.get(i).RemainCPU>judgeCPU("flavor"+StartIndex)&&ServerList.get(i).RemainMemory<=judgeMemory("flavor"+StartIndex)){//Memory·Å²»ÏÂ
                        int MaybeExchangeIndex=findMinVirtual("MEM", ServerList.get(i), StartIndex);
                        if(MaybeExchangeIndex==0)
                            continue;
                        else{
                            VirtualList.set(MaybeExchangeIndex, VirtualList.get(MaybeExchangeIndex)+1);
                            VirtualList.set(StartIndex, VirtualList.get(StartIndex)-1);
                            ServerList.get(i).RemainMemory=ServerList.get(i).RemainMemory+judgeMemory("flavor"+MaybeExchangeIndex)-judgeMemory("flavor"+StartIndex);
                            ServerList.get(i).VirtualList.put("flavor"+MaybeExchangeIndex,ServerList.get(i).VirtualList.get("flavor"+MaybeExchangeIndex)-1);
                            ServerList.get(i).VirtualList.put("flavor"+StartIndex,ServerList.get(i).VirtualList.get("flavor"+StartIndex)+1);
                            break;
                        }
                    }
                }
                else{
                    ServerList.add(new Server(ServerTypeCPU,ServerTypeMemory));//Èç¹û·Å²»ÏÂ£¬ÄÇŸÍÒªÐÂœš·þÎñÆ÷£¬¶øÇÒÐÂœšµÄ·þÎñÆ÷Ò»¶šÄÜ·ÅÏÂ
                    ServerList.get(ServerList.size()-1).RemainCPU=ServerTypeCPU-judgeCPU("flavor"+StartIndex);
                    ServerList.get(ServerList.size()-1).RemainMemory=ServerTypeMemory-judgeMemory("flavor"+StartIndex);
                    ServerList.get(ServerList.size()-1).VirtualList.put("flavor"+StartIndex, 1);
                    VirtualList.set(0, VirtualList.get(0)-1);
                    VirtualList.set(StartIndex, VirtualList.get(StartIndex)-1);
                    break;
                }
            }
            //Èç¹ûÁœžö¶Œ×°²»ÏÂ
        }
    }

}

