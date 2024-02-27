intImage = 2;
function toggleImage() {
switch (intImage) {
 case 1:
   document.images["view_details"].src = "images/buttons/btn_hide_details_off.gif"
   intImage = 2
   return(false);
case 2:
   //details.src = "images/r3_tab1_on.gif"  //This only works in IE
   document.images["view_details"].src = "images/buttons/btn_view_details_off.gif" //This way works in Netscape also
   intImage = 1
   return(false);
 }
}

//This function will swap 2 layers. 
var cur_lyr = 1;	//1 = on
function swapLayers(id) {
	if(cur_lyr == 1) {
		hideLayer(id);
		cur_lyr = 0;
	}
	else {
		showLayer(id);
		cur_lyr = 1;
	}
}
function showLayer(id) {
  var lyr = getElemRefs(id);
  if (lyr && lyr.css) lyr.css.display = "block";
}
function hideLayer(id) {
  var lyr = getElemRefs(id);
  if (lyr && lyr.css) lyr.css.display = "none";

}
function getElemRefs(id) {
	var el = (document.getElementById)? document.getElementById(id): (document.all)? document.all[id]: (document.layers)? document.layers[id]: null;
	if (el) el.css = (el.style)? el.style: el;
	return el;
}


/*
	This function is used to handle the display state of layers.  It takes a parameter of the layer to be
	made visible and will turn all other designated layers off.  
	
	NOTE:  Use DIV tags for layers and assign ids with a prefix of xxx.  Only layers with this prefix will
		   be evaluated.  All other DIV id's will be ignored.  
		   This is done so you can choose which layers to affect while other layers are untouched.

	Params:
		activate:  This is the ID of the Layer to be made visible
					all other designated layers will be made invisible.	

	Example:
		
		onClick="layerhandler('xxxEdit')"   
		
		The layer with ID = xxxEdit will be made visible while all other layers with id's prefixed xxx will
		be disabled.

*/

function layerhandler(activate) {
	
	var divs = document.getElementsByTagName("div");
	for(x=0;x<divs.length;x++) {
		if(divs[x].id.substring(0,3) == 'xxx'){  //filter to only view id's starting with xxx
			if(divs[x].id == activate)
				divs[x].style.display = "block";	
			else
				divs[x].style.display = "none";	
		}
	}//for
} // end handler


