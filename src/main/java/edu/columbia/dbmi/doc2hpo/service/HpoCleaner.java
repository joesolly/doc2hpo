package edu.columbia.dbmi.doc2hpo.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.util.ResourceUtils;

import edu.columbia.dbmi.doc2hpo.pojo.ParsingResults;
import edu.columbia.dbmi.doc2hpo.util.FileUtil;

public class HpoCleaner {
	private HashSet<String> hpSet;
	
	public HpoCleaner() {
		this.hpSet = new HashSet<String>();
		File phenotypeFile = null;
		try {
			phenotypeFile = ResourceUtils.getFile("classpath:dictionary/hp.medlee.HP_0000118.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String cleandic = phenotypeFile.toString();
		String content = FileUtil.Readfile(cleandic);
		String[] keyArray = content.split("\n");
		for (String key : keyArray) {
			String[] t = key.split("\t");
			this.hpSet.add(t[0]);
		}
	}

	public List<ParsingResults> getPhenotypeOnly(List<ParsingResults> pResults) {
		List<ParsingResults> pResultsNew = new ArrayList<ParsingResults>();
		for(ParsingResults result : pResults) {
			if(this.hpSet.contains(result.getHpoId())){
				pResultsNew.add(result);
			}
		}
		return pResultsNew;
	}
	
	public List<ParsingResults> changeHpoIdComma(List<ParsingResults> pResults) {
		for(ParsingResults result : pResults) {
			String hpoId = result.getHpoId().replaceAll("_", ":");
			result.setHpoId(hpoId);
		}
		return pResults;
	}

	public List<ParsingResults> removeAcrny(List<ParsingResults> pResults) {
		List<ParsingResults> pResultsNew = new ArrayList<ParsingResults>();

		ArrayList<String> acryn = new ArrayList<String>();
		acryn.add("mi");
		acryn.add("bo");
		acryn.add("gist");
		acryn.add("tia");
		acryn.add("tic");
		acryn.add("CP");
		acryn.add("AML");
		acryn.add("CIU");
		acryn.add("acrania");

		for(ParsingResults result : pResults) {
			String hpoName = result.getHpoName();
			if (acryn.contains(hpoName) == false) {
				pResultsNew.add(result);
			}
		}
		return pResultsNew;
	}
}
