package com.cacheserverdeploy.deploy;
import java.util.ArrayList;

import java.util.List;
import java.util.Scanner;



public class Deploy 
{
  
	
	public static   int totalPoint;
	public static   int totalLine;
	public static   int totalServicePoint;
	public static   int spend;
	public static   int step=0;
	public static   List<Line> line = new ArrayList<Line>();
	public static   String[] outStrings = new String[9999];
	public static   List<int[][]> resultLine = new ArrayList<int[][]>();
	public static   List<int[]> resultbasic = new ArrayList<int[]>();
    public static String[] deployServer(String[] graphContent)
    {
    	
        /**do your work here**/	
    	getDate(graphContent);//字符串数组转成int,保存在List<Line>

       
		List<Integer> sl = new ArrayList<Integer>();//存放当前的服务器位置
		List<Integer> sltemp = new ArrayList<Integer>();//存放当前的服务器位置
		long current = System.currentTimeMillis();
	
//		System.out.print(current);
		//设置每个服务器安装在消费节点为初始解
		if(totalLine<1000){
		for (int i =line.size();i>line.size()-totalServicePoint;i--){
			sl.add(line.get(i-1).start); 
		}
		int result = 99999999;
		int temp;
		sltemp.addAll(sl);
		result = Calculate(result, sl);//计算得到所有服务器为消费节点的初始解
		System.out.println(result);
		boolean fff = false; //若下一次循环没有产生更优解，则fff为true,结束计算
		while (fff==false){
			System.gc();
			step+=1;
			fff=true;
			sl.clear();
			for (int i =line.size();i>line.size()-totalServicePoint+20+step;i--){
				sl.add(line.get(i-1).start); 
			}
			temp = Calculate(result, sl);
			if (temp < result) {
				sltemp.clear();
				sltemp.addAll(sl);
				result = temp;
				System.out.println(result);
				fff = false;
			}
			else{
				resultbasic.remove(0);
				resultLine.remove(0);
			}
			if(((System.currentTimeMillis()-current)/1000)>80) break;
		}
	//	System.out.println();
		
		sl.clear();
		sl.addAll(sltemp);
		}
		else if(totalLine>1000 && totalLine<2500){
		for (int i =line.size();i>line.size()-totalServicePoint;i--){
			sl.add(line.get(i-1).start); 
		}
		int result = 99999999;
		int temp;
		sltemp.addAll(sl);
		result = Calculate(result, sl);//计算得到所有服务器为消费节点的初始解
		System.out.println(result);
		boolean fff = false; //若下一次循环没有产生更优解，则fff为true,结束计算
		while (fff==false){
			System.gc();
			step+=2;
			fff=true;
			sl.clear();
			for (int i =line.size();i>line.size()-totalServicePoint+40+step;i--){
				sl.add(line.get(i-1).start); 
			}
			temp = Calculate(result, sl);
			if (temp < result) {
				sltemp.clear();
				sltemp.addAll(sl);
				result = temp;
				System.out.println(result);
				fff = false;
			}
			else{
				resultbasic.remove(0);
				resultLine.remove(0);
			}
			if(((System.currentTimeMillis()-current)/1000)>60) break;
		}
	//	System.out.println();
		
		sl.clear();
		sl.addAll(sltemp);
		}
		else{
		int result = 99999999;
		int temp;
		sltemp.addAll(sl);
		System.out.println(result);
			System.gc();
			sl.clear();
			for (int i =line.size();i>line.size()-totalServicePoint+50;i--){
				sl.add(line.get(i-1).start); 
			}
			temp = Calculate(result, sl);
	//	System.out.println();
		sl.clear();
		sl.addAll(sltemp);
		}
		int[][] resultroute=resultLine.get(0);
		int []  basic = resultbasic.get(0);
		outStrings = getOut(resultroute,line.size(),line.size() + totalPoint - sl.size(),basic,sl);

		return outStrings;
	}
	/*
	 * 获得输入String[]中的数据，放在List<Line>内
	 */
	public static void getDate(String[] graphContent) {
		line.clear();
		StringBuffer sb = new StringBuffer();
    	for(int i = 0; i <graphContent.length; i++){
    	 sb. append(" "+graphContent[i]);
    	}
       	String s = sb.toString();
       	Scanner sc =  new Scanner(s);
       	totalPoint = sc.nextInt();
		totalLine = sc.nextInt();
		totalServicePoint = sc.nextInt();
		spend = sc.nextInt();
		
		for (int i =0;i<totalLine;i++){
			int tempInt1 = sc.nextInt();
			int tempInt2 = sc.nextInt();
			int tempInt3 = sc.nextInt();
			int tempint4 = sc.nextInt();
			if (totalLine<2500){
			Line e1 = new Line(tempInt1,tempInt2, tempInt3, tempint4,tempInt3);
			line.add(e1); 
			
			Line e2 = new Line(tempInt2,tempInt1, tempInt3, tempint4,tempInt3);
			line.add(e2); 
			
			
			
		}
		}

		for (int i =0;i<totalServicePoint;i++){
			int temp=sc.nextInt();
			int temp2=sc.nextInt();
			int temp3=sc.nextInt();
			Line e =new Line(temp2, temp+totalPoint, temp3,0,temp3);
			line.add(e);
			
		}
		for (int i = line.size() - 1; i > line.size() - totalServicePoint; i--) {
			for (int j = i - 1; j >= line.size() - totalServicePoint; j--) {
				if (line.get(i).broad < line.get(j).broad) {
					Line e = new Line(line.get(j).start, line.get(j).end,
							line.get(j).broad, line.get(j).cost,
							line.get(j).broadReserve);
					Line e1 = line.set(i, e);
					line.set(j, e1);
				}
			}
		}
	}
	/*
	 * 用单纯形法计算线性规划
	 */
	public static int Calculate(int result,List<Integer> sl){
		simplexalgorithm tosolve = null;
		List<Integer> rest = null;
		int amountlimit;
		int amountx;
		int amountless;
		int amountequal;
		int a[][];
		int x[];
		rest = new ArrayList<Integer>();//得到没有服务器的网络节点REST
		for (int i = 0; i < totalPoint; i++) {
			if (Util.isOfSl(sl, i)==false){
				rest.add(i);
			}

		}
		amountlimit = line.size() + totalPoint - sl.size();
		amountx = line.size();
		amountless = line.size() - totalServicePoint;
		amountequal = totalServicePoint + totalPoint - sl.size();
		a = new int[amountlimit][amountx + 1];
		x = new int[amountx];
		for (int i = 0; i < amountx; i++) {
			for (int j = 0; j < amountx + 1; j++) {
				if (i == j) {
					a[i][j] = 1;
				}
				if (j == amountx) {
					a[i][j] = line.get(i).broad;
				}
			}
			x[i] = line.get(i).cost;
		}
		for (int l = 0; l < rest.size(); l++) {
			for (int j = 0; j < line.size(); j++) {
				if (line.get(j).start == rest.get(l)) {
					a[l + amountx][j] = 1;
				} else if (line.get(j).end == rest.get(l)) {
					a[l + amountx][j] = -1;
				} else {
					a[l + amountx][j] = 0;
				}
			}
		}
		tosolve = new simplexalgorithm(-1, amountlimit, amountx, amountless,amountequal, a, x);
		int result2 = tosolve.solve(sl);
		int [][] resultline = tosolve.getA();
		int [] basic = tosolve.getBasic();
		resultLine.add(0, resultline);
		resultbasic.add(0,basic);
		result2 = result2 + spend * sl.size();
		return result2;
	}
	public static String[] getOut(int [][] a,int n,int m,int[] basic,List<Integer> sl){
		int basicp[] = new int[n+1];
		List<List<Integer>> listtemp = new ArrayList<List<Integer>>();
		String[] resultStrings = new String[1000];
		for(int i = 0; i <= n; i++)
		{
			basicp[i] = 0;
		}
		for(int i = 1; i <= m; i++)
		{
			if(basic[i] >= 1 && basic[i] <= n)
			{
				basicp[basic[i]] = i;
			}
		}
		
		for (int j =0;j<totalServicePoint;j++){
			for(int i = 1;i<=n; i++)
			{
			
				if(basicp[i] != 0)
				{
					if (a[basicp[i]][0]!=0){
						
						if (line.get(i-1).end==totalPoint+j){
							List<Integer> e = new ArrayList<Integer>();
							e.add(0,line.get(i-1).broad);
							e.add(0,line.get(i-1).end-totalPoint);
							e.add(0,line.get(i-1).start);
							listtemp.add(e);
							a[basicp[i]][0]=0;
						}
					
					}
				}	
			}
			}
			int temp2=0;
			for (;;){
			boolean flaggg =true;
			for (int j = 0; j < listtemp.size(); j++) {
				for (int i = 0;i<n; i++) {
					if (basicp[i + 1] != 0) {
						if (a[basicp[i + 1]][0] != 0) {
							if (line.get(i).end == listtemp.get(j).get(0) && Util.isOfSl(sl,listtemp.get(j).get(0))==false) {
						//		System.out.println(listtemp.get(j).get(listtemp.get(j).size()-1));
						//		System.out.println(a[basicp[i + 1]][0]);
						//		System.out.println(i+1);
								temp2 = listtemp.get(j).get(listtemp.get(j).size()-1)-a[basicp[i + 1]][0];
						//		System.out.println(temp2);
								if (temp2==0){
									listtemp.get(j).add(0, line.get(i).start);
									a[basicp[i + 1]][0] = 0;
								}
								else if (temp2>0){
									List<Integer> e = new ArrayList<Integer>();
									for (int v =0;v<listtemp.get(j).size();v++){
										e.add(0,listtemp.get(j).get(listtemp.get(j).size()-1-v));
									}
										e.set(e.size()-1, temp2);
										listtemp.add(e);
										listtemp.get(j).add(0, line.get(i).start);
										listtemp.get(j).set(listtemp.get(j).size()-1,a[basicp[i + 1]][0] );
										a[basicp[i + 1]][0] = 0;
									
								}
								else{
									listtemp.get(j).add(0, line.get(i).start);
									a[basicp[i+1]][0] = -temp2;
								}
							}
						}
					}
				}

			}
			for (int q=0;q<listtemp.size();q++){
				if (!Util.isOfSl(sl,listtemp.get(q).get(0))){
					flaggg=false;
			
				}
			}
			if (flaggg == true) 	{
				
				for (int k=0;k<listtemp.size();k++){
					System.out.println();
					StringBuilder sb = new StringBuilder();
					sb.setLength(0);
					for (int h=0;h<listtemp.get(k).size();h++){
						System.out.print(listtemp.get(k).get(h)+" ");
						sb.append(String.valueOf(listtemp.get(k).get(h)));
						sb.append(" ");
						resultStrings[k+2]=sb.toString();
					}
					
				}
				resultStrings[0]=String.valueOf(listtemp.size());
				
				resultStrings[1]="\r\n";
				return resultStrings;
			}
			}
		
		
	
		
	}
}
class Line{
    int start;
	int end;
	int broad;
	int cost;
	int broadReserve;
	public Line(int start, int end, int broad, int cost,int broadreserve) {
		
		this.start = start;
		this.end = end;
		this.broad = broad;
		this.cost = cost;
		this.broadReserve=broadreserve;
	}
}
class Util{
	public static boolean isOfSl(List<Integer> sl,int test){
		for (int i =0;i<sl.size();i++){
			if (test==sl.get(i)){
				return true;
			}
		}
		return false;
	}
	public static boolean isZero(int[][] arr,int n){
		for (int i =1;i<=n;i++){
			if (arr[i][0]!=0) return false;
		}
		return true;
	}
}
class simplexalgorithm {
	int amountlimit;  
	int amountx; 
	int amountless; 
	int amountequal;
	int judge; 
	int basic[];
	int nonbasic[];
	int a[][]; 
	int mininum; 

