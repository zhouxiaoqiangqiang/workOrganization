package com.work.workorganization;

public class MaoPaoSort {
    public static void main(String []args) {
		
		int[] arr = {18,13,50,15,4,17,18};
		
		 System.out.println("arr的排序前：\n18  13  50  15  4  17  18 ");
		
		int temp  = 0 ;
		for(int i = 0 ;i< arr.length -1; i++){
			for(int j = 0; j<arr.length-1-i; j++){
				if(arr[j]>arr[j+1]){
					temp = arr[j];
					arr[j] = arr[j+1];
					arr[j+1] = temp;
				}
			}
			
		}
		 System.out.println("arr排序后：");
				
        for(int i = 0; i<arr.length; i++){
 
			 System.out.print(arr[i]+"\t");
		}	
		
		
      
    }
}