package com.gcloud.boot.config;

import java.io.IOException;
import java.util.List;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

public class FilterCustom implements TypeFilter{
	@Override
	public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
			throws IOException {
		// TODO Auto-generated method stub
		List<String> excludePackages=ComponentPackages.getInstance().getExcludePackages();
		ClassMetadata classMetadata=metadataReader.getClassMetadata();
		//String packageName=StringUtils.substring(classMetadata.getClassName(), 0,StringUtils.lastIndexOf(classMetadata.getClassName(), "."));
		for(String module:excludePackages){
			if(classMetadata.getClassName().indexOf(module)==0){
				return true;
			}
		}
		return false;
	}

}
