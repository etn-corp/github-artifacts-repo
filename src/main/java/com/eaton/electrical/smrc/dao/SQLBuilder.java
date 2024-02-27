/*
 * SQLBuilder.java
 *
 * Created on July 14, 2004, 2:43 PM
 */

package com.eaton.electrical.smrc.dao;

import java.sql.Connection;
import java.util.*;

import com.eaton.electrical.smrc.bo.Geography;
import com.eaton.electrical.smrc.service.SMRCLogger;

/**
 *
 * @author  Jason Lubbert
 */
public class SQLBuilder extends Object implements Cloneable {
    
    HashSet fromTableNames = new HashSet();
    //  multiple "with" statements possible
    HashSet[] withFromTableNames = new HashSet[2];
    HashSet[] withSelectFields = new HashSet[2];
    HashSet[] withWhereConditions = new HashSet[2];
    HashSet[] withGroupByFields  = new HashSet[2];
    ArrayList withHintMaterialize = new ArrayList();
    HashSet selectFields = new HashSet();
    HashSet whereConditions = new HashSet();
    HashSet withName = new HashSet();
    HashSet groupByFields = new HashSet();
    HashSet orderByFields = new HashSet();
    HashSet outerSelectFields = new HashSet();
    HashSet rollUpLevelSelectFields = new HashSet();
    ArrayList graphSelectFields = new ArrayList();   
    
    String overrideSortField = new String();
    String overrideSortDirection = new String();
    boolean overrideSort = false;
   
    boolean [] withSelectDistinct = new boolean [2]; 
    boolean selectDistinct = false;
    boolean limitResults = false;
    boolean multiLevel = false;
    boolean graphSQL = false;
    boolean associatedUsersSQL = false;
    int beginWith = 0;
    int endWith = 0;
    
    /** Creates a new instance of SQLBuilder */
    public SQLBuilder() {
        withFromTableNames[0] = new HashSet();
        withFromTableNames[1] = new HashSet();
        withSelectFields[0] = new HashSet();
        withSelectFields[1] = new HashSet();
        withWhereConditions[0] = new HashSet();
        withWhereConditions[1] = new HashSet();
        withGroupByFields[0] = new HashSet();
        withGroupByFields[1] = new HashSet();
        withSelectDistinct[0] = false;
        withSelectDistinct[1] = false;
        
        
    }
    public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
    
    public void setWithHintMaterialze(int index, boolean materialize) {
    	withHintMaterialize.add(index, new Boolean(materialize));
    }
    
    public void setWithSelectDistinct (int index){
        withSelectDistinct[index] = true;
    }
    
    public void setSelectDistinct(){
        selectDistinct = true;
    }
    
    public void limitResultsReturned(int begin, int end){
        limitResults = true;  
        beginWith = begin;
        endWith = end;
    }
    
    public void turnOffLimitResults(){
        limitResults = false;
    }
    
    public boolean isLimitedQuery(){
        return limitResults;
    }
  
    public void setMultiLevel(){
        multiLevel = true;
    }
    
    public boolean isMultiLevel(){
        return multiLevel;
    }
    
    public boolean isGraphSQL(){
    	return graphSQL;
    }
    
    public void setGraphSQL(boolean graphSQL){
    	this.graphSQL = graphSQL;
    }
    
    public void addToFromTable (String table){
        fromTableNames.add(table);
    }
    
    public void addToWithFromTable (String table, int index){
        withFromTableNames[index].add(table);
    }
    
    public void addToWithSelectFields (String field, int index){
        withSelectFields[index].add(field);
    }
    
    public void addToWithGroupByFields (String field, int index){
        withGroupByFields[index].add(field);
    }
    
    public void addToSelectFields (String field){
    	selectFields.add(field);
    }
    
    public void addToWithWhereConditions (String condition, int index){
        withWhereConditions[index].add(condition);
    }
    
    public void addToWhereConditions (String condition){
        whereConditions.add(condition);
    }
    
    public void addToWithNames (String withName){
        this.withName.add(withName);
    }
    
    public void addToGroupByFields (String groupByField){
        this.groupByFields.add(groupByField);
    }
    
    public void addToOrderByFields (String orderByField){
        this.orderByFields.add(orderByField);
    }
    
    public void addToOuterSelectFields (String selectField){
    	this.outerSelectFields.add(selectField);
        if (multiLevel){
            if (selectField.equalsIgnoreCase("description") || 
                selectField.equalsIgnoreCase("description2") ||
                selectField.equalsIgnoreCase("id") ||
                selectField.equalsIgnoreCase("id2")){
                    this.rollUpLevelSelectFields.add(selectField);
            } else {
                this.rollUpLevelSelectFields.add("sum(" + selectField + ") " + selectField);
            }
        }
    }
    
