import java.util.Comparator;

//This is Node class for each job
class Job{
	 
	private String jobNum;
    private int jobLength;
      
    public Job(String jobNum, int jobLength){
        this.jobLength = jobLength;
        this.jobNum = jobNum;
    }
    
    public int getId(){
        return this.jobLength;
    }
    
    public String getName(){
        return this.jobNum;
    }
    
    public void setId(int x){
    	jobLength = x;
    }
    
    public void setName(String y){
        jobNum = y;
    }
}

//This will sort the order for SJF algorithms
class ascendingComparator implements Comparator<Job>{
	 
  public int compare(Job Job1, Job Job2) {        
      return Job1.getId() - Job2.getId();
  }    
}