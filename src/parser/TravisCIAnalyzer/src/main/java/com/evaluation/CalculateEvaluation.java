package com.evaluation;

import java.util.List;

public class CalculateEvaluation {
	
	public int getTruePositive(List<Boolean> groundtruth, List<Boolean> predicted)
	{
		int truepositive=0;
		
		if(groundtruth.size()!=predicted.size())
		{
			System.out.println("List Sizes Are not Equal");
			return 0;
		}
		
		for(int index=0;index<groundtruth.size();index++)
		{
			if(groundtruth.get(index)==true && predicted.get(index)==true)
			{
				truepositive++;
			}
		}
		
		return truepositive;		
	}
	
	public int getFalsePositive(List<Boolean> groundtruth, List<Boolean> predicted)
	{
		int falsepositive=0;
		
		if(groundtruth.size()!=predicted.size())
		{
			System.out.println("List Sizes Are not Equal");
			return 0;
		}
		
		for(int index=0;index<groundtruth.size();index++)
		{
			if(groundtruth.get(index)==false && predicted.get(index)==true)
			{
				falsepositive++;
			}
		}
		
		return falsepositive;		
	}
	
	
	public int getFalseNegative(List<Boolean> groundtruth, List<Boolean> predicted)
	{
		int falsenegative=0;
		
		if(groundtruth.size()!=predicted.size())
		{
			System.out.println("List Sizes Are not Equal");
			return 0;
		}
		
		for(int index=0;index<groundtruth.size();index++)
		{
			if(groundtruth.get(index)==true && predicted.get(index)==false)
			{
				falsenegative++;
			}
		}
		
		return falsenegative;		
	}
	
	public double getPrecission(List<Boolean> groundtruth, List<Boolean> predicted)
	{
		int tp=getTruePositive(groundtruth,predicted);
		int fp=getFalsePositive(groundtruth,predicted);
		
		double precission=(double)((double)tp/(double)(tp+fp));
		
		return precission;
		
	}
	
	public double getRecall(List<Boolean> groundtruth, List<Boolean> predicted)
	{
		int tp=getTruePositive(groundtruth,predicted);
		int fn=getFalseNegative(groundtruth,predicted);
		
		double recall=(double)((double)tp/(double)(tp+fn));
		
		return recall;		
	}
	
	
	public double getF1Score(List<Boolean> groundtruth, List<Boolean> predicted, String label)
	{
		double precission=getPrecission(groundtruth,predicted);
		double recall=getRecall(groundtruth,predicted);			
		double f1=(2*precission*recall)/(precission+recall);
		
		System.out.println("******"+label+"**********");
		System.out.println("Precision:"+precission);
		System.out.println("Recall:"+recall);
		System.out.println("F1-Score:"+f1);
		System.out.println("****************");
		
		return f1;
	}
	

}
