package com.travisdiff;

import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;

public class DecorateJSonTree {

	public String getFixedField(Action action) {
		ITree treenode = action.getNode();
		ITree treefield = action.getNode();
		String fieldlabel = "";

		while (treenode != null) {
			if(treenode.getType().toString().equals("FIELD"))
			{
				treefield=treenode;
			}
//			if(treenode.getType().toString().equals("OBJECT"))
//			{
//				break;
//			}
			treenode = treenode.getParent();
		}
		
		List<ITree> children=treefield.getChildren();
		
		if(children.size()>1)
		{
			fieldlabel=children.get(0).getLabel();
		}

		return fieldlabel;
	}
	
	public String getJsonField(Action action) {
		ITree treenode = action.getNode();
		String fieldlabel = "";

		while (treenode != null) {
			if(treenode.getType().toString().equals("FIELD"))
			{
				break;
			}
			treenode = treenode.getParent();
		}
		
		List<ITree> children=treenode.getChildren();
		
		if(children.size()>1)
		{
			fieldlabel=children.get(0).getLabel();
		}

		return fieldlabel;
	}

}