    public void setOverrideSort (String sortfield, String direction){
        this.overrideSortField = sortfield;
        this.overrideSortDirection = direction;
        this.overrideSort = true;
    }
    

    
    public String createSQLQuery (){
        StringBuffer theQuery = new StringBuffer();
        String iteratedString = null;
        if (withName.size() > 0){
            Object [] withArray = withName.toArray();
            theQuery.append("with " + (String) withArray[0] + " as (");
            
            String materialize = "";
            
            if (((Boolean)withHintMaterialize.get(0)).booleanValue()) {
            	materialize = "/*+ materialize */ ";           	
            }
        
            // Only want the customer numbers for associated users query
            if (associatedUsersSQL){
                theQuery.append(" select /*+ materialize */ distinct  customer.vista_customer_number ");
            } else {
	            if (withSelectDistinct[0]){
	                theQuery.append("select " + materialize + "distinct ");
	            } else {
	                theQuery.append("select " + materialize);
	            }
	            iteratedString = iterateThroughSet(withSelectFields[0], ",");
	            theQuery.append(iteratedString);
            }
            

            theQuery.append(" from");
            iteratedString = iterateThroughSet(withFromTableNames[0], ",");
            theQuery.append(iteratedString);

            if (withWhereConditions[0].size() > 0){
                theQuery.append(" where");
                iteratedString = iterateThroughSet(withWhereConditions[0], " and");
                theQuery.append(iteratedString);
            }
            
            if (!associatedUsersSQL){
	            if (withGroupByFields[0].size() > 0){
	                theQuery.append(" group by ");
	                iteratedString = iterateThroughSet(withGroupByFields[0], ",");
	                theQuery.append(iteratedString);
	            }
            }
            
            theQuery.append(" ) ");
        }
        
        if (withName.size() > 1){
            
            Object [] withArray = withName.toArray();
            theQuery.append(", " + (String) withArray[1] + " as (");
            
            // Only want the customer numbers for associated users query
            if (associatedUsersSQL){
                theQuery.append(" select distinct customer.vista_customer_number ");
            } else {
	            if (withSelectDistinct[1]){
	                theQuery.append("select distinct ");
	            } else {
	                theQuery.append("select ");
	            }
	            iteratedString = iterateThroughSet(withSelectFields[1], ",");
	            theQuery.append(iteratedString);
            }

            theQuery.append(" from");
            iteratedString = iterateThroughSet(withFromTableNames[1], ",");
            theQuery.append(iteratedString);

            if (withWhereConditions[1].size() > 0){
                theQuery.append(" where");
                iteratedString = iterateThroughSet(withWhereConditions[1], " and");
                theQuery.append(iteratedString);
            }
            
            if (!associatedUsersSQL){
	            if (withGroupByFields[1].size() > 0){
	                theQuery.append(" group by ");
	                iteratedString = iterateThroughSet(withGroupByFields[1], ",");
	                theQuery.append(iteratedString);
	            }
            }

            theQuery.append(" ) ");
            
        }
        
        // associate users query has very different query from this point on, so 
        // there's no need to continue past this point
        if (associatedUsersSQL){
            theQuery.append(associatedUsersMainSelect());
            return theQuery.toString();
        }
        
        // SQL returned for graphing must have description selected, followed by dollar type fields
        if (graphSQL){
        	theQuery.append("select " + getGraphingSelectFields(outerSelectFields));
        	theQuery.append(" from ( ");
        }
              
        if (limitResults){
            theQuery.append("select * from (");
            theQuery.append("select rownum rownumber, ");
            iteratedString = iterateThroughSet(outerSelectFields, ",");
            theQuery.append(iteratedString);
            theQuery.append(" from (");
        }
        // This sql required to keep row selection code seperate from code that eliminates 
        // duplicate records returned by rollup command
        if (multiLevel){
            theQuery.append("select ");
            iteratedString = iterateThroughSet(outerSelectFields, ",");
            theQuery.append(iteratedString);
            theQuery.append(" from (");
            theQuery.append(" select ");
            iteratedString = iterateThroughSet(rollUpLevelSelectFields, ",");
            theQuery.append(iteratedString);
            theQuery.append(" from (");
        }
           
 //       }
        
                
        if (selectDistinct){
            theQuery.append("select distinct ");
        } else {
            theQuery.append("select ");
        }
        iteratedString = iterateThroughSetOrdered(selectFields, ",");
        theQuery.append(iteratedString);
        
        theQuery.append(" from");
        iteratedString = iterateThroughSet(fromTableNames, ",");
        theQuery.append(iteratedString);
        
        if (whereConditions.size() > 0){
            theQuery.append(" where");
            iteratedString = iterateThroughSet(whereConditions, " and");
            theQuery.append(iteratedString);
        }
        
        if (groupByFields.size() > 0){
            theQuery.append(" group by ");
            iteratedString = iterateThroughSet(groupByFields, ",");
            theQuery.append(iteratedString);
        }
        
        if (orderByFields.size() > 0){
            theQuery.append(" order by ");
            if (overrideSort){
                theQuery.append(overrideSortField + " " + overrideSortDirection + ", ");
            }
            iteratedString = iterateThroughSet(orderByFields, ",");
            theQuery.append(iteratedString);
        }
        
        
        //  This sql code eliminates duplicate records returned by rollup command
        if (multiLevel){
            theQuery.append(") ");
            theQuery.append("group by rollup(id, description, id2, description2)");
            theQuery.append(") where description is not null ");
            theQuery.append("and not(id2 is not null and description2 is null) ");
            theQuery.append("order by description, id, description2, id2  ");
        }
        
        if (limitResults){
            theQuery.append(") ) where rownumber between " + beginWith + " and " + endWith);
          
        }
        
        if (graphSQL){
        	theQuery.append(")");
        }
        return theQuery.toString();
                    
    }
    
