package com.eaton.electrical.smrc.bo;

/**
 * @author E0062708
 *
 */
public class AccountNumbers implements java.io.Serializable {
	private double currentYTD = 0;
	private double currentMinusOneYTD = 0;
	private double currentTotal = 0;
	private double currentMinusOneTotal = 0;
	private double currentMinusTwoTotal = 0;
	private double potential = 0;
	private double forecast = 0;
	private double annualEatonSales = 0;
        private double competitor1Dollars = 0;
        private double competitor2Dollars = 0;
        private Vendor competitor1 = new Vendor();
        private Vendor competitor2 = new Vendor();
	
    	private static final long serialVersionUID = 100;
	
	public double getCurrentMinusOneTotal() {
		return currentMinusOneTotal;
	}
	public void setCurrentMinusOneTotal(double currentMinusOneTotal) {
		this.currentMinusOneTotal = currentMinusOneTotal;
	}
	public double getCurrentMinusOneYTD() {
		return currentMinusOneYTD;
	}
	public void setCurrentMinusOneYTD(double currentMinusOneYTD) {
		this.currentMinusOneYTD = currentMinusOneYTD;
	}
	public double getCurrentMinusTwoTotal() {
		return currentMinusTwoTotal;
	}
	public void setCurrentMinusTwoTotal(double currentMinusTwoTotal) {
		this.currentMinusTwoTotal = currentMinusTwoTotal;
	}
	public double getCurrentTotal() {
		return currentTotal;
	}
	public void setCurrentTotal(double currentTotal) {
		this.currentTotal = currentTotal;
	}
	public double getCurrentYTD() {
		return currentYTD;
	}
	public void setCurrentYTD(double currentYTD) {
		this.currentYTD = currentYTD;
	}
	public double getForecast() {
		return forecast;
	}
	public void setForecast(double forecast) {
		this.forecast = forecast;
	}
	public double getPotential() {
		return potential;
	}
	public void setPotential(double potential) {
		this.potential = potential;
	}
	public double getAnnualEatonSales() {
		return annualEatonSales;
	}
	public void setAnnualEatonSales(double annualEatonSales) {
		this.annualEatonSales = annualEatonSales;
	}
        public Vendor getCompetitor1(){
                return competitor1;
        }
        public void setCompetitor1(Vendor competitor1){
                this.competitor1 = competitor1;
        }
        public Vendor getCompetitor2(){
                return competitor2;
        }
        public void setCompetitor2(Vendor competitor2){
                this.competitor2 = competitor2;
        }
        public double getCompetitor1Dollars(){
                return competitor1Dollars;
        }
        public void setCompetitor1Dollars(double competitor1Dollars){
                this.competitor1Dollars = competitor1Dollars;
        }
        public double getCompetitor2Dollars(){
                return competitor2Dollars;
        }
        public void setCompetitor2Dollars(double competitor2Dollars){
                this.competitor2Dollars = competitor2Dollars;
        }

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public boolean equals(Object obj) {
		if (this == null || obj == null) {
			return false;
		}

		return super.equals(obj);
	}

	public String toString() {
		String returnString = "";

		returnString += "\tcurrentMinusOneTotal = " + this.getCurrentMinusOneTotal() + "\n";
		returnString += "\tcurrentMinusOneYTD = " + this.getCurrentMinusOneYTD() + "\n";
		returnString += "\tcurrentMinusTwoTotal = " + this.getCurrentMinusTwoTotal() + "\n";
		returnString += "\tcurrentTotal = " + this.getCurrentTotal() + "\n";
		returnString += "\tcurrentYTD = " + this.getCurrentYTD() + "\n";
		returnString += "\tforecast = " + this.getForecast() + "\n";
		returnString += "\tpotential = " + this.getPotential() + "\n";
		returnString += "\tannualEatonSales = " + this.getAnnualEatonSales() + "\n";
                returnString += "\tcompetitor1 = " + this.getCompetitor1() + "\n";
                returnString += "\tcompetitor2 = " + this.getCompetitor2() + "\n";

		return returnString;
	}
}