	public simplexalgorithm(int mininum,int amountlimit,int amountx,int amountless,int amountequal,int a[][],int x[])//构造函数
	{
		int value;
		this.judge = 0;
		this.mininum = mininum;
		this.amountlimit = amountlimit;
		this.amountx = amountx;
		this.amountless = amountless;
		this.amountequal = amountequal;
		this.a = new int[amountlimit+2][];
		for(int i = 0; i < amountlimit+2; i++)
		{
			this.a[i] = new int[amountx+amountlimit+(amountlimit-amountless-amountequal)+1];
		}
		this.basic = new int[amountlimit+2];
		this.nonbasic = new int[amountx+(amountlimit-amountless-amountequal)+1];
		for(int j = 0; j <= amountx+(amountlimit-amountless-amountequal); j++)
		{
			nonbasic[j] = j;
		}
		for(int i = 1,j = amountx+(amountlimit-amountless-amountequal)+1; i <= amountlimit; i++,j++)
		{
			basic[i]=j;
		}
		//约束系数和右端项
		for(int i = 1; i <= amountlimit; i++)
		{
			for(int j = 1; j <= amountx; j++)
			{
				value = a[i-1][j-1];
				this.a[i][j]= value;
			}
			value = a[i-1][amountx];
			if(value<0)
			{
				judge = 1;
			}
			this.a[i][0]=value;
		}
		//输入目标函数系数
		for(int j = 1; j <= amountx; j++) 
		{
			value = x[j - 1];
			this.a[0][j] = value * mininum;
		}
		 //引入人工变量，
		for(int j = 1; j <= amountx; j++)
		{
			value = 0;
			for(int i = amountless+1; i <= amountlimit; i++)
				value+=this.a[i][j];
			this.a[amountlimit+1][j]=value;
		}
		
	}
	//选取换入变量
	public int enter(int objrow)
	{
		int col = 0;
		for(int j = 1; j <= this.amountx + (amountlimit-amountless-amountequal); j++)
		{
			if(this.nonbasic[j] <= this.amountx + this.amountless + (amountlimit-amountless-amountequal) && this.a[objrow][j] > 10e-8)
			{
				col=j;
				break;
			}
		}
		return col;
	}
	//选取换出变量
	public int leave(int col)
	{
		double temp=-1;
		int row  = 0;
		for(int i = 1; i <= this.amountlimit; i++)
		{
			double val = this.a[i][col];
			if( val > 10e-8)
			{
				val = this.a[i][0]/val;
				if(val < temp || temp == -1)
				{
					row = i;
					temp = val;
				}
			}
		}
		return row;
	}
	//交换换入换出变量
	public void swapbasic(int row,int col)
	{
		int temp = this.basic[row];
		this.basic[row] = this.nonbasic[col];
		this.nonbasic[col] = temp;
	}
	//a[row][col]为轴心，变换单纯形表
	public void trans(int row,int col)
	{
		for(int j = 0;j <= this.amountx + (amountlimit-amountless-amountequal); j++)
		{
			if(j != col)
			{
				this.a[row][j] = this.a[row][j] / this.a[row][col];
			}
		}
		this.a[row][col] = 1 / this.a[row][col];
		for(int i = 0; i <= this.amountlimit + 1; i++)
		{
			if(i != row)
			{
				for(int j = 0; j <= this.amountx + (amountlimit-amountless-amountequal); j++)
				{
					if(j != col)
					{
						this.a[i][j] = this.a[i][j] - this.a[i][col] * this.a[row][j];
						if(Math.abs(this.a[i][j]) < 10e-8)
							this.a[i][j]=0;
					}
				}
				this.a[i][col] = -this.a[i][col] * this.a[row][col];
			}
		}
		swapbasic(row,col);
	}
	//执行算法
	public int simplex(int objrow)
	{
		int row = 0;
		while(true)
		{
			int col = enter(objrow);
			if(col > 0)
			{
				row=leave(col);
			}
			else
			{
				return 0;
			}
			if(row > 0)
			{
				trans(row,col);
			}
			else
			{
				return 2;
			}
		}
	}
	//构造可行解
	public int createbase()
	{
		this.judge = simplex(this.amountlimit + 1);
		if(this.judge > 0)
		{
			return this.judge;
		}
		for(int i = 1; i <= this.amountlimit; i++)
		{
			if(this.basic[i] > this.amountx + this.amountless + (amountlimit-amountless-amountequal))
			{
				if(this.a[i][0] > 10e-8)
				{
					return 3;
				}
				for(int j = 1; j <= this.amountx; j++)
				{
					if(Math.abs(this.a[i][j]) >= 10e-8)
					{
						trans(i,j);
						break;
					}
				}
			}
		}
		return 0;
	}
	//二阶段单纯形法的计算
	public int solve(List<Integer> sl)
	{
	
		if(this.judge > 0){}
		else if(this.amountlimit != this.amountless)
		{
			this.judge = createbase();
			if(this.judge > 0){}
			else {
				judge = simplex(0);
			}
		}
		int result ;
		if (judge==0){
			result = -mininum*a[0][0];
	
		}
		else {
			result =999999;
		}
		return result;
	}
	public int[][] getA(){
		return this.a;
	}
	public int[] getBasic(){
		return this.basic;
	}
	
	

}