    public String iterateThroughSet(HashSet theSet, String seperator){
        StringBuffer sql = new StringBuffer();
        Iterator iter = theSet.iterator();
        while (iter.hasNext()){
            sql.append(" " + (String) iter.next());
            if (iter.hasNext()){
                sql.append(seperator);
            }
        }
        return sql.toString();
    }
    
    public String iterateThroughSetOrdered(HashSet theSet, String seperator){
        StringBuffer sql = new StringBuffer();
//        Iterator iter = theSet.iterator();
        
        // Sort the array.  This is necessary when doing a Union and it has to match order
        
        Object[] values = (Object[])theSet.toArray();
        Arrays.sort(values);
        
        for (int i = 0; i < values.length; i++) {
            sql.append(" " + (String) values[i]);
       	    if (i < values.length - 1) {
            	
            	sql.append(seperator);
            	
            }
        	
        	
        } // end iterate through list
                
        
/*        
        while (iter.hasNext()){
            sql.append(" " + (String) iter.next());
            if (iter.hasNext()){
                sql.append(seperator);
            }
        }*/
        return sql.toString();
    }    
    
    private String getGraphingSelectFields(HashSet theSet){
    	StringBuffer sql = new StringBuffer();
    	sql.append(" description ");
        Iterator iter = theSet.iterator();
        while (iter.hasNext()){
        	String nextField = (String) iter.next();
        	//  Only want dollar type fields, and all of them should contain sales, orders, or dollars in the label
        	if ((nextField.indexOf("Invoice") > -1) || (nextField.indexOf("Order") > -1) || (nextField.indexOf("$") > -1)){
        		sql.append(", " + nextField);
        	}
        }
        return sql.toString();
    	
    }
    
    // This is used by both StandardReportSQLBuilder and AssocUsersSQLBuilder
    public String getGeogSearchString(String geog, Connection DBConn) throws Exception{
        Geography geography = new Geography();
        try {
            geography = MiscDAO.getGeography(geog, DBConn);
        }catch (Exception e)	{
			SMRCLogger.error("SQLBuilder.getGeogSearchString(): ", e);
			throw e;
		}
        String searchFor = geog;
        if (geography.getGroupCode().equalsIgnoreCase("0")){
            searchFor = geog.substring(0,1);
        } else if (geography.getZone().equalsIgnoreCase("00")){
            searchFor = geog.substring(0,2);
        } else if (geography.getDistrict().equalsIgnoreCase("0")){
            searchFor = geog.substring(0,4);
        } else if (geography.getTeam() == null){
            searchFor = geog.substring(0,5);
        }         
            
        return searchFor;
        
    }
    /**
     * @return Returns the associatedUsersSQL.
     */
    public boolean isAssociatedUsersSQL() {
        return associatedUsersSQL;
    }
    /**
     * @param associatedUsersSQL The associatedUsersSQL to set.
     */
    public void setAssociatedUsersSQL(boolean associatedUsersSQL) {
        this.associatedUsersSQL = associatedUsersSQL;
    }
    
    private String associatedUsersMainSelect(){
        StringBuffer query = new StringBuffer();
        query.append(" select distinct users.email_address ");
        query.append(" from users, user_cust_xref ");
        query.append(" where users.userid = user_cust_xref.userid ");
        query.append(" and user_cust_xref.vista_cust_num in " );
        if (withName.size() > 1){
            query.append(" ( ");
        }
        query.append("( select  custlist.vista_customer_number from custlist " );
        if (withName.size() > 1){
            query.append(") union (select custprod.vista_customer_number from custprod) ");
        }
        query.append(")");
        
        return query.toString();
    }
}
