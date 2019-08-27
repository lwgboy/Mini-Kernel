package com.gcloud.core.util;

public class MarkdownUtils {
	public static String tableTitle(String... titles){
		StringBuilder sb=new StringBuilder("\n|");
		for(String title:titles){
			sb.append(title).append("|");
		}
		sb.append("\n|");
		for(String title:titles){
			sb.append("------|");
		}
		sb.append("\n");
		return sb.toString();
	}
	public static String tableRow(String... contends){
		StringBuilder sb=new StringBuilder("|");
		for(String contend:contends){
			sb.append(contend).append("|");
		}
		sb.append(" \n ");
		return sb.toString();
	}
	
	public static String title(int level,String title){
		StringBuilder sb=new StringBuilder("\n");
		for(int i=0;i<level;i++){
			sb.append("#");
		}
		sb.append(" ").append(title);
		sb.append("\n");
		return sb.toString();
	}
}
