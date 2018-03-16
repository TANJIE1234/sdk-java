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

    public static String[] putservermethod(int ServerTypeCPU,int ServerTypeMemory,ArrayList<Integer> VirtualList_int) {//·ÖÅä·œ·š¡£ÊäÈë²ÎÊý·Ö±ðÎªÐèÒªÊä³öÎÄŒþµÄŸø¶ÔµØÖ·£¬·þÎñÆ÷CPU£¬·þÎñÆ÷ÄÚŽæ£¬ÐéÄâ»úÁÐ±í
        //ÐéÄâ»úÁÐ±íÏÂ±ê0ÎªÐéÄâ»ú×ÜÊý£¬ÏÂ±ê1-15·Ö±ðÎªÐéÄâ»ú1¡ª15µÄÊýÁ¿¡£ÏÂ±êÎª-1±íÊŸ²»ÐèÒªÅÐ¶ÏµÄÐéÄâ»ú£¬ÏÂ±êŽóÓÚµÈÓÚ0ÎªÐèÒªÅÐ¶ÏµÄÐéÄâ»ú
        ArrayList<Integer> VirtualList_int_copy=new ArrayList<Integer>();
        VirtualList_int_copy.addAll(VirtualList_int);
        ArrayList<Server> ServerList=new ArrayList<Server>();//Êä³öµÄ·þÎñÆ÷×ÊÔŽ·ÖÅä
        ServerList.add(new Server(ServerTypeCPU,ServerTypeMemory));

        while(remainVirtual(VirtualList_int)!=0) {
            int StartIndex=maxVirtualIndex(VirtualList_int);
            first_fit_putVirtualIntoServer(ServerTypeCPU,ServerTypeMemory,ServerList,"flavor"+StartIndex);
            VirtualList_int.set(StartIndex, VirtualList_int.get(StartIndex)-1);
            VirtualList_int.set(0,VirtualList_int.get(0)-1);
        }
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
}

