package com.gcloud.boot.config;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActiveProfiles {
    private final static ActiveProfiles INSTANCE = new ActiveProfiles();

    private ActiveProfiles() {
        defaults.add("controller");
        defaults.add("identity");
        defaults.add("api");
        defaults.add("compute-node");
        defaults.add("network-node");
        defaults.add("storage-node");
        defaults.add("image-node");
    }

    public static ActiveProfiles getInstance() {
        return INSTANCE;
    }

    private List<String> defaults = new ArrayList<String>();
    private List<String> excludes = new ArrayList<String>();
    private List<String> includes = new ArrayList<String>();
    private boolean withDb = false;
    private boolean hasWithDb = false;

    public void init(String[] args) {
        boolean hasExcludes = false;
        boolean hasIncludes = false;
        for (String str : args) {
            if (str.indexOf("excludes=") == 0) {
                hasExcludes = true;
                String[] arrayExcludes = str.replace("excludes=", "").split(",");
                includes = defaults;
                for (String strExclude : arrayExcludes) {
                    excludes.add(strExclude);
                    includes.remove(strExclude);
                }
                break;
            }
            if (str.indexOf("includes=") == 0) {
                hasIncludes = true;
                String[] arrayIncludes = str.replace("includes=", "").split(",");
                excludes = defaults;
                for (String strInclude : arrayIncludes) {
                    includes.add(strInclude);
                    excludes.remove(strInclude);
                }
                break;
            }
            if(str.startsWith("--gcloud.withDb=")){
                hasWithDb = true;
            }
        }
        if (!hasExcludes && !hasIncludes)
            includes = defaults;
        if (includes.contains("controller") || includes.contains("identity")) {
            withDb = true;
        }
    }

    public String[] process(String[] args) {
        List<String> argsList = new ArrayList(Arrays.asList(args));
        String includesRegEx="includes=.*";
        String excludesRegEx="excludes=.*";
        Pattern includesPattern = Pattern.compile(includesRegEx);
        Pattern excludesPattern = Pattern.compile(excludesRegEx);
        Iterator<String> it=argsList.iterator();
        while(it.hasNext()){
        	String arg=it.next();
        	Matcher matcher=includesPattern.matcher(arg);
        	if(matcher.find()){
        		it.remove();
        	}
        	matcher=excludesPattern.matcher(arg);
        	if(matcher.find()){
        		it.remove();
        	}
        }
        String activeProfiles = Joiner.on(",").join(includes);
        argsList.add("--spring.profiles.active=" + activeProfiles);
        if(!hasWithDb){
            if(withDb){
                argsList.add("--gcloud.withDb=true");
            }else{
                argsList.add("--gcloud.withDb=false");
            }
        }
        String[] strings = new String[argsList.size()];
        return argsList.toArray(strings);
    }

    public List<String> getDefaults() {
        return defaults;
    }

    public List<String> getExcludes() {
        return excludes;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public boolean isWithDb() {
        return withDb;
    }
}
