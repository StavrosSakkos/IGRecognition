package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import graph.objects.Clique;

public class CommonHelper {
	
	public ArrayList<ArrayList<Clique>> createPerms(ArrayList<Clique> mC) {
		ArrayList<ArrayList<Clique>> mCPerms = 
				new ArrayList<ArrayList<Clique>>();
		ArrayList<Integer> offsets = new ArrayList<Integer>();
		for(int i = 0; i < mC.size(); i++) {
			offsets.add(i);
		}
		for(ArrayList<Integer> L: createPerms(offsets, mC.size() - 1)) {
			ArrayList<Clique> newL = new ArrayList<Clique>();
			for(Integer i: L) {
				newL.add(mC.get(i));
			}
			mCPerms.add(newL);
		}
		return mCPerms;
		
	}
	
	public ArrayList<ArrayList<Integer>> createPerms(ArrayList<Integer> mC, int k) {
		ArrayList<ArrayList<Integer>> perms = new ArrayList<ArrayList<Integer>>();
		createPerms(mC, mC.size(), k, perms);
		return perms;
	}

	public void createPerms(ArrayList<Integer> mC, int size, int k,
			ArrayList<ArrayList<Integer>> L) {
		if(k == 0) {
			ArrayList<Integer> perm = new ArrayList<Integer>();
			for(int i = 0; i < mC.size(); i++) {
				perm.add(mC.get(i));
			}
			L.add(perm);
			return;
		}
		for(int i = 0; i < size; i++) {
			Collections.swap(mC, i, size - 1);
			createPerms(mC, size - 1, k - 1, L);
			Collections.swap(mC, i, size - 1);
		}
	}
	//NOTES: Since its interval graph, each ArrayList<Clique> has 1 Clique inside
	public ArrayList<Clique> createCPGClique(ArrayList<ArrayList<Clique>> L) {
		ArrayList<Clique> CPG = new ArrayList<Clique>();
		for(ArrayList<Clique> c: L) {
			CPG.add(c.get(0));
		}
		return CPG;
	}
	
	//NOTE: Sort O(nlogn)?
	public ArrayList<String> getSortedArr(HashSet<String> X) {
		ArrayList<String> orderedArr = new ArrayList<String>(X);
		Collections.sort(orderedArr);
		return orderedArr;
	}
	
}