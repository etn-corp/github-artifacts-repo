/*
 * $Log: not supported by cvs2svn $
 * Revision 1.1  2004/10/22 15:42:27  matthj
 * Tree control script
 *
 * Copyright ${year} Eaton, Inc. All rights reserved.
 *
 */

/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * The Original Code is Netscape code.
 *
 * The Initial Developer of the Original Code is
 * Netscape Corporation.
 * Portions created by the Initial Developer are Copyright (C) 2001
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s): Bob Clary <bclary@netscape.com>
 * Contributor(s): Jason Matthews <jasonmatthews@eaton.com>
 *
 * ***** END LICENSE BLOCK ***** */

function Graph(rootVisable, autoCollapse) {
  this.rootVisable = rootVisable;
  this.autoCollapse = autoCollapse;
  this.trees = new Array();
}

Graph.prototype.toHTML =
function renderGraph() {
	for (i = 0; i < this.trees.length; i++) {
		document.write(this.trees[i].toHTML());
	}
}

Graph.prototype.addRoot =
function addRoot(aNode) {
	if (aNode) {
		this.trees[this.trees.length] = aNode;
		aNode.tree = this;
	}
	return aNode;
}

Graph.prototype.expandAction = new Function();

Graph.prototype.collapseAction = new Function();

/**
 * Node object definition
 */

Node._id = 0;
Node._hash = new Object();
Node._dynamic = (document.documentElement && typeof(document.documentElement.innerHTML) == 'string');

function Node(handles, labels, classprefix, expanded) {
	this.id = 'treewidgetid' + Node._id++;
	Node._hash[this.id] = this;
	if (typeof(handles) == 'string') {
		this.handles = {open: handles, closed: handles};
	} else {
		this.handles = handles;
	}
	if (typeof(labels) == 'string') {
		this.labels = {open: labels, closed: labels};
	} else {
		this.labels = labels;
	}
	this.classprefix  = classprefix ? classprefix : 'treewidget';
	this.expanded = expanded ? expanded : false;
	this.children = new Array();
	this.tree = null;
}

Node.prototype.appendChild =
function xbTreeNodeAppendChild(child) {
	if (child) {
		this.children[this.children.length] = child;
		child.tree = this.tree;
	}
	return child;
}

if (document.getElementById || document.all) {
  Node.prototype.toHTML =
  function NodeToHTMLDOMIE(level) {
    var html = '';

    html += '<div class="' + this.classprefix + 'container">';
    
    html += '<div ID="' + this.id + '" class="' + this.classprefix + 'handle" onclick="NodeToggleHandle(this)">';
    if (Node._dynamic && !this.expanded) {
      html += this.handles.closed;
      html += this.labels.closed;
    } else {
      html += this.handles.open;
      html += this.labels.open;
    }
    html += '<\/div>\n';

    if (this.children.length) {
      html += '<div class="' + this.classprefix + 'children" ';

	  if (Node._dynamic  && !this.expanded) {
        html += 'style="display:none;">';
      } else {
        html += 'style="display:block;">';
      }

      for (var i = 0; i < this.children.length; i++) {
        html += this.children[i].toHTML();
      }

      html += '<\/div>';
    }

    html += '<\/div>';

    return html;
  };
} else {
  Node.prototype.toHTML =
  function xbTreeWidgeToHTMLLegacy(depth) {
    var i;
    var html = '';

    if (typeof(depth) == 'undefined') {
      depth = 0;
    }

    html += '<table>\n';
    html += '<tr>\n';
    html += '<td>\n';

//    if (document.layers)
//      html += '<ilayer visibility="hidden">\n';
//    else
//      html += '<span style="visibility: hidden">\n';

    for (i = 0; i < depth; i++) {
      html += '&nbsp;';
    }

//    if (document.layers)
//      html += '<\/ilayer>\n';
//    else
//      html += '<\/span>\n';

    html += this.handles.open + this.labels.open;

    for (i = 0; i < this.children.length; i++) {
      html += this.children[i].toHTML(depth+1);
    }

    html += '<\/td>\n';
    html += '<\/tr>\n';
    html += '<\/table>\n';

    return html;
  };
}


function xbGetNextElement(node) {
	var next;

	for (next = node.nextSibling; next; next = next.nextSibling) {
		if (next.nodeType == 1)
			return next;
	}

	return null;
}

function NodeToggleHandle(handlediv) {
	if (!handlediv)
		return false;

	var widget;

	var next = xbGetNextElement(handlediv);
	if (next) {
		switch(next.style.display) {
			case '':
			case 'block':
			    widget = Node._hash[handlediv.id];
			    // This chunk of code is checking to see if any levels below the
			    // selected level have been selected, so the user can select a node on
			    // the bottom without being forced to keep the top level node selected
			    // jdl - 12/2004
			    childSelected = false;
			    for (i=0; i < widget.children.length; i++){
			      if (getSelectObject(widget.children[i].id).checked == true){
			         childSelected = true;
			      }
			      widgetChild = Node._hash[widget.children[i].id];
			      if (widgetChild.children){
			          for (i2=0; i2 < widgetChild.children.length; i2++){
			      		if (getSelectObject(widgetChild.children[i2].id).checked == true){
			         		childSelected = true;
			         	}
			      		widgetGrandChild = Node._hash[widgetChild.children[i2].id];
			      		if (widgetGrandChild.children){
			      		   for (i3=0; i3 < widgetGrandChild.children.length; i3++){
			      				if (getSelectObject(widgetGrandChild.children[i3].id).checked == true){
			         				childSelected = true;
			         			}
			      		   }
			      		}
			      	  }
			      }
			    }
			    if (!childSelected){
 					widget.tree.collapseAction(handlediv);
					next.style.display = 'none';
					if (typeof(handlediv.innerHTML) == 'string') {
						handlediv.innerHTML = widget.handles.closed + widget.labels.closed;
						widget.expanded = false;
					}
				}
				break;
			case 'none':
				widget = Node._hash[handlediv.id];
				widget.tree.expandAction(handlediv);
				next.style.display = 'block';
				if (typeof(handlediv.innerHTML) == 'string') {
					handlediv.innerHTML = widget.handles.open + widget.labels.open;
					if (widget.tree.autoCollapse) {
						// TODO: Collapse other trees and sibling nodes
					}
					widget.expanded = true;
				}
				break;
			default:
				return false;
     }
	}
	return true;
}