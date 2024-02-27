<!-- caller must have an ArrayList or Collection of segment objects called "segments" -->

<%!

	private String getHandles(String segmentId) {
		return "{open: '<input type=\"checkbox\" name=\"segments\" value=\""
			+ segmentId
			+ "\" checked>', closed: '<input type=\"checkbox\" name=\"segments\" value=\""
			+ segmentId
			+ "\">'}";
	}
%>
							<style type="text/css">
								div.treewidgetcontainer
								{
								}
								div.treewidgethandle
								{
									cursor: pointer; cursor: hand;
								}
								div.treewidgetchildren
								{
									position: relative;
									left: 2em;
									display:none;
								}
						    </style>
							<script language="javascript" src="<%= jsURL %>selectTree.js"></script>
							<script language="javascript">
								function getSelectObject(nodeId) {
									// Get the DIV container of this node
									var parentDiv = document.getElementById(nodeId);
							
									// Make sure the event target was a checkbox
									for (var i = 0; i < parentDiv.childNodes.length; i++) {
										if (parentDiv.childNodes[i].type && parentDiv.childNodes[i].type == 'checkbox') {
											return parentDiv.childNodes[i];
										}
									}
								}
							

								var root = new Graph(false, true);
<%

								Iterator segmentIterator = segments.iterator();
								Segment segment;
								while (segmentIterator.hasNext()) {
									segment = (Segment)segmentIterator.next();
%>
									s<%=segment.getSegmentId()%> = root.addRoot(new Node(<%=getHandles(Integer.toString(segment.getSegmentId()))%>, '<%=segment.getName()%>'));
<%
									Collection childSegments = segment.getSubSegments();
									if (childSegments != null && childSegments.size() > 0) {
										Iterator childIterator = childSegments.iterator();
										Segment child;
										while (childIterator.hasNext()) {
											child = (Segment)childIterator.next();
%>
											s<%=child.getSegmentId()%> = s<%=segment.getSegmentId()%>.appendChild(new Node(<%=getHandles(Integer.toString(child.getSegmentId()))%>, '<%=child.getName()%>'));
<%
											Collection grandChildSegments = child.getSubSegments();
											if (grandChildSegments != null && grandChildSegments.size() > 0) {
												Iterator grandChildIterator = grandChildSegments.iterator();
                                                                                                Segment grandChild;
                                                                                                while (grandChildIterator.hasNext()){
												        grandChild = (Segment)grandChildIterator.next();
%>
													s<%=grandChild.getSegmentId()%> = s<%=child.getSegmentId()%>.appendChild(new Node(<%=getHandles(Integer.toString(grandChild.getSegmentId()))%>, '<%=grandChild.getName()%>'));
<%
													Collection finalSegments = grandChild.getSubSegments();
													if (finalSegments != null && finalSegments.size() > 0) {
														Iterator finalIterator = finalSegments.iterator();
														Segment finalChild;
														while (finalIterator.hasNext()) {
															finalChild = (Segment)finalIterator.next();
%>
															s<%=grandChild.getSegmentId()%>.appendChild(new Node(<%=getHandles(Integer.toString(finalChild.getSegmentId()))%>, '<%=finalChild.getName()%>'));
<%
														}
													}
												}
											}
										}
									}
								}
%>
								root.toHTML();
							</script>
